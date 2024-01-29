package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;

@java.lang.SuppressWarnings("java:S114")
interface Account_DestinationRole<A extends Account> extends Role<A>
{
    default void receive(final Double amount)
    {
        self().increaseBalanceBy(amount);
    }
}
