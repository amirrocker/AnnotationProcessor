package de.amirrocker.compiler;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ProcessingUtils {

    /* no public instantiation possible */
    private ProcessingUtils() {}

    public static Set<TypeElement> getTypeElementsToProcess(
            Set<? extends Element> elements,
            Set<? extends Element> supportedAnnotations
    ) {

        Set<TypeElement> typeElements = new HashSet<>(); // TODO replace with factory
        /* TODO once it works refactor to use Flowables */
        for(Element element : elements) {
            if( element instanceof TypeElement ) {
                boolean found = false;
                for( Element subElement : element.getEnclosedElements() ) {
                    for(AnnotationMirror mirror : subElement.getAnnotationMirrors()) {
                        for( Element annotation : supportedAnnotations ) {
                            if( mirror.getAnnotationType().asElement().equals(annotation) ) {
                                typeElements.add((TypeElement) element);
                                found = true;
                                break;
                            }
                        }
                        if(found) break;
                    }
                    if(found) break;
                }
            }
        }
        return typeElements;
    }



}
