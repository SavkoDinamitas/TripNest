package dimitrije.andzic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dimitrije.andzic.Main;
import dimitrije.andzic.Pagination;
import dimitrije.andzic.dtos.Entity;
import dimitrije.andzic.entities.Destination;
import dimitrije.andzic.errors.JsonParseError;
import dimitrije.andzic.errors.ValidationError;
import dimitrije.andzic.repositories.destination.DestinationRepository;
import dimitrije.andzic.repositories.destination.DestinationRepositoryImplementation;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DestinationService {
    private ObjectMapper objectMapper = new ObjectMapper();

    private DestinationRepository destinationRepository = new DestinationRepositoryImplementation();

    public Pagination<Destination> getAllDstinations(Request request, Response response){
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return destinationRepository.getAllDestinations(pageSize, page);
    }

    public Entity addDestination(Request request, Response response){
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }
        try {
            Destination d = objectMapper.readValue(request.body(), Destination.class);
            return  destinationRepository.addDestination(d);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }
        catch (SQLException e){
            Main.logger.debug(Arrays.toString(e.getStackTrace()));
            response.status(400);
            return new ValidationError("There is already a destination with same name");
        }

    }

    public String deleteDestination(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        destinationRepository.deleteDestination(id);
        return "Destination successfully deleted!";
    }

    public Entity updateDestination(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }
        try {
            Destination d = objectMapper.readValue(request.body(), Destination.class);
            d.setId(id);
            return destinationRepository.updateDestination(d);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }
        catch (SQLException e){
            Main.logger.debug(Arrays.toString(e.getStackTrace()));
            response.status(400);
            return new ValidationError("There is already a destination with same name");
        }
    }

    public Destination getDestinationById(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        return destinationRepository.getDestinationById(id);
    }
}
