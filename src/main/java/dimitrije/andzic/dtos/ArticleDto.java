package dimitrije.andzic.dtos;

import dimitrije.andzic.entities.Activity;
import dimitrije.andzic.entities.Comment;
import dimitrije.andzic.entities.Destination;
import dimitrije.andzic.entities.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class ArticleDto {
    private int id;
    private String title;
    private String content;
    private int author_id;
    private int destination_id;
}
