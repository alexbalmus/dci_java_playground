package com.alexbalmus.javadci.examples.bankaccounts;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alexbalmus.javadci.examples.bankaccounts.entities.Account;
import com.alexbalmus.javadci.examples.bankaccounts.repositories.AccountsRepository;
import com.alexbalmus.javadci.examples.bankaccounts.usecases.moneytransfer.MoneyTransferContext;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BankAccountsExample
{
    private final AccountsRepository accountsRepository;
    private final MoneyTransferContext moneyTransferContext;

    @Transactional
    public Pair<Long, Long> executeAToBMoneyTransferScenario()
    {
        var source = new Account(100.0);
        accountsRepository.save(source);
        System.out.println("Source account: " + source.getBalance());

        var destination = new Account(200.0);
        accountsRepository.save(destination);
        System.out.println("Destination account: " + destination.getBalance());

        System.out.println("Transferring 50 from Source to Destination.");
        moneyTransferContext.executeSourceToDestinationTransfer(50.0, source, destination);

        return Pair.of(source.getId(), destination.getId());
    }

    public void verifyAToBMoneyTransferOutcome(Long sourceId, Long destinationId)
    {
        var source = accountsRepository.findById(sourceId).orElseThrow();
        var destination = accountsRepository.findById(destinationId).orElseThrow();

        System.out.println("Source account: " + source.getBalance()); // 50.0
        System.out.println("Destination account: " + destination.getBalance()); // 250.0
    }

    @Transactional
    public Triple<Long, Long, Long> executeAToBToCMoneyTransferScenario()
    {
        var source = new Account(100.0);
        accountsRepository.save(source);
        System.out.println("Source account: " + source.getBalance());

        var intermediary = new Account(0.0);
        accountsRepository.save(intermediary);
        System.out.println("Intermediary account: " + intermediary.getBalance());

        var destination = new Account(200.0);
        accountsRepository.save(destination);
        System.out.println("Destination account: " + destination.getBalance());


        System.out.println("Transferring 50 from Source to Destination via Intermediary.");
        moneyTransferContext.executeSourceToIntermediaryToDestinationTransfer(50.0, source, destination, intermediary);

        return Triple.of(source.getId(), intermediary.getId(), destination.getId());
    }

    public void verifyAToBToCMoneyTransferOutcome(Long sourceId, Long intermediaryId, Long destinationId)
    {
        var source = accountsRepository.findById(sourceId).orElseThrow();
        var intermediary = accountsRepository.findById(intermediaryId).orElseThrow();
        var destination = accountsRepository.findById(destinationId).orElseThrow();

        System.out.println("Source account: " + source.getBalance()); // 50.0
        System.out.println("Intermediary account: " + intermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + destination.getBalance()); // 250.0
    }
}
