package io.wany.amethy.terminal.bungeecord;

import java.io.File;
import io.wany.amethy.terminal.bungeecord.modules.Json;
import net.md_5.bungee.api.plugin.Plugin;

public class AmethyTerminal extends Plugin {

  public static AmethyTerminal PLUGIN;

  public static final String NAME = "Amethy Terminal";
  public static final String PREFIX = "§l§x§d§2§b§0§d§d[" + NAME + "]:§r ";
  protected static final String PREFIX_CONSOLE = "[" + NAME + "] ";

  public static String VERSION;
  public static File FILE;
  public static File PLUGINS_DIR;
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
