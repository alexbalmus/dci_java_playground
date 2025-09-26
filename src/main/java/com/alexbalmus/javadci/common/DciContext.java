package com.alexbalmus.javadci.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A stereotype annotation that is a synonym for Spring's {@link Component}.
 * <p>
 * For projects using DCI (Data - Context - Interaction), it indicates that an annotated class is a "Context".
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DciContext {}