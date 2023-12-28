package com.xpensify.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

import lombok.Getter;

public class House  implements HouseApi {
  @Getter
  private final int capacity;
  private final Map<String, Member> members = new HashMap<>();

  public House(int capacity) {
    this.capacity = capacity;
  }

  public int getNumberOfMembers() {
    return members.size();
  }

  @Override
  public void moveIn(String memberName) throws HousefulException {
    checkHouseIsNotFull();
    this.members.put(memberName, new Member(memberName));
  }

  @Override
  public MemberDues dues(String memberName) throws HouseMemberNotFoundException {
    checkIfMembersArePresentInHouse(memberName);
    Dues dues = new Dues(this.members.values());
    return dues.of(memberName);
  }

  @Override
  public void spend(double amount, String spentByMember, String... spentForMembers)
      throws HouseMemberNotFoundException, HouseNeedsMoreMembersForExpenseTrackingException {
    String[] sharedByMembers = ArrayUtils.add(spentForMembers, spentByMember);
    checkIfMembersArePresentInHouse(sharedByMembers);
    
    checkHouseContainsEnoughMembersForExpenseTracking();

    members.get(spentByMember).spend(amount);

    double sharePerHead = amount / sharedByMembers.length;
    Arrays.stream(sharedByMembers).forEach((memberName) -> {
      members.get(memberName).borrow(sharePerHead);
    });
  }

  @Override
  public void moveOut(String memberName) throws HouseMemberNotFoundException, HouseMemberHasUnsettledDuesException {
    checkIfMembersArePresentInHouse(memberName);
    Member member = members.get(memberName);
    if (member.hasDuesToSettle()) {
      throw new HouseMemberHasUnsettledDuesException(member.getAmountDue());
    }
    members.remove(memberName);
  }

  @Override
  public AmountDue clearDue(String memberName, String lentByMemberName, double amount)
      throws HouseMemberNotFoundException, IncorrectSettlementAmount {
    checkIfMembersArePresentInHouse(memberName, lentByMemberName);

    MemberDues dues = dues(memberName);
    double amountDue = dues.amountDue(lentByMemberName).getAmount();
    if (amount > amountDue) {
      throw new IncorrectSettlementAmount(amountDue);
    }

    Member member = members.get(memberName);
    Member lentByMember = members.get(lentByMemberName);

    member.spend(amount);
    lentByMember.borrow(amount);

    return dues(memberName).amountDue(lentByMemberName);
  }

  private void checkHouseIsNotFull() throws HousefulException {
    if (this.members.size() == this.capacity) {
      throw new HousefulException(this.capacity);
    }
  }

  private void checkIfMembersArePresentInHouse(String... memberNames) throws HouseMemberNotFoundException {
    Optional<String> nonMember = Arrays.stream(memberNames)
        .filter(memberName -> !this.members.containsKey(memberName))
        .findFirst();
    if (nonMember.isPresent()) {
      throw new HouseMemberNotFoundException(nonMember.get());
    }
  }

  private void checkHouseContainsEnoughMembersForExpenseTracking()
      throws HouseNeedsMoreMembersForExpenseTrackingException {
    if (members.size() < 2) {
      throw new HouseNeedsMoreMembersForExpenseTrackingException();
    }
  }

}
