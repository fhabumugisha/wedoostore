# Documentation

Le service WedooStore est implementé avec Spring Boot 3, Java 17, PostGreSql . Ce service fourni des APIS REST.

### Architecture

![Architecture.drawio.png](Architecture.drawio.png)

### Database

![db_schema.png](db_schema.png)

### Lancer l'api

Sur un poste avec docker installer , aller dans le dossier wedoostore

```bash
docker compose up -d
```

### COmment tester l'api

Avec Swagger-Ui ou Postman

#### Swagger openapi

![swagger_openaapi-doc.PNG](swagger_openaapi-doc.PNG)

#### Collection postman

[Wedoogift API.postman_collection.json](Wedoogift%20API.postman_collection.json)
![Collection_postman.PNG](Collection_postman.PNG)

# Wedoogift Backend challenge

Thank you for your interest in joining our team!
Please complete the following challenge, and we will be glad to review your code and provide feedback.

## Guidelines

* Implement the solution in Java.
* Use the stack and architecture of your choice.
* Clone this repository (do not fork it).
* Commit your changes after completing each level, and include the .git folder when submitting your test.
* We do not expect a user interface for this challenge.

## Evaluation

Your solution will be evaluated based on the following criteria:

* Effective use of object-oriented programming principles.
* Code quality and cleanliness.
* Appropriate use of design patterns.
* Ability to write unit tests.

## Statements

Companies can use Wedoogift services to distribute:

- Gift deposits
- Meal deposits

### Gift deposits

Gift deposits has 365 days lifespan, beyond this period it will no longer be counted in the user's balance.

example:
John receives a Gift distribution with the amount of $100 euros from Tesla. he will therefore have $100 in gift cards in
his account.
He received it on 06/15/2021. The gift distribution will expire on 06/14/2022.

### Meal deposits

Meal deposit works like the Gift deposit excepting for the end date. In fact meal deposits expires at the end of
February of the year following the distribution date.

example:
Jessica receives a Meal distribution from Apple with the amount of $50 on 01/01/2020, the distribution ends on
02/28/2021.

* Implement one or two services allowing companies to distribute gift and meal deposits to a user if the company balance
  allows it.
* Implement a service to calculate the user's balance.
