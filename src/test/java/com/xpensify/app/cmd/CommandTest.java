package com.xpensify.app.cmd;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CommandTest {

  private final String TEXT = "MyCmd Arg1 Arg2";

  @Test
  void createCommandWithGivenTypeFromText() {
    Command command = Command.parse(TEXT);

    String type = command.getType();

    assertThat(type).isEqualTo("MyCmd");
  }

  @Test
  void createCommandWithGivenArguments() {
    Command command = Command.parse(TEXT);

    List<String> arguments = command.getArguments();

    assertThat(arguments).containsExactly("Arg1", "Arg2");
  }

}
