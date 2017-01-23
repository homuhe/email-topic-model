package com.ibm.s4cl.project

import org.clulab.processors.corenlp.CoreNLPProcessor
import org.clulab.processors.shallownlp.ShallowNLPProcessor
import org.clulab.processors.{Document, Processor}
import org.clulab.struct.DirectedGraphEdgeIterator
/**
  *
  */
object EmailTopicModel {

  def preprocess(text: String): Unit = {
    val proc: Processor = new CoreNLPProcessor
    val doc = proc.mkDocument(text)
    proc.tagPartsOfSpeech(doc)
    proc.lemmatize(doc)
    proc.recognizeNamedEntities(doc)
    doc.clear()

    val sents = doc.sentences
    sents.foreach(_.entities.foreach(entities => println(s"Named entities: ${entities.mkString(" ")}")))
  }

  def main(args: Array[String]) {
    //println("Hello group member!")
    val test = "John Smith went to China. He visited Beijing, on January 10th, 2013."
    preprocess(test)
  }

}
