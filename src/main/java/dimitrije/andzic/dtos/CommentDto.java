package dimitrije.andzic.dtos;

import lombok.Data;

@Data
public class CommentDto {
    private int id;
    private String author;
    private String text;
    private int article_id;
}
