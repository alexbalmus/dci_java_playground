package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MoneyTransferService<A extends Account>
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    // DCI context (use case): transfer amount from source account to destination account
    public void executeSourceToDestinationTransfer(
        final Double amountToTransfer,
        final A sourceAccount,
        final A destinationAccount)
    {
        //----- Roles:

        final A SOURCE = sourceAccount;
        final A DESTINATION = destinationAccount;


        //----- Role methods:

        // Destination account:
        Consumer<Double> DESTINATION_receive = (amount) ->
        {
            DESTINATION.increaseBalanceBy(amount);
        };

        // Source account:
        Consumer<Double> SOURCE_transferToDestination = (amount) ->
        {
            if (SOURCE.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            SOURCE.decreaseBalanceBy(amount);

            // equivalent of: DESTINATION.receive(amount):
            DESTINATION_receive.accept(amount);
        };


        //----- Interaction:

        // equivalent of: sourceAccount.transferToDestination(amount)
        SOURCE_transferToDestination.accept(amountToTransfer);
    }

    // DCI context (use case): transfer amount from source account to destination account via intermediary account
    public void executeSourceToIntermediaryToDestinationTransfer(
        final Double amountToTransfer,
        final A sourceAccount,
        final A destinationAccount,
        final A intermediaryAccount
    )
    {
        //----- Roles:

        final A SOURCE = sourceAccount;
        final A DESTINATION = destinationAccount;
        final A INTERMEDIARY = intermediaryAccount;


        //----- Role methods:

        // Destination account:
        Consumer<Double> DESTINATION_receive = (amount) ->
        {
            DESTINATION.increaseBalanceBy(amount);
        };

        // Intermediary account:
        Consumer<Double> INTERMEDIARY_receive = (amount) ->
        {
            INTERMEDIARY.increaseBalanceBy(amount);
        };

        Consumer<Double> INTERMEDIARY_transferToDestination = (amount) ->
        {
            if (INTERMEDIARY.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            INTERMEDIARY.decreaseBalanceBy(amount);

            // equivalent of: DESTINATION.receive(amount):
            DESTINATION_receive.accept(amount);
        };

        // Source account:
        Consumer<Double> SOURCE_transferToIntermediary = (amount) ->
        {
            if (SOURCE.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            SOURCE.decreaseBalanceBy(amount);

            // equivalent of: intermediaryAccount.receive(amount):
            INTERMEDIARY_receive.accept(amount);
        };


        //----- Interaction:

        // equivalent of: SOURCE.transferToIntermediary(amount)
        SOURCE_transferToIntermediary.accept(amountToTransfer);

        // equivalent of: INTERMEDIARY.transferToDestination(amount):
        INTERMEDIARY_transferToDestination.accept(amountToTransfer);
    }
}
