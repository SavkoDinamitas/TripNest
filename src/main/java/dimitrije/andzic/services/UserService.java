package dimitrije.andzic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dimitrije.andzic.Main;
import dimitrije.andzic.Pagination;
import dimitrije.andzic.dtos.LoginRequest;
import dimitrije.andzic.dtos.Status;
import dimitrije.andzic.entities.User;
import dimitrije.andzic.repositories.user.UserRepository;
import dimitrije.andzic.repositories.user.UserRepositoryImplementation;
import dimitrije.andzic.security.PasswordUtils;
import spark.Request;
import spark.Response;

import java.util.List;

public class UserService {
    private UserRepository userRepository = new UserRepositoryImplementation();
    private ObjectMapper objectMapper = new ObjectMapper();

    private String secret = "tvojakeva";

    public User login(Request req, Response res) throws JsonProcessingException {
        LoginRequest lr = objectMapper.readValue(req.body(), LoginRequest.class);
        User u = userRepository.findUser(lr.getEmail());
        if(u != null && PasswordUtils.checkPassword(lr.getPassword(), u.getPassword()) && u.getStatus() == 1){
            return u;
        }
        return null;
    }

    public User addUser(Request req, Response res) throws JsonProcessingException {
        Main.logger.debug("Usro se i umro");
        User u = objectMapper.readValue(req.body(), User.class);
        //hesiramo password pre slanja u bazu
        String hashedPassword = PasswordUtils.hashPassword(u.getPassword());
        u.setPassword(hashedPassword);
        return userRepository.addUser(u);
    }

    public void toggle (Request req, Response res){
        userRepository.changeStatus(Integer.parseInt(req.params(":id")));
    }

    public User changeUser(Request req, Response res) throws JsonProcessingException {
        int id = Integer.parseInt(req.params(":id"));
        User u = objectMapper.readValue(req.body(), User.class);
        u.setId(id);
        return userRepository.updateUser(u);
    }

    public boolean status(Request req, Response res){
        try{
            if(req.requestMethod().equals("GET"))
                return true;
            User u = userRepository.findUserById(req.session().attribute("id"));
            if(u.getType() > 1)
                return false;
            return u.getStatus() == 1;
        }
        catch (Exception e){
            Main.logger.debug("Druze koji k\n" + e);
            return false;
        }
    }

    public String getType(Request req, Response res){
        try {
            int type = req.session().attribute("type");
            int id = req.session().attribute("id");
            User u = userRepository.findUserById(id);
            if (u.getStatus() == 1) {
                res.status(200);
                return "Authorized";
            }
            else{
                res.status(401);
                return "Account not active anymore!";
            }
        }
        catch (Exception e){
            res.status(401);
            return "Unauthorized access!";
        }

    }

    public Pagination<User> getAllUsers(Request request, Response response){
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return userRepository.getAllUsers(pageSize, page);
    }

    public User findUserById(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        return userRepository.findUserById(id);
    }
}
