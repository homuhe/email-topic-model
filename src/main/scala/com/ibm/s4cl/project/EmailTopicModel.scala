package com.ibm.s4cl.project

import java.io.{FileInputStream, ObjectInputStream}
import edu.stanford.nlp.simple._

object EmailTopicModel {

  def main(args: Array[String]) {

    val in = new FileInputStream("C:\\Users\\Julia\\Dropbox\\SCALA-PROJECT_2017\\wikimails")
    val inObject = new ObjectInputStream(in)
    val mails = inObject.readObject().asInstanceOf[List[Email]]
    println(mails.size)

    mails.take(1000).par.foreach(mail => {

      val sent: Sentence = new Sentence(mail.getContent)
      val tokens = sent.words().toArray()
      val nerTags = sent.nerTags().toArray()
      val POSTags = sent.posTags().toArray()

      println(tokens.length)
      println(nerTags.length)
      println(POSTags.length)


      /*for (i <- 0 until tokens.length)
        println(tokens(i), POSTags(i), nerTags(i))*/

      /* print("\nNamed Entities: ")
       for (i <- 0 until tokens.length)
         if (!nerTags(i).equals("O"))
           println(tokens(i), nerTags(i)) */

      val multiNEs: Map[String, String] = {
        var ne = ""
        var currentTag = "O"
        var neMap: Map[String, String] = Map()
        for (i <- 0 until nerTags.length) {
          if (nerTags(i).equals(currentTag) && !currentTag.equals("O"))
            ne += " " + tokens(i)
          else {
            if (!currentTag.equals("O"))
              neMap += (ne -> currentTag)
            currentTag = nerTags(i).toString
            ne = tokens(i).toString
          }
        }
        neMap
      }

      println()
      println("Test multi-token NEs:")
      multiNEs.foreach(println(_))

      val personsMentioned = multiNEs.filter(_._2.equals("PERSON"))
      val topics = multiNEs.filterNot(_._2.equals("PERSON"))
      println(personsMentioned)
      println(topics)
    })
  }

}