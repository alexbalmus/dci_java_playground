package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

public class MoneyTransferContext
{
    private final SourceAccountRole sourceAccount;
    private final DestinationAccountRole destinationAccount;
    private final Double amount;

    protected SourceAccountRole getSourceAccount()
    {
        return sourceAccount;
    }

    protected DestinationAccountRole getDestinationAccount()
    {
        return destinationAccount;
    }

    protected Double getAmount()
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
        this.sourceAccount = assignSourceAccountRoleTo(accountsRepository.findById(sourceId));
        this.destinationAccount = assignDestinationAccountRoleTo(accountsRepository.findById(destinationId));
    }

    public void execute()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

    SourceAccountRole assignSourceAccountRoleTo(final Account source)
    {
        return () -> source;
    }

    DestinationAccountRole assignDestinationAccountRoleTo(final Account destination)
    {
        return () -> destination;
    }
}
