package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

public class ABCMoneyTransferContext
{
    private final MoneyTransferContext moneyTransferContext;
    private final Account_SourceAndDestinationRole intermediaryAccount;
    public ABCMoneyTransferContext(
            final Double amount,
            final AccountsRepository accountsRepository,
            final Long sourceId,
            final Long intermediaryId,
            final Long destinationId)
    {
        moneyTransferContext = new MoneyTransferContext(amount, accountsRepository, sourceId, destinationId);
        intermediaryAccount = assignSourceAndDestinationRoleTo(accountsRepository.findById(intermediaryId));
    }

    public void execute()
    {
        moneyTransferContext.getSourceAccount().transfer(moneyTransferContext.getAmount(), intermediaryAccount);
        intermediaryAccount.transfer(moneyTransferContext.getAmount(), moneyTransferContext.getDestinationAccount());
    }

    Account_SourceAndDestinationRole assignSourceAndDestinationRoleTo(final Account account)
    {
        return () -> account;
    }
}
