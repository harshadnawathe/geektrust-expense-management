package com.xpensify.core;

import lombok.Getter;

public class HouseMemberHasUnsettledDuesException extends Exception {
  @Getter
  private final double amountDue;

  public HouseMemberHasUnsettledDuesException(double amountDue) {
    super("member has unsettled dues");
    this.amountDue = amountDue;
  }

}
