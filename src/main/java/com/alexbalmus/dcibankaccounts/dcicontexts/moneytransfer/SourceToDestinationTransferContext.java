package com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer;

import com.alexbalmus.dcibankaccounts.entities.Account;


/**
 * DCI Context for money transfer use case variation A to B
 */
public class SourceToDestinationTransferContext
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    private Account source;
    private Account destination;
    private Double amount;

    public SourceToDestinationTransferContext(
        Account sourceAccount,
        Account destinationAccount,
        Double amountToTransfer)
    {
        this.source = sourceAccount;
        this.destination = destinationAccount;
        this.amount = amountToTransfer;
    }

    // Source role:
    void source_transferToDestination(Double amount)
    {
        if (source.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        source.decreaseBalanceBy(amount);

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

        // equivalent of: source.transferToDestination(amount)
        source_transferToDestination(amount);
    }
}
