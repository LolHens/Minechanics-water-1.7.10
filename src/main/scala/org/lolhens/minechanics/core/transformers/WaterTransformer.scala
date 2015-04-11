package org.lolhens.minechanics.core.transformers

import org.lolhens.minechanics.core.obfuscate.ObfMapper
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree._

/**
 * Created by LolHens on 09.04.2015.
 */
object WaterTransformer extends MethodTransformer {

  def transform(name: String, methodNode: MethodNode): Unit = {
    name match {
      case "updateTick" =>
        addWaterMechanic(methodNode)
        removeInfiniteSource(methodNode)
      case "func_149809_q" =>
        makeWaterReplacable(methodNode)
      case "func_149813_h" =>
        makeWaterNotAlwaysReplacable(methodNode)
      case "func_149807_p" =>
        makeWaterSourcesWaterproof(methodNode)
    }
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

  def makeWaterReplacable(methodNode: MethodNode): Unit = {
    val i = methodNode.instructions.iterator()

    while (i.hasNext) {
      i.next() match {
        case node: VarInsnNode if (node.getOpcode == Opcodes.ASTORE && node.`var` == 5) =>
          i.add(new VarInsnNode(Opcodes.ALOAD, 5))
          i.add(new VarInsnNode(Opcodes.ALOAD, 0))
          i.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/BlockDynamicLiquid", ObfMapper.obf("blockMaterial"), "Lnet/minecraft/block/material/Material;"))
          val l2 = new LabelNode()
          i.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l2))
          i.add(new VarInsnNode(Opcodes.ALOAD, 1))
          i.add(new VarInsnNode(Opcodes.ILOAD, 2))
          i.add(new VarInsnNode(Opcodes.ILOAD, 3))
          i.add(new VarInsnNode(Opcodes.ILOAD, 4))
          i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfMapper.obf("getBlockMetadata"), "(III)I", false))
          i.add(new JumpInsnNode(Opcodes.IFLE, l2))
          i.add(new VarInsnNode(Opcodes.ALOAD, 1))
          i.add(new VarInsnNode(Opcodes.ILOAD, 2))
          i.add(new VarInsnNode(Opcodes.ILOAD, 3))
          i.add(new VarInsnNode(Opcodes.ILOAD, 4))
          i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfMapper.obf("getBlockMetadata"), "(III)I", false))
          i.add(new IntInsnNode(Opcodes.BIPUSH, 8))
          i.add(new JumpInsnNode(Opcodes.IF_ICMPGE, l2))
          i.add(new InsnNode(Opcodes.ICONST_1))
          i.add(new InsnNode(Opcodes.IRETURN))
          i.add(l2)
        case _ =>
      }
    }
  }

  def makeWaterNotAlwaysReplacable(methodNode: MethodNode) = {
    val i = methodNode.instructions.iterator()
    i.add(new VarInsnNode(Opcodes.ALOAD, 0))
    i.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/BlockDynamicLiquid", ObfMapper.obf("blockMaterial"), "Lnet/minecraft/block/material/Material;"))
    i.add(new VarInsnNode(Opcodes.ALOAD, 1))
    i.add(new VarInsnNode(Opcodes.ILOAD, 2))
    i.add(new VarInsnNode(Opcodes.ILOAD, 3))
    i.add(new VarInsnNode(Opcodes.ILOAD, 4))
    i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfMapper.obf("getBlock"), "(III)Lnet/minecraft/block/Block;", false))
    i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/Block", ObfMapper.obf("getMaterial"), "()Lnet/minecraft/block/material/Material;", false))
    val l1 = new LabelNode()
    i.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l1))
    i.add(new InsnNode(Opcodes.RETURN))
    i.add(l1)
  }

  def makeWaterSourcesWaterproof(methodNode: MethodNode) = {
    val i = methodNode.instructions.iterator()

    while (i.hasNext) {
      i.next() match {
        case node: VarInsnNode if (node.getOpcode == Opcodes.ASTORE && node.`var` == 5) =>
          i.add(new VarInsnNode(Opcodes.ALOAD, 5))
          i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/Block", ObfMapper.obf("getMaterial"), "()Lnet/minecraft/block/material/Material;", false))
          i.add(new VarInsnNode(Opcodes.ALOAD, 0))
          i.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/block/BlockDynamicLiquid", ObfMapper.obf("blockMaterial"), "Lnet/minecraft/block/material/Material;"))
          val l2 = new LabelNode()
          i.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l2))
          i.add(new VarInsnNode(Opcodes.ALOAD, 1))
          i.add(new VarInsnNode(Opcodes.ILOAD, 2))
          i.add(new VarInsnNode(Opcodes.ILOAD, 3))
          i.add(new VarInsnNode(Opcodes.ILOAD, 4))
          i.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ObfMapper.obf("getBlockMetadata"), "(III)I", false))
          i.add(new JumpInsnNode(Opcodes.IFNE, l2))
          i.add(new InsnNode(Opcodes.ICONST_1))
          i.add(new InsnNode(Opcodes.IRETURN))
          i.add(l2)
        case _ =>
      }
    }
  }
}
