package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MoneyTransferService<A extends Account>
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    public void executeSourceToDestinationTransfer(
        final Double amountToTransfer,
        final A sourceAccount,
        final A destinationAccount)
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
        sourceAccount_transferToDestinationAccount.accept(amountToTransfer);
    }

    public void executeSourceToIntermediaryToDestinationTransfer(
        final Double amountToTransfer,
        final A sourceAccount,
        final A destinationAccount,
        final A intermediaryAccount
    )
    {
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
        sourceAccount_transferToIntermediaryAccount.accept(amountToTransfer);

        // equivalent of: intermediaryAccount.transferToDestination(amount):
        intermediaryAccount_transferToDestinationAccount.accept(amountToTransfer);
    }
}
