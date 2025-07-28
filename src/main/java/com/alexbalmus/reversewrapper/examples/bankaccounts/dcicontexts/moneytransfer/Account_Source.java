package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.reversewrapper.common.Role;
import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;

/**
 * Source account role
 */
interface Account_Source extends Role<Account>
{
    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    default void transfer(final Double amount, final Account destination)
    {
        if (!(destination.role() instanceof Account_Destination))
        {
            throw new IllegalArgumentException("Not a destination account.");
        }

        if (thiz().getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }

        thiz().decreaseBalanceBy(amount);

        destination.<Account_Destination>role().receive(amount);
    }
}
