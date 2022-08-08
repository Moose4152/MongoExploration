package Registries

import org.bson.codecs.configuration.{CodecProvider, CodecRegistries}
import org.mongodb.scala.bson.codecs.Macros
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection}
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Codecs extends App {
/** Sample json object stored in dev db , Iris Collection */
  /**
   * {
    "_id" : ObjectId("62f0e58bcd9d9d40ec98723b"),
    "sepalLength" : 5.1,
    "sepalWidth" : 3.5,
    "petalLength" : 1.4,
    "petalWidth" : 0.2,
    "species" : "setosa"
  }
   */


  val mongoConnectionURL = s"mongodb://127.0.0.1:27018/?minPoolSize=1&maxPoolSize=2"

  /** Note that the case class Params name should be as equal to Header saved in  the Mongo Doc */
  case class Iris(sepalLength:Double, sepalWidth:Double, petalLength:Double, petalWidth:Double, species:String)
  val IrisCodecProvider = Macros.createCodecProvider[Iris]()
  val allCodecProvider = Seq(IrisCodecProvider)
  /** create registries using codecs */
  val IrisCodecRegistry = fromRegistries(fromProviders(IrisCodecProvider),DEFAULT_CODEC_REGISTRY)

  val mongoClient:MongoClient = MongoClient(mongoConnectionURL)

  /** assigning the registries with to databases */
  val devDB: MongoDatabase = mongoClient.getDatabase("dev").withCodecRegistry(IrisCodecRegistry)
  val IrisCollection:MongoCollection[Iris]  = devDB.getCollection("Iris")


  val selector = Filters.eq("species","setosa")
  val doc: Seq[Iris] = Await.result(IrisCollection.find(selector).toFuture,10 seconds)

  doc.foreach(println)





}
