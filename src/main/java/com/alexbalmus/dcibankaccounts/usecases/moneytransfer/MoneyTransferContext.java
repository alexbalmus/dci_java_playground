package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;

public class MoneyTransferContext<A extends Account>
{
    private final Account_SourceRole<A> sourceAccount;
    private final Account_DestinationRole<A> destinationAccount;
    private final Double amount;

    Account_SourceRole<A> getSourceAccount()
    {
        return sourceAccount;
    }

    Account_DestinationRole<A> getDestinationAccount()
    {
        return destinationAccount;
    }

    Double getAmount()
    {
        return amount;
    }

    public MoneyTransferContext(
        final Double amount,
        final A sourceAccount,
        final A destinationAccount)
    {
        this.amount = amount;
        this.sourceAccount = assignSourceRoleTo(sourceAccount);
        this.destinationAccount = assignDestinationRoleTo(destinationAccount);
    }

    public void execute()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

    Account_SourceRole<A> assignSourceRoleTo(final A source)
    {
        return () -> source;
    }

    Account_DestinationRole<A> assignDestinationRoleTo(final A destination)
    {
        return () -> destination;
    }
}
