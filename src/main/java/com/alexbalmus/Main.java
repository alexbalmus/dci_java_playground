package com.alexbalmus;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.usecases.moneytransfer.ABCMoneyTransferContext;
import com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;


public class Main
{
    public static void main(String[] args)
    {
        try (
            final EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("com.alexbalmus.dcibankaccounts");
            final EntityManager entityManager = entityManagerFactory.createEntityManager())
        {
            System.out.println("\nExecuting A to B money transfer scenario: \n");
            executeAToBMoneyTransferScenario(entityManager);

            System.out.println("\nExecuting A to B to C money transfer scenario: \n");
            executeAToBToCMoneyTransferScenario(entityManager);
        }
    }

    public static void executeAToBMoneyTransferScenario(final EntityManager entityManager)
    {
        var source = new Account(100.0);
        entityManager.persist(source);
        System.out.println("Source account: " + source.getBalance());

        var destination = new Account(200.0);
        entityManager.persist(destination);
        System.out.println("Destination account: " + destination.getBalance());

        var moneyTransferContext = new MoneyTransferContext<>(50.0, source, destination);

        System.out.println("Transferring 50 from Source to Destination.");
        doInTransaction(moneyTransferContext::execute, entityManager.getTransaction());

        System.out.println("Detaching source...");
        entityManager.detach(source);

        System.out.println("Detaching destination...");
        entityManager.detach(destination);

        var retSource = entityManager.find(Account.class, source.getId());
        var retDestination = entityManager.find(Account.class, destination.getId());

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
        var source = new Account(100.0);
        entityManager.persist(source);
        System.out.println("Source account: " + source.getBalance());

        var intermediary = new Account(0.0);
        entityManager.persist(intermediary);
        System.out.println("Intermediary account: " + intermediary.getBalance());

        var destination = new Account(200.0);
        entityManager.persist(destination);
        System.out.println("Destination account: " + destination.getBalance());

        var abcMoneyTransferContext = new ABCMoneyTransferContext<>(50.0, source, intermediary, destination);

        System.out.println("Transferring 50 from Source to Destination via Intermediary.");
        doInTransaction(abcMoneyTransferContext::execute, entityManager.getTransaction());

        System.out.println("Source account: " + source.getBalance()); // 50.0
        System.out.println("Intermediary account: " + intermediary.getBalance()); // 0.0
        System.out.println("Destination account: " + destination.getBalance()); // 250.0
    }

    private static void doInTransaction(Runnable task, EntityTransaction transaction)
    {
        try
        {
            transaction.begin();
            task.run();
            transaction.commit();
        }
        catch (Exception e)
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
            throw e;
        }
    }
}
