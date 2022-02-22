package Database;

import Models.AbstractEntity;
import Util.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

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
                if(entity.getInitialHash() != entity.hashCode()) {
                    System.out.println(entity);
                    this.update(entity);
                }
            }
        }
    }

    private void update(AbstractEntity entity) {
        String tableName = entity.getClass().getAnnotation(Entity.class).name();
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field: FieldTools.getAllFields(entity.getClass())) {
            Column column = field.getAnnotation(Column.class);
            if (
                    column == null ||
                    field.getAnnotation(Id.class) != null ||
                    field.getAnnotation(OneToManyRelation.class) != null
            ) {
                continue;
            }

            try {
                values.add(field.get(entity).toString());
                fields.add(column.name() + " = ?");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String sqlStatement = String.format(
                DatabaseService.DEFAULT_UPDATE,
                tableName,
                String.join(", ", fields)
        );

        values.add(entity.getId());
        int updatedRows = this.runPreparedUpdate(sqlStatement, values);
        if(updatedRows > 0) {
            FieldTools.setInitialHash(entity);
        }

        System.out.println(sqlStatement);
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
