package io.wany.amethy.terminal.bungeecord.panels.dashboard;

import com.sun.management.OperatingSystemMXBean;
import io.wany.amethy.terminal.bungeecord.AmethyTerminal;
import io.wany.amethy.terminal.bungeecord.TerminalNode;
import io.wany.amethyst.Json;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalDashboard {

  private static final ExecutorService onEnableExecutor = Executors.newFixedThreadPool(1);
  private static final Timer onEnableTimer1s = new Timer();

  private static Json cachedSystemInfo = null;

  public static void onEnable() {
    onEnableExecutor.submit(() -> {
      onEnableTimer1s.schedule(new TimerTask() {
        @Override
        public void run() {
          sendSystemStatus();
        }
      }, 0, 1000);
    });
  }

  public static void onDisable() {
    onEnableTimer1s.cancel();
    onEnableExecutor.shutdownNow();
  }

  public static Json getCachedSystemInfo() {
    if (cachedSystemInfo != null) {
      return cachedSystemInfo;
    }

    Json object = new Json();

    // 시스템 정보
    try {
      Json system = new Json();
      OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      system.set("name", osb.getName());
      system.set("version", osb.getVersion());
      system.set("arch", osb.getArch());
      system.set("availableProcessors", osb.getAvailableProcessors());
      system.set("totalMemorySize", osb.getTotalMemorySize());
      system.set("committedVirtualMemorySize", osb.getCommittedVirtualMemorySize());
      object.set("system", system);
    } catch (Exception ignored) {
    }

    // 사용자 정보
    try {
      Json user = new Json();
      user.set("name", System.getProperty("user.name"));
      user.set("home", System.getProperty("user.home"));
      user.set("dir", System.getProperty("user.dir"));
      object.set("user", user);
    } catch (Exception ignored) {
    }

    // OS 정보
    try {
      Json os = new Json();
      os.set("version", System.getProperty("os.version"));
      os.set("name", System.getProperty("os.name"));
      os.set("arch", System.getProperty("os.arch"));
      object.set("os", os);
    } catch (Exception ignored) {
    }

    // JVM 정보
    try {
      Json java = new Json();
      java.set("version", System.getProperty("java.vm.version"));
      java.set("runtime", System.getProperty("java.runtime.name"));
      java.set("vendor", System.getProperty("java.vm.vendor"));
      java.set("home", System.getProperty("java.home"));
      object.set("java", java);
    } catch (Exception ignored) {
    }

    // 번지 서버 정보
    try {
      Json server = new Json();
      ProxyServer ps = ProxyServer.getInstance();
      ListenerInfo li = ProxyServer.getInstance().getConfigurationAdapter().getListeners().iterator().next();
      server.set("name", ps.getName());
      server.set("port", li.getQueryPort());
      server.set("maxPlayers", li.getMaxPlayers());
      server.set("version", ps.getVersion());
      server.set("motd", li.getMotd());
      server.set("dir", AmethyTerminal.SERVER_DIR.getAbsolutePath().replace("\\", "/"));
      object.set("server", server);
    } catch (Exception ignored) {
    }

    // 네트워크 정보
    try {
      Json network = new Json();
      String ip = null;
      String hostname = null;
      try {
        ip = InetAddress.getLocalHost().toString();
        hostname = InetAddress.getLocalHost().getHostName();
      } catch (Exception ignored) {
      }
      network.set("ip", ip);
      network.set("hostname", hostname);
      List<Json> netInterfaces = new ArrayList<Json>();
      try {
        Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
        while (nics.hasMoreElements()) {
          NetworkInterface nic = nics.nextElement();
          Enumeration<InetAddress> addrs = nic.getInetAddresses();
          while (addrs.hasMoreElements()) {
            InetAddress addr = addrs.nextElement();
            Json netInterface = new Json();
            netInterface.set("name", nic.getName());
            netInterface.set("address", addr.getHostAddress());
            netInterfaces.add(netInterface);
          }
        }
      } catch (Exception ignored) {
      }
      network.set("interfaces", netInterfaces);
      object.set("network", network);
    } catch (Exception ignored) {
    }

    cachedSystemInfo = object;
    return cachedSystemInfo;
  }

  public static Json getSystemStatus() {
    Json object = new Json();

    // 서버 업타임
    try {
      long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
      object.set("uptime", uptime);
    } catch (Exception ignored) {
    }

    // 메모리 상태
    try {
      Runtime r = Runtime.getRuntime();
      object.set("memory-free", r.freeMemory());
      object.set("memory-max", r.maxMemory());
      object.set("memory-total", r.totalMemory());
    } catch (Exception ignored) {
    }

    // 프로세서 상태
    try {
      OperatingSystemMXBean osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      object.set("cpu-system-load", osb.getCpuLoad());
      object.set("cpu-process-load", osb.getProcessCpuLoad());
    } catch (Exception ignored) {
    }

    // 플레이어 수
    object.set("players-count", ProxyServer.getInstance().getPlayers().size());

    return object;
  }

  public static void sendSystemInfo() {
    TerminalNode.event("dashboard/systeminfo", getCachedSystemInfo());
  }

  public static void sendSystemStatus() {
    TerminalNode.event("dashboard/systemstatus", getSystemStatus());
  }

}
