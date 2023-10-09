package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;

@java.lang.SuppressWarnings("java:S114")
interface Account_SourceRole extends Role<Account>
{
    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    default void transfer(final Double amount, final Account_DestinationRole destination)
    {
        if (self().getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        self().decreaseBalanceBy(amount);
        destination.receive(amount);
    }
}
