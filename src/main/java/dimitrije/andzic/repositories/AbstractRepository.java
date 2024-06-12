package dimitrije.andzic.repositories;

import java.sql.*;
import java.util.Optional;

public abstract class AbstractRepository {
    protected Connection newConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mariadb://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabaseName(), this.getUsername(), this.getPassword()
        );
    }

    protected String getHost() {
        return "localhost";
    }

    protected int getPort() {
        return 3306;
    }

    protected String getDatabaseName() {
        return "tourist_guide";
    }
//dimitrije
    protected String getUsername() {
        return "root";
    }

    //salabajzer
    protected String getPassword() {
        return "";
    }

    protected void closeStatement(Statement statement) {
        try {
            Optional.of(statement).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void closeResultSet(ResultSet resultSet) {
        try {
            if(resultSet != null)
                Optional.of(resultSet).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void closeConnection(Connection connection) {
        try {
            Optional.of(connection).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
