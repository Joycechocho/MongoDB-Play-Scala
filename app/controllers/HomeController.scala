package controllers

import javax.inject._

import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc.{AnyContent, _}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.CollStatsResult
import reactivemongo.api.indexes.NSIndex
import reactivemongo.api.{Cursor, CursorProducer, DefaultDB, MongoConnection}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.{collection, _}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents {

  def index = Action {implicit request =>
    Ok(views.html.index("Welcome to Analytic Platform"))
  }

  def showdbs = TODO

  def getCollections: Action[AnyContent] = Action.async {
    val futurePostsList: Future[List[String]] = db.collectionNames

    val res: List[String] = Await.result(futurePostsList, 1 second)
    //res(0)) is the first collection
    //res(1)) is the second collection

    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }
    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(Json.prettyPrint(posts))
      //Ok(views.html.showcols(posts.toString()))
      //Ok(views.html.showcols(res))
    }
  }


  def getDocuments(collectionName: String)= Action.async{
    val collection :JSONCollection = db.collection[JSONCollection](collectionName)
    val cursor: Cursor[JsValue] = collection.find(Json.obj()).cursor[JsValue]
    // gather all the JsObjects in a list
    val futurePostsList: Future[List[JsValue]] = cursor.collect[List]()
    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }
    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(Json.prettyPrint(posts))
    }
  }


  def getMetadataPage(collectionName: String): Action[AnyContent] = Action.async {
    val collection :JSONCollection = db.collection[JSONCollection](collectionName)
    // gather all the JsObjects in a list
    val futurePostsList: Future[CollStatsResult] = collection.stats()
    val futurePostsJsonArray: Future[CollStatsResult] = futurePostsList.map { posts =>
      (posts)
    }
    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(posts.toString)
    }
  }



}
