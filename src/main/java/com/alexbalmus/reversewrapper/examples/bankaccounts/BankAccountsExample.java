package com.alexbalmus.reversewrapper.examples.bankaccounts;

import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;
import com.alexbalmus.reversewrapper.examples.bankaccounts.repositories.AccountsRepository;
import com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer.MoneyTransferDciContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
@Transactional(readOnly = true)
@Component
public class BankAccountsExample
{
    @PersistenceContext
    @SuppressWarnings("unused")
    private EntityManager entityManager;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    MoneyTransferDciContext moneyTransferDciContext;


    @Transactional
    public void executeAToBMoneyTransferScenario()
    {
        var source = new Account(100.0);
        accountsRepository.save(source);
        System.out.println("Source account: " + source.getBalance());

        var destination = new Account(200.0);
        accountsRepository.save(destination);
        System.out.println("Destination account: " + destination.getBalance());

        System.out.println("Transferring 50 from Source to Destination.");
        moneyTransferDciContext.transferFromSourceToDestination(source, destination, 50.0);

        accountsRepository.flush();

        System.out.println("Detaching source...");
        entityManager.detach(source);

        System.out.println("Detaching destination...");
        entityManager.detach(destination);

        var retSource = accountsRepository.findById(source.getId()).orElseThrow();
        var retDestination = accountsRepository.findById(destination.getId()).orElseThrow();

        System.out.println("Same source object references? " + (source == retSource)); // false
        System.out.println("Same destination object references? " + (destination == retDestination)); // false

        System.out.println("Equal source? " + source.equals(retSource)); // true
        System.out.println("Equal destination? " + destination.equals(retDestination)); // true

        System.out.println("Source account: " + retSource.getBalance()); // 50.0
        System.out.println("Destination account: " + retDestination.getBalance()); // 250.0
    }

    @Transactional
    public void executeAToBToCMoneyTransferScenario()
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
        moneyTransferDciContext.transferFromSourceToDestinationViaTemporary(source, destination, intermediary, 50.0);

        accountsRepository.flush();

        entityManager.detach(source);
        entityManager.detach(destination);
        entityManager.detach(intermediary);

        var retSource = accountsRepository.findById(source.getId()).orElseThrow();
        var retIntermediary = accountsRepository.findById(intermediary.getId()).orElseThrow();
        var retDestination = accountsRepository.findById(destination.getId()).orElseThrow();

        System.out.println("Source account: " + retSource.getBalance()); // 50.0
        System.out.println("Intermediary account: " + retIntermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + retDestination.getBalance()); // 250.0
    }
}
