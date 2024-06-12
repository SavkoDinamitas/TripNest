package dimitrije.andzic.errors;

import dimitrije.andzic.dtos.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class ValidationError extends Entity {
    private String error;
}
