package com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.reversewrapper.common.DciContext;
import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;
import org.apache.commons.lang3.Validate;

/**
 * DCI money transfer context
 */
@DciContext
public class MoneyTransferDciContext
{
    static Account_Source createSourceRole(final Account account)
    {
        return () -> account;
    }

    static Account_Destination createDestinationRole(final Account account)
    {
        return () -> account;
    }


    // Use case variations:

    public void transferFromSourceToDestination(
        final Account source, final Account destination, final Double amount)
    {
        //--- Use case roles setup:
        source.acceptRole(createSourceRole(source));
        destination.acceptRole(createDestinationRole(destination));

        //--- Interaction:
        source.<Account_Source>role().transfer(amount, destination);

        //--- Clear roles:
        source.clearRole();
        destination.clearRole();
    }

    public void transferFromSourceToDestinationViaTemporary(
        final Account source, final Account destination, final Account temp, final Double amount)
    {
        transferMoney(source, temp, null, amount);
        transferMoney(temp, destination, temp, amount);
    }

    private void transferMoney(
        final Account source,
        final Account destination,
        final Account previousDestination,
        final Double amount)
    {
        Validate.isTrue(source != destination,
            "Source and destination can't be the same.");

        //--- Use case roles setup:
        source.acceptRole(createSourceRole(source));
        destination.acceptRole(createDestinationRole(destination));

        if (previousDestination != null)
        {
            // Identity check: it's the same object though different roles were played in different installments:
            Validate.isTrue(source == previousDestination,
                "Source must match previous destination in this step of A-B-C transfer scenario.");
        }

        //--- Interaction:
        source.<Account_Source>role().transfer(amount, destination);

        //--- Clear roles:
        source.clearRole();
        destination.clearRole();
    }
}
