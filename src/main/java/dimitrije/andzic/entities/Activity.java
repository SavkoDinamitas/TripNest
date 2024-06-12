package dimitrije.andzic.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter@AllArgsConstructor@NoArgsConstructor@Setter
public class Activity {
    private int id;
    private String keywords;

    public Activity(String keywords) {
        this.keywords = keywords;
    }
}
