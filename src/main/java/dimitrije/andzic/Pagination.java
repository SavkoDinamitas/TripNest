package dimitrije.andzic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Pagination<T> {
    private List<T> items;
    private int pageSize;
    private int pageNumber;

    private boolean isLast;

    public Pagination(List<T> items, int pageSize, int pgNumber, boolean isLast){
        this.pageSize = pageSize;
        this.pageNumber = pgNumber;
        this.items = items;
        this.isLast = isLast;
    }

}
