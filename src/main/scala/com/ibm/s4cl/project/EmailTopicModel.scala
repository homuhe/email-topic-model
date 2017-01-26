package com.ibm.s4cl.project

import edu.stanford.nlp.simple._

object EmailTopicModel {

  def main(args: Array[String]) {

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

    val sent: Sentence = new Sentence(text)
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

  }

}