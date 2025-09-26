package com.alexbalmus.javadci.examples.bankaccounts.usecases.moneytransfer;

class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
