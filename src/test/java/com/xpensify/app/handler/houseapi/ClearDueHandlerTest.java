package com.xpensify.app.handler.houseapi;

import static com.xpensify.util.SystemOutTap.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.anyDouble;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.xpensify.app.cmd.Command;
import com.xpensify.core.AmountDue;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.IncorrectSettlementAmount;

import lombok.SneakyThrows;

public class ClearDueHandlerTest {
  @ParameterizedTest
  @CsvSource({
      "CLEAR_DUE BO ANDY 500, true",
      "DUES WOODY, false",
  })
  void canHandle_DUES_command(String commandText, boolean canHandle) {
    ClearDueHandler handler = new ClearDueHandler(mock(HouseApi.class));

    boolean actualCanHandle = handler.canHandle(Command.parse(commandText));

    assertThat(actualCanHandle).isEqualTo(canHandle);
  }

  @SneakyThrows
  @Test
  void invokeHouseApiWithMemberName() {
    String memberName = RandomStringUtils.randomAlphabetic(1, 16);
    String lentByMemberName = RandomStringUtils.randomAlphabetic(1, 16);
    int amount = RandomUtils.nextInt(1, 1000);
    String commandText = String.format("CLEAR_DUE %s %s %d", memberName, lentByMemberName, amount);
    HouseApi mockApi = mock(HouseApi.class);
    doReturn(new AmountDue(lentByMemberName, 0)).when(mockApi).clearDue(memberName, lentByMemberName, amount);
    ClearDueHandler handler = new ClearDueHandler(mockApi);

    handler.doHandle(Command.parse(commandText));

    verify(mockApi).clearDue(memberName, lentByMemberName, amount);
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
      "CLEAR_DUE ANDY BO 500, '100\n'",
      "CLEAR_DUE WOODY BO 2500, 'INCORRECT_PAYMENT\n'",
      "CLEAR_DUE REX BO 500, 'MEMBER_NOT_FOUND\n'"
  })
  void printCommandOutput(String commandText, String expectedOutput) {
    HouseApi mockApi = mock(HouseApi.class);
    doReturn(new AmountDue("BO", 100)).when(mockApi).clearDue(eq("ANDY"), anyString(), anyDouble());
    doThrow(new IncorrectSettlementAmount(1000)).when(mockApi).clearDue(eq("WOODY"), anyString(), anyDouble());
    doThrow(new HouseMemberNotFoundException("REX")).when(mockApi).clearDue(eq("REX"), anyString(), anyDouble());
    ClearDueHandler handler = new ClearDueHandler(mockApi);

    String output = tapSystemOut(() -> {
      handler.doHandle(Command.parse(commandText));
    });

    assertThat(output).isEqualTo(expectedOutput);
  }

}
