package dimitrije.andzic.repositories.destination;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.Destination;
import dimitrije.andzic.entities.User;
import dimitrije.andzic.repositories.AbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationRepositoryImplementation extends AbstractRepository implements DestinationRepository {
    @Override
    public Pagination<Destination> getAllDestinations(int pageSize, int page) {
        List<Destination> destinations = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select * from destinations limit ?, ?");
            statement.setInt(1, (page-1)*pageSize);
            statement.setInt(2, pageSize+1);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                destinations.add(new Destination(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        boolean isLast = true;
        if(destinations.size() > pageSize){
            isLast = false;
            destinations.removeLast();
        }
        return new Pagination<>(destinations, pageSize, page, isLast);
    }

    @Override
    public Destination addDestination(Destination destination) throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO destinations (name, description) VALUES(?, ?)", generatedColumns);
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                destination.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public Destination updateDestination(Destination destination) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("UPDATE destinations set name = ?, " +
                    "description = ?" +
                    "where destination_id = ?", generatedColumns);
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.setInt(3, destination.getId());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                destination.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public void deleteDestination(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE from destinations "+
                    "where destination_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public Destination getDestinationById(int id) {
        Destination destination = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select * from destinations where destination_id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                destination = new Destination(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }
}
