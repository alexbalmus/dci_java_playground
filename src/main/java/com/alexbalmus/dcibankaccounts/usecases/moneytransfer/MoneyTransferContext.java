package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.apache.commons.lang3.Validate;

public class MoneyTransferContext<A extends Account>
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    private final Double amount;

    private final A sourceAccount;
    private final A destinationAccount;
    private final A intermediaryAccount;

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
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.intermediaryAccount = intermediaryAccount;
    }

    public void executeSourceToDestinationTransfer()
    {
        source_transferTo(sourceAccount, destinationAccount, amount);
    }

    public void executeSourceToIntermediaryToDestinationTransfer()
    {
        Validate.notNull(intermediaryAccount, "intermediaryAccount must not be null.");
        source_transferTo(sourceAccount, intermediaryAccount, amount);
        source_transferTo(intermediaryAccount, destinationAccount, amount);
    }


    // Account roles:

    // equivalent of: source.transferTo(destination, amount)
    void source_transferTo(final A self, A destination, final Double amount)
    {
        if (self.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        self.decreaseBalanceBy(amount);
        destination_receive(destination, amount);
    }

    // equivalent of: destination.receive(amount)
    void destination_receive(final A self, final Double amount)
    {
        self.increaseBalanceBy(amount);
    }
}
