package com.ibm.s4cl.project
import scala.collection.immutable.ListMap
/**
  * Created by root on 29.01.17.
  */
class TopicModel(mails:List[Email]) {
  val reader = new emailreader(mails)
  val adresser_namedEntityMap = reader.getAdresser_TopicMap("adresser")
  val adressee_namedEntityMap = reader.getAdresser_TopicMap("adressee")
  val timemap = reader.getTopicsOverTime
  val adresser_contentWordMap = reader.getTopicsbyContentWords("adresser")
  //val adressee_contentWordMap = reader.getTopicsbyContentWords("adressee")

  def getFrequentNouns(map:Map[String, List[String]]) = {
    val sortedMap = map map {case (key, value) => (key, value.groupBy(el => el).map(e => (e._1 -> e._2.length)))}
    val topTen = ListMap(sortedMap.toSeq.sortBy(_._1):_*)
    topTen
  }

  def getAllAdresser = adresser_namedEntityMap.keySet
  def getAllAdressee = adressee_namedEntityMap.keySet

  def getMostFrequentCommunications: Set[(String, String)] = {
    val communications = mails.map(mail => (mail.getAdresser, mail.getAdressee))
    communications.groupBy(el => el).map(e => e._1 -> e._2.length).keySet.take(10)
  }

  def getTopicsOverTime(timemap: Map[String, Set[(String, String)]]) : Map[(String, String), Set[(String, String)]] ={
    val time = timemap map { case (key, value) => ((key.split("-")(0), key.split("-")(1)), value)}
    val sorted = ListMap(time.toSeq.sortBy(_._1):_*).toMap
    sorted
  }

}

object TopicModel {
  def main(args: Array[String]): Unit = {
    import java.io._

    val in = new FileInputStream("/home/neele/Dokumente/Wikimails")
    val inObject = new ObjectInputStream(in)
    val newmails = inObject.readObject().asInstanceOf[List[Email]]
    println("emails have been read")
    val model = new TopicModel(newmails)
    println("maps have been created")
    model.getFrequentNouns(model.adresser_contentWordMap.toMap).foreach {case (key, value) => println(key, value)}
    //println(model.getTopicsOverTime(model.timemap.toMap).foreach{ case (key, value) => println(key, value)})

  }
}
