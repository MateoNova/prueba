package cat.tecnocampus.fcgexam21.application.persistence;

import cat.tecnocampus.fcgexam21.domain.Journey;

public interface JourneyDAO {
    int saveJourney(Journey journey);

    String getJourneyId(Journey journey);

    Journey findJourney(String journeyId);
}
