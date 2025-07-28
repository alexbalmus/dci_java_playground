package com.alexbalmus.reversewrapper.common.jpa;

import com.alexbalmus.reversewrapper.common.Role;
import com.alexbalmus.reversewrapper.common.RolePlayer;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

/**
 * Abstract class implementing {@link com.alexbalmus.reversewrapper.common.RolePlayer}
 * for JPA entities
 */
@MappedSuperclass
public class AbstractRolePlayingJpaEntity implements RolePlayer
{
    @Transient // This field is not persisted in the database
    Role<? extends RolePlayer> role;

    @Override
    public void acceptRole(Role<? extends RolePlayer> role)
    {
        if (role != null && role.thiz() != this)
        {
            throw new IllegalArgumentException("Invalid role.");
        }

        this.role = role;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Role<? extends RolePlayer>> R role()
    {
        if (this.role == null)
        {
            throw new IllegalStateException("No role has been assigned to this entity.");
        }

        try
        {
            return (R) this.role;
        }
        catch (ClassCastException e)
        {
            throw new IllegalStateException("Attempting to play an invalid role.");
        }
    }
}
