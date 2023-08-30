package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;

interface Account_SourceRole extends Role<Account>
{
    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    default void transfer(Double amount, Account_DestinationRole destination)
    {
        // Begin transaction.
        if (self().getBalance() < amount)
        {
            throw new RuntimeException(INSUFFICIENT_FUNDS); // Rollback.
        }
        self().decreaseBalanceBy(amount);
        destination.receive(amount);
        // End transaction.
    }
}
