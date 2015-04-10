package org.lolhens.minechanics.core.transformers

import org.lolhens.minechanics.core.obfuscate.ObfMapper
import org.objectweb.asm.tree.ClassNode

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by LolHens on 09.04.2015.
 */
class UniversalClassTransformer extends ClassTransformer {
  private val methodTransformers = mutable.Map[String, mutable.MutableList[MethodTransformer]]()

  def registerMethodTransformer(transformer: (String, MethodTransformer)): Unit = {
    val obfName = ObfMapper(transformer._1)
    val transformers = methodTransformers.get(obfName) match {
      case Some(transformers) => transformers
      case None => {
        val transformers = new mutable.MutableList[MethodTransformer]();
        methodTransformers += obfName -> transformers
        transformers
      }
    }
    transformers += transformer._2
  }

  override def transform(name: String, classNode: ClassNode): Unit = {
    for (methodNode <- classNode.methods) {
      val name = ObfMapper(methodNode.name)
      methodTransformers.get(name) match {
        case Some(transformers) =>
          transformers.foreach(_.transform(name, methodNode))
        case None =>
      }
    }
  }
}
