package com.xpensify.app.cmd;

import static java.util.Arrays.asList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.*;

public class CommandSwitchTest {

  @ParameterizedTest
  @CsvSource({
    "Cmd1, true, false",
    "Cmd2, false, true",
    "Cmd3, false, false",
  })
  void delegateToHandlerIfItCanHandle(String commandType, boolean handledBy1, boolean handledBy2) {
    TestCommandHandler handler1 = new TestCommandHandler("Cmd1");
    TestCommandHandler handler2 = new TestCommandHandler("Cmd2");
    CommandSwitch commandSwitch = new CommandSwitch(asList(handler1, handler2));

    commandSwitch.handle(new Command(commandType, asList()));

    assertThat(handler1.isCommandHandled()).isEqualTo(handledBy1);
    assertThat(handler2.isCommandHandled()).isEqualTo(handledBy2);
  }

}

@RequiredArgsConstructor
class TestCommandHandler implements CommandHandler {
  private final String commandTypeCanHandle;
  
  @Getter
  private boolean commandHandled = false;

  @Override
  public boolean canHandle(Command cmd) {
    return commandTypeCanHandle.equals(cmd.getType());
  }

  @Override
  public void doHandle(Command cmd) {
    commandHandled = true;
  }
}
