package com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class SourceToIntermediaryToDestinationTransferContextTest
{
    @Test
    public void testExecuteSourceToDestinationTransfer()
    {
        var source = new Account(100.0);
        source.setId(1L);

        var intermediary = new Account(0.0);
        intermediary.setId(2L);

        var destination = new Account(200.0);
        destination.setId(3L);

        var target = new SourceToIntermediaryToDestinationTransferContext(source, destination, intermediary, 50.0);

        target.perform();

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }
}
