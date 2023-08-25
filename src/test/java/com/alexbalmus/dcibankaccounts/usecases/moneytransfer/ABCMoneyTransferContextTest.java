package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ABCMoneyTransferContextTest {

    @Test
    public void testExecute()
    {
        AccountsRepository accountsRepository = new AccountsRepository();

        Account source = accountsRepository.create(100.0);
        Account intermediary = accountsRepository.create(0.0);
        Account destination = accountsRepository.create(200.0);

        ABCMoneyTransferContext moneyTransferContext = new ABCMoneyTransferContext(50.0, accountsRepository,
                source.getId(), intermediary.getId(), destination.getId());

        moneyTransferContext.execute();

        assertEquals(source.getBalance(), 50);
        assertEquals(intermediary.getBalance(), 0);
        assertEquals(destination.getBalance(), 250);
    }
}