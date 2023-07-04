package com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

public class ABCMoneyTransferContext
{
    private final MoneyTransferContext moneyTransferContext;
    private final SourceAndDestinationAccountRole intermediaryAccount;
    public ABCMoneyTransferContext(
            final Double amount,
            final AccountsRepository accountsRepository,
            final Long sourceId,
            final Long intermediaryId,
            final Long destinationId)
    {
        moneyTransferContext = new MoneyTransferContext(amount, accountsRepository, sourceId, destinationId);
        intermediaryAccount = assignSourceAndDestinationAccountRoleTo(accountsRepository.findById(intermediaryId));
    }

    public void execute()
    {
        moneyTransferContext.getSourceAccount().transfer(moneyTransferContext.getAmount(), intermediaryAccount);
        intermediaryAccount.transfer(moneyTransferContext.getAmount(), moneyTransferContext.getDestinationAccount());
    }

    SourceAndDestinationAccountRole assignSourceAndDestinationAccountRoleTo(final Account account)
    {
        return () -> account;
    }
}
