package dimitrije.andzic.errors;

import dimitrije.andzic.dtos.Entity;
import lombok.Data;

@Data
public class JsonParseError extends Entity {
    private String error;

    public JsonParseError(){
        this.error = "Json is in wrong format!";
    }
}
