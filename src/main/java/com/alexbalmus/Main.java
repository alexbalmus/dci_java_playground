package com.alexbalmus;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;
import com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
@EnableTransactionManagement
@Transactional
public class Main implements CommandLineRunner
{
    @PersistenceContext
    @SuppressWarnings("unused")
    private EntityManager entityManager;

    @Autowired
    AccountsRepository accountsRepository;

    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args)
    {
        System.out.println("\nExecuting A to B money transfer scenario: \n");
        executeAToBMoneyTransferScenario();

        System.out.println("\nExecuting A to B to C money transfer scenario: \n");
        executeAToBToCMoneyTransferScenario();
    }

    public void executeAToBMoneyTransferScenario()
    {
        var source = new Account(100.0);
        accountsRepository.save(source);
        System.out.println("Source account: " + source.getBalance());

        var destination = new Account(200.0);
        accountsRepository.save(destination);
        System.out.println("Destination account: " + destination.getBalance());

        var moneyTransferContext = new MoneyTransferContext<>(50.0, source, destination);

        System.out.println("Transferring 50 from Source to Destination.");
        moneyTransferContext.executeSourceToDestinationTransfer();
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

        System.out.println("\n\n");
    }

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

        var abcMoneyTransferContext = new MoneyTransferContext<>(50.0, source, intermediary, destination);

        System.out.println("Transferring 50 from Source to Destination via Intermediary.");
        abcMoneyTransferContext.executeSourceToIntermediaryToDestinationTransfer();

        System.out.println("Source account: " + source.getBalance()); // 50.0
        System.out.println("Intermediary account: " + intermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + destination.getBalance()); // 250.0
    }
}
