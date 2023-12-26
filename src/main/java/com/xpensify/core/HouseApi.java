package com.xpensify.core;

public interface HouseApi {

  void moveIn(String memberName) throws HousefulException;

  MemberDues dues(String memberName);

  void spend(double amount, String spentByMember, String... spentForMembers)
      throws HouseMemberNotFoundException, HouseNeedsMoreMembersForExpenseTrackingException;

  void moveOut(String memberName) throws HouseMemberNotFoundException, HouseMemberHasUnsettledDuesException;

  void clearDue(String memberName, String lentByMemberName, double amount)
      throws HouseMemberNotFoundException, IncorrectSettlementAmount;

}
