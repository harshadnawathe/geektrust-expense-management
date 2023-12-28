package com.xpensify.app.cmd;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;


@Value
public class Command {
    String type;
    List<String> arguments;

    public static Command parse(String text) {
        List<String> tokens = new ArrayList<>(asList(text.split("\\s+")));
        String type = tokens.get(0);
        tokens.remove(0);
        return new Command(type, tokens);
    }
}
