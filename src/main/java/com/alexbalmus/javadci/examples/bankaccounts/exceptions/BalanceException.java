package com.alexbalmus.javadci.examples.bankaccounts.exceptions;

public class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
