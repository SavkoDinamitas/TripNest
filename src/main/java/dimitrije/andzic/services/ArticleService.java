package dimitrije.andzic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dimitrije.andzic.Pagination;
import dimitrije.andzic.dtos.ActivityDto;
import dimitrije.andzic.dtos.ArticleDto;
import dimitrije.andzic.dtos.CommentDto;
import dimitrije.andzic.dtos.Entity;
import dimitrije.andzic.entities.Activity;
import dimitrije.andzic.entities.Article;
import dimitrije.andzic.entities.Comment;
import dimitrije.andzic.entities.Destination;
import dimitrije.andzic.errors.JsonParseError;
import dimitrije.andzic.errors.ValidationError;
import dimitrije.andzic.repositories.article.ArticleRepository;
import dimitrije.andzic.repositories.article.ArticleRepositoryImplementation;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;

public class ArticleService {
    private ArticleRepository articleRepository = new ArticleRepositoryImplementation();
    private ObjectMapper objectMapper = new ObjectMapper();

    public Pagination<Article> getAllSortedArticlesByCreateDate(Request request, Response response){
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return articleRepository.getAllSortedArticlesByCreateDate(pageSize, page);
    }

    public Pagination<Article> getAllDestinationArticles(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return articleRepository.getAllDestinationArticles(pageSize, page, id);
    }

    public Pagination<Article> getAllSortedArticlesBuViews(Request request, Response response){
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return articleRepository.getAllSortedArticlesByViews(pageSize, page);
    }

    public Pagination<Article> getAllSortedActivityArticles(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        int page = request.queryParams("page") != null ? Integer.parseInt(request.queryParams("page")) : 1;
        int pageSize = request.queryParams("pageSize") != null ? Integer.parseInt(request.queryParams("pageSize")) : 10;
        return articleRepository.getAllActivityArticles(pageSize, page, id);
    }

    public Article getArticleById(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        return articleRepository.getById(id);
    }

    public Entity addArticle(Request request, Response response) {
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }
        try {
            ArticleDto a = objectMapper.readValue(request.body(), ArticleDto.class);
            int author_id = request.session().attribute("id");
            a.setAuthor_id(author_id);
            return articleRepository.addArticle(a);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }


    }

    public Entity addActivity(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }
        try {
            ActivityDto a = objectMapper.readValue(request.body(), ActivityDto.class);
            a.setArticle_id(id);
            return articleRepository.addActivity(a);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }
    }

    public Entity addComment(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }

        try {
            CommentDto a = objectMapper.readValue(request.body(), CommentDto.class);
            a.setArticle_id(id);
            return articleRepository.addComment(a);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }
    }

    public Entity updateArticle(Request request, Response response) throws JsonProcessingException {
        int id = Integer.parseInt(request.params(":id"));
        if(request.body().contains("\"\"")){
            response.status(400);
            return new ValidationError("All fields must be filled!");
        }
        try {
            ArticleDto a = objectMapper.readValue(request.body(), ArticleDto.class);
            a.setId(id);
            return articleRepository.updateArticle(a);
        }
        catch (JsonProcessingException e){
            response.status(400);
            return new JsonParseError();
        }
    }

    public String deleteArticle(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        articleRepository.deleteArticle(id);
        return "Article successfully deleted!";
    }

    public String deleteActivity(Request request, Response response){
        int id_article = Integer.parseInt(request.params(":id_article"));
        int id_activity = Integer.parseInt(request.params(":id_activity"));
        articleRepository.deleteActivity(id_activity, id_article);
        return "Activity successfully deleted!";
    }

    public Activity getActivity(Request request, Response response){
        int id = Integer.parseInt(request.params(":id"));
        return articleRepository.getActivity(id);
    }
}
