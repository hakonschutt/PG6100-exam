# Setup

Considerations:
- Consider having a database microservices??

## Microservices

- [User Details](#user-details)
- Authentication
- Venues
- Movies
- Events
- Booking



### User Details
Should be a container for user information such as their booking history, current tickets, email.
Should email be used as the identifier?

ID ... SECURITY?


#### Entity
```java
@Entity
@Table(name="USERS")
class UserEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      @get:NotBlank
      var name: String?,

      @get:ElementCollection
      @get:NotNull
      var purchaseHistory: Set<Booking>? = setOf(),

      @get:ElementCollection
      @get:NotNull
      var favorites: Set<Movies>? = setOf(),
)```

#### Endpoints
*Should the collections have their own endpoints?*

- [ ] **GET**: /users
- [ ] **GET**: /users/{id}
- [ ] **POST**: /users
- [ ] **Figure out PATCH best practices**
- [ ] **PATCH**: /users/{id}/
- [ ] **PATCH**: /users/{id}/history/
- [ ] **PATCH**: /users/{id}/
- [ ] **DELETE**: /users/{id}

### Authentication
Authentication of the user.

#### Entity
- Email
- Valid tickets
  - Pagination?
- Favorites
  - Pagination
- History
  - Pagination

#### Endpoints
- [ ] Endpoint 1


### Venues
Geolokasjon?
Informasjon om kinosalene.


#### Entity #1 - Venues
ID
Geolocation
Adress
Events?

#### Entity #2 - Rooms
Id
Label
Layout
Seats?

#### Endpoints
- [ ] **GET**: /venues
- [ ] **GET**: /venues/{id}
- [ ] **POST**: /venues
- [ ] **Figure out PATCH best practices**
- [ ] **PATCH**: /venues/{id}/
- [ ] **DELETE**: /venues/{id}

### Movies
A collection of the movies.

#### Entity
Id
Title
Poster
Vimeo???
Events

#### Endpoints
- [ ] **GET**: /movies
- [ ] **GET**: /movies/{id}
- [ ] **POST**: /movies
- [ ] **Figure out PATCH best practices**
- [ ] **PATCH**: /movies/{id}/
- [ ] **DELETE**: /movies/{id}/

### Events
A collection microservice.
Keeps information about a movie, where it is being played, at what time, and in which room in the venue.
Potential use-case for GraphQL API.

#### Entity
ID
Date
Time
MovieId
VenueId
RoomId

#### Endpoints
- [ ] **GET**: /events
- [ ] **GET**: /events/{id}
- [ ] **Figure out PATCH best practices**
- [ ] **POST**: /events/
- [ ] **PATCH**: /events/{id}/
- [ ] **PUT**: /events/{id}/ **USE DATE TIME MOVIE VENUE AND ROOM????**
- [ ] **DELETE**: /events/{id}

### Booking
The bookings that has been made, by getting tickets, and implementation of Stripe??

#### Entity #1 - Ticket
ID
UserId
EventId
Seats

#### Endpoints
- [ ] **GET**: /tickets
- [ ] **GET**: /tickets/{id}
- [ ] **Figure out PATCH best practices**
- [ ] **PATCH**: /tickets/{id}/
- [ ] **DELETE**: /tickets/{id}

### Logger/Stalker - NTH
Logging of actions/events.

#### Entity #1 - Logger
ID

*PUT A SETTINGS FILE | WHICH CHANGES BASED ON LOG LEVEL*
#### Endpoints
- [ ] **GET**: /loggers
- [ ] **GET**: /logger/{id}
- [ ] **Figure out PATCH best practices**
- [ ] **POST**: /loggers/
- [ ] **DELETE**: /loggers/{id}
