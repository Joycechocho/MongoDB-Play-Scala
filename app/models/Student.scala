package models

import play.api.libs.json.Json

/**
  * Created by justin on 7/5/2017.
  */
case class Student(name: String, gender: String, age: Int)

object Student{
  implicit val studentJsonFormat = Json.format[Student]
}