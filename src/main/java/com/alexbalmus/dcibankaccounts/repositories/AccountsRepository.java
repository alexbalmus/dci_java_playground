package com.alexbalmus.dcibankaccounts.repositories;

import com.alexbalmus.dcibankaccounts.entities.Account;

import java.util.HashMap;

public class AccountsRepository
{
    private static Long sequence = 0L;
    private final HashMap<Long, Account> accounts = new HashMap<>();

    public Account create(Double amount)
    {
        Long id = ++sequence;
        accounts.put(id, new Account(id, amount));
        return accounts.get(id);
    }

    public Account findById(Long id)
    {
        return accounts.get(id);
    }
}
