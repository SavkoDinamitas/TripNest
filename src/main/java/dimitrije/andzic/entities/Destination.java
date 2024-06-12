package dimitrije.andzic.entities;

import dimitrije.andzic.dtos.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Destination extends Entity {
    private int id;
    private String name;
    private String description;

    public Destination(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
