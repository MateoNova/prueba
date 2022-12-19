package cat.tecnocampus.fcgexam21.persistence;

import cat.tecnocampus.fcgexam21.domain.Friends;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FriendDAO implements cat.tecnocampus.fcgexam21.application.persistence.FriendDAO {
    private JdbcTemplate jdbcTemplate;

    ResultSetExtractorImpl<Friends> friendsRowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("username", "friend")
                    .newResultSetExtractor(Friends.class);

    public FriendDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Friends getFriends(String username) {
        List<Friends> result;
        String query = "select * from friend where username = ?";

        result = jdbcTemplate.query(query, friendsRowMapper, username);
        return result.get(0);
    }

    public List<Friends> getFriends() {
        String query = "select * from friend";
        return jdbcTemplate.query(query, friendsRowMapper);
    }

    //TODO 4.1 insert new friends to the data base. Note that a Friends object has a list of friends. So, you need to inset
    // a new row foreach friend. Use a batch update in order to do a unique commit to the database
    public void saveFriends(Friends friends) {
        String query = "insert into friend(username, friend) values(?, ?)";

        jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        String friend = friends.getFriends().get(i);
                        preparedStatement.setString(1, friends.getUsername());
                        preparedStatement.setString(2, friend);
                    }

                    @Override
                    public int getBatchSize() {
                        return friends.getFriends().size();
                    }
                });

    }
}
