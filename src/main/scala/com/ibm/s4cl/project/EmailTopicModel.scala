package com.ibm.s4cl.project

/**
  *
  */

import scala.io.Source
import java.io._
import edu.stanford.nlp.simple._

@SerialVersionUID(100L)
class Email(file:String) extends scala.Serializable {
  val emailAsText = Source.fromFile(file).getLines().toSeq
  private val numberOfMeta = emailAsText.count(el => el.contains("META"))
  private val adresser = emailAsText(1).replace("FROM-NAME# ", "")
  private val adressee = emailAsText(3).replace("TO-NAME# ", "")

  private val content: String = {
    emailAsText(emailAsText.indexOf("TEXT-CONTENT# ") + 1).trim
  }

  private val subject = emailAsText(5).replace("SUBJECT# ", "")
  private val date = emailAsText(4).replace("DATE# ", "")
  private val adressermail = emailAsText(0)
  private val adresseemail = emailAsText(2)

  //val (namedEntities, contentwords) = getNamedEntities
  val contenNouns = getContentNouns
  val namedEntities = getNamedEntities(contenNouns)
  def getMentionedPersons : Set[(String, String)] = namedEntities.filter(el => el._2 == "PERSON")

  def getAdresser: String = this.adresser

  def getAdressee: String = this.adressee

  def getContent = this.content

  def getSubject: String = this.subject

  def getDate: String = this.date


  def getNamedEntities(nouns: List[String]):Set[(String, String)] = {
    val ners = nouns.map(noun => (noun, (new Sentence(noun).nerTags()).get(0)))
    ners.toSet.filter(el => el._2.equals("O") == false)
  }

  def getContentNouns: List[String] = {
    if (content.length > 5) {
      val sent = new Sentence(content)
      val tokens = sent.words().toArray().map(_.toString)
      val tags = sent.posTags().toArray().map(_.toString)
      tokens.zip(tags).toMap.toList.filter(el => el._2 == "NNP" || el._2 == "NN" || el._2 ==  "NNPS" || el._2 == "NNS").map(el => el._1)
    }
    else {
      List()
    }
  }
}

object  Email {
  def main(args: Array[String]): Unit = {
    import java.io.File
    def getListOfFiles(dir: String): List[File] = {
      val d = new File(dir)
      if (d.exists && d.isDirectory) {
        d.listFiles.filter(_.isFile).toList
      } else {
        List[File]()
      }
    }
    val filedir = "/home/neele/Schreibtisch/ScalaProject/"
    val files = getListOfFiles(filedir)
    var counter = 0
    val emails = scala.collection.mutable.ListBuffer[Email]()
   files.foreach{el =>
      emails.append(new Email(el.toString))
      println(counter)
    counter+=1}
    println(emails.toList.length)
    val out = new FileOutputStream("/home/neele/Dokumente/Wikimails")
    val objectout = new ObjectOutputStream(out)
    objectout.writeObject(emails.toList)
    objectout.close()
    out.close()
    println("emails have been read")


  }

}
