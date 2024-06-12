package dimitrije.andzic.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor@NoArgsConstructor@Setter@Getter
public class Comment {
    private int id;
    private String author;
    private String text;
    private LocalDateTime create_date;

    public Comment(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
