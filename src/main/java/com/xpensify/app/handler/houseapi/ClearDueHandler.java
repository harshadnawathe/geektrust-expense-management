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
  private final HouseApi api;

  @Override
  public boolean canHandle(Command cmd) {
    return cmd.getType().equals("CLEAR_DUE");
  }

  @Override
  public void doHandle(Command cmd) {
    String memberName = cmd.getArguments().get(0);
    String lentByMemberName = cmd.getArguments().get(1);
    double amount = Double.parseDouble(cmd.getArguments().get(2));

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
