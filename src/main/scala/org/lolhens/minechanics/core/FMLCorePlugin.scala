package org.lolhens.minechanics.core

import java.util

import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import org.lolhens.minechanics.Minechanics
import org.lolhens.minechanics.core.transformers.ASMTransformer

/**
 * Created by LolHens on 09.04.2015.
 */
@IFMLLoadingPlugin.Name(Minechanics.name)
@IFMLLoadingPlugin.MCVersion(Minechanics.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions(Array("scala", "org.lolhens.minechanics", "cpw.mods.fml"))
@IFMLLoadingPlugin.SortingIndex(value = 1001)
class FMLCorePlugin extends IFMLLoadingPlugin {
  override def getASMTransformerClass: Array[String] = Array(classOf[ASMTransformer].getName)

  override def injectData(data: util.Map[String, AnyRef]): Unit = {}

  override def getModContainerClass: String = classOf[ModContainer].getName

  override def getAccessTransformerClass: String = null

  override def getSetupClass: String = classOf[SetupClass].getName
}