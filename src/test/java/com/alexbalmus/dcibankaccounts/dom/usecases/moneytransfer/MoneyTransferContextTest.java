package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class MoneyTransferContextTest
{
    @Test
    public void testExecute()
    {
        AccountsRepository accountsRepository = new AccountsRepository();

        Account source = accountsRepository.create(100.0);
        Account destination = accountsRepository.create(200.0);

        MoneyTransferContext moneyTransferContext = new MoneyTransferContext(50.0, accountsRepository,
                source.getId(), destination.getId());

        moneyTransferContext.execute();

        assertEquals(source.getBalance(), 50);
        assertEquals(destination.getBalance(), 250);
    }

    @Test
    public void testExecuteInsufficientFunds()
    {
        AccountsRepository accountsRepository = new AccountsRepository();

        Account source = accountsRepository.create(20.0);
        Account destination = accountsRepository.create(200.0);

        MoneyTransferContext moneyTransferContext = new MoneyTransferContext(50.0, accountsRepository,
                source.getId(), destination.getId());

        try {
            moneyTransferContext.execute();
            fail("Exception should have been thrown.");
        }
        catch (RuntimeException e)
        {
            assertEquals(e.getMessage(), SourceAccountRole.INSUFFICIENT_FUNDS);
        }
    }
}