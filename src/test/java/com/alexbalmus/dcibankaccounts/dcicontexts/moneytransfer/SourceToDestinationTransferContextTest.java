package com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class SourceToDestinationTransferContextTest
{
    @Test
    public void testExecuteSourceToDestinationTransfer()
    {
        var source = new Account(100.0);
        source.setId(1L);

        var destination = new SpecialAccount(200.0);
        destination.setId(2L);

        var target = new SourceToDestinationTransferContext(source, destination, 50.0);

        target.perform();

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

        var target = new SourceToDestinationTransferContext(source, destination, 50.0);

        try
        {
            target.perform();
            fail("Exception should have been thrown.");
        }
        catch (RuntimeException e)
        {
            assertEquals(e.getMessage(), SourceToDestinationTransferContext.INSUFFICIENT_FUNDS);
        }
    }

    static class SpecialAccount extends Account
    {
        public SpecialAccount(Double balance)
        {
            super(balance);
        }
    }
}
