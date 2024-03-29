package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class ABCMoneyTransferContextTest
{
    @Test
    public void testExecute()
    {
        var source = new Account(100.0);
        source.setId(1L);

        var intermediary = new Account(0.0);
        intermediary.setId(2L);

        var destination = new Account(200.0);
        destination.setId(3L);

        var abcMoneyTransferContext = new ABCMoneyTransferContext<>(50.0, source, intermediary, destination);

        abcMoneyTransferContext.execute();

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testIdentity()
    {
        var account1 = new Account(20.0);
        account1.setId(1L);

        var abcMoneyTransferContext = new ABCMoneyTransferContext<>(null, null, null, null);
        var bidirectionalAccount = abcMoneyTransferContext.assignSourceAndDestinationRoleTo(account1);

        var moneyTransferContext = new MoneyTransferContext<>(null, null, null);
        var sourceRef = moneyTransferContext.assignSourceRoleTo(account1);
        var destRef = moneyTransferContext.assignDestinationRoleTo(account1);

        assertEquals(bidirectionalAccount.self(), sourceRef.self());
        assertEquals(bidirectionalAccount.self(), destRef.self());
    }
}
