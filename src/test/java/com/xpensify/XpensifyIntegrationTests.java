package com.xpensify;

import static com.xpensify.util.SystemOutTap.tapSystemOut;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lombok.SneakyThrows;

public class XpensifyIntegrationTests {

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
      "'./sample_input/input1.txt', './sample_input/output1.txt'",
      "'./sample_input/input2.txt', './sample_input/output2.txt'",
      "'./sample_input/input3.txt', './sample_input/output3.txt'"
  })
  void parseFileAndPrintOutput(String inputFilePath, String expectedOutputFilePath) {
    String[] args = new String[] { inputFilePath };

    String output = tapSystemOut(() -> {
      Main.main(args);
    });

    String expectedOutput = Files.readString(Paths.get(expectedOutputFilePath));
    assertThat(output).isEqualTo(expectedOutput);
  }

}
