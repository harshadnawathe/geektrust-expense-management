package com.xpensify.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MemberTest {

  @Nested
  @DisplayName("when initialized")
  class MemberInitializationTest {

    @Test
    void setNameToGivenValue() {
      String expectedName = "ANDY";
      Member member = new Member(expectedName);

      String actualName = member.getName();

      assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    void setSpentAmountToZero() {
      Member member = new Member("ANDY");

      double spent = member.getSpent();

      assertThat(spent).isEqualTo(0.0);
    }

    @Test
    void setBorrowedAmoutToZero() {
      Member member = new Member("ANDY");

      double borrowed = member.getBorrowed();

      assertThat(borrowed).isEqualTo(0.0);
    }

    @Test
    void hasNoDuesToSettled() {
      Member member = new Member("ANDY");

      assertThat(member.hasDuesToSettle()).isFalse();
    }

    
    
  }

  @Nested
  class SpendAndBorrowTest {

    @Test
    void whenBorrowedAmountIsMoreThanSpent() {
      Member member = new Member("ANDY");

      member.spend(1000.0);
      member.borrow(2000.0);

      assertThat(member.getSpent()).isEqualTo(0.0);
      assertThat(member.getBorrowed()).isEqualTo(1000.0);
      assertThat(member.getAmountDue()).isEqualTo(1000.0);
      assertThat(member.hasDuesToSettle()).isTrue();
    }

    @Test
    void whenBorrowedAmountIsLessThanSpent() {
      Member member = new Member("ANDY");

      member.spend(2000.0);
      member.borrow(1000.0);

      assertThat(member.getSpent()).isEqualTo(1000.0);
      assertThat(member.getAmountDue()).isEqualTo(1000.0);
      assertThat(member.getBorrowed()).isEqualTo(0.0);
      assertThat(member.hasDuesToSettle()).isTrue();
    }

    @Test
    void whenBorrowedAmoutIsEqualToSpent() {
       Member member = new Member("ANDY");

      member.spend(1000.0);
      member.borrow(1000.0);

      assertThat(member.getSpent()).isEqualTo(0.0);
      assertThat(member.getBorrowed()).isEqualTo(0.0);
      assertThat(member.getAmountDue()).isEqualTo(0.0);
      assertThat(member.hasDuesToSettle()).isFalse(); 
    }

  }

}
