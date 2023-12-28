package com.xpensify.app.handler.houseapi;

import java.util.List;

import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.HouseNeedsMoreMembersForExpenseTrackingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpendHandler implements CommandHandler {
  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("SPEND");
  }

  @Override
  public void doHandle(Command cmd) {
    double amount = Double.parseDouble(cmd.getArguments().get(0));
    String spentByMember = cmd.getArguments().get(1);
    List<String> spentForMembers = cmd.getArguments().subList(2, cmd.getArguments().size());

    try {
      api.spend(amount, spentByMember, spentForMembers.toArray(new String[0]));
      System.out.println("SUCCESS");
    } catch (HouseMemberNotFoundException e) {
      System.out.println("MEMBER_NOT_FOUND");
    } catch (HouseNeedsMoreMembersForExpenseTrackingException e) {
      System.out.println("FAILURE");
    }
  }

}
