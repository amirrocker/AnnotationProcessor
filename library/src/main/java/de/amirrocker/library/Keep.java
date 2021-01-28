package de.amirrocker.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is needed to keep proguard from obfuscating the generated classes.
 * 
 * 
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Keep {}
