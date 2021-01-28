package de.amirrocker.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // use annotation on class level (classes, interfaces, ...)
@Retention(RetentionPolicy.SOURCE) // not needed at runtime
public @interface AutoParcel {}