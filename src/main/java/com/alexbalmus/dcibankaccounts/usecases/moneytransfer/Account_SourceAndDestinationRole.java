package com.alexbalmus.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;

@java.lang.SuppressWarnings("java:S114")
public interface Account_SourceAndDestinationRole<A extends Account>
    extends Account_SourceRole<A>, Account_DestinationRole<A>
{
}
