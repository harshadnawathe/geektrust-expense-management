package com.xpensify.app.handler.houseapi;

import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HousefulException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveInHandler implements CommandHandler {

  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("MOVE_IN");
  }

  @Override
  public void doHandle(Command cmd) {
    String memberName = cmd.getArguments().get(0);

    try {
      api.moveIn(memberName);
      System.out.println("SUCCESS");
    } catch (HousefulException e) {
      System.out.println("HOUSEFUL");
    }
  }

}
