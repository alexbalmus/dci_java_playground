package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import lombok.experimental.ExtensionMethod;

import com.alexbalmus.dcibankaccounts.entities.Account;

/**
 * Money transfer DCI context.
 * Uses extension method MoneyTransferContext.Account_Source#transfer
 */
@ExtensionMethod(MoneyTransferContext.Account_Source.class)
public class MoneyTransferContext
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    /**
     * DCI context (use case): transfer amount from source account to destination account
     */
    public void executeSourceToDestinationTransfer(
        final Double amountToTransfer,
        final Account source,
        final Account destination)
    {
        source.transfer(destination, amountToTransfer);
    }

    /**
     *  Use case: transfer amount from source account to destination account
     *  via intermediary account
     */
    public void executeSourceToIntermediaryToDestinationTransfer(
        final Double amountToTransfer,
        final Account source,
        final Account destination,
        final Account intermediary)
    {
        source.transfer(intermediary, amountToTransfer);
        intermediary.transfer(destination, amountToTransfer);
    }

    /**
     * Account_Source role
     * Uses extension method MoneyTransferContext.Account_Destination#receive
     */
    @ExtensionMethod(MoneyTransferContext.Account_Destination.class)
    static class Account_Source
    {
        @SuppressWarnings("unused")
        public static void transfer(Account thiz, Account destination, Double amount)
        {
            if (thiz.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }

            thiz.decreaseBalanceBy(amount);

            destination.receive(amount);
        }
    }

    /**
     * Account_Destination role
     */
    static class Account_Destination
    {
        @SuppressWarnings("unused")
        public static void receive(Account thiz, Double amount)
        {
            thiz.increaseBalanceBy(amount);
        }
    }
}
