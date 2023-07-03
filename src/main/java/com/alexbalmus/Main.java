package com.alexbalmus;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;
import com.alexbalmus.dcibankaccounts.dom.usecases.moneytransfer.MoneyTransfer;
import com.alexbalmus.dcibankaccounts.repositories.AccountsRepository;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        AccountsRepository accountsRepository = new AccountsRepository();
        Account source = accountsRepository.create(100.0);
        out.println("Source account: " + source.getBalance());
        Account destination = accountsRepository.create(200.0);
        out.println("Destination account: " + destination.getBalance());
        MoneyTransfer moneyTransfer = new MoneyTransfer(50.0, accountsRepository,
                source.getId(), destination.getId());
        out.println("Transfering 50 from source to destination.");
        moneyTransfer.execute();
        out.println("Source account: " + source.getBalance());
        out.println("Destination account: " + destination.getBalance());
    }
}