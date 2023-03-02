package io.wany.amethy.terminal.bungeecord;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;

public class Console {

  public static Logger logger = ProxyServer.getInstance().getLogger();

  private static void log(Level level, String message) {
    logger.log(level, message);
  }

  public static void info(String message) {
    log(Level.INFO, AmethyTerminal.PREFIX_CONSOLE + message + "\u001b[0m");
  }

  public static void warn(String message) {
    log(Level.WARNING, AmethyTerminal.PREFIX_CONSOLE + "\u001b[93m" + message + "\u001b[0m");
  }

  public static void debug(String message) {
    if (AmethyTerminal.DEBUG) {
      log(Level.INFO, AmethyTerminal.PREFIX_CONSOLE + "[DEBUG] " + message + "\u001b[0m");
    }
  }

  public static void log(String message) {
    info(message);
  }

}
