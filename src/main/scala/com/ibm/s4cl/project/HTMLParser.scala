package com.ibm.s4cl.project

/**
  * Created by alex on 22.01.17.
  */

import java.io._

import scala.io.Source


object HTMLParser {

  def main(args: Array[String]): Unit = {


    val folder = "/home/alex/Desktop/WikiLeaks-ScalaProject/"


    var baseHeader = ""
    var baseContent = ""

    var header = ""
    var content = ""
    var text = ""

    var isTabContent = false
    var divCount = 0

    //30946
    for(number <- 1 until 30946) {

      baseHeader = ""
      baseContent = ""

      header = ""
      content = ""
      text = ""



      isTabContent = false
      divCount = 0


      for (line <- Source.fromFile(folder + number).getLines()) {


        if (line.contains("<div class=\"tab-content\">")) isTabContent = true

        if (isTabContent)
          if (line.nonEmpty) {
            if (line.contains("<div class="))
              divCount += 1

            if (divCount <= 3) {

              // header info, contains repititions
              if (divCount == 2) {
                baseHeader += line
              }

              //e-mail content
              if (divCount == 3) {
                baseContent += line + " "
              }

            }
            else isTabContent = false
          }

      }

      def createHeader: Unit = {

        header += "FROM-MAIL# " +
          baseHeader.replaceFirst("(.*From: <span title=\")(.*?)(\">)(.*?)(</span>.*)", "$2" +
          "\nFROM-NAME# $4\n")
        header += "TO-MAIL# " +
          baseHeader.replaceFirst("(.*To: <span title=\")(.*?)(\">)(.*?)(</span>.*)", "$2" +
          "\nTO-NAME# $4\n")
        header += "DATE# " + baseHeader.replaceFirst("(.*</span>.*Date: )(.*)(Subject:.*)", "$2\n")
        header += "SUBJECT# " + baseHeader.replaceFirst("(.*Subject: )(.*)(</header>.*)", "$2\n")
        header += "\n"
      }

      baseHeader = baseHeader.replaceAll("\\s+", " ")
      createHeader


      // #### work with baseContent here:
      def createContent: Unit = {

        //        content += "UNCLASSIFIED# " + baseContent.replaceFirst("(.*?<span class=\"unclassified\">)(.*?)(</span>.*)", "$2") + "\n"

        if(baseContent.contains("From:"))
          content += "META-FROM# " + baseContent.replaceFirst("(.*?<span class=\"inlinemeta\">From:)(.*?)(<.*)", "$2") + "\n"

        if(baseContent.contains("To:"))
          content += "META-TO_FW_SUBJ# " +
            baseContent.replaceFirst("(.*?<span class=\"inlinemeta\">.*?To:)(.*?)(<.*)", "$2") + "\n"

        if(baseContent.contains("Cc:"))
          content += "META-CC# " +
            baseContent.replaceAll("(.*?)(Cc)(:)(.*?)(<|Subject)(.*)", "$4") + "\n"


        content += "\nTEXT-CONTENT# " + "\n"

        text = baseContent.replaceFirst("(.*?<span class=\"inlinemeta\">.*?</span>)(.*?)(</div> </div>)", "$2")

        text = text.replaceAll("(<.*?>.*?</.*?>)", "")
        text = text.replaceAll("\\s+", " ")
      }


      baseContent = baseContent.replaceAll("\\s+", " ")
      createContent


      //write a file pro emailID
      writeFile

      def writeFile = {
        val pw = new PrintWriter(new File("/home/alex/Desktop/ScalaProject/" + number + ".txt"))
        pw.write(header)
        pw.write(content)
        pw.write(text)
        pw.close
      }

    }

  }


}
