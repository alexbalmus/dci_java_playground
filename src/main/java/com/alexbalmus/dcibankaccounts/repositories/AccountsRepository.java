package com.alexbalmus.dcibankaccounts.repositories;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long>
{
}
