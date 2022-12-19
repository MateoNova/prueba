package cat.tecnocampus.fcgexam21.application;


import cat.tecnocampus.fcgexam21.application.DTOs.DayTimeStartDTO;
import cat.tecnocampus.fcgexam21.application.DTOs.FavoriteJourneyDTO;
import cat.tecnocampus.fcgexam21.application.DTOs.FriendsDTO;
import cat.tecnocampus.fcgexam21.application.exception.UserDoesNotExistsException;
import cat.tecnocampus.fcgexam21.domain.*;
import cat.tecnocampus.fcgexam21.application.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FgcController {
    private StationDAO stationDAO;
    private UserDAO userDAO;
    private FavoriteJourneyDAO favoriteJourneyDAO;
    private JourneyDAO journeyDAO;
    private FriendDAO friendDAO;

    public FgcController(StationDAO stationDAO, UserDAO userDAO,
                         FavoriteJourneyDAO favoriteJourneyDAO, JourneyDAO journeyDAO,
                         FriendDAO friendDAO) {
        this.stationDAO = stationDAO;
        this.userDAO = userDAO;
        this.favoriteJourneyDAO = favoriteJourneyDAO;
        this.journeyDAO = journeyDAO;
        this.friendDAO = friendDAO;
    }

    public List<Station> getStations() {
        return stationDAO.findAll();
    }

    public Station getStation(String nom) {
        return stationDAO.findByName(nom);
    }

    //TODO 2.0 Note that userDAO.findByUsername only needs to return the User without her favorite journeys since they
    // are added afterwards
    public User getUser(String username) {
        //get the user
        User user = userDAO.findByUsername(username);

        //get the user's favorite journey
        user.setFavoriteJourneyList(favoriteJourneyDAO.findFavoriteJourneys(username));

        return user;
    }

    //TODO 2.0 Note that userDAO.findByUsername only needs to return the User without her favorite journeys since they
    // are added afterwards
    public List<User> getUsers() {
        //get the users
        List<User> users = userDAO.getUsers();

        //get the users' favorite journeys
        users.forEach(u -> u.setFavoriteJourneyList(favoriteJourneyDAO.findFavoriteJourneys(u.getUsername())));

        return users;
    }

    public boolean existsUser(String username) {
        return userDAO.existsUser(username);
    }

    public void addUserFavoriteJourney(String username, FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = convertFavoriteJourneyDTO(favoriteJourneyDTO);
        saveFavoriteJourney(favoriteJourney, username);
    }

    private void saveFavoriteJourney(FavoriteJourney favoriteJourney, String username) {
        String journeyId = saveJourneyIfDoesNotExist(favoriteJourney.getJourney());
        favoriteJourney.getJourney().setId(journeyId);
        favoriteJourneyDAO.saveFavoriteJourney(favoriteJourney,username);
    }

    private String saveJourneyIfDoesNotExist(Journey journey) {
        String journeyId = journeyDAO.getJourneyId(journey);
        if (journeyId.equals("-1")) {
            journeyId = UUID.randomUUID().toString();
            journey.setId(journeyId);
            journeyDAO.saveJourney(journey);
        }
        return journeyId;
    }

    public List<FavoriteJourney> getFavoriteJourneys(String username) {
        if (!existsUser(username)) {
            UserDoesNotExistsException e = new UserDoesNotExistsException("user " + username + " doesn't exist");
            e.setUsername(username);
            throw e;
        }
        return favoriteJourneyDAO.findFavoriteJourneys(username);
    }

    private FavoriteJourney convertFavoriteJourneyDTO(FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = new FavoriteJourney();
        favoriteJourney.setId(UUID.randomUUID().toString());
        Journey journey = new Journey(stationDAO.findByName(favoriteJourneyDTO.getOrigin()),
                                      stationDAO.findByName(favoriteJourneyDTO.getDestination()),
                                      "empty id");
        favoriteJourney.setJourney(journey);

        List<DayTimeStart> dayTimeStarts = favoriteJourneyDTO.getDayTimes().stream().map(this::convertDayTimeStartDTO).collect(Collectors.toList());
        favoriteJourney.setDateTimeStarts(dayTimeStarts);

        return favoriteJourney;
    }

    private DayTimeStart convertDayTimeStartDTO(DayTimeStartDTO dayTimeStartDTO) {
        return new DayTimeStart(dayTimeStartDTO.getDayOfWeek(), dayTimeStartDTO.getTime(), UUID.randomUUID().toString());
    }

    public Friends getUserFriends(String username) {
        return friendDAO.getFriends(username);
    }

    public List<Friends> getAllUserFriends() {
        return friendDAO.getFriends();
    }

    //TODO 4.2 ask the FriendsDAO to save the friend. Make sure the username exists in the DB and otherwise thow a UserDoesNotExistsException
    // with message "User <userame> doesn't exist"
    public void saveFriends(FriendsDTO friendsDTO) {
        if (!existsUser(friendsDTO.getUsername())) {
            UserDoesNotExistsException e = new UserDoesNotExistsException("User " + friendsDTO.getUsername() + " doesn't exist");
            e.setUsername(friendsDTO.getUsername());
            throw e;
        }

        Friends friends = convertFriendsDTO(friendsDTO);
        friendDAO.saveFriends(friends);
    }

    //TODO 4.3 you'll need to convert the DTO to a domain Friends
    private Friends convertFriendsDTO(FriendsDTO friendsDTO) {
        Friends friends = new Friends();
        List<String> friendsList = new ArrayList<>();
        friends.setUsername(friendsDTO.getUsername());
        friends.setFriends(friendsDTO.getFriends());
        return friends;
    }
}
