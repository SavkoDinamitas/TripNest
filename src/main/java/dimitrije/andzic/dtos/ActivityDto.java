package dimitrije.andzic.dtos;

import lombok.Data;

@Data
public class ActivityDto extends Entity{
    private int id;
    private String keywords;
    private int article_id;
}
