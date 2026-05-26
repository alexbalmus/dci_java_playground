package com.alexbalmus.javadci.examples.bankaccounts.usecases.moneytransfer;

import lombok.experimental.ExtensionMethod;

import com.alexbalmus.javadci.common.DciContext;
import com.alexbalmus.javadci.common.DciRole;
import com.alexbalmus.javadci.examples.bankaccounts.entities.Account;

/**
 * Money transfer DCI context.
 * Uses extension method MoneyTransferContext.Account_Source#transfer.
 */
@DciContext
@ExtensionMethod(MoneyTransferContext.Account_Source.class)
public class MoneyTransferContext
{
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
    @DciRole
    @ExtensionMethod(MoneyTransferContext.Account_Destination.class)
    static class Account_Source
    {
        @SuppressWarnings("unused")
        public static void transfer(Account thiz, Account destination, Double amount)
        {
            thiz.decreaseBalanceBy(amount);
            destination.receive(amount);
        }
    }

    /**
     * Account_Destination role
     */
    @DciRole
    static class Account_Destination
    {
        @SuppressWarnings("unused")
        public static void receive(Account thiz, Double amount)
        {
            thiz.increaseBalanceBy(amount);
        }
    }
}
