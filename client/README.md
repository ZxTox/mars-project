# MarsMile Client Repository
Welcome to the Client Repository of the MarsMile bookingsystem. This README.md file will instruct you to run a local version of the MarsMile booking system.

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

## Features
At the time of writing (22/12/2021), the following Features are implemented:

- Login
  - Simulation of a succesfull login with MarsID using NFC.
  - Simulation of a failed login with MarsMile using NFC.
- My account
  - Overview of bought tickets
  - Groupschat
  - Possibilty to go to the booking page.
- About us page
- Flight classes
- Contact page
- Booking a ticket
  - Searching a flight using filters (Destination, Class, Departure time and amount of passengers)
  - Seat selection
  - Booking Overview
  - Payment with external API (Stripe)

### Bonus features
In addition to these features, the following bonus features were also implemented:
- CSS animations (Error messages and animations when switching pages)
- Use of external APIs or external online services in
the server project (payment with Stripe)

## Bugs
**Stripe payment's won't work on our howest deployed version since the firewall is not allowing api calls to Stripe.**

So we decided to host it on a private linux server.

[Client link](https://zxtox.com)

[Server link](https://api.zxtox.com)

## How to Start
### Local deployment
#### 1. Configuring the server
See the [Server configuration]()
#### 2. Configuring the web client
1. Clone the client repository to your machine
2. Open the Client Repository in your IDE of choice.
3. In the `src` directory, add a JSON file and call it `config.json`. This file should be filled with the following code:
```json
{
  "host": "http://localhost:8080",
  "folder": "",
  "group": ""
}
```
4. Using your IDE, open the `index.html` section.

### Offsite deployment
If you want, you can also visit the [version](https://project-ii.ti.howest.be/mars-04/) that is running on Howest's servers, which is fully working and identical to the one on this repo.

## Development environment
While this application has been made for standard desktop environments (16:9) in mind, the website should be flexible enough to also been used on mobile devices like phones and tablets.
