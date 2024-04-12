package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;

@java.lang.SuppressWarnings("java:S114")
interface Account_SourceRole<A extends Account> extends Role<A>
{
    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    default void transfer(final Double amount, final Account_DestinationRole<? super A> destination)
    {
        if (self().getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        self().decreaseBalanceBy(amount);
        destination.receive(amount);
    }
}
