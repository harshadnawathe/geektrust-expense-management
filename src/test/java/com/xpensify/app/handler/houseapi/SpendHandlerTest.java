package com.xpensify.app.handler.houseapi;

import static com.xpensify.util.SystemOutTap.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.xpensify.app.cmd.Command;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.HouseNeedsMoreMembersForExpenseTrackingException;

import lombok.SneakyThrows;

public class SpendHandlerTest {

  @ParameterizedTest
  @CsvSource({
      "SPEND 1000 ANDY BO, true",
      "DUES REX, false"
  })
  void canHandle_SPEND_command(String commandText, boolean canHandle) {
    SpendHandler handler = new SpendHandler(mock(HouseApi.class));

    boolean actualCanHandle = handler.canHandle(Command.parse(commandText));

    assertThat(actualCanHandle).isEqualTo(canHandle);
  }

  @SneakyThrows
  @Test
  void invokeHouseApiWithCommandArguemts() {
    String memberName = RandomStringUtils.randomAlphabetic(1, 16);
    String[] spentForMembers = randomSpentForMembers();
    String commandText = String.format("SPEND %d %s %s", 1000, memberName, String.join(" ", spentForMembers));
    HouseApi mockApi = mock(HouseApi.class);
    SpendHandler handler = new SpendHandler(mockApi);

    handler.doHandle(Command.parse(commandText));

    verify(mockApi).spend(1000, memberName, spentForMembers);
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
      "SPEND 3000 ANDY WOODY BO, 'SUCCESS\n'",
      "SPEND 3000 WOODY BO, 'FAILURE\n'",
      "SPEND 3000 REX WOODY, 'MEMBER_NOT_FOUND\n'",
  })
  void printCommandOutput(String commandText, String expectedOutput) {
    HouseApi mockApi = mock(HouseApi.class);
    doThrow(new HouseMemberNotFoundException("REX")).when(mockApi).spend(anyDouble(), eq("REX"), any());
    doThrow(new HouseNeedsMoreMembersForExpenseTrackingException()).when(mockApi).spend(anyDouble(), eq("WOODY"),
        any());
    SpendHandler handler = new SpendHandler(mockApi);

    String output = tapSystemOut(() -> {
      handler.doHandle(Command.parse(commandText));
    });

    assertThat(output).isEqualTo(expectedOutput);
  }

  private String[] randomSpentForMembers() {
    int size = RandomUtils.nextInt(1, 5);
    ArrayList<String> list = new ArrayList<String>(size);
    for (int i = 0; i < size; i++) {
      list.add(RandomStringUtils.randomAlphabetic(1, 16));
    }
    return list.toArray(new String[0]);
  }
}
