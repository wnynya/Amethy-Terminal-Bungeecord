package io.wany.amethy.terminal.bungeecord.panels.filesystem;

import io.wany.amethyst.Json;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TerminalDir {

  private File file;

  public TerminalDir(File file) {
    this.file = file;
  }

  public File file() {
    return this.file;
  }

  public Json info() {
    Json json = new Json();
    json.set("type", "dir");
    json.set("path", this.file.getAbsolutePath());
    json.set("name", this.file.getName());
    json.set("perm.r", this.file.canRead());
    json.set("perm.w", this.file.canWrite());
    json.set("perm.x", this.file.canExecute());
    json.set("size", 0);
    try {
      BasicFileAttributes attr = Files.readAttributes(this.file.toPath(), BasicFileAttributes.class);
      json.set("creation", attr.creationTime().toMillis());
      json.set("modified", attr.lastModifiedTime().toMillis());
    } catch (Exception e) {
      json.set("creation", 0);
      json.set("modified", 0);
    }
    return json;
  }

  public long size() {
    try {
      return Files.size(this.file.toPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void create() {
    this.file.mkdirs();
  }

  public void delete() {
    try {
      FileUtils.deleteDirectory(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<Json> files() {
    List<Json> files = new ArrayList<>();

    if (this.file.getParentFile() != null) {
      TerminalDir parent = new TerminalDir(this.file.getParentFile());
      Json parentInfo = parent.info();
      parentInfo.set("name", "..");
      files.add(parentInfo);
    }
    TerminalDir current = new TerminalDir(this.file);
    Json currentInfo = current.info();
    currentInfo.set("name", ".");
    files.add(currentInfo);

    for (File file : this.file.listFiles()) {
      if (file.isDirectory()) {
        TerminalDir td = new TerminalDir(file);
        files.add(td.info());
      } else if (file.isFile()) {
        TerminalFile tf = new TerminalFile(file);
        files.add(tf.info());
      }
    }
    return files;
  }
}
