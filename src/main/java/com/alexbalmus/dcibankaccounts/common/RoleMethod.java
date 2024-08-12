package com.alexbalmus.dcibankaccounts.common;

/**
 * Represents a DCI role method that contributes new contextual behavior to an object
 * @param <T> method argument
 */
public interface RoleMethod<T>
{
    void call(T t);
}
