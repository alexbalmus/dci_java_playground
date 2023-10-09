package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import jakarta.persistence.EntityManager;

public class ABCMoneyTransferContext
{
    private final MoneyTransferContext moneyTransferContext;
    private final Account_SourceAndDestinationRole intermediaryAccount;
    private final EntityManager entityManager;

    public ABCMoneyTransferContext(
        final EntityManager entityManager,
        final Double amount, Account source, Account intermediary, Account destination)
    {
        this.entityManager = entityManager;
        moneyTransferContext = new MoneyTransferContext(entityManager, amount, source, destination);
        intermediaryAccount = assignSourceAndDestinationRoleTo(intermediary);
    }

    public void execute()
    {
        entityManager.getTransaction().begin();

        moneyTransferContext.getSourceAccount().transfer(moneyTransferContext.getAmount(), intermediaryAccount);
        intermediaryAccount.transfer(moneyTransferContext.getAmount(), moneyTransferContext.getDestinationAccount());

        entityManager.getTransaction().commit();
    }

    Account_SourceAndDestinationRole assignSourceAndDestinationRoleTo(final Account account)
    {
        return () -> account;
    }

//    Old-school alternative to the above:
//    Account_SourceAndDestinationRole assignSourceAndDestinationRoleTo(final Account account)
//    {
//        return new Account_SourceAndDestinationRole()
//        {
//            @Override
//            public Account self()
//            {
//                return account;
//            }
//        };
//    }
}
