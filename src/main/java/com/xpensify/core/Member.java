package com.xpensify.core;

import lombok.Getter;

@Getter
class Member {
  Member(String name) {
    this.name = name;
  }

  private final String name;
  private double amountDue;

  double getSpent() {
    return amountDue > 0 ? amountDue : 0;
  }

  double getBorrowed() {
    return amountDue < 0 ? -amountDue : 0;
  }

  void spend(double amount) {
    this.amountDue += amount;
  }

  void borrow(double amount) {
    this.amountDue -= amount;
  }

  boolean hasDuesToSettle() {
    return amountDue != 0;
  } 

  double getAmountDue() {
    return Math.abs(amountDue);
  }
}
