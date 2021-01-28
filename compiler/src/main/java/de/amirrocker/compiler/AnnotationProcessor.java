package de.amirrocker.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import de.amirrocker.library.BindView;
import de.amirrocker.library.Keep;
import de.amirrocker.library.OnClick;


/**
 * the AnnotationProcessor that is used to process the used annotations throughout the codebase.
 * See the README file for a more detailed description of the processor.
 */
final public class AnnotationProcessor extends AbstractProcessor {

    /* Filer provides API to write the generated source code file */
    private Filer filer;

    /* Messager is used to print messages when the compilation is taking place. */
    private Messager messager;

    /* Elements provides utils for filtering the different type of elements in the processor. */
    private Elements elementUtils;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        System.out.println("We need a logger setup :) This is where the magic must happen ..... ");
        if( !roundEnvironment.processingOver() ) {

            // find all classes that use the supported annotations - see getSupportedAnnotationTypes
            final Set<TypeElement> typeElements = ProcessingUtils.getTypeElementsToProcess(
                    roundEnvironment.getRootElements(),
                    annotations
            );
            // for each such annotation create a wrapper class
            for( Element typeElement : typeElements ) {
                final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
                final String typeName = typeElement.getSimpleName().toString();
                final ClassName className = ClassName.get(packageName, typeName);

                final ClassName generatedClassName = ClassName
                        .get(packageName, NameStore.getGeneratedClassname(typeName));

                // define the wrapper class
                final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Keep.class);

                // now add a constructor to the newly created class
                classBuilder.addMethod(
                        MethodSpec
                            .constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(className, NameStore.Variable.ANDROID_ACTIVITY)
                            .addStatement(
                                    "$N($N)",
                                    NameStore.Method.BIND_VIEWS,
                                    NameStore.Variable.ANDROID_ACTIVITY)
                            .addStatement(
                                    "$N($N)",
                                    NameStore.Method.BIND_ON_CLICKS,
                                    NameStore.Variable.ANDROID_ACTIVITY
                            )
                            .build());

                // add the method that maps the view to the ID
                MethodSpec.Builder bindViewsMethodBuilder = MethodSpec
                        .methodBuilder(NameStore.Method.BIND_VIEWS)
                        .addModifiers(Modifier.PRIVATE)
                        .returns(void.class)
                        .addParameter(className, NameStore.Variable.ANDROID_ACTIVITY);

                for(VariableElement variableElement : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
                    BindView bindView = variableElement.getAnnotation(BindView.class);
                    if( bindView != null ) {
                        bindViewsMethodBuilder.addStatement(
                                "$N.$N = ($T)$N.findViewById($L)",
                                NameStore.Variable.ANDROID_ACTIVITY,
                                variableElement.getSimpleName(),
                                variableElement,
                                NameStore.Variable.ANDROID_ACTIVITY,
                                bindView.value());
                    }
                }

                classBuilder.addMethod(bindViewsMethodBuilder.build());

                // fill the class Name templates with values to be used when type is created
                ClassName androidOnClickListenerClassName = ClassName.get(
                        NameStore.Package.ANDROID_VIEW,
                        NameStore.Class.ANDROID_VIEW,
                        NameStore.Class.ANDROID_VIEW_ON_CLICK_LISTENER);

                ClassName androidViewClassName = ClassName.get(
                        NameStore.Package.ANDROID_VIEW,
                        NameStore.Class.ANDROID_VIEW);

                /* define the template for the bindOnClicks method */
                MethodSpec.Builder bindOnClicksMethodBuilder = MethodSpec
                        .methodBuilder(NameStore.Method.BIND_ON_CLICKS)
                        .addModifiers(Modifier.PRIVATE)
                        .returns(void.class)
                        .addParameter(className, NameStore.Variable.ANDROID_ACTIVITY, Modifier.FINAL);

                /* all click listeners are bundled in a method */
                for(ExecutableElement executableElement : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
                    final OnClick onClick = executableElement.getAnnotation(OnClick.class);

                    if( onClick != null ) {
                        log("we are processing the body of the method", typeElement);

                        TypeSpec OnClickListenerClass = TypeSpec.anonymousClassBuilder("")
                                .addSuperinterface(androidOnClickListenerClassName)
                                .addMethod(
                                        MethodSpec
                                            .methodBuilder(NameStore.Method.ANDROID_VIEW_ON_CLICK)
                                            .addModifiers(Modifier.PUBLIC)
                                            .addParameter(androidViewClassName, NameStore.Variable.ANDROID_VIEW)
                                            .addStatement("$N.$N($N)",
                                                    NameStore.Variable.ANDROID_ACTIVITY, // activity
                                                    executableElement.getSimpleName(), // expect a value of "btn1Click" the method that was annotated
                                                    NameStore.Variable.ANDROID_VIEW // view parameter
                                                    )
                                            .returns(void.class)
                                            .build()
                                )
                                .build();
                        bindOnClicksMethodBuilder.addStatement(
                            "$N.findViewById($L).setOnClickListener($L)",
                            NameStore.Variable.ANDROID_ACTIVITY, // activity
                            onClick.value(),
                            OnClickListenerClass);

                    } else {
                        log("OnClick class is null", typeElement);
                    }
                }
                classBuilder.addMethod(bindOnClicksMethodBuilder.build());

                // let's see if we can get the class created
                try {
                    JavaFile.builder(
                                packageName,
                                classBuilder.build()
                            )
                            .build()
                            .writeTo(filer);
                } catch (IOException ex) {
                    log(Diagnostic.Kind.ERROR, ex.getLocalizedMessage(), typeElement);
                }
            }

        } else {
            log("We are done processing.", null);
        }
        return true;
    }

    private void log(final String msg, final Element typeElement) {
        printMessage(Diagnostic.Kind.NOTE, msg, typeElement);
    }

    private void log(final Diagnostic.Kind kind, final String msg, final Element typeElement) {
        printMessage(kind, msg, typeElement);
    }

    private void printMessage(final Diagnostic.Kind kind, final String msg, final Element typeElement) {
        messager.printMessage(kind, msg, typeElement);
    }

    /**
     * tell the processor which Annotations are to be supported.
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                BindView.class.getCanonicalName(),
                OnClick.class.getCanonicalName(),
                Keep.class.getCanonicalName()
        ));
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
    }

}