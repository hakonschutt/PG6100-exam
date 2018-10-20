# Microservices

Considerations:
- Consider having a database microservices??

## Microservices

- [User Details](#user-details)
- [Authentication](#authentication)
- [Venues](#venues)
- [Movies](#movies)
- [Events](#events)
- [Booking](#booking)
- [Chat](#chat)
- [Logger/Stalker](#loggerstalker---nth)



### User Details
Should be a container for user information such as their booking history, current tickets, email.
Should email be used as the identifier?

ID ... SECURITY?


#### Entity
- Id (Big int)
- Name (String)
- ChatId (List<Big int>)
- PurchaseHistory (List<Bookings>)
  - Pagination?
- Favorites (List<Movies>)
  - Pagination

```java
@Entity
class UserEntity(

      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var name: String,
      var chatIds: Set<Long>,
      var purchaseHistory: Set<Booking>,
      var favorites: Set<Movies>
){}
```

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
- Email (String)
- Password (String)
- UserId (Big int)

```java
@Entity
class AuthEntity(
      @get:Id
      var email: String?,

      var password: String?,
      var userId: Long? = null
){}
```

#### Endpoints
- [ ] Endpoint 1


### Venues
Geolokasjon?
Informasjon om kinosalene.


#### Entity #1 - Venues
- ID (Big int)
- Name (String)
- Geolocation (String)
- Adress (String)
- Rooms (List<Rooms>)

```java
@Entity
class VenueEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var name: String,
      var geolocation: String,
      var address: String,
      var rooms: Set<Rooms>
){}
```

#### Entity #2 - Rooms
- Id (Big int)
- Name (String)
- Rows (int)
- Columns (int)

```java
@Entity
class RoomEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var name: String,
      var rows: Int,
      var columns: Int
){}
```

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
- Id (Big int)
- Title (String)
- Poster (String)
- Cover Art (String)
- Trailer (String)
- Metascrore (int)
- Synopsis (String)
- Date (Date)
- Actors (List<String>)
- Genere (List<String>)
- Length (int)

```java
@Entity
class MovieEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var title: String,
      var poster: String,
      var coverArt: String,
      var trailer: String,
      var metaScore: Int,
      var synopsis: String,
      var date: Date,
      var actors: Set<String>,
      var generes: Set<String>,
      var length: Int
){}
```

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
- Id (Big int)
- Date (Date)
- Time (Time?)
- MovieId (Big int)
- VenueId (Big int)
- RoomId (Big int)
- Seats (Int[][])

```java
@Entity
class EventEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var date: Date,
      var time: String,
      var movieId: Long,
      var venueId: Long,
      var roomId: Long,
      var seats: Integer[][]? = null
){}
```

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

#### Entity #1 - Booking
- Id (Big int)
- UserId (Big int)
- EventId (Big int)
- Tickets (List<Ticket>)

```java
@Entity
class BookingEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var userId: Long,
      var eventId: Long,
      var tickets: Set<Ticket>? = null
){}
```

#### Entity #2 - Ticket
- Id (Big int)
- Seat (String)
- price (Double)

```java
@Entity
class TicketEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var seat: String,
      var price: Double
){}
```

#### Endpoints
- [ ] **GET**: /tickets
- [ ] **GET**: /tickets/{id}
- [ ] **Figure out PATCH best practices**
- [ ] **PATCH**: /tickets/{id}/
- [ ] **DELETE**: /tickets/{id}

### Chat
Customer support with websockets

#### Entity #1 - Chat
- Id (Big int)
- Chat (List<String>)

```java
@Entity
class ChatEntity(
      @get:Id @get:GeneratedValue
      var id: Long? = null,

      var chat: Set<String>
){}
```

#### Endpoints
- [ ] **GET**: /chats

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
