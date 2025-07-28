package com.alexbalmus.reversewrapper.common;

/**
 * Interface to be implemented by role-playing objects
 */
public interface RolePlayer
{
    /**
     * Accept a role during use-case execution
     * @param role the role to be played
     */
    void acceptRole(Role<? extends RolePlayer> role);

    /**
     * Expose the role object to invoke the role methods
     * @return the role object
     * @param <R> the generic type of the role object to cast to
     */
    <R extends Role<? extends RolePlayer>> R role();

    /**
     * Clear the role object at the end of use-case execution
     */
    default void clearRole()
    {
        acceptRole(null);
    }
}
