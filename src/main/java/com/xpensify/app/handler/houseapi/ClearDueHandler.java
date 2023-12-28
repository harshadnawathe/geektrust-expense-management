package com.xpensify.app.handler.houseapi;

import com.xpensify.app.cmd.Command;
import com.xpensify.app.cmd.CommandHandler;
import com.xpensify.core.AmountDue;
import com.xpensify.core.HouseApi;
import com.xpensify.core.HouseMemberNotFoundException;
import com.xpensify.core.IncorrectSettlementAmount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClearDueHandler implements CommandHandler {
  private static final int MEMBER_NAME_POSITION = 0;
  private static final int LENT_BY_MEMBER_NAME = 1;
  private static final int AMOUT_POSITION = 2;
  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("CLEAR_DUE");
  }

  @Override
  public void doHandle(Command cmd) {
    String memberName = cmd.getArguments().get(MEMBER_NAME_POSITION);
    String lentByMemberName = cmd.getArguments().get(LENT_BY_MEMBER_NAME);
    double amount = Double.parseDouble(cmd.getArguments().get(AMOUT_POSITION));

    try {
      AmountDue amountDue = api.clearDue(memberName, lentByMemberName, amount);
      System.out.println(String.format("%d", (int) amountDue.getAmount()));
    } catch (HouseMemberNotFoundException e) {
      System.out.println("MEMBER_NOT_FOUND");
    } catch (IncorrectSettlementAmount e) {
      System.out.println("INCORRECT_PAYMENT");
    }
  }

}
