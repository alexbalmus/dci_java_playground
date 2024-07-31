package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.apache.commons.lang3.Validate;

import java.util.function.Consumer;

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
        // Role methods:

        // Destination account:
        Consumer<Double> destinationAccount_receive = (amount) ->
        {
            destinationAccount.increaseBalanceBy(amount);
        };

        // Source account:
        Consumer<Double> sourceAccount_transferToDestinationAccount = (amount) ->
        {
            if (sourceAccount.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            sourceAccount.decreaseBalanceBy(amount);

            // equivalent of: destinationAccount.receive(amount):
            destinationAccount_receive.accept(amount);
        };

        // Interaction:

        // equivalent of: sourceAccount.transferToDestination(amount)
        sourceAccount_transferToDestinationAccount.accept(amount);
    }

    public void executeSourceToIntermediaryToDestinationTransfer()
    {
        Validate.notNull(intermediaryAccount, "intermediaryAccount must not be null.");

        // Role methods:

        // Destination account:
        Consumer<Double> destinationAccount_receive = (amount) ->
        {
            destinationAccount.increaseBalanceBy(amount);
        };

        // Intermediary account:
        Consumer<Double> intermediaryAccount_receive = (amount) ->
        {
            intermediaryAccount.increaseBalanceBy(amount);
        };
        Consumer<Double> intermediaryAccount_transferToDestinationAccount = (amount) ->
        {
            if (intermediaryAccount.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            intermediaryAccount.decreaseBalanceBy(amount);

            // equivalent of: destinationAccount.receive(amount):
            destinationAccount_receive.accept(amount);
        };

        // Source account:
        Consumer<Double> sourceAccount_transferToIntermediaryAccount = (amount) ->
        {
            if (sourceAccount.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            sourceAccount.decreaseBalanceBy(amount);

            // equivalent of: intermediaryAccount.receive(amount):
            intermediaryAccount_receive.accept(amount);
        };

        // Interaction:

        // equivalent of: sourceAccount.transferToIntermediary(amount)
        sourceAccount_transferToIntermediaryAccount.accept(amount);

        // equivalent of: intermediaryAccount.transferToDestination(amount):
        intermediaryAccount_transferToDestinationAccount.accept(amount);
    }
}
