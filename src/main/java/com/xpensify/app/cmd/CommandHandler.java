package com.xpensify.app.cmd;

public interface CommandHandler {
  boolean canHandle(Command cmd);

  void doHandle(Command cmd);
}
