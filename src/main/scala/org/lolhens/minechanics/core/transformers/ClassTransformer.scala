package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.tree.ClassNode

/**
 * Created by LolHens on 09.04.2015.
 */
trait ClassTransformer {
  def transform(name: String, classNode: ClassNode)
}
