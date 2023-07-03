package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.dom.usecases.Role;

interface SourceAccountRole extends Role<Account>
{

    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    default void transfer(Double amount, DestinationAccountRole destination)
    {
        // Begin transaction.
        if (self().getBalance() < amount)
        {
            throw new RuntimeException(INSUFFICIENT_FUNDS);
        }
        self().decreaseBalanceBy(amount);
        destination.receive(amount);
        // End transaction.
    }
}
