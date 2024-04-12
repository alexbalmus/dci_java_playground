package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
