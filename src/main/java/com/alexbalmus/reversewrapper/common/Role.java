package com.alexbalmus.reversewrapper.common;

/**
 * Basic role interface
 * @param <E> the generic type of the entity which will play the role
 */
public interface Role<E>
{
    /**
     * @return a reference to the role-playing entity
     */
    E thiz();
}
