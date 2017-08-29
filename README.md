# Description
We will provide a simple Web interface to show what collections in the database, and the fields of each collection, and other meta data, such as time periods, etc.

# Features
Data access rest API

UI display

Rest API documentation: Swagger UI

Authentication: Basic Auth with username: user and password: pass


# Environment

Rest API -> Scala 2.11.11

Framework -> Play 2.5

Database -> MongoDB

Scala/Mongo Driver -> Casbah 3.1.1

Build Tool => SBT

API Documentation -> Swagger UI


# Code Structure

####app/controllers/HomeController.scala
 Here we define all the rest APIs here. These includes:
 *    "showdbs" which is the same with mongo command "show dbs"
 *    "getCollections" which is the same with mongo command "show collections"
 *    "getDocuments" which is the same with mongo command "db.collection.find()"
 *    "getMetadata" which is the same with mongo command "collection.stats"

####app/controllers/BasicAuthAction.scala
Here we check if the username and password matches as setting

####conf/routes
This file defines all application routes 

####conf/application.conf
This is the main configuration file for the application, includes the mongodb and username/password setting

####public/html/index.html
This is the front end page to dispaly what data we have from the hub. 

####public/javascript/index.js
Here we make the rest API calls to retrieve and parse data from the hub. 



# Running

Remember to add "Run Configuration" as SBT RUN if you are using intelliJ
```
sbt run
```

And then go to http://localhost:9000/index.html to see the running web application.

If you would like to see rest API documentation, go to http://localhost:9000/docs/



