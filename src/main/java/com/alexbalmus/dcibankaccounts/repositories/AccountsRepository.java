package com.alexbalmus.dcibankaccounts.repositories;

import com.alexbalmus.dcibankaccounts.dom.entities.Account;

import java.util.HashMap;

public class AccountsRepository
{
    private static Long sequenceGenerator = 0L;
    private HashMap<Long, Account> accounts = new HashMap<>();

    public Account create(Double amount)
    {
        Long id = ++sequenceGenerator;
        accounts.put(id, new Account(id, amount));
        return accounts.get(id);
    }

    public Account findById(Long id)
    {
        return accounts.get(id);
    }
}
