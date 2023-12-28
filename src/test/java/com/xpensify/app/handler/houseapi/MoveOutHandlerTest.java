package com.xpensify.app.handler.houseapi;

import static com.xpensify.util.SystemOutTap.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.xpensify.app.cmd.Command;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberHasUnsettledDuesException;
import com.xpensify.core.HouseMemberNotFoundException;

import lombok.SneakyThrows;

public class MoveOutHandlerTest {

    @ParameterizedTest
    @CsvSource({
            "MOVE_IN ANDY, false",
            "MOVE_OUT REX, true"
    })
    void canHandle_MOVE_IN_command(String commandText, boolean canHandle) {
        MoveOutHandler handler = new MoveOutHandler(mock(HouseApi.class));

        boolean actualCanHandle = handler.canHandle(Command.parse(commandText));

        assertThat(actualCanHandle).isEqualTo(canHandle);
    }

    @SneakyThrows
    @Test
    void invokeHouseApiWithMemberName() {
        String memberName = RandomStringUtils.randomAlphabetic(1, 16);
        String commandText = String.format("MOVE_OUT %s", memberName);
        HouseApi mockApi = mock(HouseApi.class);
        MoveOutHandler handler = new MoveOutHandler(mockApi);

        handler.doHandle(Command.parse(commandText)); 

        verify(mockApi).moveOut(memberName);
    }

    @SneakyThrows
    @ParameterizedTest(name = "[{index}] print {1} for {0}")
    @CsvSource({
        "MOVE_OUT ANDY, 'SUCCESS\n'",
        "MOVE_OUT REX, 'FAILURE\n'",
        "MOVE_OUT BO, 'MEMBER_NOT_FOUND\n'"
    })
    void printCommandOutput(String commandText, String expectedOutput) {
        HouseApi mockApi = mock(HouseApi.class);
        doThrow(new HouseMemberNotFoundException("BO")).when(mockApi).moveOut("BO");
        doThrow(new HouseMemberHasUnsettledDuesException(1150)).when(mockApi).moveOut("REX");
        MoveOutHandler handler = new MoveOutHandler(mockApi);

        String output = tapSystemOut(() -> {
            handler.doHandle(Command.parse(commandText)); 
        });

       assertThat(output).isEqualTo(expectedOutput); 
    }

}
