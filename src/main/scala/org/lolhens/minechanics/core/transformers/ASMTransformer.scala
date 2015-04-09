package org.lolhens.minechanics.core.transformers

import net.minecraft.launchwrapper.IClassTransformer
import org.lolhens.minechanics.LogHelper
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.{ClassReader, ClassWriter}

import scala.collection.mutable

/**
 * Created by LolHens on 09.04.2015.
 */
class ASMTransformer extends IClassTransformer {
  ASMTransformer.registerClassTransformer("net.minecraft.block.BlockDynamicLiquid" -> WaterTransformer)

  override def transform(obfName: String, name: String, bytes: Array[Byte]): Array[Byte] = {
    ASMTransformer.classTransformers.get(name) match {
      case Some(transformers) =>LogHelper.fatal(name)
        val classReader = new ClassReader(bytes)
        val classNode = new ClassNode()
        classReader.accept(classNode, 0)

        transformers.foreach(_.transform(name, classNode))

        val classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
        classNode.accept(classWriter)
        classWriter.toByteArray
      case None => bytes
    }
  }
}

object ASMTransformer {
  private val classTransformers = mutable.Map[String, mutable.MutableList[ClassTransformer]]()

  def registerClassTransformer(transformer: (String, ClassTransformer)): Unit = {
    val transformers = classTransformers.get(transformer._1) match {
      case Some(transformers) => transformers
      case None => {
        val transformers = new mutable.MutableList[ClassTransformer]();
        classTransformers += transformer._1 -> transformers
        transformers
      }
    }
    transformers += transformer._2
  }

  def registerMethodTransformer(transformer: ((String, String), MethodTransformer)): Unit = {
    val transformers = classTransformers.get(transformer._1._1) match {
      case Some(transformers) => transformers
      case None => {
        val transformers = new mutable.MutableList[ClassTransformer]();
        classTransformers += transformer._1._1 -> transformers
        transformers
      }
    }

    val universalClassTransformer = {
      def func: UniversalClassTransformer = {
        transformers.foreach(transformer => {
          if (transformer.isInstanceOf[UniversalClassTransformer]) return transformer.asInstanceOf[UniversalClassTransformer]
        })
        val transformer = new UniversalClassTransformer()
        transformers += transformer
        transformer
      }
      func
    }

    universalClassTransformer.registerMethodTransformer((transformer._1._2, transformer._2))
  }
}