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
    emailAsText(emailAsText.indexOf("TEXT-CONTENT# ") + 1).trim
  }

  private val subject = emailAsText(5).replace("SUBJECT# ", "")
  private val date = emailAsText(4).replace("DATE# ", "")
  private val adressermail = emailAsText(0)
  private val adresseemail = emailAsText(2)
  if (content.length < 5){
    val (namedEntities, contentwords) = (Map(""->""), Set())
  }else {
    val (namedEntities, contentwords) = getNamedEntitiysAndNouns
  }
  def getAdresser: String = this.adresser

  def getAdressee: String = this.adressee

  def getContent = this.content

  def getSubject: String = this.subject

  def getDate: String = this.date

  def getNamedEntitiysAndNouns: (List[(String, String)], Set[String]) = {
      val sentences = content.toString.split(".")
      def getEntites(sentences: Array[String], namedEntities:List[((String, String))]) :List[(String, String)]= {
        if(sentences.isEmpty){
          namedEntities
        }
        val sent = new Sentence(sentences.head)
        val tokens = sent.nerTags().toArray().map(_.toString)
        val nertags = sent.nerTags().toArray().map(_.toString)
        val entities = tokens.zip(nertags).toMap.filter(el => el._2.equals("0")).toList
        getEntites(sentences.tail, namedEntities:::entities)
      }
      val entitymap = getEntites(sentences, Nil)
      val sent = new Sentence(content.toString)
      val tokens = sent.words().toArray().map(_.toString)
      val POSTags = sent.posTags().toArray().map(_.toString)
      val wordtags = tokens.zip(POSTags).toMap.filter(el => el._2 == "NNP" || el._2 == "NN" || el._2 ==  "NNPS" || el._2 == "NNS").keySet
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
    val filedir = "/home/neele/Dokumente/ScalaProject/"
    val files = getListOfFiles(filedir)
    var counter = 0
    val emails = files.par.foreach{el =>
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
