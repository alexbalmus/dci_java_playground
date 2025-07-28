package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@Test
public class MoneyTransferDciContextTest
{
    @Test
    public void testExecuteSourceToDestinationTransfer()
    {
        var source = new Account(100.0);
        source.setId(1L);

        var destination = new SpecialAccount(200.0);
        destination.setId(2L);

        var moneyTransferUseCase = new MoneyTransferDciContext();

        moneyTransferUseCase.transferFromSourceToDestination(source, destination, 50.0);

        assertEquals(source.getBalance(), 50.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testExecuteInsufficientFunds()
    {
        var source = new Account(20.0);
        source.setId(1L);

        var destination = new Account(200.0);
        destination.setId(2L);

        var moneyTransferUseCase = new MoneyTransferDciContext();

        try
        {
            moneyTransferUseCase.transferFromSourceToDestination(source, destination, 50.0);
            fail("Exception should have been thrown.");
        }
        catch (RuntimeException e)
        {
            assertEquals(e.getMessage(), Account_Source.INSUFFICIENT_FUNDS);
        }
    }

    @Test
    public void testIdentity()
    {
        var sourceAccount = new Account(20.0);
        sourceAccount.setId(1L);

        sourceAccount.acceptRole(MoneyTransferDciContext.createSourceRole(sourceAccount));

        assertEquals(sourceAccount.role().thiz(), sourceAccount);
    }

    static class SpecialAccount extends Account
    {
        public SpecialAccount(Double balance)
        {
            super(balance);
        }
    }
}
