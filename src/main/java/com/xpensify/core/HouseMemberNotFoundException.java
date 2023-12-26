package com.xpensify.core;

import lombok.Getter;

public class HouseMemberNotFoundException extends Exception {
  @Getter
  private final String memberName;

  public HouseMemberNotFoundException(String memberName) {
    super("member not found in the house");
    this.memberName = memberName;
  }
}
