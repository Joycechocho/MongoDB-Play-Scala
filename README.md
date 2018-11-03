# Description
From the front-end to the back-end (develped in Scala), a web service that fetches data from MongoDB and has a simple interface for data displaying.

# Features
Data access REST API

UI display

Rest API documentation: Swagger UI

Authentication: Basic Auth


# Environment

Rest API -> Scala 2.11.11

Framework -> Play 2.5

Database -> MongoDB

Scala/Mongo Driver -> Casbah 3.1.1

Build Tool => SBT

API Documentation -> Swagger UI


# Code Structure

#### app/controllers/HomeController.scala
 *    "showdbs" which is the same with mongo command "show dbs"
 *    "getCollections" which is the same with mongo command "show collections"
 *    "getDocuments" which is the same with mongo command "db.collection.find()"
 *    "getMetadata" which is the same with mongo command "collection.stats"

#### app/controllers/BasicAuthAction.scala
Check if the username and password matches as setting

#### conf/routes
Defines all application routes 

#### conf/application.conf
Main configuration file for the application

#### public/html/index.html
Front end page dispalys what data we have from the hub. 

#### public/javascript/index.js
Make rest API calls to retrieve and parse data from the hub. 



# Running
Turn on your MongoDB Connection 
Remember to add "Run Configuration" as SBT RUN if you are using intelliJ
```
sbt run
```

And then go to http://localhost:9000/index.html to see the running web application.

If you would like to see rest API documentation, go to http://localhost:9000/docs/



