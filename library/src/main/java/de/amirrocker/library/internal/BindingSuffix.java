package de.amirrocker.library.internal;

/**
 * moved to library so it is
 * a) easily exchangeable
 * b) can be used in compiler and in binder modules
 */
public class BindingSuffix {
    public static final String GENERATED_CLASS_SUFFIX = "$Binding";

    private BindingSuffix() {}
}
