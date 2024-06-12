package dimitrije.andzic.repositories.article;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.dtos.ActivityDto;
import dimitrije.andzic.dtos.ArticleDto;
import dimitrije.andzic.dtos.CommentDto;
import dimitrije.andzic.entities.*;
import dimitrije.andzic.repositories.AbstractRepository;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImplementation extends AbstractRepository implements ArticleRepository {
    @Override
    public Pagination<Article> getAllSortedArticlesByCreateDate(int pageSize, int page) {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSet commentResultset = null;
        ResultSet activitiesResultset = null;
        PreparedStatement commentsStatement = null;
        PreparedStatement activityStatement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("select articles.article_id, title, create_time, views, first_name, last_name, user_id, email, destinations.destination_id, name, description, content from articles join users using(user_id) join destinations using (destination_id) order by create_time desc limit ?, ?");
            statement.setInt(1, (page-1)*pageSize);
            statement.setInt(2, pageSize+1);
            resultSet = statement.executeQuery();
            commentsStatement = connection.prepareStatement("select comment_id, author, text, create_date from comments where article_id = ?");
            activityStatement = connection.prepareStatement("select activities.activity_id, keywords from activities join `articles-activities` using(activity_id) where article_id = ?");

            while (resultSet.next()) {
                Article article = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3).toLocalDateTime(), resultSet.getInt(4), resultSet.getString(12));
                article.setAuthor(new User(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(5), resultSet.getString(6)));
                article.setDestination(new Destination(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
                commentsStatement.setInt(1, article.getId());
                commentResultset = commentsStatement.executeQuery();
                activityStatement.setInt(1, article.getId());
                activitiesResultset = activityStatement.executeQuery();
                //trpanje komentara
                while (commentResultset.next()){
                    Comment c = new Comment(commentResultset.getInt(1), commentResultset.getString(2), commentResultset.getString(3),
                            commentResultset.getTimestamp(4).toLocalDateTime());
                    article.getComments().add(c);
                }
                //trpanje aktivitija
                while (activitiesResultset.next()){
                    Activity a = new Activity(activitiesResultset.getInt(1), activitiesResultset.getString(2));
                    article.getActivities().add(a);
                }
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeStatement(activityStatement);
            this.closeResultSet(commentResultset);
            this.closeStatement(commentsStatement);
            this.closeResultSet(activitiesResultset);
            this.closeConnection(connection);
        }

        boolean isLast = true;
        if(articles.size() > pageSize){
            isLast = false;
            articles.removeLast();
        }
        return new Pagination<>(articles, pageSize, page, isLast);
    }

    @Override
    public Pagination<Article> getAllSortedArticlesByViews(int pageSize, int page) {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSet commentResultset = null;
        ResultSet activitiesResultset = null;
        PreparedStatement commentsStatement = null;
        PreparedStatement activityStatement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("select articles.article_id, title, create_time, views, first_name, last_name, user_id, email, destinations.destination_id, name, description, content from articles join users using(user_id) join destinations using (destination_id) where create_time >= NOW() - INTERVAL 30 DAY order by views desc limit ?, ?");
            statement.setInt(1, (page-1)*pageSize);
            statement.setInt(2, pageSize+1);
            resultSet = statement.executeQuery();
            commentsStatement = connection.prepareStatement("select comment_id, author, text, create_date from comments where article_id = ?");
            activityStatement = connection.prepareStatement("select activities.activity_id, keywords from activities join `articles-activities` using(activity_id) where article_id = ?");

            while (resultSet.next()) {
                Article article = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3).toLocalDateTime(), resultSet.getInt(4), resultSet.getString(12));
                article.setAuthor(new User(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(5), resultSet.getString(6)));
                article.setDestination(new Destination(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
                commentsStatement.setInt(1, article.getId());
                commentResultset = commentsStatement.executeQuery();
                activityStatement.setInt(1, article.getId());
                activitiesResultset = activityStatement.executeQuery();
                //trpanje komentara
                while (commentResultset.next()){
                    Comment c = new Comment(commentResultset.getInt(1), commentResultset.getString(2), commentResultset.getString(3),
                            commentResultset.getTimestamp(4).toLocalDateTime());
                    article.getComments().add(c);
                }
                //trpanje aktivitija
                while (activitiesResultset.next()){
                    Activity a = new Activity(activitiesResultset.getInt(1), activitiesResultset.getString(2));
                    article.getActivities().add(a);
                }
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeStatement(activityStatement);
            this.closeResultSet(commentResultset);
            this.closeStatement(commentsStatement);
            this.closeResultSet(activitiesResultset);
            this.closeConnection(connection);
        }

        boolean isLast = true;
        if(articles.size() > pageSize){
            isLast = false;
            articles.removeLast();
        }
        return new Pagination<>(articles, pageSize, page, isLast);
    }

    @Override
    public Pagination<Article> getAllDestinationArticles(int pageSize, int page, int destination_id) {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSet commentResultset = null;
        ResultSet activitiesResultset = null;
        PreparedStatement commentsStatement = null;
        PreparedStatement activityStatement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("select articles.article_id, title, create_time, views, first_name, last_name, user_id, email, destinations.destination_id, name, description, content from articles join users using(user_id) join destinations using (destination_id) where destination_id = ? order by create_time desc limit ?, ?");
            statement.setInt(1, destination_id);
            statement.setInt(2, (page-1)*pageSize);
            statement.setInt(3, pageSize+1);
            resultSet = statement.executeQuery();
            commentsStatement = connection.prepareStatement("select comment_id, author, text, create_date from comments where article_id = ?");
            activityStatement = connection.prepareStatement("select activities.activity_id, keywords from activities join `articles-activities` using(activity_id) where article_id = ?");

            while (resultSet.next()) {
                Article article = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3).toLocalDateTime(), resultSet.getInt(4), resultSet.getString(12));
                article.setAuthor(new User(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(5), resultSet.getString(6)));
                article.setDestination(new Destination(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
                commentsStatement.setInt(1, article.getId());
                commentResultset = commentsStatement.executeQuery();
                activityStatement.setInt(1, article.getId());
                activitiesResultset = activityStatement.executeQuery();
                //trpanje komentara
                while (commentResultset.next()){
                    Comment c = new Comment(commentResultset.getInt(1), commentResultset.getString(2), commentResultset.getString(3),
                            commentResultset.getTimestamp(4).toLocalDateTime());
                    article.getComments().add(c);
                }
                //trpanje aktivitija
                while (activitiesResultset.next()){
                    Activity a = new Activity(activitiesResultset.getInt(1), activitiesResultset.getString(2));
                    article.getActivities().add(a);
                }
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeStatement(activityStatement);
            this.closeResultSet(commentResultset);
            this.closeStatement(commentsStatement);
            this.closeResultSet(activitiesResultset);
            this.closeConnection(connection);
        }

        boolean isLast = true;
        if(articles.size() > pageSize){
            isLast = false;
            articles.removeLast();
        }
        return new Pagination<>(articles, pageSize, page, isLast);
    }

    @Override
    public Pagination<Article> getAllActivityArticles(int pageSize, int page, int article_id) {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSet commentResultset = null;
        ResultSet activitiesResultset = null;
        PreparedStatement commentsStatement = null;
        PreparedStatement activityStatement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("select articles.article_id, title, create_time, views, first_name, last_name, user_id, email, destinations.destination_id, name, description, content from articles join users using(user_id) join destinations using (destination_id) join `articles-activities` using(article_id) where activity_id = ? order by create_time desc limit ?, ?");
            statement.setInt(1, article_id);
            statement.setInt(2, (page-1)*pageSize);
            statement.setInt(3, pageSize+1);
            resultSet = statement.executeQuery();
            commentsStatement = connection.prepareStatement("select comment_id, author, text, create_date from comments where article_id = ?");
            activityStatement = connection.prepareStatement("select activities.activity_id, keywords from activities join `articles-activities` using(activity_id) where article_id = ?");

            while (resultSet.next()) {
                Article article = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3).toLocalDateTime(), resultSet.getInt(4), resultSet.getString(12));
                article.setAuthor(new User(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(5), resultSet.getString(6)));
                article.setDestination(new Destination(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
                commentsStatement.setInt(1, article.getId());
                commentResultset = commentsStatement.executeQuery();
                activityStatement.setInt(1, article.getId());
                activitiesResultset = activityStatement.executeQuery();
                //trpanje komentara
                while (commentResultset.next()){
                    Comment c = new Comment(commentResultset.getInt(1), commentResultset.getString(2), commentResultset.getString(3),
                            commentResultset.getTimestamp(4).toLocalDateTime());
                    article.getComments().add(c);
                }
                //trpanje aktivitija
                while (activitiesResultset.next()){
                    Activity a = new Activity(activitiesResultset.getInt(1), activitiesResultset.getString(2));
                    article.getActivities().add(a);
                }
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeStatement(activityStatement);
            this.closeResultSet(commentResultset);
            this.closeStatement(commentsStatement);
            this.closeResultSet(activitiesResultset);
            this.closeConnection(connection);
        }

        boolean isLast = true;
        if(articles.size() > pageSize){
            isLast = false;
            articles.removeLast();
        }
        return new Pagination<>(articles, pageSize, page, isLast);
    }

    @Override
    public Article getById(int id) {
        Article article = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet commentResultset = null;
        ResultSet activitiesResultset = null;
        PreparedStatement commentsStatement = null;
        PreparedStatement activityStatement = null;
        PreparedStatement viewStatement = null;
        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select articles.article_id, title, create_time, views, first_name, last_name, user_id, email, destinations.destination_id, name, description, content from articles join users using(user_id) join destinations using (destination_id) where article_id = " + id);
            commentsStatement = connection.prepareStatement("select comment_id, author, text, create_date from comments where article_id = ? order by create_date");
            activityStatement = connection.prepareStatement("select activities.activity_id, keywords from activities join `articles-activities` using(activity_id) where article_id = ?");
            viewStatement = connection.prepareStatement("update articles set views = ? where article_id = ?");

            if (resultSet.next()) {
                article = new Article(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3).toLocalDateTime(), resultSet.getInt(4) + 1, resultSet.getString(12));
                article.setAuthor(new User(resultSet.getInt(7), resultSet.getString(8), resultSet.getString(5), resultSet.getString(6)));
                article.setDestination(new Destination(resultSet.getInt(9), resultSet.getString(10), resultSet.getString(11)));
                viewStatement.setInt(1, article.getViews());
                viewStatement.setInt(2, id);
                viewStatement.executeUpdate();
                commentsStatement.setInt(1, article.getId());
                commentResultset = commentsStatement.executeQuery();
                activityStatement.setInt(1, article.getId());
                activitiesResultset = activityStatement.executeQuery();
                //trpanje komentara
                while (commentResultset.next()){
                    Comment c = new Comment(commentResultset.getInt(1), commentResultset.getString(2), commentResultset.getString(3),
                            commentResultset.getTimestamp(4).toLocalDateTime());
                    article.getComments().add(c);
                }
                //trpanje aktivitija
                while (activitiesResultset.next()){
                    Activity a = new Activity(activitiesResultset.getInt(1), activitiesResultset.getString(2));
                    article.getActivities().add(a);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeStatement(activityStatement);
            this.closeResultSet(commentResultset);
            this.closeStatement(commentsStatement);
            this.closeResultSet(activitiesResultset);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public ArticleDto addArticle(ArticleDto article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO articles (title, views, user_id, destination_id, content) VALUES(?, ?, ?, ?, ?)", generatedColumns);
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, article.getAuthor_id());
            preparedStatement.setInt(4, article.getDestination_id());
            preparedStatement.setString(5, article.getContent());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                article.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public Article addActivity(ActivityDto a) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement statement;
        ResultSet resSet;
        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("select activity_id from activities where keywords = ?");
            statement.setString(1, a.getKeywords());
            resSet = statement.executeQuery();
            if(resSet.next()){
                a.setId(resSet.getInt(1));
            }
            else{
                String[] generatedColumns = {"id"};
                statement = connection.prepareStatement("insert into activities (keywords) value (?)", generatedColumns);
                statement.setString(1, a.getKeywords());
                statement.executeUpdate();
                resSet = statement.getGeneratedKeys();
                if(resSet.next()){
                    a.setId(resSet.getInt(1));
                }
            }

            preparedStatement = connection.prepareStatement("INSERT INTO `articles-activities` (article_id, activity_id) VALUES(?, ?)");
            preparedStatement.setInt(1, a.getArticle_id());
            preparedStatement.setInt(2, a.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return getById(a.getArticle_id());
    }

    @Override
    public Article addComment(CommentDto c) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO comments (article_id, author, text) VALUES(?, ?, ?)", generatedColumns);
            preparedStatement.setInt(1, c.getArticle_id());
            preparedStatement.setString(2, c.getAuthor());
            preparedStatement.setString(3, c.getText());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                c.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return getById(c.getArticle_id());
    }

    @Override
    public Article updateArticle(ArticleDto a) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE articles set title = ?, " +
                    "content = ?, " + " destination_id = ? " +
                    "where article_id = ?");
            preparedStatement.setString(1, a.getTitle());
            preparedStatement.setString(2, a.getContent());
            preparedStatement.setInt(3, a.getDestination_id());
            preparedStatement.setInt(4, a.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return getById(a.getId());
    }

    @Override
    public void deleteArticle(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE from articles "+
                    "where article_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public void deleteActivity(int activity_id, int article_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE from `articles-activities` "+
                    "where article_id = ? and activity_id = ?");
            preparedStatement.setInt(1, article_id);
            preparedStatement.setInt(2, activity_id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }
}
