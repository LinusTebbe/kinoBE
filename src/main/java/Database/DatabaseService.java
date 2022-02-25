package Database;

import Models.AbstractEntity;
import Util.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseService {
    private static final String DSN = "jdbc:sqlite:database.sqlite";

    private static final String DEFAULT_UPDATE = "UPDATE %s SET %s WHERE id = ?";

    private static final String DEFAULT_INSERT = "INSERT INTO %s (%s) VALUES (%s)";

    private Connection connection;

    private static DatabaseService instance;

    public static Map<String, Map<Object, AbstractEntity>> IDENTITY_MAP;

    public static DatabaseService getInstance() {
        if(DatabaseService.instance == null) {
            try {
                DatabaseService.instance = new DatabaseService();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return DatabaseService.instance;
    }

    private DatabaseService() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        this.connection = DriverManager.getConnection(DatabaseService.DSN);
        DatabaseService.IDENTITY_MAP = new HashMap<>();
    }

    public void flush() {
        for (Map<Object, AbstractEntity> map : DatabaseService.IDENTITY_MAP.values()) {
            for (AbstractEntity entity : map.values()) {
                System.out.println(entity.getClass().toString() + entity.getInitialHash());
                if (
                        entity.getInitialHash() != 0 &&
                        entity.getInitialHash() != entity.hashCode()
                ) {
                    System.out.println(entity);
                    this.update(entity);
                }
            }
        }
    }

    public void persist(AbstractEntity entity) {
        this.insert(entity);
        this.saveEntityToIdentityMap(entity);
    }

    private void insert(AbstractEntity entity) {
        Map<String, Object> fieldsAndValues = this.getFieldsAndValues(entity);
        Set<String> fields = fieldsAndValues.keySet();
        List<Object> values = new ArrayList<>(fieldsAndValues.values());

        String tableName = entity.getClass().getAnnotation(Entity.class).name();

        String sqlStatement = String.format(
                DatabaseService.DEFAULT_INSERT,
                tableName,
                String.join(", ", fields),
                fields.stream().map(field -> "?").collect(Collectors.joining(", "))
        );

        int updatedRows;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = this.prepareStatement(sqlStatement, values);
            updatedRows = preparedStatement.executeUpdate();

            if(updatedRows > 0) {
                FieldTools.setInitialHash(entity);
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Field field = entity.getClass().getSuperclass().getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity, resultSet.getInt(1));
            }
        } catch (SQLException|NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(sqlStatement);
    }

    private void update(AbstractEntity entity) {
        Map<String, Object> fieldsAndValues = this.getFieldsAndValues(entity);
        Set<String> fields = fieldsAndValues.keySet();
        Collection<Object> values = fieldsAndValues.values();

        fields = fields.stream().map(field -> field + " = ?").collect(Collectors.toSet());

        String tableName = entity.getClass().getAnnotation(Entity.class).name();

        String sqlStatement = String.format(
                DatabaseService.DEFAULT_UPDATE,
                tableName,
                String.join(", ", fields)
        );

        values.add(entity.getId());
        int updatedRows = this.runPreparedUpdate(sqlStatement, (List<Object>) values);
        if(updatedRows > 0) {
            FieldTools.setInitialHash(entity);
        }

        System.out.println(sqlStatement);
    }

    private Map<String, Object> getFieldsAndValues(AbstractEntity entity) {
        Map<String, Object> outputMap = new HashMap<>();
        for (Field field: FieldTools.getAllFields(entity.getClass())) {
            Column column = field.getAnnotation(Column.class);

            if (field.getAnnotation(Id.class) != null) {
                continue;
            }

            Object value;

            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            OneToManyRelation oneToManyRelation = field.getAnnotation(OneToManyRelation.class);
            System.out.println(123);
            if (oneToManyRelation != null) {
                outputMap.put(oneToManyRelation.localField(), ((AbstractEntity) value).getId());
                continue;
            }

            if (column == null) {
                continue;
            }
            outputMap.put(column.name(), value.toString());
        }
        return outputMap;
    }

    public ResultSet runQuery(String query) {
        ResultSet resultSet;
        try {
            Statement statement = this.connection.createStatement();
            resultSet = statement.executeQuery(query);
            System.out.println(query);
        } catch (SQLException e) {
            e.printStackTrace();
            resultSet = new EmptyResultSet();

        }
        return resultSet;
    }

    private PreparedStatement prepareStatement(String query, List<Object> values) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(query);
        System.out.println(query);
        for (int i = 0; i < values.size(); i++) {
            this.setParameterToPreparedQuery(statement, i, values.get(i));
        }

        return statement;
    }

    public int runPreparedUpdate(String query, List<Object> values) {
        try {
            return this.prepareStatement(query, values).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ResultSet runPreparedQuery(String query, List<Object> values) {
        ResultSet resultSet;
        try {
            PreparedStatement statement = this.prepareStatement(query, values);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            resultSet = new EmptyResultSet();
        }

        return resultSet;
    }

    public void saveEntityToIdentityMap(AbstractEntity entity) {
        Map<Object, AbstractEntity> map =
                DatabaseService.IDENTITY_MAP.computeIfAbsent(entity.getClass().toString(), k -> new HashMap<>());

        map.put(entity.getId(), entity);
    }

    public <T extends AbstractEntity> T getEntityFromIdentityMap(Class<T> entityClass, Object identifier) {
        Map<Object, ? extends AbstractEntity> map = DatabaseService.IDENTITY_MAP.get(entityClass.toString());
        if(map == null) {
            return null;
        }

        //noinspection unchecked
        return (T) map.get(identifier);
    }

    private void setParameterToPreparedQuery(PreparedStatement statement, int key, Object value) {
        try {
            if(value instanceof String) {
                statement.setString(key + 1, (String) value);
            } else if (value instanceof Integer) {
                statement.setInt(key + 1, (Integer) value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
