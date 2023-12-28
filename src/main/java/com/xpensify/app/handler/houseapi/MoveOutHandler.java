package com.xpensify.app.handler.houseapi;

import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberHasUnsettledDuesException;
import com.xpensify.core.HouseMemberNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveOutHandler implements CommandHandler {

  private static final int MEMBER_NAME_POSITION = 0;
  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("MOVE_OUT");
  }

  @Override
  public void doHandle(Command cmd) {
    String memberName = cmd.getArguments().get(MEMBER_NAME_POSITION);

    try {
      api.moveOut(memberName);
      System.out.println("SUCCESS");
    } catch (HouseMemberNotFoundException e) {
      System.out.println("MEMBER_NOT_FOUND");
    } catch (HouseMemberHasUnsettledDuesException e) {
      System.out.println("FAILURE");
    }
  }

}
