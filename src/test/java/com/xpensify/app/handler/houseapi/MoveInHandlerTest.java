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
import com.xpensify.core.HousefulException;

import lombok.SneakyThrows;

public class MoveInHandlerTest {

    @ParameterizedTest
    @CsvSource({
            "MOVE_IN ANDY, true",
            "MOVE_OUT REX, false"
    })
    void canHandle_MOVE_IN_command(String commandText, boolean canHandle) {
        MoveInHandler handler = new MoveInHandler(mock(HouseApi.class));

        boolean actualCanHandle = handler.canHandle(Command.parse(commandText));

        assertThat(actualCanHandle).isEqualTo(canHandle);
    }

    @SneakyThrows
    @Test
    void invokeHouseApiWithMemberName() {
        String memberName = RandomStringUtils.randomAlphabetic(1, 16);
        String commandText = String.format("MOVE_IN %s", memberName);
        HouseApi mockApi = mock(HouseApi.class);
        MoveInHandler handler = new MoveInHandler(mockApi);

        handler.doHandle(Command.parse(commandText)); 

        verify(mockApi).moveIn(memberName);
    }

    @SneakyThrows
    @ParameterizedTest(name = "[{index}] print {1} for {0}")
    @CsvSource({
        "MOVE_IN ANDY, 'SUCCESS\n'",
        "MOVE_IN REX, 'HOUSEFUL\n'"
    })
    void printCommandOutput(String commandText, String expectedOutput) {
        HouseApi mockApi = mock(HouseApi.class);
        doThrow(new HousefulException(2)).when(mockApi).moveIn("REX");
        MoveInHandler handler = new MoveInHandler(mockApi);

        String output = tapSystemOut(() -> {
            handler.doHandle(Command.parse(commandText)); 
        });

       assertThat(output).isEqualTo(expectedOutput); 
    }

    

}
