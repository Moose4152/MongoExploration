package CRUD

import com.mongodb.client.model.CreateCollectionOptions
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonDouble, BsonInt32, BsonString}
import org.mongodb.scala.model.{Filters, ValidationOptions}
import org.mongodb.scala.{Document, MongoClient, Observable}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object MongoCRUD extends App {
//  TODO  1.Create
//  TODO  2.Read
//  TODO  3.Update
//  TODO  4.Delete

  val mongoConnectionURL = s"mongodb://localhost:27018/?minPoolSize=2&maxPoolSize=3"
  val mongoClient:MongoClient = MongoClient(mongoConnectionURL)
  val database = mongoClient.getDatabase("dev")



/**
  {
    FirstName : "Arjun",
    LastName : "Sigh",
    Address : ["Jamnagar House, 10A, Akbar Rd",
    "New Delhi, Delhi 110001"],
    Job : "Software Enginner",
    Company : "Google",
    Age : 32,
    Experience(in years) : 7.2
  }
  */

  val collectionName = "Employee"
  /** to create new collection */

  val createCollectionOptions = new CreateCollectionOptions().capped(true).sizeInBytes(1024)
  val validatorOptions = ValidationOptions().validator(Filters.or(Filters.exists("FirstName"),Filters.exists("Age")))
  createCollectionOptions.validationOptions(validatorOptions)

  //  val createStatus = Await.result(database.createCollection(collectionName,createCollectionOptions).toFuture(),10 seconds)
  /** since we have added the validator checks any document which do not have FirstName and Age , its insertion will fail */


  /** simple method to create a document in a new collection */
  val insertDocument =  new BsonDocument()
  insertDocument.append("FirstName",BsonString("Arjun")).append("LastName" ,BsonString("Singh")).append("Address",
    BsonArray(BsonString("Jamnagar House, 10A, Akbar Rd"),
    BsonString("New Delhi, Delhi 110001")))
    .append("Job",BsonString("Software Engineer"))
    .append("Company",BsonString("Google"))
    .append("Age",BsonInt32(32))
    .append("Experience(in years)",BsonDouble(7.2))


  /** list the collection names , and add the documents to it */
  val collectionNames = Await.result(database.listCollectionNames().toFuture(),10 seconds)
  collectionNames.foreach(println)


  val isEmployeeCollectionExists = collectionNames.exists(x => x==collectionName)
  if(isEmployeeCollectionExists){
    val collection = database.getCollection(collectionName)
    val insertResult = Await.result(collection.insertOne(insertDocument).toFuture(),10 seconds)
    println(s"Insertion of mongo doc is successfull  : ${insertResult.wasAcknowledged()}")
  }



}
