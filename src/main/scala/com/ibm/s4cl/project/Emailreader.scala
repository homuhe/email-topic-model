package com.ibm.s4cl.project

/**
  * Created by root on 28.01.17.
  */
import java.io.{FileInputStream, ObjectInputStream}
import edu.stanford.nlp.simple._
class emailreader(emails: List[Email]){
  def getNounsOverTime: scala.collection.mutable.Map[String, Set[String]] = {
    val timemap = scala.collection.mutable.Map[String, Set[String]]()
    emails.foreach { email =>
      val time = email.getDate
      val nouns = email.contenNouns.toSet
      val value = timemap.getOrElse(time, Set())
      val newvalue = value.union(nouns)
      timemap.update(time, newvalue)
    }
    println("ready")
    timemap
  }
  def getPersonsMentionedWithTopics: scala.collection.mutable.Map[String, Set[(String, String)]] = {
    val personmap = scala.collection.mutable.Map[String, Set[(String, String)]]()
    emails.foreach { email =>
      val persons = email.getMentionedPersons.map(el => el._1)
      val topics = email.namedEntities
      persons.foreach { person =>
        val value: Set[(String, String)] = personmap.getOrElse(person.toString, Set())
        personmap.update(person, value.union(topics))
      }
    }
    personmap
  }

  def getAdresser_TopicMap(person:String) : scala.collection.mutable.Map[String, List[(String, String)]]={
    val adressermap = scala.collection.mutable.Map[String, List[(String, String)]]()
    emails.foreach{email =>
      val entities = email.namedEntities.toList
      val p = person match {
        case "adresser" => email.getAdresser
        case "adressee" => email.getAdressee
      }
      val value = adressermap.getOrElse(p, List())
      val newvalue = entities ::: value
      adressermap.update(p, newvalue)
    }
    println("ready")
    adressermap
  }

  def getTopicsbyContentWords(person:String) : scala.collection.mutable.Map[String, List[String]] = {
    val adressermap = scala.collection.mutable.Map[String, List[String]]()
    emails.foreach{email =>
      val nouns = email.contenNouns
      val p = person match {
        case "adresser" => email.getAdresser
        case "adressee" => email.getAdressee
      }
      val value = adressermap.getOrElse(p, List())
      val newvalue = value ::: nouns
      adressermap.update(p, newvalue)
    }
    println("ready")
    adressermap
  }

  def getTopicsOverTime : scala.collection.mutable.Map[String, Set[(String, String)]] = {
    val timemap = scala.collection.mutable.Map[String, Set[(String, String)]]()
    emails.foreach{email =>
      val time = email.getDate
      val topics = email.namedEntities
      val value = timemap.getOrElse(time, Set())
      timemap.update(time, topics.union(value))
    }
    timemap
  }

}
object emailreader {
  def main(args: Array[String]): Unit = {
    import java.io.{FileOutputStream, ObjectOutputStream}

    val in = new FileInputStream("C:\\Users\\Julia\\Dropbox\\SCALA-PROJECT_2017\\Wikimails")
    val inObject = new ObjectInputStream(in)
    val newmails = inObject.readObject().asInstanceOf[List[Email]]
    println(newmails.length)
    /*
    val reader = new emailreader(newmails)
    val map = reader.getAdresser_TopicMap("adresser")
    map foreach{case (key, value) => println(key, value)}
*/
  }
}