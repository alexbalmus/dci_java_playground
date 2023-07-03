package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

public class MoneyTransfer
{
    final private SourceAccountRole sourceAccount;
    final private DestinationAccountRole destinationAccount;
    final private Double amount;

    public MoneyTransfer(
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
