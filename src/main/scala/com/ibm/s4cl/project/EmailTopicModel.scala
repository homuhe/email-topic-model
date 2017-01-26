package com.ibm.s4cl.project

import java.util.Properties

import edu.stanford.nlp.ling.{CoreAnnotations, CoreLabel}
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.util._



/**
  *
  */
object EmailTopicModel {

  def main(args: Array[String]) {
    //println("Hello group member!")

    /**val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref")

    val pipeline = new StanfordCoreNLP(props)

    val text = "The fate of Lehman Brothers," +
      "the beleaguered investment bank, hung in the" +
      "balance on Sunday as Federal Reserve officials and" +
      "the leaders of major financial institutions continued to" +
      "gather in emergency meetings trying to complete a plan to rescue" +
      "the stricken bank.  Several possible plans emerged from the talks," +
      "held at the Federal Reserve Bank of New York and led by Timothy R. Geithner," +
      "the president of the New York Fed, and Treasury Secretary Henry M. Paulson Jr."

    val document = new Annotation(text)

    pipeline.annotate(document)**/

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

    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref")

    /**
    val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)
    val annotation: Annotation = pipeline.process(text)

    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).toArray()
    val tokens = annotation.get(classOf[CoreAnnotations.TokensAnnotation]).toArray()
    val nes = annotation.get(classOf[CoreAnnotations.NamedEntityTagAnnotation])
    println(sentences)
    println(tokens)
    println(nes)

    for (sentence <- sentences)
      println(sentence)
    for (token <- tokens)
      println(token)**/



    val pipe2 = new StanfordCoreNLP(props)
    val document = new Annotation(text)
    pipe2.annotate(document)

    val sentences2 = document.get(classOf[CoreAnnotations.SentencesAnnotation])
    //val tokens2 = annotation.get(classOf[CoreAnnotations.TokensAnnotation])
    //val nes2 = annotation.get(classOf[CoreAnnotations.NamedEntityTagAnnotation])

    for (sentence <- sentences2) {

      println(sentence)

      for (token: CoreLabel <- sentence)
        {
          val word = token.get(classOf[CoreAnnotations.TextAnnotation])
          println(word)

          val pos = token.get(classOf[CoreAnnotations.PartOfSpeechAnnotation])
          println(pos)

          val ne = token.get(classOf[CoreAnnotations.NamedEntityTagAnnotation])
          println(ne)
        }
    }


    //println(sentences2)
    //println(tokens2)
    //println(nes2)


    //println(annotation)
  }

}