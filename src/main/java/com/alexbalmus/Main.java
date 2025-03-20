package com.alexbalmus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alexbalmus.dcibankaccounts.services.AccountService;
import com.alexbalmus.dcibankaccounts.services.MoneyTransferService;

@SpringBootApplication
@EnableTransactionManagement
public class Main implements CommandLineRunner
{
    @Autowired
    AccountService accountService;

    @Autowired
    MoneyTransferService moneyTransferService;

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
        var source = accountService.createAccount(100.0);
        System.out.println("Source account: " + source.getBalance());

        var destination = accountService.createAccount(200.0);
        System.out.println("Destination account: " + destination.getBalance());

        System.out.println("Transferring 50 from Source to Destination.");
        moneyTransferService.executeSourceToDestinationTransfer(source.getId(), destination.getId(), 50.0);

        var retSource = accountService.accountById(source.getId());
        var retDestination = accountService.accountById(destination.getId());

        System.out.println("Source account: " + retSource.getBalance()); // 50.0
        System.out.println("Destination account: " + retDestination.getBalance()); // 250.0

        System.out.println("\n\n");
    }

    public void executeAToBToCMoneyTransferScenario()
    {
        var source = accountService.createAccount(100.0);
        System.out.println("Source account: " + source.getBalance());

        var intermediary = accountService.createAccount(0.0);
        System.out.println("Intermediary account: " + intermediary.getBalance());

        var destination = accountService.createAccount(200.0);
        System.out.println("Destination account: " + destination.getBalance());

        System.out.println("Transferring 50 from Source to Destination via Intermediary.");
        moneyTransferService.executeSourceToIntermediaryToDestinationTransfer(
            source.getId(), destination.getId(), intermediary.getId(), 50.0);

        var retSource = accountService.accountById(source.getId());
        var retIntermediary = accountService.accountById(intermediary.getId());
        var retDestination = accountService.accountById(destination.getId());

        System.out.println("Source account: " + retSource.getBalance()); // 50.0
        System.out.println("Intermediary account: " + retIntermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + retDestination.getBalance()); // 250.0
    }
}
