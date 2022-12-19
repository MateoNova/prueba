package cat.tecnocampus.fcgexam21.application.persistence;

import cat.tecnocampus.fcgexam21.domain.FavoriteJourney;

import java.util.List;

public interface FavoriteJourneyDAO {
    void saveFavoriteJourney(FavoriteJourney favoriteJourney, String username);

    List<FavoriteJourney> findFavoriteJourneys(String username);
}
