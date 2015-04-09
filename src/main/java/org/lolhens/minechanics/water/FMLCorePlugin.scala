package org.lolhens.minechanics.water

import java.util

import cpw.mods.fml.common.{DummyModContainer, ModMetadata}
import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import net.minecraft.launchwrapper.IClassTransformer
import org.lolhens.minechanics.water.Minechanics.ASMTransformer

/**
 * Created by LolHens on 09.04.2015.
 */
@IFMLLoadingPlugin.Name(Minechanics.name)
@IFMLLoadingPlugin.MCVersion(Minechanics.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions(Array("scala", "org.lolhens.minechanics.water"))
@IFMLLoadingPlugin.SortingIndex(value = 1001)
class FMLCorePlugin extends IFMLLoadingPlugin {
  override def getASMTransformerClass: Array[String] = Array(classOf[Minechanics.ASMTransformer].getName)

  override def injectData(data: util.Map[String, AnyRef]): Unit = null

  override def getModContainerClass: String = classOf[Minechanics.ModContainer].getName

  override def getAccessTransformerClass: String = null

  override def getSetupClass: String = null
}

object Minechanics {
  final val id = "mcncs"
  final val name = "Minechanics-water-1.7.10"
  final val version = "@VERSION@"
  final val mcVersion = "1.7.10"

  class ModContainer extends DummyModContainer(new ModMetadata()) {
    val metadata = getMetadata()
    metadata.modId = id
    metadata.name = name
    metadata.version = version
  }

  class ASMTransformer extends IClassTransformer {
    override def transform(name: String, srgName: String, bytes: Array[Byte]): Array[Byte] = {
      println(name + " - " + srgName)
      bytes
    }
  }
}