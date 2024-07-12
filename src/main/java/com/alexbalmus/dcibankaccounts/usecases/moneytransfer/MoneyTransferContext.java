package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.Role;
import org.apache.commons.lang3.Validate;

public class MoneyTransferContext<A extends Account>
{
    private final Double amount;

    private final Account_SourceRole<A> sourceAccount;
    private final Account_DestinationRole<A> destinationAccount;
    private final Account_SourceAndDestinationRole<A> intermediaryAccount;

    public MoneyTransferContext(
        final Double amount,
        final A sourceAccount,
        final A destinationAccount)
    {
        this(amount, sourceAccount, destinationAccount, null);
    }

    public MoneyTransferContext(
        final Double amount,
        final A sourceAccount,
        final A destinationAccount,
        final A intermediaryAccount)
    {
        this.amount = amount;
        this.sourceAccount = assignSourceRoleTo(sourceAccount);
        this.destinationAccount = assignDestinationRoleTo(destinationAccount);
        this.intermediaryAccount = intermediaryAccount != null
            ? assignSourceAndDestinationRoleTo(intermediaryAccount)
            : null;
    }

    public void executeSourceToDestinationTransfer()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

    public void executeSourceToIntermediaryToDestinationTransfer()
    {
        Validate.notNull(intermediaryAccount, "intermediaryAccount must not be null.");
        sourceAccount.transfer(amount, intermediaryAccount);
        intermediaryAccount.transfer(amount, destinationAccount);
    }

    // Role assignments:

    Account_SourceRole<A> assignSourceRoleTo(final A source)
    {
        return () -> source;
    }

    Account_DestinationRole<A> assignDestinationRoleTo(final A destination)
    {
        return () -> destination;
    }

    Account_SourceAndDestinationRole<A> assignSourceAndDestinationRoleTo(final A account)
    {
        return () -> account;
    }

    // Account roles:

    @SuppressWarnings("java:S114")
    interface Account_SourceRole<A extends Account> extends Role<A>
    {
        String INSUFFICIENT_FUNDS = "Insufficient funds.";

        default void transfer(final Double amount, final Account_DestinationRole<? super A> destination)
        {
            if (self().getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            self().decreaseBalanceBy(amount);
            destination.receive(amount);
        }
    }

    @SuppressWarnings("java:S114")
    interface Account_DestinationRole<A extends Account> extends Role<A>
    {
        default void receive(final Double amount)
        {
            self().increaseBalanceBy(amount);
        }
    }

    @SuppressWarnings("java:S114")
    public interface Account_SourceAndDestinationRole<A extends Account>
        extends Account_SourceRole<A>, Account_DestinationRole<A>
    {
    }
}
