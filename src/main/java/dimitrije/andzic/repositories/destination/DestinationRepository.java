package dimitrije.andzic.repositories.destination;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.Destination;

import java.sql.SQLException;
import java.util.List;

public interface DestinationRepository {

    Pagination<Destination> getAllDestinations(int pageSize, int page);

    Destination addDestination(Destination destination) throws SQLException;

    Destination updateDestination(Destination destination) throws SQLException;

    void deleteDestination(int id);

    Destination getDestinationById(int id);
}
