package com.xpensify.core;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class MemberDues {
  @Getter
  private final String memberName;
  @Getter
  private final List<AmountDue> dues;

  public MemberDues(String memberName, List<AmountDue> dues) {
    this.memberName = memberName;
    this.dues = dues.stream()
        .sorted(Comparator.comparing(AmountDue::getAmount).reversed().thenComparing(AmountDue::getLentByMember))
        .collect(Collectors.toUnmodifiableList());
  }

  AmountDue amountDue(String lentByMember) {
    return this.dues.stream()
        .filter(amountDue -> amountDue.getLentByMember().equals(lentByMember))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("member not found"));
  }

}
