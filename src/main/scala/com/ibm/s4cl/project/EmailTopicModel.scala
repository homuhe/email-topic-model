package com.ibm.s4cl.project

/**
  *
  */

import scala.io.Source
import java.io._

class Email(file:String) extends scala.Serializable {
  val emailAsText = Source.fromFile(file).getLines().toSeq
  private val numberOfMeta = emailAsText.count(el => el.contains("META"))
  private val adresser = emailAsText(1).replace("FROM-NAME# ", "")
  private val adressee = emailAsText(3).replace("TO-NAME# ", "")

  private val content:String = {
    emailAsText(emailAsText.indexOf("TEXT-CONTENT# ")+1)
  }

  private val subject = emailAsText(5).replace("SUBJECT# ", "")
  private val date = emailAsText(4).replace("DATE# ", "")
  private val adressermail = emailAsText(0)
  private val adresseemail = emailAsText(2)

  def getAdresser : String = this.adresser

  def getAdressee: String = this.adressee
  def getContent = this.content
  def getSubject: String = this.subject
  def getDate: String = this.date

}

object  Email{
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
    val emails = files.map(el => new Email(el.toString))
    val out = new FileOutputStream("/home/neele/Dokumente/Wikimails")
    val objectout = new ObjectOutputStream(out)
    objectout.writeObject(emails)
    objectout.close()
    out.close()
    val in = new FileInputStream("/home/neele/Dokumente/Wikimails")
    val inObject = new ObjectInputStream(in)
    val newmails = inObject.readObject().asInstanceOf[List[Email]]
    println(newmails.size)

  }

}
