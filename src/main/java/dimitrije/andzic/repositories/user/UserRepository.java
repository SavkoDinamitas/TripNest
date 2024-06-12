package dimitrije.andzic.repositories.user;

import dimitrije.andzic.Pagination;
import dimitrije.andzic.entities.User;

import java.util.List;

public interface UserRepository {
    Pagination<User> getAllUsers(int pageSize, int page);

    User findUser(String email);

    User findUserById(int id);

    User addUser(User user);

    User updateUser(User user);

    void changeStatus(int id);
}
