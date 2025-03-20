package com.alexbalmus.dcibankaccounts.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer.SourceToDestinationTransferContext;
import com.alexbalmus.dcibankaccounts.dcicontexts.moneytransfer.SourceToIntermediaryToDestinationTransferContext;

/**
 * Money transfer service orchestrating the DCI contexts for money transfer
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MoneyTransferService
{
    private final AccountService accountService;

    @Transactional
    public void executeSourceToDestinationTransfer(
        final Long sourceId,
        final Long destinationId,
        final Double amount)
    {
        new SourceToDestinationTransferContext(
            accountService.accountById(sourceId),
            accountService.accountById(destinationId),
            amount).perform();
    }

    @Transactional
    public void executeSourceToIntermediaryToDestinationTransfer(
        final Long sourceId,
        final Long destinationId,
        final Long intermediaryAccount,
        final Double amount
    )
    {
        new SourceToIntermediaryToDestinationTransferContext(
            accountService.accountById(sourceId),
            accountService.accountById(destinationId),
            accountService.accountById(intermediaryAccount),
            amount).perform();
    }
}
