package com.xpensify.core;

import lombok.Getter;

public class HousefulException extends Exception {
  @Getter
  private final int capacity;
  public HousefulException(int capacity) {
    super("house is at full capacity");
    this.capacity = capacity;
  }

}

