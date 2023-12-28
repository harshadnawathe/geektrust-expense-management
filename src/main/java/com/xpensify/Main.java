package com.xpensify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.app.cmd.CommandSwitch;
import com.xpensify.app.handler.houseapi.ClearDueHandler;
import com.xpensify.app.handler.houseapi.DuesHandler;
import com.xpensify.app.handler.houseapi.MoveInHandler;
import com.xpensify.app.handler.houseapi.MoveOutHandler;
import com.xpensify.app.handler.houseapi.SpendHandler;
import com.xpensify.core.House;

public class Main {
  public static void main(String[] args) {
    House house = new House(3);

    List<CommandHandler> handlers = new ArrayList<>();
    handlers.add(new MoveInHandler(house));
    handlers.add(new SpendHandler(house));
    handlers.add(new DuesHandler(house));
    handlers.add(new ClearDueHandler(house));
    handlers.add(new MoveOutHandler(house));

    CommandSwitch commandSwitch = new CommandSwitch(handlers);

    Path inputFilePath = Paths.get(args[0]);

    try (Stream<String> lines = Files.lines(inputFilePath)) {
      lines.map(Command::parse).forEach(commandSwitch::handle);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
