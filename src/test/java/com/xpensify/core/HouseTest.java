package com.xpensify.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;

import lombok.SneakyThrows;

class HouseTest {

  @Nested
  @DisplayName("when initialized")
  class HouseInitializationTest {

    @Test
    void setCapacityToGivenValue() {
      int expectedCapacity = 3;
      final House house = new House(expectedCapacity);

      int actualCapacity = house.getCapacity();

      assertThat(actualCapacity).isEqualTo(expectedCapacity);
    }

    @Test
    void hasNoMembers() {
      House house = new House(3);

      int numberOfMembers = house.getNumberOfMembers();

      assertThat(numberOfMembers).isEqualTo(0);
    }

  }

  @Nested
  @DisplayName("when new member moves in")
  class HouseMoveInTest {

    @SneakyThrows
    @Test
    void incrementNumberOfMembers() {
      House house = new House(3);
      int numberOfMembers = house.getNumberOfMembers();

      house.moveIn("ANDY");

      int numberOfMembersAfterMoveIn = house.getNumberOfMembers();
      assertThat(numberOfMembersAfterMoveIn).isEqualTo(numberOfMembers + 1);
    }

    @SneakyThrows
    @Test
    void throwExceptionWhenHouseIsAtFullCapacity() {
      House house = new House(2);
      house.moveIn("ANDY");
      house.moveIn("WOODY");

      assertThatThrownBy(() -> {
        house.moveIn("BO");
      }).isInstanceOf(HousefulException.class).hasMessage("house is at full capacity");
    }

    @SneakyThrows
    @Test
    void newMemberHasNoDuesAgainstOtherMembersInTheHouse() {
      House house = new House(3);
      house.moveIn("ANDY");
      String newMemberName = "WOODY";

      house.moveIn(newMemberName);

      MemberDues memberDues = house.dues(newMemberName);
      assertThat(memberDues.getMemberName()).isEqualTo(newMemberName);
      assertThat(memberDues.getDues()).containsExactly(new AmountDue("ANDY", 0));
    }

  }

  @Nested
  @DisplayName("when member spends")
  class HouseMemberSpendTest {

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "ANDY, 'BO, WOODY', WOODY",
        "WOODY, 'ANDY, BO', WOODY"
    })
    void throwExceptionIfMemberNotFound(
        String spentByMember,
        @ConvertWith(StringArrayConverter.class) String[] spentForMembers,
        String unknownMemberName) {
      House house = new House(2);
      house.moveIn("ANDY");
      house.moveIn("BO");

      assertThatThrownBy(() -> {
        house.spend(100, spentByMember, spentForMembers);
      }).isInstanceOf(HouseMemberNotFoundException.class).hasMessage("member not found in the house")
          .hasFieldOrPropertyWithValue("memberName", unknownMemberName);
    }

    @SneakyThrows
    @Test
    void throwExceptionWhenMinimumMembersForExpenseTrackingAreNotFound() {
      House house = new House(2);
      house.moveIn("ANDY");

      assertThatThrownBy(() -> {
        house.spend(100, "ANDY");
      }).isInstanceOf(HouseNeedsMoreMembersForExpenseTrackingException.class)
          .hasMessage("house need more members for expense tracking");
    }

    @SneakyThrows
    @Test
    void dueIsCreatedAgainstSpentForMembers() {
      House house = new House(3);
      house.moveIn("ANDY");
      house.moveIn("WOODY");
      house.moveIn("BO");

      house.spend(3000, "ANDY", "WOODY");

      MemberDues duesForWoody = house.dues("WOODY");
      assertThat(duesForWoody.getMemberName()).isEqualTo("WOODY");
      assertThat(duesForWoody.getDues()).containsExactly(new AmountDue("ANDY", 1500), new AmountDue("BO", 0));
    }

  }

  @Nested
  class HouseDuesTest {

    @SneakyThrows
    @Test
    void throwExceptionIfMemberNotFound() {
      House house = new House(3);
      house.moveIn("ANDY");

      assertThatThrownBy(() -> {
        house.dues("UNKNOWN");
      }).isInstanceOf(HouseMemberNotFoundException.class)
          .hasMessage("member not found in the house");
    }

  }

  @Nested
  @DisplayName("when member moves out")
  class HouseMemberMoveOutTest {

    @SneakyThrows
    @Test
    void decrementNumberOfMembers() {
      House house = new House(2);
      house.moveIn("ANDY");
      house.moveIn("WOODY");

      house.moveOut("WOODY");

      assertThat(house.getNumberOfMembers()).isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void throwExceptionWhenMemberNotFound() {
      House house = new House(2);
      house.moveIn("ANDY");

      assertThatThrownBy(() -> {
        house.moveOut("WOODY");
      }).isInstanceOf(HouseMemberNotFoundException.class)
          .hasMessage("member not found in the house")
          .hasFieldOrPropertyWithValue("memberName", "WOODY");
    }

    @SneakyThrows
    @Test
    void throwExceptionWhenMemberHasUnsettledDues()
        {
      House house = new House(3);
      house.moveIn("ANDY");
      house.moveIn("WOODY");
      house.spend(3000, "ANDY", "WOODY");

      assertThatThrownBy(() -> {
        house.moveOut("ANDY");
      })
          .isInstanceOf(HouseMemberHasUnsettledDuesException.class)
          .hasMessage("member has unsettled dues")
          .hasFieldOrPropertyWithValue("amountDue", 1500.0);
    }

  }

  @Nested
  @DisplayName("when member clear dues")
  class HouseMemberClearDuesTest {

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "WOODY, BO, BO",
        "BO, ANDY, BO"
    })
    void throwExceptionWhenMemberNotFound(String memberName, String lentByMemberName, String unknownMemberName)
        {
      House house = new House(3);
      house.moveIn("ANDY");
      house.moveIn("WOODY");
      house.spend(3000, "ANDY", "WOODY");

      assertThatThrownBy(() -> {
        house.clearDue(memberName, lentByMemberName, 1000);
      }).isInstanceOf(HouseMemberNotFoundException.class)
          .hasMessage("member not found in the house")
          .hasFieldOrPropertyWithValue("memberName", unknownMemberName);
    }

    @SneakyThrows
    @Test
    void throwExceptionWhenSettlementAmountIsMoreThanAmountDue()
        {
      House house = new House(3);
      house.moveIn("ANDY");
      house.moveIn("WOODY");
      house.spend(3000, "ANDY", "WOODY");

      assertThatThrownBy(() -> {
        house.clearDue("WOODY", "ANDY", 2000);
      }).isInstanceOf(IncorrectSettlementAmount.class)
          .hasMessage("incorrect settlement amount")
          .hasFieldOrPropertyWithValue("amountDue", 1500.0);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
        "1500, 0",
        "1000, 500"
    })
    void settleDueWithAnotherMember(double amountToSettle, double expectedDue) {
      House house = new House(3);
      house.moveIn("ANDY");
      house.moveIn("WOODY");
      house.spend(3000, "ANDY", "WOODY");

      AmountDue amountDue = house.clearDue("WOODY", "ANDY", amountToSettle);

      //Return amaout due
      assertThat(amountDue.getLentByMember()).isEqualTo("ANDY");
      assertThat(amountDue.getAmount()).isEqualTo(expectedDue);
      
      //Settle dues
      MemberDues woodyDues = house.dues("WOODY");
      assertThat(woodyDues.amountDue("ANDY")).isEqualTo(new AmountDue("ANDY", expectedDue));
    }

  }
}

class StringArrayConverter extends SimpleArgumentConverter {

  @Override
  protected Object convert(Object source, Class<?> targetType) {
    if (source instanceof String && String[].class.isAssignableFrom(targetType)) {
      return ((String) source).split("\\s*,\\s*");
    } else {
      throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
          + targetType + " not supported.");
    }
  }

}
