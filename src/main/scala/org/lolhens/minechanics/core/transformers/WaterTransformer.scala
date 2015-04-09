package org.lolhens.minechanics.core.transformers

import org.lolhens.minechanics.LogHelper
import org.objectweb.asm.tree.ClassNode

import scala.collection.JavaConversions._

/**
 * Created by LolHens on 09.04.2015.
 */
object WaterTransformer extends ClassTransformer {
  override def transform(name: String, classNode: ClassNode): Unit = {
    for (method <- classNode.methods) LogHelper.fatal(method.name)
  }
}
