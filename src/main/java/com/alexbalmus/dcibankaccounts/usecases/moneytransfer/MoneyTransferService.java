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
        final A source,
        final A destination)
    {
        //----- Role methods:

        // Destination account:
        RoleMethod<Double> destination_receive = (amount) ->
        {
            destination.increaseBalanceBy(amount);
        };

        // Source account:
        RoleMethod<Double> source_transferToDestination = (amount) ->
        {
            if (source.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            source.decreaseBalanceBy(amount);

            // equivalent of: destination.receive(amount):
            destination_receive.call(amount);
        };


        //----- Interaction:

        // equivalent of: source.transferToDestination(amount)
        source_transferToDestination.call(amountToTransfer);
    }

    /**
     *  DCI context (use case): transfer amount from source account to destination account
     *  via intermediary account
     */
    public void executeSourceToIntermediaryToDestinationTransfer(
        final Double amountToTransfer,
        final A source,
        final A destination,
        final A intermediary
    )
    {
        //----- Role methods:

        // Destination account:
        RoleMethod<Double> destination_receive = (amount) ->
        {
            destination.increaseBalanceBy(amount);
        };

        // Intermediary account:
        RoleMethod<Double> intermediary_receive = (amount) ->
        {
            intermediary.increaseBalanceBy(amount);
        };

        RoleMethod<Double> intermediary_transferToDestination = (amount) ->
        {
            if (intermediary.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            intermediary.decreaseBalanceBy(amount);

            // equivalent of: destination.receive(amount):
            destination_receive.call(amount);
        };

        // Source account:
        RoleMethod<Double> source_transferToIntermediary = (amount) ->
        {
            if (source.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            source.decreaseBalanceBy(amount);

            // equivalent of: intermediary.receive(amount):
            intermediary_receive.call(amount);
        };


        //----- Interaction:

        // equivalent of: source.transferToIntermediary(amount)
        source_transferToIntermediary.call(amountToTransfer);

        // equivalent of: intermediary.transferToDestination(amount):
        intermediary_transferToDestination.call(amountToTransfer);
    }
}
