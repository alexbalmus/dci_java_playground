package com.alexbalmus.reversewrapper.examples.bankaccounts.repositories;

import com.alexbalmus.reversewrapper.examples.bankaccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long>
{
}
