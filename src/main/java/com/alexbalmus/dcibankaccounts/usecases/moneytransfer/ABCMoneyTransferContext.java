package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;

public class ABCMoneyTransferContext<A extends Account>
{
    private final MoneyTransferContext<A> moneyTransferContext;
    private final Account_SourceAndDestinationRole<A> intermediaryAccount;

    public ABCMoneyTransferContext(
        final Double amount,
        final A sourceAccount,
        final A intermediaryAccount,
        final A destinationAccount)
    {
        this.moneyTransferContext = new MoneyTransferContext<>(amount, sourceAccount, destinationAccount);
        this.intermediaryAccount = assignSourceAndDestinationRoleTo(intermediaryAccount);
    }

    public void execute()
    {
        moneyTransferContext.getSourceAccount()
            .transfer(moneyTransferContext.getAmount(), intermediaryAccount);
        intermediaryAccount.transfer(moneyTransferContext.getAmount(),
            moneyTransferContext.getDestinationAccount());
    }

    Account_SourceAndDestinationRole<A> assignSourceAndDestinationRoleTo(final A account)
    {
        return () -> account;
    }
}
