package com.ibm.s4cl.project

/**
  * Created by root on 28.01.17.
  */
import java.io.{FileInputStream, ObjectInputStream}
import edu.stanford.nlp.simple._
class emailreader(emails: List[Email]){

  def getAdresser_TopicMap(person:String) : scala.collection.mutable.Map[String, Set[(String, String)]]={
    val adressermap = scala.collection.mutable.Map[String, Set[(String, String)]]()
    emails.foreach{email =>
    val entities = email.namedEntities.toList.toSet
    val p = person match {
      case "adresser" => email.getAdresser
      case "adressee" => email.getAdressee
    }
      val value = adressermap.getOrElse(p, Set())
      val newvalue = value.union(entities)
      adressermap.update(p, newvalue)
    }
    adressermap
  }

}
object emailreader {
  def main(args: Array[String]): Unit = {
    import java.io.{FileOutputStream, ObjectOutputStream}

    val in = new FileInputStream("/home/neele/Dokumente/Wikimails")
    val inObject = new ObjectInputStream(in)
    val newmails = inObject.readObject().asInstanceOf[List[Email]]
    println(newmails.length)



  }
}
