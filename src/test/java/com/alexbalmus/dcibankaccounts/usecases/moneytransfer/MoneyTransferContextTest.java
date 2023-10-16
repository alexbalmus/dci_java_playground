package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.mockito.Mock;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.alexbalmus.dcibankaccounts.entities.Account;

@Test
public class MoneyTransferContextTest
{
    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction entityTransaction;

    @BeforeMethod
    void setup()
    {
        initMocks(this);
        doNothing().when(entityTransaction).begin();
        doNothing().when(entityTransaction).commit();
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
    }

    @Test
    public void testExecute()
    {
        Account source = new Account(100.0);
        source.setId(1L);
        when(entityManager.find(Account.class, 1L)).thenReturn(source);

        Account destination = new Account(200.0);
        destination.setId(2L);
        when(entityManager.find(Account.class, 2L)).thenReturn(destination);


        MoneyTransferContext moneyTransferContext =
            new MoneyTransferContext(entityManager, 50.0, source.getId(), destination.getId());

        moneyTransferContext.execute();

        assertEquals(source.getBalance(), 50.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testExecuteInsufficientFunds()
    {
        Account source = new Account(20.0);
        source.setId(1L);
        when(entityManager.find(Account.class, 1L)).thenReturn(source);

        Account destination = new Account(200.0);
        destination.setId(2L);
        when(entityManager.find(Account.class, 2L)).thenReturn(destination);

        MoneyTransferContext moneyTransferContext =
            new MoneyTransferContext(entityManager, 50.0, source.getId(), destination.getId());

        try {
            moneyTransferContext.execute();
            fail("Exception should have been thrown.");
        }
        catch (RuntimeException e)
        {
            assertEquals(e.getMessage(), Account_SourceRole.INSUFFICIENT_FUNDS);
        }
    }
}