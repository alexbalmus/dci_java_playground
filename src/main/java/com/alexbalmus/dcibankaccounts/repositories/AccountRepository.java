package com.alexbalmus.dcibankaccounts.repositories;

import com.alexbalmus.dcibankaccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>
{
}
