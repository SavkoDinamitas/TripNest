package dimitrije.andzic.repositories.destination;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.Destination;

import java.util.List;

public interface DestinationRepository {

    Pagination<Destination> getAllDestinations(int pageSize, int page);

    Destination addDestination(Destination destination);

    Destination updateDestination(Destination destination);

    void deleteDestination(int id);

    Destination getDestinationById(int id);
}
