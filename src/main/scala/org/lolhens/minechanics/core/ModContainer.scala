package org.lolhens.minechanics.core

import com.google.common.eventbus.EventBus
import cpw.mods.fml.common.{DummyModContainer, LoadController, ModMetadata}
import org.lolhens.minechanics.Minechanics

/**
 * Created by LolHens on 09.04.2015.
 */
class ModContainer extends DummyModContainer(new ModMetadata()) {
  val metadata = getMetadata()
  metadata.modId = Minechanics.id
  metadata.name = Minechanics.name
  metadata.version = Minechanics.version

  override def registerBus(bus: EventBus, controller: LoadController): Boolean = {
    bus.register(this)
    true
  }
}