package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;

interface Account_DestinationRole extends Role<Account>
{
    default void receive(final Double amount)
    {
        self().increaseBalanceBy(amount);
    }
}
