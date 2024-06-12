package dimitrije.andzic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dimitrije.andzic.entities.User;
import dimitrije.andzic.services.ArticleService;
import dimitrije.andzic.services.DestinationService;
import dimitrije.andzic.services.UserService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        // mast za localDateTime
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        defaultResponseTransformer(objectMapper::writeValueAsString);
        UserService userService = new UserService();
        DestinationService destinationService = new DestinationService();
        ArticleService articleService = new ArticleService();

        port(8080);
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        staticFiles.externalLocation(projectDir + staticDir);


        //filteri
        before("/api/users", (req, res) -> {
            Integer type = req.session().attribute("type");
            logger.debug("Brate sta je ovo:" + type);
            if(type == null || type != 0){
                halt(401, "Not authorized!");
            }
        });
        before("/api/users/*", (req, res) -> {
            Integer type = req.session().attribute("type");
            logger.debug("Brate sta je ovo:" + type);
            if(type == null || type != 0){
                halt(401, "Not authorized!");
            }
        });

        filterZaUrednika("api/articles");
        filterZaUrednika("api/articles/*");
        filterZaUrednika("api/destinations");
        filterZaUrednika("api/destinations/*");

        //rute
        path("/api", () -> {
            get("/type", userService::getType);
            post("/login", (req, res) ->{
                User u = userService.login(req, res);
                if(u == null){
                    res.status(401); // Set Unauthorized status
                    res.type("application/json");
                    return "Invalid credentials";
                }
                else {
                    req.session().attribute("type", u.getType());
                    req.session().attribute("id", u.getId());
                    return "Login successful";
                }
            });
            //rute za user-a
            path("/users", () ->{
                post("", userService::addUser);
                post("/toggle/:id", (req, res) -> {
                    userService.toggle(req, res);
                    res.status(200);
                    return "Usepesna promena!";
                });
                put("/:id", userService::changeUser);
                get("", userService::getAllUsers);
                get("/:id", userService::findUserById);
            });
            //rute za destinaciju
            path("/destinations", () ->{
                get("", destinationService::getAllDstinations);
                get("/:id", destinationService::getDestinationById);
                post("", destinationService::addDestination);
                put("/:id", destinationService::updateDestination);
                delete("/:id", destinationService::deleteDestination);
            });

            //rute za artikle
            post("/comments/:id", articleService::addComment);
            path("/articles", () -> {
                get("", articleService::getAllSortedArticlesByCreateDate);
                get("/views", articleService::getAllSortedArticlesBuViews);
                get("/dest/:id", articleService::getAllDestinationArticles);
                get("/activity/:id", articleService::getAllSortedActivityArticles);
                get("/:id", articleService::getArticleById);
                post("", articleService::addArticle);
                put("/:id", articleService::updateArticle);
                delete("/:id", articleService::deleteArticle);
                delete("/activities/:id_article/:id_activity", articleService::deleteActivity);
                post("/activities/:id", articleService::addActivity);
            });
        });

    }

    private static void filterZaUrednika(String path){
        UserService userService= new UserService();
        before(path, (req, res) -> {
            if(!userService.status(req, res)){
                halt(401, "Your account is no longer active!");
            }
        });
    }
}