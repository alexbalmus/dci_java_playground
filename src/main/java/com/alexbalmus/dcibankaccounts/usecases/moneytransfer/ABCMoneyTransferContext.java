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
        final Double amount,
        final Long sourceId,
        final Long intermediaryId,
        final Long destinationId)
    {
        this.entityManager = entityManager;
        moneyTransferContext = new MoneyTransferContext(entityManager, amount, sourceId, destinationId);
        intermediaryAccount = assignSourceAndDestinationRoleTo(entityManager.find(Account.class, intermediaryId));
    }

    public void execute()
    {
        var transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();

            moneyTransferContext.getSourceAccount()
                .transfer(moneyTransferContext.getAmount(), intermediaryAccount);

            intermediaryAccount.transfer(moneyTransferContext.getAmount(),
                moneyTransferContext.getDestinationAccount());

            transaction.commit();
        }
        catch (Exception e)
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
            throw e;
        }
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
