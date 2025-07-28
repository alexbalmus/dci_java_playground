package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
