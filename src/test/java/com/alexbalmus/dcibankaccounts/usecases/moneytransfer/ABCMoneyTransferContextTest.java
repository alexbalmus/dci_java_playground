package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.javadci.examples.bankaccounts.entities.Account;
import com.alexbalmus.javadci.examples.bankaccounts.usecases.moneytransfer.MoneyTransferContext;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class ABCMoneyTransferContextTest
{
    @Test
    public void testExecuteSourceToDestinationTransfer()
    {
        var source = new Account(1L, 100.0);
        var intermediary = new Account(2L, 0.0);
        var destination = new Account(3L, 200.0);

        var moneyTransferService = new MoneyTransferContext();

        moneyTransferService.executeSourceToIntermediaryToDestinationTransfer(50.0, source, destination, intermediary);

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }
}
