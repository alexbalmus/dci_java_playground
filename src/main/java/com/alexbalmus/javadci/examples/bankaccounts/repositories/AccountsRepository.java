package com.alexbalmus.javadci.examples.bankaccounts.repositories;

import com.alexbalmus.javadci.examples.bankaccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long>
{
}
