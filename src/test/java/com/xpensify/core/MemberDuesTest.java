package com.xpensify.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MemberDuesTest {

    @Test
    void setMemberNameToGivenValue() {
        String expectedMemberName = "ANDY";
        MemberDues dues = new MemberDues(expectedMemberName, Collections.emptyList());

        String actualMemberName = dues.getMemberName();

        assertThat(actualMemberName).isEqualTo(expectedMemberName);
    }

    @Test
    void setDuesSortedByAmountInDescendingOrder() {
        List<AmountDue> dues = new ArrayList<AmountDue>() {
            {
                add(new AmountDue("ANDY", 0));
                add(new AmountDue("WOODY", 1150));
            }
        };
        MemberDues memberDues = new MemberDues("BO", dues);

        List<AmountDue> actualDues = memberDues.getDues();

        assertThat(actualDues).containsExactly(
                new AmountDue("WOODY", 1150),
                new AmountDue("ANDY", 0));
    }

    @Test
    void setDuesSortedByNamesInAscendingOrderWhenAmountIsEqual() {
        List<AmountDue> dues = new ArrayList<AmountDue>() {
            {
                add(new AmountDue("WOODY", 1150));
                add(new AmountDue("ANDY", 1150));
            }
        };
        MemberDues memberDues = new MemberDues("BO", dues);

        List<AmountDue> actualDues = memberDues.getDues();

        assertThat(actualDues).containsExactly(
                new AmountDue("ANDY", 1150),
                new AmountDue("WOODY", 1150));
    }

    @Test
    void returnAmountDueFromGivenMember() {
        List<AmountDue> dues = new ArrayList<AmountDue>() {
            {
                add(new AmountDue("WOODY", 500));
                add(new AmountDue("ANDY", 1150));
            }
        };
        MemberDues memberDues = new MemberDues("BO", dues);

        AmountDue amountDue = memberDues.amountDue("ANDY");

        assertThat(amountDue).isEqualTo(new AmountDue("ANDY", 1150));
    }

    @Test
    void throwExceptionIfMemberNotFound() {
        List<AmountDue> dues = new ArrayList<AmountDue>() {
            {
                add(new AmountDue("WOODY", 500));
            }
        };
        MemberDues memberDues = new MemberDues("BO", dues);

        assertThatThrownBy(() -> {
            memberDues.amountDue("ANDY");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("member not found");
    }

}
