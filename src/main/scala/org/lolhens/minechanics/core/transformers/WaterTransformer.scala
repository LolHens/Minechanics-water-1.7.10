package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.{InsnNode, MethodNode}

/**
 * Created by LolHens on 09.04.2015.
 */
object WaterTransformer extends MethodTransformer {
  def transform(name: String, methodNode: MethodNode): Unit = {
    methodNode.instructions.insert(new InsnNode(Opcodes.RETURN))
  }
}
