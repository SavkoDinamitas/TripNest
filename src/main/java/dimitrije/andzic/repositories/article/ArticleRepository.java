package dimitrije.andzic.repositories.article;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.dtos.ActivityDto;
import dimitrije.andzic.dtos.ArticleDto;
import dimitrije.andzic.dtos.CommentDto;
import dimitrije.andzic.entities.Activity;
import dimitrije.andzic.entities.Article;
import dimitrije.andzic.entities.Comment;

import java.util.List;

public interface ArticleRepository {
    Pagination<Article> getAllSortedArticlesByCreateDate(int pageSize, int page);

    Pagination<Article> getAllSortedArticlesByViews(int pageSize, int page);

    Pagination<Article> getAllDestinationArticles(int pageSize, int page, int destination_id);

    Pagination<Article> getAllActivityArticles(int pageSize, int page, int article_id);

    Article getById(int id);

    ArticleDto addArticle(ArticleDto article);

    Article addActivity(ActivityDto a);

    Article addComment(CommentDto c);

    Article updateArticle(ArticleDto a);

    void deleteArticle(int id);

    void deleteActivity(int activity_id, int article_id);

    Activity getActivity(int id);
}
