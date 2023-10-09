package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.testng.Assert.assertEquals;

import org.mockito.Mock;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.alexbalmus.dcibankaccounts.entities.Account;

@Test
public class ABCMoneyTransferContextTest
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
        Account intermediary = new Account(0.0);
        Account destination = new Account(200.0);

        ABCMoneyTransferContext abcMoneyTransferContext = new ABCMoneyTransferContext(entityManager,50.0,
            source, intermediary, destination);

        abcMoneyTransferContext.execute();

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }
}