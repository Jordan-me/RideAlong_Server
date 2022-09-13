# RideAlong_Server

Check out our RideAlong Client - https://github.com/Jordan-me/RideAlong
REST APIs implemented using Spring Boot Multi Module Maven Project.
This server support of registeration, login, find event and assign to events, pick up partners to travel with and more...

## How to Run
Build the project and his client, run Application.java at src->main->java->iob.
You got server on, now go to our client code and run the command: "yarn start" at the VS code terminal.

## REST APIs Endpoints
### Get User
  http://localhost:8085/iob/users/login/2022b.yarden.dahan/admin@google.com
  Accept: application/json

### Post User
  http://localhost:8085/iob/users?
  Accept: application/json
  Content-Type: application/json
  {
    "email": "player@google.com",
    "username": "demo username",
    "role":"player",
     "avatar": "av"
  }
### Put User
  http://localhost:8085/iob/users/2022b.yarden.dahan/yar@gmail.com
  Content-Type: application/json
  {
    "userId": {"domain":"2022b.yarden.dahan","email":"manager@google.com"},
    "role":"Manager",
    "username":"demo username",
    "avatar":"avatar"
  }
### Get Instances
  http://localhost:8085/iob/instances?userDomain=2022b.yarden.dahan&userEmail
  Accept: application/json
  Params: userDomain
          userEmail
### Post Instance 
  http://localhost:8085/iob/instances
  Accept: application/json
  Content-Type: application/json
  {
    "type":"OfficialEvent",
	  "name":"concert",
	  "active":true,
    "createdTimestamp":null,
    "createdBy":{"userId":{"domain":"2022b.yarden.dahan","email":"manager@google.com"}},
    "location":{"lat":125.5,"lng":133.0},
    "instanceAttributes":{"date":"futureDate","artist":"artist","countAttendToGo":0}
  }
### Put Instance
  http://localhost:8085/iob/instances/2022b.yarden.dahan/2b1f0f9f-6e56-492a-a945-c189f2fde673?
  Accept: application/json
  Content-Type: application/json
  Parameters: userDomain=2022b.yarden.dahan&userEmail=manager@google.com
  {
    "type":"traveller",
	  "name":"Yossi-Boaron",
	  "active":false,
    "createdTimestamp": "2022-05-18T17:50:52.517+00:00",
    "createdBy":{"userId":
                {"domain":"2022b.yarden.dahan",
                "email":"yarden.dahan@s.afeka.ac.il"}},
    "location":null,
    "instanceAttributes":null
  }
### Get Instance
  http://localhost:8085/iob/instances/2022b.yarden.dahan/d647a64b-b00c-4f60-87f1-52b2ee742441?
  Accept: application/json
  Parameters: userDomain=2022b.yarden.dahan&userEmail=player@google.com
  
### Post Activity:
  http://localhost:8085/iob/activities
  Accept: application/json
  Content-Type: application/json
  {
    "activityId":null,
    "type":"fetchSuggestedEvents",
    "instance": {
            "instanceId":{"domain": "2022b.yarden.dahan",     
                "id": "c8d2a515-47d2-40bf-941e-b12fe89a1a32"
            }
    },
    "createdTimestamp":null,
    "invokedBy": {
        "userId": {
            "domain": "2022b.yarden.dahan",
            "email": "yardda2@gmail.com"
        }
    },
    "activityAttributes":{"page":0,"size":5,"distance":120.0,"instanceType":"eventUser"}
}
And more...
