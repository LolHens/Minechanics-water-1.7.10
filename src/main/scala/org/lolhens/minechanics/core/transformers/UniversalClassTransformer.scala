package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.tree.ClassNode

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by LolHens on 09.04.2015.
 */
class UniversalClassTransformer extends ClassTransformer {
  private val methodTransformers = mutable.Map[String, mutable.MutableList[MethodTransformer]]()

  def registerMethodTransformer(transformer: (String, MethodTransformer)): Unit = {
    val transformers = methodTransformers.get(transformer._1) match {
      case Some(transformers) => transformers
      case None => {
        val transformers = new mutable.MutableList[MethodTransformer]();
        methodTransformers += transformer._1 -> transformers
        transformers
      }
    }
    transformers += transformer._2
  }

  override def transform(name: String, classNode: ClassNode): Unit = {
    for (methodNode <- classNode.methods) {
      val name = methodNode.name
      methodTransformers.get(name) match {
        case Some(transformers) =>
          transformers.foreach(_.transform(name, methodNode))
        case None =>
      }
    }
  }
}
