package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.common.RoleMethod;
import com.alexbalmus.dcibankaccounts.entities.Account;
import org.springframework.stereotype.Service;

/**
 * Money transfer use case variations
 * @param <A> type of the role playing entities (Account of subtype)
 */
@Service
public class MoneyTransferService<A extends Account>
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    /**
     * DCI context (use case): transfer amount from source account to destination account
     */
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
        RoleMethod<Double> DESTINATION_receive = (amount) ->
        {
            DESTINATION.increaseBalanceBy(amount);
        };

        // Source account:
        RoleMethod<Double> SOURCE_transferToDestination = (amount) ->
        {
            if (SOURCE.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            SOURCE.decreaseBalanceBy(amount);

            // equivalent of: DESTINATION.receive(amount):
            DESTINATION_receive.call(amount);
        };


        //----- Interaction:

        // equivalent of: SOURCE.transferToDestination(amount)
        SOURCE_transferToDestination.call(amountToTransfer);
    }

    /**
     *  DCI context (use case): transfer amount from source account to destination account
     *  via intermediary account
     */
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
        RoleMethod<Double> DESTINATION_receive = (amount) ->
        {
            DESTINATION.increaseBalanceBy(amount);
        };

        // Intermediary account:
        RoleMethod<Double> INTERMEDIARY_receive = (amount) ->
        {
            INTERMEDIARY.increaseBalanceBy(amount);
        };

        RoleMethod<Double> INTERMEDIARY_transferToDestination = (amount) ->
        {
            if (INTERMEDIARY.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            INTERMEDIARY.decreaseBalanceBy(amount);

            // equivalent of: DESTINATION.receive(amount):
            DESTINATION_receive.call(amount);
        };

        // Source account:
        RoleMethod<Double> SOURCE_transferToIntermediary = (amount) ->
        {
            if (SOURCE.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            SOURCE.decreaseBalanceBy(amount);

            // equivalent of: intermediaryAccount.receive(amount):
            INTERMEDIARY_receive.call(amount);
        };


        //----- Interaction:

        // equivalent of: SOURCE.transferToIntermediary(amount)
        SOURCE_transferToIntermediary.call(amountToTransfer);

        // equivalent of: INTERMEDIARY.transferToDestination(amount):
        INTERMEDIARY_transferToDestination.call(amountToTransfer);
    }
}
