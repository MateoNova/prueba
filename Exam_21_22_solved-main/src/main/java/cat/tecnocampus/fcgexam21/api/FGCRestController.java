package cat.tecnocampus.fcgexam21.api;

import cat.tecnocampus.fcgexam21.application.DTOs.FavoriteJourneyDTO;
import cat.tecnocampus.fcgexam21.application.DTOs.FriendsDTO;
import cat.tecnocampus.fcgexam21.application.FgcController;
import cat.tecnocampus.fcgexam21.domain.FavoriteJourney;
import cat.tecnocampus.fcgexam21.domain.Friends;
import cat.tecnocampus.fcgexam21.domain.Station;
import cat.tecnocampus.fcgexam21.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/")
public class FGCRestController {
    private FgcController fgcController;

    public FGCRestController(FgcController fgcController) {
        this.fgcController = fgcController;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return fgcController.getUsers();
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return fgcController.getUser(username);
    }

    @GetMapping("/users/me")
    public User getLoggedInUser(Principal principal) {
        return fgcController.getUser(principal.getName());
    }

    @GetMapping("/stations")
    public List<Station> getStations() {
        return fgcController.getStations();
     }

    @GetMapping("/stations/{nom}")
    public Station getStation(@PathVariable String nom) {
        return fgcController.getStation(nom);
    }

    @PostMapping("/user/favoriteJourney")
    public void postAddFavoriteJourney(Principal principal, @RequestBody @Valid FavoriteJourneyDTO favoriteJourney) {
        fgcController.addUserFavoriteJourney(principal.getName(), favoriteJourney);
    }

    @GetMapping("/user/favoriteJourneys")
    public List<FavoriteJourney> getFavoriteJourneys(Principal principal) {
        return fgcController.getFavoriteJourneys(principal.getName());
    }

    @GetMapping("/user/friends")
    public Friends getFriends(Principal principal) {
        return fgcController.getUserFriends(principal.getName());
    }

    @GetMapping("/users/friends")
    public List<Friends> getAllFriends() {
        return fgcController.getAllUserFriends();
    }

    //TODO 4.4 add the post mapping to save the friends. You only need one parameter since the FriendsDTO has the username
    @PostMapping("/users/friends")
    public void saveFriends(@RequestBody @Valid FriendsDTO friendsDTO) {
        fgcController.saveFriends(friendsDTO);
    }
}
