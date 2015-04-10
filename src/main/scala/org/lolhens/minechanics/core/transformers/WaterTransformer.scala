package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.{InsnNode, MethodNode, VarInsnNode}

/**
 * Created by LolHens on 09.04.2015.
 */
object WaterTransformer extends MethodTransformer {
  def transform(name: String, methodNode: MethodNode): Unit = {
    removeInfiniteSource(methodNode)
  }

  def removeInfiniteSource(methodNode: MethodNode) = {
    val i = methodNode.instructions.iterator()

    val iconst_0 = new InsnNode(Opcodes.ICONST_0)
    val istore = new VarInsnNode(Opcodes.ISTORE, 10)

    var found = false

    while (i.hasNext) {
      i.next() match {
        case node: InsnNode if (node.getOpcode == Opcodes.ICONST_0) =>
          found = true
        case node: VarInsnNode if (node.getOpcode == Opcodes.ISTORE && node.`var` == 10 && found == 1) =>
          i.previous()
          i.previous()
          i.remove()
          i.next()
          i.remove()

          found = false
        case _ =>
          found = false
      }
    }
  }
}
