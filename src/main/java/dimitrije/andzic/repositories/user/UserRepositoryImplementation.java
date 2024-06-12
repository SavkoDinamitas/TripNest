package dimitrije.andzic.repositories.user;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.User;
import dimitrije.andzic.repositories.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImplementation extends AbstractRepository implements UserRepository{
    @Override
    public Pagination<User> getAllUsers(int pageSize, int page) {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select * from users limit ?, ? ");
            statement.setInt(1, (page-1)*pageSize);
            statement.setInt(2, pageSize+1);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5)
                , resultSet.getInt(7)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        boolean isLast = true;
        if(users.size() > pageSize){
            isLast = false;
            users.removeLast();
        }
        return new Pagination<>(users, pageSize, page, isLast);
    }

    @Override
    public User findUser(String email) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select * from users where email = ?");
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5),
                        resultSet.getString(6), resultSet.getInt(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User findUserById(int id) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select * from users where user_id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5),
                        resultSet.getString(6), resultSet.getInt(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User addUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO users (email, first_name, last_name, type, password, status) VALUES(?, ?, ?, ?, ?, ?)", generatedColumns);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirst_name());
            preparedStatement.setString(3, user.getLast_name());
            preparedStatement.setInt(4, user.getType());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getStatus());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User updateUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("UPDATE users set email = ?, " +
                    "first_name = ?," +
                    "last_name = ?, " +
                    "type = ? " +
                    "where user_id = ?", generatedColumns);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirst_name());
            preparedStatement.setString(3, user.getLast_name());
            preparedStatement.setInt(4, user.getType());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public void changeStatus(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement status = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            status = connection.prepareStatement("SELECT status from users where  user_id = ?");
            status.setInt(1, id);
            resultSet = status.executeQuery();
            int trsta = 0;
            if(resultSet.next()){
                trsta = resultSet.getInt(1);
                trsta = (trsta + 1) % 2;
            }

            preparedStatement = connection.prepareStatement("UPDATE users set status = ? where user_id = ?");
            preparedStatement.setInt(1, trsta);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
    }
}
