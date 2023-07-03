package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.dom.usecases.Role;

interface DestinationAccountRole extends Role<Account>
{
    default void receive(Double amount)
    {
        try
        {
            self().increaseBalanceBy(amount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
