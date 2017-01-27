package com.ibm.s4cl.project

import edu.stanford.nlp.simple._

object EmailTopicModel {

  def main(args: Array[String]) {

    val text1 = "The fate of Lehman Brothers," +
      "the beleaguered investment bank, hung in the " +
      "balance on Sunday as Federal Reserve officials and" +
      "the leaders of major financial institutions continued to" +
      "gather in emergency meetings trying to complete a plan to rescue " +
      "the stricken bank.  Several possible plans emerged from the talks," +
      "held at the Federal Reserve Bank of New York and led by Timothy R. Geithner," +
      "the president of the New York Fed, and Treasury Secretary Henry M. Paulson Jr."

    val text = "I hope you've been well." +
      "Following up on the Secretary's request, " +
      "when would you like to regroup about communications " +
      "support in Pakistan? As food for thought, I've been talking " +
      "with She's a great source of insight and information on the " +
      "ground, with a valuable independent perspective. She is very " +
      "tuned into the government's cornmunications challenges and " +
      "opportunities. In her view, while the need is more urgent " +
      "than ever, the government does have positive programs and " +
      "policies that would generate good will if people knew about them."

    val sent: Sentence = new Sentence(text1)
    val tokens = sent.words().toArray()
    val nerTags = sent.nerTags().toArray()
    val POSTags = sent.posTags().toArray()

    println(tokens.length)
    println(nerTags.length)
    println(POSTags.length)



    for (i <- 0 until tokens.length)
      println(tokens(i), POSTags(i), nerTags(i))

    print("\nNamed Entities: ")
    for (i <- 0 until tokens.length)
      if (!nerTags(i).equals("O"))
        println(tokens(i), nerTags(i))

    //fasst multi-token named entities zu einem einzelnen String zusammen
    val multiNEs: Map[String, String] = {
      var ne = ""
      var currentTag = "O"
      var NEMap: Map[String, String] = Map()
      for (i <- 0 until nerTags.length) {
          if (nerTags(i).equals(currentTag) && !currentTag.equals("O"))
            ne += " " + tokens(i)
          else {
            if(!currentTag.equals("O"))
              NEMap += (ne -> currentTag)
            currentTag = nerTags(i).toString
            ne = tokens(i).toString
          }
      }
      NEMap
    }

    println()
    println("Test multi-token NEs:")
    multiNEs.foreach(println(_))
  }

}