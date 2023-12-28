package com.xpensify.app.cmd;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandSwitch {

  private final List<CommandHandler> handlers;

  public void handle(Command command) {
    handlers.stream()
        .filter(it -> it.canHandle(command))
        .findFirst()
        .ifPresent(it -> it.doHandle(command));
  }
}
