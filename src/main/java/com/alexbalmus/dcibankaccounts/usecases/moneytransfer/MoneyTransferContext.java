package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

public class MoneyTransferContext
{
    private final Account_SourceRole sourceAccount;
    private final Account_DestinationRole destinationAccount;
    private final Double amount;

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
            final Double amount,
            final AccountsRepository accountsRepository,
            final Long sourceId,
            final Long destinationId)
    {
        this.amount = amount;
        this.sourceAccount = assignSourceRoleTo(accountsRepository.findById(sourceId));
        this.destinationAccount = assignDestinationRoleTo(accountsRepository.findById(destinationId));
    }

    public void execute()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

    Account_SourceRole assignSourceRoleTo(final Account source)
    {
        return () -> source;
    }

    Account_DestinationRole assignDestinationRoleTo(final Account destination)
    {
        return () -> destination;
    }
}
