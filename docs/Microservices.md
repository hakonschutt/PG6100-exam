# Setup 

Considerations: 
- Consider having a database microservices??

## Microservices


### User Details
Should be a container for user information such as their booking history, current tickets, email. 
Should email be used as the identifier? 

ID ... SECURITY? 


#### Entity
- ID
- Email
- Valid tickets
  - Pagination?
- Favorites
  - Pagination
- History
  - Pagination

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

#### Endpoints
- [ ] Endpoint 1


### Venues
Geolokasjon?
Informasjon om kinosalene.


#### Entity
ID
Geolocation
Adress
Events? 

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
- [ ] **PATCH**: /events/{id}/
- [ ] **DELETE**: /events/{id}


