package com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer;

class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
