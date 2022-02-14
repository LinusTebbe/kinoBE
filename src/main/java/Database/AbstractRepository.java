package Database;

import Exceptions.EntityNotFoundException;
import Models.AbstractEntity;
import Util.Column;
import Util.Id;
import Util.OneToManyRelation;
import Util.Entity;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends AbstractEntity> {
    private final Class<T> modelClass;

    private final DatabaseService databaseService;

    private static final String DEFAULT_SELECT_TEMPLATE = "SELECT * FROM %s";

    private static final String DEFAULT_SELECT_BY_ID_TEMPLATE = "SELECT * FROM %s WHERE id = ? LIMIT 0,1";

    protected AbstractRepository(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.databaseService = DatabaseService.getInstance();
    }

    public List<T> getAll() {
        return this.getObjects(this.getDefaultSelectStatement());
    }

    public T getById(int id) throws EntityNotFoundException {
        T cachedEntity = this.databaseService.getEntityFromIdentityMap(this.modelClass, id);
        if (cachedEntity != null) {
            return cachedEntity;
        }

        List<T> results = this.getObjects(this.getOneByIdSelectStatement(id));

        if(results.size() == 0) {
            throw new EntityNotFoundException();
        }
        return results.get(0);
    }

    protected ResultSet getDefaultSelectStatement() {
        return this.databaseService.runQuery(String.format(
                AbstractRepository.DEFAULT_SELECT_TEMPLATE,
                this.modelClass.getAnnotation(Entity.class).name()
        ));
    }

    protected ResultSet getOneByIdSelectStatement(int id) {
        return this.databaseService.runPreparedQuery(
                String.format(
                    AbstractRepository.DEFAULT_SELECT_BY_ID_TEMPLATE,
                    this.modelClass.getAnnotation(Entity.class).name()
                ),
                List.of(id)
        );
    }

    protected List<T> getObjects(ResultSet resultSet) {
        List<Field> fields = this.getAllFields();

        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(this.getModel(fields, resultSet));
            }
        } catch (SQLException | IllegalAccessException sqlException) {
            sqlException.printStackTrace();
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private T getModel(List<Field> fields, ResultSet resultSet) throws IllegalAccessException {
        T model = this.create(this.modelClass);

        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {

                T cachedEntity = this.databaseService.getEntityFromIdentityMap(this.modelClass, this.parseField(field, resultSet));
                if (cachedEntity != null) {
                    return cachedEntity;
                }
            }
            field.set(model, this.parseField(field, resultSet));
        }

        this.databaseService.saveEntityToIdentityMap(model);

        return model;
    }

    private Object parseField(Field field, ResultSet resultSet) {
        Object relation = this.parseRelation(field, resultSet);
        if(relation != null) {
            return relation;
        }

        return this.parseSimpleField(field, resultSet);
    }

    private Object parseSimpleField(Field field, ResultSet resultSet) {
        Column col = field.getAnnotation(Column.class);
        if (col == null) {
            return null;
        }

        String name = col.name();
        try{
            if (int.class.equals(field.getType())) {
                return resultSet.getInt(name);
            } else {
                return field.getType().getConstructor(String.class).newInstance(resultSet.getString(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object parseRelation(Field field, ResultSet resultSet) {
        OneToManyRelation oneToManyRelation = field.getAnnotation(OneToManyRelation.class);
        if(oneToManyRelation != null) {
            return this.parseOneToMany(oneToManyRelation.relatedEntity(), field, resultSet);
        }

        return null;
    }

    private AbstractEntity parseOneToMany(Class<? extends AbstractEntity> clazz, Field field, ResultSet resultSet) {
        T cachedEntity = this.databaseService.getEntityFromIdentityMap(this.modelClass, this.parseSimpleField(field, resultSet));
        if (cachedEntity != null) {
            return cachedEntity;
        }

        Entity relatedEntity = clazz.getAnnotation(Entity.class);
        OneToManyRelation column = field.getAnnotation(OneToManyRelation.class);
        try {
            AbstractRepository<?> relatedRepository = relatedEntity.repository().getConstructor().newInstance();
            return relatedRepository.getById(resultSet.getInt(column.localField()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException | EntityNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();

        Class<?> clazz = this.modelClass;

        do {
            fields.addAll(List.of(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        } while (clazz != null);

        for (Field field : fields) {
            field.setAccessible(true);
        }

        return fields;
    }

    private T create(Class<T> clazz) {
        try {
            ReflectionFactory rf =
                    ReflectionFactory.getReflectionFactory();
            Constructor<Object> objDef = Object.class.getDeclaredConstructor();
            Constructor<?> intConstr = rf.newConstructorForSerialization(
                    clazz, objDef
            );
            return clazz.cast(intConstr.newInstance());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create object", e);
        }
    }
}
