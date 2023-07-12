package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.dom.usecases.Role;

interface Account_DestinationRole extends Role<Account>
{
    default void receive(Double amount)
    {
        self().increaseBalanceBy(amount);
    }
}
