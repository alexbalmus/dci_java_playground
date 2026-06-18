package com.alexbalmus.javadci.examples.bankaccounts.entities;

import jakarta.persistence.*;
import lombok.*;

import com.alexbalmus.javadci.examples.bankaccounts.exceptions.BalanceException;

@Getter
@Entity
@Table(name="account")
public class Account
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";
    public static final String INVALID_AMOUNT = "Amount must be a positive finite value.";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    public Account(final Double balance)
    {
        this.balance = balance;
    }

    // Needed by tests:
    public Account(final Long id, final Double balance)
    {
        this.id = id;
        this.balance = balance;
    }

    // Needed by ORM:
    protected Account()
    {
    }

    public void deposit(final Double amount)
    {
        validateAmount(amount);
        balance += amount;
    }

    public void withdraw(final Double amount)
    {
        validateAmount(amount);
        if (balance < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS);
        }
        balance -= amount;
    }

    private void validateAmount(final Double amount)
    {
        if (amount == null || amount <= 0 || amount.isNaN() || amount.isInfinite())
        {
            throw new IllegalArgumentException(INVALID_AMOUNT);
        }
    }

    // equals and hashCode for JPA based on tutorials from Vlad Mihalcea and Thorben Janssen:

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Account other = (Account) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}

