package com.xpensify.app.handler.houseapi;

import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.MemberDues;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DuesHandler implements CommandHandler {

  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("DUES");
  }

  @Override
  public void doHandle(Command cmd) {
    String memberName = cmd.getArguments().get(0);

    try {
      MemberDues dues = api.dues(memberName);
      dues.getDues().forEach((amountDue) -> {
        System.out.println(
          String.format("%s %d", amountDue.getLentByMember(), (int) amountDue.getAmount())
        );
      });
    } catch (HouseMemberNotFoundException e) {
      System.out.println("MEMBER_NOT_FOUND");
    }
  }

}
