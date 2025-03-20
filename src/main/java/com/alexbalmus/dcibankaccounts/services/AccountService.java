package com.alexbalmus.dcibankaccounts.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexbalmus.dcibankaccounts.entities.Account;
import com.alexbalmus.dcibankaccounts.repositories.AccountRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService
{
    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(Double initialBalance)
    {
        var account = new Account(initialBalance);
        accountRepository.save(account);
        return account;
    }

    public Account accountById(Long accountId)
    {
        return accountRepository.findById(accountId).orElseThrow();
    }
}
