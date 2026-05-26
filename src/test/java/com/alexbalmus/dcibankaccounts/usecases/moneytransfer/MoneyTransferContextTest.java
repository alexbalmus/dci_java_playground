package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.javadci.examples.bankaccounts.entities.Account;
import com.alexbalmus.javadci.examples.bankaccounts.usecases.moneytransfer.MoneyTransferContext;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class MoneyTransferContextTest
{
    @Test
    public void testExecuteSourceToDestinationTransfer()
    {
        var source = new Account(1L, 100.0);
        var destination = new SpecialAccount(2L, 200.0);

        var moneyTransferService = new MoneyTransferContext();

        moneyTransferService.executeSourceToDestinationTransfer(50.0, source, destination);

        assertEquals(source.getBalance(), 50.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testExecuteInsufficientFunds()
    {
        var source = new Account(1L, 20.0);
        var destination = new Account(2L, 200.0);

        var moneyTransferService = new MoneyTransferContext();

        try
        {
            moneyTransferService.executeSourceToDestinationTransfer(50.0, source, destination);
            fail("Exception should have been thrown.");
        }
        catch (RuntimeException e)
        {
            assertEquals(e.getMessage(), Account.INSUFFICIENT_FUNDS);
        }
    }

    static class SpecialAccount extends Account
    {
        public SpecialAccount(Long id, Double balance)
        {
            super(id, balance);
        }
    }
}
