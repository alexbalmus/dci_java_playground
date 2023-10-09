package com.alexbalmus.dcibankaccounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="account")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    public Account(final Double balance)
    {
        this.balance = balance;
    }

    protected Account()
    {
    }

    public void increaseBalanceBy(final Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(final Double amount)
    {
        balance -= amount;
    }
}

