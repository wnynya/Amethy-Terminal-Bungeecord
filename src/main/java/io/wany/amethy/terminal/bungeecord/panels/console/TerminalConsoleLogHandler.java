package io.wany.amethy.terminal.bungeecord.panels.console;

import io.wany.amethy.terminal.bungeecord.TerminalNode;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TerminalConsoleLogHandler extends Handler {

  @Override
  public void close() throws SecurityException {
  }

  @Override
  public void flush() {
  }

  @Override
  public void publish(LogRecord logRecord) {
    try {
      // 로그 메시지 가져오기
      String message = logRecord.getMessage();

      // 로그에 스택이 있을 경우
      StringBuilder stack = new StringBuilder();
      Throwable thrown = logRecord.getThrown();
      if (thrown != null) {
        stack.append("\r\n");
        stack.append(thrown.getClass().getName()).append(" => ");
        stack.append(thrown.getMessage());
        StackTraceElement[] stea = thrown.getStackTrace();
        for (StackTraceElement ste : stea) {
          stack.append("\r\n\tat ").append(ste.getFileName()).append(":").append(ste.getLineNumber()).append(" (")
              .append(ste.getClassName()).append(".").append(ste.getMethodName()).append(")");
        }
        Throwable thrownCause = thrown.getCause();
        while (thrownCause != null) {
          stack.append("\r\nCaused by: ");
          stack.append(thrownCause.getClass().getName());
          StackTraceElement[] cstea = thrownCause.getStackTrace();
          for (StackTraceElement ste : cstea) {
            stack.append("\r\n\tat ").append(ste.getFileName()).append(":").append(ste.getLineNumber()).append(" (")
                .append(ste.getClassName()).append(".").append(ste.getMethodName()).append(")");
          }
          thrownCause = thrownCause.getCause();
        }
      }
      message += stack.toString();

      // 로깅 시각
      long time = logRecord.getMillis();

      // 로그 레벨
      String level = logRecord.getLevel().getName();

      // 로그 로거 이름
      String logger = logRecord.getLoggerName();

      // 로그 호출 소스
      String source = "";
      source += logRecord.getSourceClassName() + ".";
      source += logRecord.getSourceMethodName();

      try {
        TimeUnit.MICROSECONDS.sleep(1);
      } catch (InterruptedException ignored) {
      }

      // 로그 오브젝트 만들기
      TerminalConsole.Log log = new TerminalConsole.Log(message, time, level, logger, source);

      // 터미널이 열려 있는 경우
      if (TerminalNode.isOpened()) {
        // 오프라인 로그 스택이 비어 있는 경우 -> 로그 전송
        if (TerminalConsole.offlineLogs.size() <= 0) {
          TerminalConsole.log(log);
        }
        // 오프라인 로그 스택이 차 있는 경우 -> 오프라인 로그부터 전송
        else {
          TerminalConsole.offlineLogs.add(log);
          TerminalConsole.logOffline();
        }
      }
      // 터미널이 닫혀 있는 경우 -> 오프라인 로그 스택에 넣음
      else {
        TerminalConsole.offlineLogs.add(log);
      }

    } catch (Exception ignored) {
      // 로그 필터에서 예외가 발생하면 -> 서버가 폭발함
    }
  }

}
