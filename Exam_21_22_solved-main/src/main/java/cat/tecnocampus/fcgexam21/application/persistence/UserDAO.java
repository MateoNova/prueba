package cat.tecnocampus.fcgexam21.application.persistence;

import cat.tecnocampus.fcgexam21.domain.User;

import java.util.List;

public interface UserDAO {
    User findByUsername(String username);

    List<User> getUsers();

    boolean existsUser(String username);
}
