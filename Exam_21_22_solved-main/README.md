# Internet Lab: Final Examination
# FGC La Pobla line: Favorite Journeys

The domain of the exercise is the public transportation (Ferrocarrils de la Generalitat de Catalunya - Catalan Trains) and their users.
Its aim is to give some complementary services around FGC lines.

## Before you begin
* Read this file carefully
* Clone the repository to your computer
* BEFORE **touching** the code, run the application and see it working on your computer. Request for example GET http://localhost:8080/api/stations 
(need to log as user. Use basic security with username: tina and password: password123)
* Read the questions and the readme
* When attempting to answer the questions, modify the code in small steps and try the application (run it) after every step. In this way is easier to track possible errors
* A code that doesn't compile or run will be marked zero points
* All the questions are independent and can be answered in any order
* In the code you'll see **TODO**s where you need to insert new code. TODOs explain what you need to do and may contain some clues. Please,
don't delete the TODOs from the code. TODOs are numbered according to the question number. When a question has more than one TODO they are
numbered TODO X.1, TODO X.2 and so on, where X is the question number. There are few TODOs that don't need any code, they are there to explain code relevant to the question (and its answer) 

## Classes in the domain:
* User
* Station (name and position)
* Journey (consist of two stations: the origin and the destination. Both stations cannot be the same)
* DayTimeStart (consist of a dayOfWeek and a start time)
* FavoriteJourney (a journey that a user travels frequently and the moment he uses it: consists of a Journey and a list of DayTimeStart)
* Friends (a username, existing as a user in the system, that declares who are her friends. The later names don't need to be in the system).
A user cannot have the same friend twice.

## Notes on the Architecture
This application has an anemic domain model, that is, it has very few logics. For this reason (and to make things easier) we decided to use
as few DTOs as possible:
* The DAOs take in parameters and return domain objects rather than DTOs
* Few DTOs are used to transfer data from the REST API to the application controller
* At this moment there are only FavoriteJourneyDTO and DayTimeStartDTO used to POST a new favourite journey

## Application's entry points
* @GetMapping("/api/users") gets the users and their favorite journeys
* @GetMapping("/api/users/{username}") gets a given user with her favorite journeys
* @GetMapping("/api/users/me") gets the logged-in user with her favorite journeys
* @GetMapping("/api/user/favoriteJourneys") gets the favorite journeys of the logged-in user
* @GetMapping("/api/stations") gets a list of all the stations
* @GetMapping("/api/stations/{nom}") gets the station with name "nom"
* @GetMapping("/api/user/friends") gets the friends of the logged-in user
* @GetMapping("/api/users/friends") gets the friends of all the users
* @PostMapping("/api/user/favoriteJourney") creates a new favorite journey for the logged-in user.
The body (a favorite Journey DTO) could be:
```
{
  "origin": "Lleida-Pirineus",
  "destination": "Alcoletge",
  "dayTimes": [
      {"dayOfWeek" : "Monday",
       "time" : "12:50"
       },
      {"dayOfWeek" : "Tuesday",
       "time" : "13:50"
       }]
 }
  ```
* @PostMapping("/users/friends") create friends for a user (specified in the body). To be done!

## The application has already two users:
* **username**: tina, **password**: password123 (with ROLE_USER and ROLE_ADMIN)
* **username**: joanra, **password**: password123 (with ROLE_USER)

## Questions (2 points each)

1. Note that security is already implemented and it is using basic authentication. So, when making requests you should add the username
and password in the headers. 

In this question you need to modify the security configuration so that
* @GetMapping("/api/users") **accessed by ADMIN**
* @GetMapping("/api/users/{username}") **accessed by ADMIN**
* @GetMapping("/api/users/me") **accessed by USER**
* @GetMapping("/api/user/favoriteJourneys") **accessed by USER**
* @GetMapping("/api/stations") **accessed by everybody**
* @GetMapping("/api/stations/{nom}") **accessed by everybody**
* @GetMapping("/api/user/friends") **accessed by USER**
* @GetMapping("/api/users/friends") **accessed by both USER and ADMIN**
* @PostMapping("/api/user/favoriteJourney") **accessed by USER**
* @PostMapping("/users/friends") **accessed by ADMIN**

2. Write the methods in order to get the users from the database see TODO 2.0 in FgcController, and TODO 2.1 and TODO 2.2
in UserDAO. Note that the sql queries you need are simple since no join is required

3. When posting a user's favorite journey we need to validate the data. Namely
   * The origin and destination names must be of size between 4 and 25 characters. If not the message should be: "Size must be between 4 and 25" 
   * The day of week (DayTimeStart) must be Monday, Tuesday, ... If not the message must be "Day of the week should be a week day name beginning in capital letter"
   * The time (DayTimeStart) must follow the pattern 00:00  If not the message must be message = "Time should follow the pattern 00:00"

   Also make sure that the error messages get to the client by treating the exceptions: ConstraintViolationException and MethodArgumentNotValidException
   
4. Create the POST "/users/friends" in order to add new friends to a user. You'll need to implement the whole process, that is,
the mapping in the FGCRestController, a method in FgcController, a method in FriendDAO and also use a DTO because we want to validate it.
To see how friends look like you may want to try (call) the request /api/users/friends
The required validations are:
   * The username must begin with a lower case letter and must be 3 to 255 characters long
   * The list of friend cannot be empty

5. The API (or some advice) should send appropriate messages when errors occur. Namely, it should intercept the following exceptions 
and response with status and messages:
   * UserDoesNotExistsException: return NOT_FOUND as status and the exception message
   * EmptyResultDataAccessException: return NOT_FOUND as status and "Resource not found" as message
   * SameOriginDestinationException: return CONFLICT as status and "Origin and destination must be different" as message