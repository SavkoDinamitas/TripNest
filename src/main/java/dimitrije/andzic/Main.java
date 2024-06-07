package dimitrije.andzic;

import dimitrije.andzic.services.Servis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        port(8080);
        get("/hello", (req, res) -> Servis.xd());
    }
}