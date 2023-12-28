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
  private static final int AMOUNT_POSITION = 0;
  private static final int SPENT_BY_MEMBER = 1;
  private static final int SPENT_FOR_MEMBERS_START_POSITION = 2;
  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("SPEND");
  }

  @Override
  public void doHandle(Command cmd) {
    double amount = Double.parseDouble(cmd.getArguments().get(AMOUNT_POSITION));
    String spentByMember = cmd.getArguments().get(SPENT_BY_MEMBER);
    List<String> spentForMembers = cmd.getArguments().subList(SPENT_FOR_MEMBERS_START_POSITION,
        cmd.getArguments().size());

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
