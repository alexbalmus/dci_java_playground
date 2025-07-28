package com.alexbalmus.reversewrapper.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * A stereotype annotation for DCI (Data, Context, and Interaction) Contexts.
 * <p>
 * This annotation marks a class as a DCI Context, which is responsible for
 * orchestrating the interactions between role-playing objects within a specific
 * use case.
 * <p>
 * By being meta-annotated with {@link org.springframework.stereotype.Component},
 * classes marked with {@code @DciContext} are eligible for component scanning
 * and will be registered as Spring beans in the application context. This allows
 * for dependency injection and management by the Spring Framework.
 *
 * @see org.springframework.stereotype.Component
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DciContext
{
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an auto-detected component.
     * @return the suggested component name, if any (or empty String otherwise)
     */
    String value() default "";
}