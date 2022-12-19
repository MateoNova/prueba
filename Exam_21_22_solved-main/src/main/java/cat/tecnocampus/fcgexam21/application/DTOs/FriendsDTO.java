package cat.tecnocampus.fcgexam21.application.DTOs;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class FriendsDTO {
    @Pattern(regexp = "^[a-z]*")
    @Size(min=3, max=255)
    private String username;

    @NotEmpty
    private List<String> friends;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}
