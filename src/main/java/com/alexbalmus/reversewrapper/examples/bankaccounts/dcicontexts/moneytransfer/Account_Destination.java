package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.reversewrapper.common.Role;
import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;

/**
 * Destination account role
 */
interface Account_Destination extends Role<Account>
{
    default void receive(final Double amount)
    {
        thiz().increaseBalanceBy(amount);
    }
}
