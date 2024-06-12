package dimitrije.andzic.entities;

import dimitrije.andzic.dtos.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Article extends Entity implements Comparable<Article> {
    private int id;
    private String title;
    private LocalDateTime create_time;
    private int views;
    private String content;
    private User author;
    private Destination destination;
    private List<Activity> activities = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public Article(String title, int views, User author, Destination destination, List<Activity> activities, List<Comment> comments) {
        this.title = title;
        this.views = views;
        this.author = author;
        this.destination = destination;
        this.activities = activities;
        this.comments = comments;
    }

    public Article(String title, int views, User author, Destination destination) {
        this.title = title;
        this.views = views;
        this.author = author;
        this.destination = destination;
    }

    public Article(int id, String title, LocalDateTime create_time, int views, String content) {
        this.id = id;
        this.title = title;
        this.create_time = create_time;
        this.views = views;
        this.content = content;
    }

    @Override
    public int compareTo(Article o) {
        return create_time.compareTo(o.getCreate_time());
    }
}
