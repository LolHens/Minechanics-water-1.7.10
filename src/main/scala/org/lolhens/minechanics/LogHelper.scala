package org.lolhens.minechanics

import cpw.mods.fml.common.FMLLog
import org.apache.logging.log4j.Level

/**
 * Created by LolHens on 09.04.2015.
 */
object LogHelper {
  def log(logLevel: Level, obj: Any) = FMLLog.log(Minechanics.name, logLevel, String.valueOf(obj))

  def all(obj: Any) = log(Level.ALL, obj)

  def debug(obj: Any) = log(Level.DEBUG, obj)

  def error(obj: Any) = log(Level.ERROR, obj)

  def fatal(obj: Any) = log(Level.FATAL, obj)

  def info(obj: Any) = log(Level.INFO, obj)

  def off(obj: Any) = log(Level.OFF, obj)

  def trace(obj: Any) = log(Level.TRACE, obj)

  def warn(obj: Any) = log(Level.WARN, obj)
}
