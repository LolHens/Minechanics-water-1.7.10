package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.tree.MethodNode

/**
 * Created by LolHens on 09.04.2015.
 */
trait MethodTransformer {
  def transform(name: String, methodNode: MethodNode)
}
