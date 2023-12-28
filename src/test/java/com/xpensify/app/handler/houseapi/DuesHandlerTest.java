package com.xpensify.app.handler.houseapi;

import static com.xpensify.util.SystemOutTap.tapSystemOut;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.xpensify.app.cmd.Command;
import com.xpensify.core.AmountDue;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.MemberDues;

import lombok.SneakyThrows;

public class DuesHandlerTest {

  @ParameterizedTest
  @CsvSource({
      "MOVE_IN ANDY, false",
      "DUES REX, true"
  })
  void canHandle_DUES_command(String commandText, boolean canHandle) {
    DuesHandler handler = new DuesHandler(mock(HouseApi.class));

    boolean actualCanHandle = handler.canHandle(Command.parse(commandText));

    assertThat(actualCanHandle).isEqualTo(canHandle);
  }

  @SneakyThrows
  @Test
  void invokeHouseApiWithMemberName() {
    String memberName = RandomStringUtils.randomAlphabetic(1, 16);
    String commandText = String.format("DUES %s", memberName);
    HouseApi mockApi = mock(HouseApi.class);
    when(mockApi.dues(memberName)).thenReturn(new MemberDues(memberName, asList()));
    DuesHandler handler = new DuesHandler(mockApi);

    handler.doHandle(Command.parse(commandText));

    verify(mockApi).dues(memberName);
  }

  @SneakyThrows
  @ParameterizedTest(name = "[{index}] print {1} for {0}")
  @CsvSource({
      "DUES ANDY, 'BO 500\nWOODY 0\n'",
      "DUES REX, 'MEMBER_NOT_FOUND\n'"
  })
  void printCommandOutput(String commandText, String expectedOutput) {
    HouseApi mockApi = mock(HouseApi.class);
    when(mockApi.dues("ANDY"))
        .thenReturn(
            new MemberDues(
                "ANDY",
                asList(new AmountDue("BO", 500), new AmountDue("WOODY", 0))));
    when(mockApi.dues("REX")).thenThrow(new HouseMemberNotFoundException("REX"));
    DuesHandler handler = new DuesHandler(mockApi);

    String output = tapSystemOut(() -> {
      handler.doHandle(Command.parse(commandText));
    });

    assertThat(output).isEqualTo(expectedOutput);
  }
}
