package org.lolhens.minechanics.core.transformers

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree._

/**
 * Created by LolHens on 09.04.2015.
 */
object WaterTransformer extends MethodTransformer {
  def transform(name: String, methodNode: MethodNode): Unit = {
    addWaterMechanic(methodNode)
    removeInfiniteSource(methodNode)

  }

  def removeInfiniteSource(methodNode: MethodNode) = {
    val i = methodNode.instructions.iterator()

    var lastIconst0: InsnNode = null

    while (i.hasNext) {
      i.next() match {
        case node: InsnNode if (node.getOpcode == Opcodes.ICONST_0) =>
          lastIconst0 = node
        case node: VarInsnNode if (node.getOpcode == Opcodes.ISTORE && node.`var` == 10 && lastIconst0 != null) =>
          i.remove()
          methodNode.instructions.remove(lastIconst0)
          lastIconst0 = null
        case _ =>
          lastIconst0 = null
      }
    }
  }

  def addWaterMechanic(methodNode: MethodNode): Unit = {
    val i = methodNode.instructions.iterator()

    var lastAload0: VarInsnNode = null
    var matches = 0

    while (i.hasNext) {
      i.next() match {
        case node: VarInsnNode if (node.getOpcode == Opcodes.ALOAD && node.`var` == 0) =>
          lastAload0 = node
        case node: MethodInsnNode if (node.getOpcode == Opcodes.INVOKESPECIAL && node.owner == "net/minecraft/block/BlockDynamicLiquid" && node.name == "func_149813_h" && node.desc == "(Lnet/minecraft/world/World;IIII)V" && lastAload0 != null) =>
          i.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lolhens/minechanics/core/hooks/Hooks", "onWaterFlowdown", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIII)V", false))
          //methodNode.instructions.remove(lastAload0)
          lastAload0 = null
          matches += 1
          if (matches >= 2) return
        case _ =>
      }
    }
  }
}
