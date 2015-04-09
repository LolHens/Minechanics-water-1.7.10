package org.lolhens.minechanics.core

import cpw.mods.fml.common.{DummyModContainer, ModMetadata}
import org.lolhens.minechanics.Minechanics

/**
 * Created by LolHens on 09.04.2015.
 */
class ModContainer extends DummyModContainer(new ModMetadata()) {
  val metadata = getMetadata()
  metadata.modId = Minechanics.id
  metadata.name = Minechanics.name
  metadata.version = Minechanics.version
}