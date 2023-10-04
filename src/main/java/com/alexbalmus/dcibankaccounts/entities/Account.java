package com.alexbalmus.dcibankaccounts.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Account
{
    private Long id;
    private Double balance;

    protected Account()
    {
    }

    public void increaseBalanceBy(final Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(final Double amount)
    {
        balance -= amount;
    }
}
