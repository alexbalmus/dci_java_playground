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
        source.setId(1L);
        when(entityManager.find(Account.class, 1L)).thenReturn(source);

        Account intermediary = new Account(0.0);
        intermediary.setId(2L);
        when(entityManager.find(Account.class, 2L)).thenReturn(intermediary);

        Account destination = new Account(200.0);
        destination.setId(3L);
        when(entityManager.find(Account.class, 3L)).thenReturn(destination);

        ABCMoneyTransferContext abcMoneyTransferContext = new ABCMoneyTransferContext(entityManager,50.0,
            source.getId(), intermediary.getId(), destination.getId());

        abcMoneyTransferContext.execute();

        assertEquals(source.getBalance(), 50.0);
        assertEquals(intermediary.getBalance(), 0.0);
        assertEquals(destination.getBalance(), 250.0);
    }

    @Test
    public void testIdentity()
    {
        Account account1 = new Account(20.0);
        account1.setId(1L);

        ABCMoneyTransferContext abcMoneyTransferContext = new ABCMoneyTransferContext();
        Account_SourceAndDestinationRole bidirectionalAccount =
            abcMoneyTransferContext.assignSourceAndDestinationRoleTo(account1);

        MoneyTransferContext moneyTransferContext = new MoneyTransferContext();
        Account_SourceRole sourceRef = moneyTransferContext.assignSourceRoleTo(account1);
        Account_DestinationRole destRef = moneyTransferContext.assignDestinationRoleTo(account1);

        assertEquals(bidirectionalAccount.self(), sourceRef.self());
        assertEquals(bidirectionalAccount.self(), destRef.self());
    }
}
