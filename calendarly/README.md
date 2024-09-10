# Harbor Take Home Project

Welcome to the Harbor take home project. We hope this is a good opportunity for you to showcase your skills.

## The Challenge

Build us a REST API for calendly. Remember to support

- Setting own availability
- Showing own availability
- Finding overlap in schedule between 2 users

It is up to you what else to support.

## Expectations

We care about

- Have you thought through what a good MVP looks like? Does your API support that?
- What trade-offs are you making in your design?
- Working code - we should be able to pull and hit the code locally. Bonus points if deployed somewhere.
- Any good engineer will make hacks when necessary - what are your hacks and why?

We don't care about

- Authentication
- UI
- Perfection - good and working quickly is better

It is up to you how much time you want to spend on this project. There are likely diminishing returns as the time spent goes up.

## Submission

Please fork this repository and reach out to Prakash when finished.

## Next Steps

After submission, we will conduct a 30 to 60 minute code review in person. We will ask you about your thinking and design choices.

## Supported Functional Requirements
- Setting up own availability.
- Showing own availability.

- Finding overlap in schedule between 2 users.
- See availability of users, using user id for given start and end time range.
- Book a meeting.
- The availability owner can accept or decline a booking.
- If a person's slot is already booked for a given time slot, we can still book it, the availability owner has to decide which booking they have to entertain.

[//]: # (- )

## Constraints
- A meeting must have exactly 2 users.
- We will not support recurring meetings(as a part of MVP).
- We will support only one timezone.

## Improvements
- Instead of setting up own availability, we can set up blocked time and all other slots are open by default.
- We should write unit tests for better code testing.
- We should do dependency injection via constructor rather than field DI, for mocking.

## Next Requirements
- We should support more than 2 people in the meeting.
- We can add support for recurring meetings.

## Clarifications
- Since a single availability slot can be booked by multiple people, it is not required to be thread safe.

## How to Run 
This application can be run by following the below steps:
  - Create the jar file by running: 
    - mvn clean package
  - This will create a jar file under the target directory.
  - The project has Dockerfile and docker-compose. It can be run by the below command:
    - docker-compose up
  - You can now hit the APIs using postman at localhost:8080