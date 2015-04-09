package org.lolhens.minechanics.core

import net.minecraft.launchwrapper.IClassTransformer

/**
 * Created by LolHens on 09.04.2015.
 */
class ASMTransformer extends IClassTransformer {
  override def transform(obfName: String, name: String, bytes: Array[Byte]): Array[Byte] = {
    if (name == "BlockDynamicLiquid") {

    }
    bytes
  }
}
