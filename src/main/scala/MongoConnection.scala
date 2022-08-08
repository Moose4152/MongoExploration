import org.mongodb.scala.{Document, MongoClient, MongoCollection}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object MongoConnection extends App {
  val mongoConnectionURL = s"mongodb://localhost:27018/?minPoolSize=2&maxPoolSize=10"
  val mongoClient:MongoClient = MongoClient(mongoConnectionURL)
  val database = mongoClient.getDatabase("dev")
  val user:MongoCollection[Document] = database.getCollection("user")

  val result = Await.result(user.find().toFuture(),10 seconds)
  println(result)

}
