package de.amirrocker.binder;

import android.app.Activity;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.amirrocker.library.internal.BindingSuffix;

public class Binding {

    private Binding() {}


    private static <T extends Activity> void instantiateBinder(final T target, final String suffix) {

        final Class<?> targetClass = target.getClass();
        final String className = targetClass.getName();

        try { // TODO improve on the try catch Structure
            Class<?> bindingClass = targetClass
                    .getClassLoader()
                    .loadClass(className+suffix);
            Constructor<?> classConstructor = bindingClass
                    .getConstructor(targetClass);
            try {
                classConstructor.newInstance(target);
            } catch (IllegalAccessException iax) {
                System.out.println("Error cnfx: "+iax);
            } catch (InstantiationException e) {
                System.out.print(e.getLocalizedMessage());
            } catch (InvocationTargetException e) {
                System.out.print(e.getLocalizedMessage());
            }
        } catch (ClassNotFoundException cnfx) {
            System.out.println("Error ClassNotFoundException: "+cnfx);
        } catch (NoSuchMethodException nsmx) {
            System.out.println("Error NoSuchMethodException: "+nsmx);
        }
    }

    public static <T extends Activity> void bind(final T target ) {
        instantiateBinder(target, BindingSuffix.GENERATED_CLASS_SUFFIX);
    }


}
