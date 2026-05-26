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

    public void increaseBalanceBy(final Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(final Double amount)
    {
        if (balance < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS);
        }
        balance -= amount;
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

