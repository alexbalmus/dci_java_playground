package com.alexbalmus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.moneytransfer.ABCMoneyTransferContext;
import com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext;


public class Main
{
    public static void main(String[] args)
    {
        EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("com.alexbalmus.dcibankaccounts");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try
        {
            System.out.println("\nExecuting A to B money transfer scenario: \n");
            executeAToBMoneyTransferScenario(entityManager);

            System.out.println("\nExecuting A to B to C money transfer scenario: \n");
            executeAToBToCMoneyTransferScenario(entityManager);
        }
        finally
        {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    public static void executeAToBMoneyTransferScenario(final EntityManager entityManager)
    {
        Account source = new Account(100.0);
        entityManager.persist(source);
        System.out.println("Source account: " + source.getBalance());

        Account destination = new Account(200.0);
        entityManager.persist(destination);
        System.out.println("Destination account: " + destination.getBalance());

        MoneyTransferContext moneyTransferContext =
            new MoneyTransferContext(entityManager, 50.0, source.getId(), destination.getId());

        System.out.println("Transferring 50 from Source to Destination.");
        moneyTransferContext.execute();

        System.out.println("Detaching source...");
        entityManager.detach(source);

        System.out.println("Detaching destination...");
        entityManager.detach(destination);

        Account retSource = entityManager.find(Account.class, source.getId());
        Account retDestination = entityManager.find(Account.class, destination.getId());

        System.out.println("Same source object references? " + (source == retSource)); // false
        System.out.println("Same destination object references? " + (destination == retDestination)); // false

        System.out.println("Equal source? " + source.equals(retSource)); // true
        System.out.println("Equal destination? " + destination.equals(retDestination)); // true

        System.out.println("Source account: " + retSource.getBalance()); // 50.0
        System.out.println("Destination account: " + retDestination.getBalance()); // 250.0

        System.out.println("\n\n");
    }

    public static void executeAToBToCMoneyTransferScenario(final EntityManager entityManager)
    {
        Account source = new Account(100.0);
        entityManager.persist(source);
        System.out.println("Source account: " + source.getBalance());

        Account intermediary = new Account(0.0);
        entityManager.persist(intermediary);
        System.out.println("Intermediary account: " + intermediary.getBalance());

        Account destination = new Account(200.0);
        entityManager.persist(destination);
        System.out.println("Destination account: " + destination.getBalance());

        ABCMoneyTransferContext abcMoneyTransferContext = new ABCMoneyTransferContext(entityManager,50.0,
            source.getId(), intermediary.getId(), destination.getId());

        System.out.println("Transferring 50 from Source to Destination via Intermediary.");
        abcMoneyTransferContext.execute();

        System.out.println("Source account: " + source.getBalance()); // 50.0
        System.out.println("Intermediary account: " + intermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + destination.getBalance()); // 250.0
    }
}
