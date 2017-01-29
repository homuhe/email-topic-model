package com.ibm.s4cl.project

/**
  *
  */

import scala.io.Source
import java.io._
import edu.stanford.nlp.simple._
class Email(file:String) extends scala.Serializable {
  val emailAsText = Source.fromFile(file).getLines().toSeq
  private val numberOfMeta = emailAsText.count(el => el.contains("META"))
  private val adresser = emailAsText(1).replace("FROM-NAME# ", "")
  private val adressee = emailAsText(3).replace("TO-NAME# ", "")

  private val content: String = {
    emailAsText(emailAsText.indexOf("TEXT-CONTENT# ") + 1)
  }

  private val subject = emailAsText(5).replace("SUBJECT# ", "")
  private val date = emailAsText(4).replace("DATE# ", "")
  private val adressermail = emailAsText(0)
  private val adresseemail = emailAsText(2)

  val(namedEntities, contentwords) = getNamedEntitiysAndNouns

  def getAdresser: String = this.adresser

  def getAdressee: String = this.adressee

  def getContent = this.content

  def getSubject: String = this.subject

  def getDate: String = this.date

    def getNamedEntitiysAndNouns: (Map[String, String], Set[String]) = {

      val sent: Sentence = new Sentence(content.toString())
      val tokens = sent.words().toArray().map(_.toString)
      val nerTags = sent.nerTags().toArray().map(_.toString)
      val POSTags = sent.posTags().toArray().map(_.toString)
      val entitymap = tokens.zip(nerTags).toMap.filter(el => el._2.equals("0"))
      val wordtags = tokens.zip(POSTags).toMap.filter(el => el._2.equals("NNP")).keySet
      (entitymap, wordtags)
    }

  def getNamedEntities(nouns: Set[String]):Set[(String, String)] = {
    val ners = nouns.map(noun => (noun, (new Sentence(noun).nerTags()).get(0)))
    ners
  }

  def getContentNouns: Set[String] = {
    if (content.length > 5) {
      val sent = new Sentence(content)
      val tokens = sent.words().toArray().map(_.toString)
      val tags = sent.posTags().toArray().map(_.toString)
      tokens.zip(tags).toMap.filter(el => el._2 == "NNP" || el._2 == "NN" || el._2 ==  "NNPS" || el._2 == "NNS").keySet
    }
    else {
      Set()
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
    val filedir = "/home/neele/Downloads/ScalaProject/"
    val files = getListOfFiles(filedir)
    var counter = 0
    val emails = files.foreach{el =>
      new Email(el.toString)
      println(counter)
    counter+=1}
    val out = new FileOutputStream("/home/neele/Dokumente/Wikimails")
    val objectout = new ObjectOutputStream(out)
    objectout.writeObject(emails)
    objectout.close()
    out.close()
    println("emails have been read")


  }

}
