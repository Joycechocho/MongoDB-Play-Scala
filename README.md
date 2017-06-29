# Description
We will provide a simple Web interface to show what collections in the database, and the fields of each collection, and other meta data, such as time periods, etc.

AC: 
1) backend web (rest) service for the meta data about collections for a given mongoDB database

2) front end, web UI, to display the meta data

3) unit and integration tests created and passed


# Play Scala Starter

First, I used the Play Scala Starter example to start. We will remove some unused files later. 

# Environment

Play 2.5 as a web framework

ReactiveMongo 0.11.14 (a non-blocking and asynchronous Scala driver for MongoDB)

Add below commands into build.sbt
```
// only for Play 2.5.x
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14"
)
```

MongoDB (currently I have one dummy database named db1 and two dummy collections named students and terms)


## Running

Remember to add "Run Configuration" as SBT RUN if you are using intelliJ
```
sbt run
```

And then go to http://localhost:9000 to see the running web application.


## Progress 

Currently, we have rest APIs to show collections, show documents, show metadata... We will move onto the simple UI for displaying and keep adding more rest APIs. 


## rest APIs 
```
# An example controller showing a sample home page
GET     /dataservice                        controllers.HomeController.index

#Other APIs
GET     /dataservice/collection                controllers.HomeController.getCollections
GET     /dataservice/collection/:col                controllers.HomeController.getDocuments(col:String)
GET     /dataservice/collection/metadata/:col      controllers.HomeController.getMetadata(col:String)
```
Name of rest APIs can be modified later.  