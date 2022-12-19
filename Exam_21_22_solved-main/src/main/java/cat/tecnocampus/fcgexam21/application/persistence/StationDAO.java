package cat.tecnocampus.fcgexam21.application.persistence;

import cat.tecnocampus.fcgexam21.domain.Station;

import java.util.List;

public interface StationDAO {
    List<Station> findAll();

    Station findByName(String nom);
}
