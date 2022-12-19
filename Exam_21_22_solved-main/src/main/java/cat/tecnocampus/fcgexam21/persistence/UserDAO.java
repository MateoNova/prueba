package cat.tecnocampus.fcgexam21.persistence;


import cat.tecnocampus.fcgexam21.application.exception.UserDoesNotExistsException;
import cat.tecnocampus.fcgexam21.domain.User;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.simpleflatmapper.jdbc.spring.RowMapperImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO implements cat.tecnocampus.fcgexam21.application.persistence.UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapperImpl<User> userRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username")
                    .newRowMapper(User.class);

    ResultSetExtractorImpl<User> usersRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username")
                    .newResultSetExtractor(User.class);

    /* TODO 2.2 get the "username" user from the database. Write the query and query the DB using the appropriate row mapper from above
        If the user doesn't exist throw an UserDoesNotExistsException with the message "User <username> doesn't exist"
        Make sure to read TODO_2.0 (only get the user without Favorite Journeys. You don't need a join in the query) */
    public User findByUsername(String username) {
        final String query = "select username, name, second_name, email from user where username = ?";

        try {
            return jdbcTemplate.queryForObject(query, userRowMapper, username);
        }
        catch (EmptyResultDataAccessException e) {
            throw new UserDoesNotExistsException("User " + username + " doesn't exist");
        }
    }

    //TODO 2.1 get all users from the database. Write the query and query the DB using the appropriate row mapper from above
    //   Make sure to read TODO_2.0 (only get the user without Favorite Journeys. You don't need a join in the query)
    public List<User> getUsers() {
        final String query = "SELECT username, name, second_name, email FROM USER";

        return jdbcTemplate.query(query, usersRowMapper);
    }

    public boolean existsUser(String username) {
        final String query = "SELECT count(*) FROM user WHERE username = ?";

        try {
            Integer cnt = jdbcTemplate.queryForObject(query, Integer.class, username);
            return cnt > 0;
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
