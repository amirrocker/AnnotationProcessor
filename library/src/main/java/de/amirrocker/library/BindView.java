package de.amirrocker.library;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RetentionPolicy defines the presence of annotations in the code and the location allowed.
 * Target defines on what types the annotation can be used.
 * Value defines the primitive value that can be passed through the annotation.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface BindView {
    @IdRes int value();
}
