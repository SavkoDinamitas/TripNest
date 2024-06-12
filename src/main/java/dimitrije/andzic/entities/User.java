package dimitrije.andzic.entities;

import dimitrije.andzic.dtos.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class User extends Entity {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    //0 je admin, 1 je siroce
    private int type;
    private String password;
    //1 je aktivan, 0 nije
    private int status;

    public User(String email, String first_name, String last_name, int type, String password, int status) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.type = type;
        this.password = password;
        this.status = status;
    }

    public User(int id, String email, String first_name, String last_name, int type, int status) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.type = type;
        this.status = status;
    }

    public User(int id, String email, String first_name, String last_name) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
