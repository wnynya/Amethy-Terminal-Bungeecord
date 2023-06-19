package io.wany.amethy.terminal.bungeecord;

import io.wany.amethyst.Json;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 *
 * Amethy Terminal (Bungeecord)
 * https://amethy.wany.io
 * https://github.com/wnynya/Amethy-Terminal-Bungeecord
 * 
 * ©2023 Wany <sung@wany.io> (https://wany.io)
 *
 */
public class AmethyTerminal extends Plugin {

  public static AmethyTerminal PLUGIN;

  public static final String NAME = "Amethy Terminal";
  public static final String PREFIX = "§l§x§d§2§b§0§d§d[" + NAME + "]:§r ";
  protected static final String PREFIX_CONSOLE = "[" + NAME + "] ";

  public static String VERSION;
  public static File FILE;
  public static File PLUGINS_DIR;
  public static File PLUGIN_DIR;
  public static File SERVER_DIR;
  public static Json CONFIG;
  public static boolean DEBUG = false;
  protected static String UID = "";
  protected static String KEY = "";

  @Override
  public void onLoad() {

    PLUGIN = this;

    VERSION = PLUGIN.getDescription().getVersion();
    FILE = PLUGIN.getFile().getAbsoluteFile();
    PLUGINS_DIR = FILE.getParentFile();
    PLUGIN_DIR = PLUGINS_DIR.toPath().resolve("Amethy-Terminal").toFile();
    SERVER_DIR = PLUGINS_DIR.getParentFile();

    CONFIG = new Json(SERVER_DIR.toPath().resolve("terminal.json").toFile());

    if (CONFIG.has("debug")) {
      DEBUG = CONFIG.getBoolean("debug");
    } else {
      CONFIG.set("debug", false);
    }

    TerminalNode.onLoad();

  }

  @Override
  public void onEnable() {

    TerminalNode.onEnable();

  }

  @Override
  public void onDisable() {

    TerminalNode.onDisable();

  }

}
