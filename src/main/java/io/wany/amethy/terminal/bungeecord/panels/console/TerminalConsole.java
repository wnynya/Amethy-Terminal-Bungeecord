package io.wany.amethy.terminal.bungeecord.panels.console;

import io.wany.amethy.terminal.bungeecord.TerminalNode;
import io.wany.amethy.terminal.bungeecord.console;
import io.wany.amethyst.Json;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TerminalConsole {

  public static Logger logger = ProxyServer.getInstance().getLogger();
  public static TerminalConsoleLogHandler logHandler;
  public static List<Log> offlineLogs = new ArrayList<>();

  public static void onLoad() {
    logHandler = new TerminalConsoleLogHandler();
    logger.addHandler(logHandler);
    console.debug("Logger LogHandler enabled");

    addEventListener();
  }

  public static void onDisable() {
    logger.removeHandler(logHandler);
    console.debug("Logger LogHandler disabled");
  }

  public static void addEventListener() {
    TerminalNode.on("console/command", (client, data) -> {
      command(client, data.getString("command"));
    });
  }

  public static class Log {
    public final String message;
    public final long time;
    public final String level;
    public final String thread;
    public final String logger;
    public final String loggerFqcn;
    public final String marker;
    public final String source;

    public Log(String message, long time, String level, String thread, String logger, String loggerFqcn, String marker,
        String source) {
      this.message = message;
      this.time = time;
      this.level = level;
      this.thread = thread;
      this.logger = logger;
      this.loggerFqcn = loggerFqcn;
      this.marker = marker;
      this.source = source;
    }

    public Log(String message, long time, String level, String logger, String source) {
      this.message = message;
      this.time = time;
      this.level = level;
      this.thread = "";
      this.logger = logger;
      this.loggerFqcn = "";
      this.marker = "";
      this.source = source;
    }

    public Log(String message, long time, String level) {
      this.message = message;
      this.time = time;
      this.level = level;
      this.thread = "";
      this.logger = "";
      this.loggerFqcn = "";
      this.marker = "";
      this.source = "";
    }

    public Json toJson() {
      Json data = new Json();
      data.set("message", this.message);
      data.set("time", this.time);
      data.set("level", this.level);
      data.set("thread", this.thread);
      data.set("logger", this.logger);
      data.set("loggerFqcn", this.loggerFqcn);
      data.set("marker", this.marker);
      data.set("source", this.source);
      return data;
    }
  }

  public static void log(Log log) {
    TerminalNode.event("console/log", log.toJson());
  }

  public static void logOffline() {
    while (offlineLogs.size() > 0) {
      if (!TerminalNode.isOpened()) {
        return;
      }
      Log log = offlineLogs.get(0);
      log(log);
      offlineLogs.remove(0);
    }
  }

  public static void command(Json client, String input) {
    log(new Log("> " + input, System.currentTimeMillis(), "INFO", "ConsoleCommand", "ConsoleCommand"));
    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), input);
  }

}
