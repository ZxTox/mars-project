# MarsMile - Server Repository
Welcome to MarsMile's server repository

![Website](https://img.shields.io/website?style=for-the-badge&up_message=online&url=https%3A%2F%2Fproject-ii.ti.howest.be%2Fmars-04%2F)

[![license](https://img.shields.io/badge/license-MIT-blue)]()   [![version](https://img.shields.io/badge/version-Beta-blue)]() [![Security Rating](https://sonar.ti.howest.be/api/project_badges/measure?project=2021.project-ii%3Amars-server-04&metric=security_rating)](https://sonar.ti.howest.be/dashboard?id=2021.project-ii%3Amars-server-04) 

[![Quality gate](https://sonar.ti.howest.be/api/project_badges/quality_gate?project=2021.project-ii%3Amars-server-04)](https://sonar.ti.howest.be/dashboard?id=2021.project-ii%3Amars-server-04)

## Index
- [Documentation](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation)
    - [Introduction](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#introduction)
    - [Proof of Concept](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#proof-of-concept)
        - [Bookingsystem](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#bookingsystem)
        - [On board entertainment](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#on-board-entertainment)
        - [On board administration](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#on-board-administration)
        - [Bonus features](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#bonus-features)
    - [Deliverables](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#deliverables)
    - [The application itself](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#the-application-itself)
    - [Credits](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/documentation/-/blob/main/README.md#credits)
- [Client](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client)
  - [Features](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#features)
    - [Bonus Features](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#bonus-features)
  - [Bugs](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#bugs)
  - [How to start](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#how-to-start)
    - [Local deployment](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#local-deployment)
    - [Offsite deployment](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#offsite-deployment)
  - [Development environment](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/client/-/blob/main/README.md#development-environment)
  - [Server](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server)
    - [Back-end Stack](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#back-end-stack)
    - [Bugs](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#bugs)
    - [Endpoints](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#endpoints)
    - [Database](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#database)
    - [Domain](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#domain)
      - [Booking](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#booking)
      - [Chatroom](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#chatroom)
    - [Repositories](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#repositories)
    - [Before you start](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#before-you-start)
    - [Quick start](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#quick-start)
      - [Config project](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#config-project)
      - [Stripe settings](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#stripe-settings)
    - [Run the server](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#run-the-server)
      - [Via CLI](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#via-cli)
      - [Via IDE](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#via-ide)
    - [Testing](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#testing)
    - [What now?](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#what-now-?)
    - [Contributing](https://git.ti.howest.be/TI/2021-2022/s3/analysis-and-development-project/projects/group-04/server/-/blob/main/README.md#contributing)


## Back-end Stack
- [Vert.x](https://vertx.io/)
	- [Vert.x Core](https://vertx.io/docs/vertx-core/java/)
	- [Vert.x Web](https://vertx.io/docs/vertx-web/java/)
- [Java](https://www.java.com/en/)
	- [Gradle](https://gradle.org/)
	- [Stripe](https://stripe.com/)
- [H2 Database](https://www.h2database.com/html/main.html)

### 3rd party libraries


Facilitating online payments with the help of [Stripe](https://stripe.com)

## Bugs
**Stripe payment's won't work on our howest deployed version since the firewall is not allowing api calls to Stripe.**

## Endpoints
Our OpenAPI schema can be found here:[ `openapi-mars.yaml`](https://project-ii.ti.howest.be/monitor/swagger-ui/?url=https://project-ii.ti.howest.be/monitor/apis/group-04)

|Endpoint|Is Implemented?|
|---|---|
|GET `/api/bookings`|✅|
|GET `/api/sessions/{sessionId}`|✅|
|POST `/api/sessions`|✅|
|GET `/api/seats/{flightId}`|✅|
|GET `/api/bookings/data`|✅|
|GET `/api/chatusers`|✅|
|GET `/api/flights`|✅|
|POST **[Webhook for Stripe](https://stripe.com/docs/webhooks)** `/api/webhook`|✅|
|POST `/api/flights`|❌|
|PATCH `/api/flights/{flightId}`|❌|
|GET `/api/flights/position`|❌|
|GET `/api/flights/{flightId}/position`|❌|
|GET `/api/spaceships`|❌|
|POST `/api/spaceships/{spaceshipId}/maintenance`|❌|


## Database

- `flights` ➡️ All flights which can be booked
- `bookings` ➡️ Booking made by Martians
- `bookingPassengers` ➡️ Passengers for which the booking was made


## Domain (Classes)

### Booking
- `Booking`
- `Flight`
- `Passenger`
- `StripePayment`
- `TicketClass`
- `Currency`

### Chatroom
- `Chatroom`
  - `BroadcastEvent`
  - `DiscardEvent`
  - `Event`
  - `EventFactory`
  - `EventType`
  - `IncomingEvent`
  - `JoinEvent`
  - `MessageEvent`
  - `OutgoingEvent`
  - `UnicastEvent`


## Repositories
Utility class `MarsH2Repository` which manages the connection & SQL-queries with the H2-database.

## Before you start
Choose Zulu jdk version 11 or opendjk 11 (Configure through this through intelij)
Make sure to clone all the repositories client, server & documentation


- Use the following folder structure
 	- root_folder_with_name_of_choice
		- client
		- documentation
		- server

## Quick Start

### Config project

#### General setting file (port setting, H2 datbase)
Please create the config file in `src/main/resources/conf/config.json`

```json
{
  "http": {
    "port": 8080
  },
  "db" : {
    "url": "jdbc:h2:~/mars-db",
    "username": "",
    "password": "",
    "webconsole.port": 9000
  },
  "api" : {
    "url": "../documentation/api-spec/openapi-mars.yaml"
  }
}
```

### Stripe settings

Please fill in your Stripe credentials in `src/main/java/be/howest/ti/mars/logic/domain/StripePayment`

```java
    private static final String YOUR_DOMAIN = "YOUR_FRONT_END_DOMAIN"; 
    
	private static final String SUCCESS_URL = String.format("%s/SUCCESS_PAGE?session_id={CHECKOUT_SESSION_ID}", YOUR_DOMAIN);

    private static final String CANCEL_URL = String.format("%s/CANCEL_PAGE", YOUR_DOMAIN);


    private static final String ENDPOINT_SECRET = "STRIPE_WEBHOOK_SECRET_KEY";
    static {
        Stripe.apiKey = "YOUR PRIVATE STRIPE API KEY HERE"	
    }

```
## Run the server

**Make sure that port 8080 & 9000 are both open!**

### Via CLI
Building the jar
```bash
$ git clone SERVER_REPO
$ cd server
$ ./gradlew shadowJar
$ java -jar PATH_TO_JAR
# INFO: Succeeded in deploying verticle
```

Run without building JAR
```bash
$ git clone SERVER_REPO
$ cd server
$ ./gradlew run
# INFO: Succeeded in deploying verticle
```


### Via IDE (IntelliJ)
1. Open the server project with IntelliJ
2. Build the gradle project
3. Run the gradle task in `application/run`


The server will then be exposed on [127.0.0.1:8080/api/](127.0.0.1:8080/api/)

The H2 database will be exposed on [127.0.0.1:9000](127.0.0.1:9000)

## Testing

### Via CLI

```bash
$ cd mars-server/
$ ./gradlew test
```

### Via IntelliJ
1. Open the server project with IntelliJ
2. Build the gradle project
3. Run the gradle task in `verification/test`


## What now?

Make sure the client is running.

If you don't have the client, please consult the documentation about our client repo.


## Contributing
1. Check the open issues or open a new issue to start a discussion around your feature idea or the bug you found.
2. Fork the repository and make your changes.
3. Open a new pull request.


# License
### MIT License

Copyright (c) [2021] 

- Djelal Fida
- Milan Lemaire
- Dries Deschoolmeester
- Randy Bosmans
- Kobe Defoor


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
