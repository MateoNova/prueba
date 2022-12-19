package cat.tecnocampus.fcgexam21.application.persistence;

import cat.tecnocampus.fcgexam21.domain.Friends;

import java.util.List;

public interface FriendDAO {
    Friends getFriends(String username);

    List<Friends> getFriends();

    void saveFriends(Friends friends);
}
