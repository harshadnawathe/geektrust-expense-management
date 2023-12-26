package com.xpensify.core;

import lombok.Getter;

public class IncorrectSettlementAmount extends Exception {
  @Getter
  private final double amountDue;

  public IncorrectSettlementAmount(double amountDue) {
    super("incorrect settlement amount");
    this.amountDue = amountDue;
  }
}
