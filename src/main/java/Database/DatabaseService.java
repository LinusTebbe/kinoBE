package Database;

import Models.AbstractEntity;
import Util.EmptyResultSet;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {
    private static final String DSN = "jdbc:sqlite:database.sqlite";

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

    public ResultSet runPreparedQuery(String query, List<Object> values) {
        ResultSet resultSet;
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            System.out.println(query);
            for (int i = 0; i < values.size(); i++) {
                this.setParameterToPreparedQuery(statement, i, values.get(i));
            }
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
            statement.setInt(key + 1, (Integer) value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        if(value instanceof String) {
//            statement.setString(key, (String) value);
//        } else if (value instanceof Integer) {
//        }
    }
}
