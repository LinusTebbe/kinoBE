package Database.Repository;

import Database.DatabaseService;
import Exceptions.EntityNotFoundException;
import Models.AbstractEntity;
import Util.*;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends AbstractEntity> {
    private final Class<T> modelClass;

    private final DatabaseService databaseService;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    private static final String DEFAULT_SELECT_TEMPLATE = "SELECT * FROM %s";

    private static final String WHERE_SELECT_TEMPLATE = "SELECT * FROM %s WHERE %s = ?";

    private static final String DEFAULT_SELECT_BY_ID_TEMPLATE = "SELECT * FROM %s WHERE id = ? LIMIT 0,1";

    protected AbstractRepository(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.databaseService = DatabaseService.getInstance();
    }

    public List<T> findAll() {
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

    public List<T> getByField(String field, Object value) {
        return this.getObjects(this.getWhereStatement(field, value));
    }

    protected ResultSet getDefaultSelectStatement() {
        return this.databaseService.runQuery(String.format(
                AbstractRepository.DEFAULT_SELECT_TEMPLATE,
                this.modelClass.getAnnotation(Entity.class).name()
        ));
    }

    protected ResultSet getWhereStatement(String field, Object value) {
        return this.databaseService.runPreparedQuery(
                String.format(
                        AbstractRepository.WHERE_SELECT_TEMPLATE,
                        this.modelClass.getAnnotation(Entity.class).name(),
                        field
                ),
                List.of(value)
        );
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
        List<Field> fields = FieldTools.getAllFields(this.modelClass);

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

        Field idField = this.getIdFieldFromFieldList(fields);
        if (idField != null) {
            Object parsedValue = this.parseField(idField, resultSet);
            T cachedEntity = this.databaseService.getEntityFromIdentityMap(
                    this.modelClass,
                    parsedValue
            );
            if (cachedEntity != null) {
                return cachedEntity;
            }

            idField.set(model, parsedValue);
        }

        this.databaseService.saveEntityToIdentityMap(model);

        for (Field field : fields) {
            Object parsedValue = this.parseField(field, resultSet);
            if (parsedValue == null) {
                continue;
            }

            field.set(model, parsedValue);
        }
        FieldTools.setInitialHash(model);

        return model;
    }

    private Field getIdFieldFromFieldList(List<Field> fields) {
        for (Field field : fields) {
            if(field.getAnnotation(Id.class) != null) {
                return field;
            }
        }

        return null;
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
            } else if (boolean.class.equals(field.getType())) {
                return resultSet.getBoolean(name);
            } else if (float.class.equals(field.getType())) {
                return resultSet.getFloat(name);
            } else if(LocalDateTime.class.equals(field.getType())) {
                return LocalDateTime.parse(resultSet.getString(name), AbstractRepository.dateTimeFormatter);
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
            //noinspection unchecked
            return this.parseOneToMany((Class<? extends AbstractEntity>) field.getType(), field, resultSet);
        }

        ManyToOneRelation manyToOneRelation = field.getAnnotation(ManyToOneRelation.class);
        if(manyToOneRelation != null) {
            return this.parseManyToOne(manyToOneRelation.targetClass(), field, resultSet);
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

    private List<AbstractEntity> parseManyToOne(Class<? extends AbstractEntity> clazz, Field field, ResultSet resultSet) {
        Entity relatedEntity = clazz.getAnnotation(Entity.class);
        ManyToOneRelation manyToOneRelation = field.getAnnotation(ManyToOneRelation.class);
        try {
            AbstractRepository<?> relatedRepository = relatedEntity.repository().getConstructor().newInstance();
            //noinspection unchecked
            return (List<AbstractEntity>) relatedRepository.getByField(manyToOneRelation.remoteField(), resultSet.getInt("id"));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
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
