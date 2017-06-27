package controllers

import javax.inject._

import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.CollStatsResult
import reactivemongo.api.indexes.NSIndex
import reactivemongo.api.{Cursor, CursorProducer, DefaultDB, MongoConnection}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends Controller with MongoController with ReactiveMongoComponents  {

  def index = Action {implicit request =>
    Ok(views.html.index("Welcome to Analytic Platform"))
  }

  def showdbs = TODO

  def showcols: Action[AnyContent] = Action.async {
    val futurePostsList: Future[List[String]] = db.collectionNames //should define in services
    val futurePostsJsonArray: Future[JsArray] = futurePostsList.map { posts =>
      Json.arr(posts)
    }
    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      //Ok(Json.prettyPrint(posts))
      Ok(views.html.showcols(posts.toString()))
    }
  }

  def collection_1 :JSONCollection = db.collection[JSONCollection]("students")
  def collection_2 :JSONCollection = db.collection[JSONCollection]("terms")

  def collection1_metadata: Action[AnyContent] = Action.async {
    // gather all the JsObjects in a list
    val futurePostsList: Future[CollStatsResult] = collection_1.stats()
    val futurePostsJsonArray: Future[CollStatsResult] = futurePostsList.map { posts =>
      (posts)
    }
    // everything's ok! Let's reply with the array
    futurePostsJsonArray.map { posts=>
      Ok(posts.toString)
    }
  }

  def col1: Action[AnyContent] = Action.async {
    // let's do our query
    val cursor: Cursor[JsValue] = collection_1.find(Json.obj()).cursor[JsValue]
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

  def col2: Action[AnyContent] = Action.async {
    // let's do our query
    val cursor: Cursor[JsValue] = collection_2.find(Json.obj()).cursor[JsValue]
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


}
