package com.xpensify.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

class Dues {
    private final Map<String, MemberDues> dues;

    Dues(Collection<Member> members) {
        dues = dues(members);
    }

    MemberDues of(String memberName) {
        return dues.get(memberName);
    }

    private Map<String, MemberDues> dues(Collection<Member> members) {
        List<MemberAmount> lenders = getLenders(members);
        List<MemberAmount> borrowers = getBorrowers(members);

        Map<String, MemberDues> dues = new HashMap<>();
        borrowers.forEach((borrower) -> {
            ArrayList<AmountDue> borrowerDues = new ArrayList<>();
            lenders.stream()
                    .filter(lender -> !lender.getName().equals(borrower.getName()))
                    .forEach((lender) -> {
                        AmountDue amountDue = amountDue(lender, borrower);
                        borrowerDues.add(amountDue);
                        lender.setAmount(lender.getAmount() - amountDue.getAmount());
                        borrower.setAmount(borrower.getAmount() - amountDue.getAmount());
                    });
            dues.put(borrower.getName(), new MemberDues(borrower.getName(), borrowerDues));
        });

        return dues;
    }

    private List<MemberAmount> getBorrowers(Collection<Member> members) {
        return members.stream().sorted(Comparator.comparing(Member::getName))
                .collect(Collectors.mapping(
                        member -> new MemberAmount(member.getName(), member.getBorrowed()),
                        Collectors.toList()));
    }

    private AmountDue amountDue(MemberAmount lender, MemberAmount borrower) {
        if (borrower.getAmount() == 0) {
            return new AmountDue(lender.getName(), 0);
        } else if (borrower.getAmount() < lender.getAmount()) {
            return new AmountDue(lender.getName(), borrower.getAmount());
        } else {
            return new AmountDue(lender.getName(), lender.getAmount());
        }
    }

    private List<MemberAmount> getLenders(Collection<Member> members) {
        return members.stream().sorted(Comparator.comparing(Member::getName))
                .collect(Collectors.mapping(
                        member -> new MemberAmount(member.getName(), member.getSpent()),
                        Collectors.toList()));
    }
}

@Data
@AllArgsConstructor
class MemberAmount {
    String name;
    double amount;
}
