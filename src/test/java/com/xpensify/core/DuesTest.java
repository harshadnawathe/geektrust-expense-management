package com.xpensify.core;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class DuesTest {

  @Test
  void noDuesIfMemberHasNotBorrowedAnyMoney() {
    Dues dues = new Dues(getMembers());
    MemberDues memberDues = dues.of("ANDY");

    assertThat(memberDues.getMemberName()).isEqualTo("ANDY");
    assertThat(memberDues.getDues()).containsExactly(new AmountDue("BO", 0), new AmountDue("WOODY", 0));
  }

  @Test
  void duesIfMemberHasNotBorrowedAnyMoney() {
    Dues dues = new Dues(getMembers());
    MemberDues memberDues = dues.of("BO");

    assertThat(memberDues.getMemberName()).isEqualTo("BO");
    assertThat(memberDues.getDues()).containsExactly(new AmountDue("ANDY", 1150), new AmountDue("WOODY", 0));
  }

  private static List<Member> getMembers() {
    Member andy = new Member("ANDY");
    andy.spend(2000);

    Member woody = new Member("WOODY");
    woody.borrow(850);

    Member bo = new Member("BO");
    bo.borrow(1150);

    return Arrays.stream(new Member[] { andy, woody, bo }).collect(Collectors.toUnmodifiableList());
  }

}
