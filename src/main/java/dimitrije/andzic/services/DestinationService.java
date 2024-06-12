package dimitrije.andzic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.Destination;
import dimitrije.andzic.repositories.destination.DestinationRepository;
import dimitrije.andzic.repositories.destination.DestinationRepositoryImplementation;
import spark.Request;
import spark.Response;

import java.util.List;

public class DestinationService {
    private ObjectMapper objectMapper = new ObjectMapper();

    private DestinationRepository destinationRepository = new DestinationRepositoryImplementation();

    public Pagination<Destination> getAllDstinations(Request request, Response response){
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return destinationRepository.getAllDestinations(pageSize, page);
    }

    public Destination addDestination(Request request, Response response) throws JsonProcessingException {
        Destination d = objectMapper.readValue(request.body(), Destination.class);
        return  destinationRepository.addDestination(d);
    }

    public String deleteDestination(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        destinationRepository.deleteDestination(id);
        return "Destination successfully deleted!";
    }

    public Destination updateDestination(Request request, Response response) throws JsonProcessingException {
        int id = Integer.parseInt(request.params(":id"));
        Destination d = objectMapper.readValue(request.body(), Destination.class);
        d.setId(id);
        return destinationRepository.updateDestination(d);
    }

    public Destination getDestinationById(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        return destinationRepository.getDestinationById(id);
    }
}
