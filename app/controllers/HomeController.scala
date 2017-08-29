package controllers

import javax.inject._

import play.api.libs.json.{JsArray, Json}
import play.api.mvc.{AnyContent, _}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import com.mongodb.casbah.Imports._
import models.Student
import play.api.Logger
import play.api.data._
import play.api.data.Forms._

import io.swagger.annotations._


/*
 * In HomeController, we define all the rest APIs here. These includes:
 *   1. "showdbs" which is the same with mongo command "show dbs"
 *   2. "getCollections" which is the same with mongo command "show collections"
 *   3. "getDocuments" which is the same with mongo command "db.collection.find()"
 *   4. "getMetadata" which is the same with mongo command "collection.stats"
*/

@Singleton
@Api(value = "/MongoConnections")
class HomeController @Inject() (configuration: play.api.Configuration) extends Controller {

  /*
   * Configuration for username/password and MongoDB from application.conf
  */
  private val basicAuth = new BasicAuthAction(configuration.underlying.getString("basic.auth.username"), configuration.underlying.getString("basic.auth.password"))
  val host = configuration.underlying.getString("mongodb.default.host")
  val client = MongoClient(host = host)

  @ApiOperation(
    value = "Show databases",
    notes = "This API is the same as Mongo Command: show dbs",
    httpMethod = "GET")
  @ApiResponses(Array (new ApiResponse(code = 500, message = "Internal server error")))
  def showdbs: Action[AnyContent] = basicAuth.async  {
    val futurePostsList: Future[scala.collection.mutable.Buffer[String]] = Future {
      client.getDatabaseNames()
    }
    val futurePostsJsonArray: Future[scala.collection.mutable.Buffer[String]] = futurePostsList.map { posts =>
      (posts)
    }
    futurePostsJsonArray.map { posts =>
      Ok(posts.toString())
    }
  }

  @ApiOperation(
    value = "Show collections",
    notes = "This API is the same as Mongo Command: show collections",
    httpMethod = "GET")
  @ApiResponses(Array (new ApiResponse(code = 500, message = "Internal server error")))
  def getCollections(@ApiParam(name = "databaseName", value = "Specify the database that you want to retrieve data", required = true)  databaseName: String): Action[AnyContent] = basicAuth.async {
    val db = client(databaseName)
    val futurePostsList: Future[scala.collection.mutable.Set[String]] = Future {
      db.collectionNames
    }
    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }
    futurePostsJsonArray.map { posts =>
      Ok(Json.prettyPrint(posts))
    }
  }


  @ApiOperation(
    value = "show documents",
    notes = "This API is the same as Mongo Command: db.collection.find()",
    httpMethod = "GET")
  @ApiResponses(Array (new ApiResponse(code = 500, message = "Internal server error")))
  def getDocuments(databaseName: String, collectionName: String, @ApiParam(value = "start point", required = true) offset: Int, @ApiParam(value = "end point", required = true) limit: Int)= basicAuth.async{
    val collection = client(databaseName)(collectionName)
    val futurePostsJsonArray: Future[String] = Future {
      "[%s]".format(
        collection.find(MongoDBObject.empty).skip(offset).limit(limit-offset).toList.mkString(",") //get an array of the 1000-1100th elements of a cursor
      )
    }
    futurePostsJsonArray.map { posts =>
      Ok(Json.prettyPrint(Json.parse(posts)))
    }
  }

  @ApiOperation(
    value = "show metadata in specific collection",
    notes = "This API is the similiar with Mongo Command: collection.stats",
    httpMethod = "GET")
  @ApiResponses(Array (new ApiResponse(code = 500, message = "Internal server error")))
  def getMetadata(databaseName: String, collectionName: String): Action[AnyContent] = basicAuth.async {
    val collection = client(databaseName)(collectionName)

    val documentCount = collection.getCount()
    val totalDocSize = collection.stats.getLong("size", 0L)
    val avgDocSize = totalDocSize / documentCount
    val numIndex = collection.stats.getLong("nindexes", 0L)

    val map = Map(
      "collection" -> collectionName,
      "documentCount" -> documentCount,
      "totalDocSize" -> totalDocSize,
      "avgDocSize" -> avgDocSize,
      "numIndex" -> numIndex
    )

    val futurePostsList = Future {
      scala.util.parsing.json.JSONObject(map)
    }
    val futurePostsJsonArray = futurePostsList.map { posts =>
      (posts.toString())
    }
    futurePostsJsonArray.map { posts =>
      Ok(posts)
    }
  }


  val studentForm = Form(
    mapping(
      "name" -> text,
      "gender" -> text,
      "age" -> number
    )(Student.apply)(Student.unapply)
  )

  //API handling Post Request to Add Document to Database
  //Currently statically bins post request to a specific form...
  def post = Action.async {
    implicit request =>
      studentForm.bindFromRequest().fold(
        formWithErrors => {
          Logger.error("Form has errors")
          Future.successful(BadRequest(views.html.collectionDocuments("db1", "students")(studentForm)))
        },
        student => {

          val db = client("db1")
          val collection = client("db1")("students")

          val newDocument = MongoDBObject(
            "name" -> student.name,
            "gender" -> student.gender,
            "age" -> student.age)


          val futureSavedDocuemnt: Future[com.mongodb.casbah.TypeImports.WriteResult] = Future {
            collection.save(newDocument)
          }

          futureSavedDocuemnt.map { insertedDocument =>
            //Ok(insertedDocument.toString())
            Redirect(controllers.routes.HomeController.viewDocuments("db1", "students"))
          }


        }
      )
  }


  def viewDocuments(databaseName: String, collectionName: String) = Action.async {
    implicit request =>
      val emptyForm = studentForm

      Future.successful(
        Ok(views.html.collectionDocuments(databaseName, collectionName)(studentForm))
      )
  }

  @ApiOperation(
    value = "delete document",
    notes = "This API is the same as Mongo Command: db.collection.deleteOne()",
    httpMethod = "GET")
  @ApiResponses(Array (new ApiResponse(code = 500, message = "Internal server error")))
  def deleteDocument(databaseName: String, collectionName: String, docID: String): Action[AnyContent] = Action.async {
    //// Casbah implentation
    val db = client(databaseName)
    val collection = client(databaseName)(collectionName)

    val objId = new ObjectId(docID)
    //build query to search for objid
    val query = MongoDBObject("_id" -> objId)

    //call remove
    val futurePostsList: Future[Option[DBObject]] = Future {
      collection.findAndRemove(query)
    }

    futurePostsList.map { deletedDocument =>
      Ok("Deleted Document \n id: " + docID + "\n In collection: " + collectionName)
    }
  }
}