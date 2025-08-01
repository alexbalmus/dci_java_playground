package com.alexbalmus.reversewrapper.examples.bankaccounts.entities;

import com.alexbalmus.reversewrapper.common.jpa.AbstractRolePlayingJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="account")
@Getter
@Setter
public class Account extends AbstractRolePlayingJpaEntity
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


// Implementing equals and hashCode for JPA & Hibernate entities:
//
// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
// https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
// https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/

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

