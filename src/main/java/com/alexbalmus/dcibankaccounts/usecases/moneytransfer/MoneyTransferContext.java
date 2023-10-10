package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import jakarta.persistence.EntityManager;

public class MoneyTransferContext
{
    private final Account_SourceRole sourceAccount;
    private final Account_DestinationRole destinationAccount;
    private final Double amount;
    private final EntityManager entityManager;

    Account_SourceRole getSourceAccount()
    {
        return sourceAccount;
    }

    Account_DestinationRole getDestinationAccount()
    {
        return destinationAccount;
    }

    Double getAmount()
    {
        return amount;
    }

    public MoneyTransferContext(
        final EntityManager entityManager,
        final Double amount,
        final Account source,
        final Account destination)
    {
        this.entityManager = entityManager;
        this.amount = amount;
        this.sourceAccount = assignSourceRoleTo(source);
        this.destinationAccount = assignDestinationRoleTo(destination);
    }

    public void execute()
    {
        var transaction = entityManager.getTransaction();

        try
        {
            transaction.begin();
            sourceAccount.transfer(amount, destinationAccount);
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

    Account_SourceRole assignSourceRoleTo(final Account source)
    {
        return () -> source;
    }

//    Old-school alternative to the above:
//    Account_SourceRole assignSourceRoleTo(final Account source)
//    {
//        return new Account_SourceRole()
//        {
//            @Override
//            public Account self()
//            {
//                return source;
//            }
//        };
//    }

    Account_DestinationRole assignDestinationRoleTo(final Account destination)
    {
        return () -> destination;
    }
}
