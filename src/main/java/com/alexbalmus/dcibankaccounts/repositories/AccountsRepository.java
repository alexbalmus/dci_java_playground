package com.alexbalmus.dcibankaccounts.repositories;

import com.alexbalmus.dcibankaccounts.entities.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountsRepository
{
    private static Long sequence = 0L;
    private final Map<Long, Account> accounts = new HashMap<>();

    public Account create(final Double amount)
    {
        Long id = ++sequence;
        accounts.put(id, new Account(id, amount));
        return accounts.get(id);
    }

    public Account findById(final Long id)
    {
        return accounts.get(id);
    }
}
