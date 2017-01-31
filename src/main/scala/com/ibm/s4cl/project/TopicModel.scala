package com.ibm.s4cl.project

import scala.collection.immutable.ListMap

/**
  * Created by root on 29.01.17.
  */
class TopicModel(mails: List[Email]) {
  val reader = new emailreader(mails)
  val adresser_namedEntityMap = reader.getAdresser_TopicMap("adresser")
  val adressee_namedEntityMap = reader.getAdresser_TopicMap("adressee")
  val timemap = reader.getNounsOverTime
  val adresser_contentWordMap = reader.getTopicsbyContentWords("adresser")
  val personsMentionedMap = reader.getPersonsMentionedWithTopics
  val topicsOverTime = reader.getTopicsOverTime
  val adressee_contentWordMap = reader.getTopicsbyContentWords("adressee")

  def getFrequentNouns(map: Map[String, List[String]]) = {
    val normalizedmap = map map { case (key, value) => (key, value.map(_.toLowerCase)) }
    val sortedMap = normalizedmap map { case (key, value) => (key, value.groupBy(el => el).map(e => (e._1 -> e._2.length))) }
   // println("hier")
    val topTen = sortedMap map { case (key, mapvalue) => (key, mapvalue.filter(e => e._2 > 3)) }
    topTen map { case (key, map) => (key, ListMap(map.toSeq.sortWith(_._2 > _._2): _*)) }
  }

  def getAllAdresser = adresser_namedEntityMap.keySet

  def getAllAdressee = adressee_namedEntityMap.keySet

  def getMostFrequentCommunications: Set[(String, String)] = {
    val communications = mails.map(mail => (mail.getAdresser, mail.getAdressee))
    communications.groupBy(el => el).map(e => e._1 -> e._2.length).keySet.take(10)
  }

  def getTopicsOverTime(timemap: Map[String, Set[(String, String)]]): Map[(String, String), Set[(String, String)]] = {
    val time = timemap map { case (key, value) => ((key.split("-")(0), key.split("-")(1)), value) }
    val sorted = ListMap(time.toSeq.sortBy(_._1): _*).toMap
    sorted
  }

  def getNounsOverTime(timemap: Map[String, Set[String]]): Map[(String, String), Set[String]] = {
    val time = timemap map { case (key, value) => ((key.split("-")(0), key.split("-")(1)), value) }
    val sorted = ListMap(time.toSeq.sortBy(_._1): _*)
    sorted
  }
}

object TopicModel {
  def main(args: Array[String]): Unit = {
    import java.io._


    val in = new FileInputStream("C:\\Users\\Julia\\Dropbox\\scala-project_2017\\Wikimails")
    val inObject = new ObjectInputStream(in)
    val newmails = inObject.readObject().asInstanceOf[List[Email]]
    println("emails have been read")

    val model = new TopicModel(newmails)
   // val model = new TopicModel(Nil)
    println("maps have been created")
    while (true) {
      println("options: \n #addressee, #addresser, #frequent-nouns, #timemap, #named-entities, #communications")
      val input = userinput
      if (input.equals("#addressee")) {
        println(model.getAllAdressee)
      }
      else if (input.equals("#addresser")) {
        println(model.getAllAdresser)
      }
      else if (input.equals("#frequent-nouns")) {
        println("please enter the person you're interested in")
        val name = userinput
        println(model.getFrequentNouns(model.adresser_contentWordMap.toMap).getOrElse(name, "person not in Map"))
      }
      else if (input.equals("#timemap")) {
        // timemap = model.adresser_namedEntityMap.toMap.map(el => (el._1, el._2.toSet))
        model.timemap.foreach { case (key, value) => println(key, value) }
      }
      //Hillary Clinton, Lauren Jiloty, Prime Minister, World Bank
      else if (input.equals("#named-entities")) {
        println("please enter the person you're interested in")
        val name = userinput
        println(model.adresser_namedEntityMap.getOrElse(name, "not in map"))
      }
      else if (input.equals("#communications")) {
        println(model.getMostFrequentCommunications)
      }

    }
    def userinput = {
      import scala.io.StdIn
      StdIn.readLine().toString
    }

  }
}
