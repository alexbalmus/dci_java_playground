package com.alexbalmus.dcibankaccounts.dom.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account
{
    private Long id;
    private Double balance;
    public void increaseBalanceBy(Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(Double amount)
    {
        balance -= amount;
    }
}
