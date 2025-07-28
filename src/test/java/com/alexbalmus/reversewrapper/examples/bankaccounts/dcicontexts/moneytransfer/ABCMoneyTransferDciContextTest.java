package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class ABCMoneyTransferDciContextTest
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

        var abcMoneyTransferUseCase = new MoneyTransferDciContext();

        abcMoneyTransferUseCase.transferFromSourceToDestinationViaTemporary(source, destination, intermediary, 50.0);

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testIdentity()
    {
        var previousDestinationAccount = new Account(200.0);
        previousDestinationAccount.acceptRole(MoneyTransferDciContext.createDestinationRole(previousDestinationAccount));

        var sourceAccount = previousDestinationAccount;
        sourceAccount.acceptRole(MoneyTransferDciContext.createSourceRole(sourceAccount));

        assertEquals(sourceAccount, previousDestinationAccount);
        assertEquals(previousDestinationAccount.role().thiz(), previousDestinationAccount);
        assertEquals(sourceAccount.role().thiz(), previousDestinationAccount.role().thiz());
    }
}
