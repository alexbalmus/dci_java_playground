package com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;


/**
 * DCI Context for money transfer use case variation A to B to C
 */
public class SourceToIntermediaryToDestinationTransferContext
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    private Account source;
    private Account destination;
    private Account intermediary;
    private Double amount;

    public SourceToIntermediaryToDestinationTransferContext(
        Account sourceAccount,
        Account destinationAccount,
        Account intermediaryAccount,
        Double amountToTransfer)
    {
        this.source = sourceAccount;
        this.destination = destinationAccount;
        this.intermediary = intermediaryAccount;
        this.amount = amountToTransfer;
    }

    //----- Role methods:

    // Source role:

    void source_transferToIntermediary(Double amount)
    {
        if (source.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        source.decreaseBalanceBy(amount);

        // equivalent of: intermediary.receive(amount):
        intermediary_receive(amount);
    }


    // Intermediary role:

    void intermediary_receive(Double amount)
    {
        intermediary.increaseBalanceBy(amount);
    }

    void intermediary_transferToDestination(Double amount)
    {
        if (intermediary.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        intermediary.decreaseBalanceBy(amount);

        // equivalent of: destination.receive(amount):
        destination_receive(amount);
    }


    // Destination role:

    void destination_receive(Double amount)
    {
        destination.increaseBalanceBy(amount);
    }


    /**
     * DCI context enactment
     */
    public void perform()
    {
        //----- Interaction:

        // equivalent of: source.transferToIntermediary(amount)
        source_transferToIntermediary(amount);

        // equivalent of: intermediary.transferToDestination(amount):
        intermediary_transferToDestination(amount);
    }
}
