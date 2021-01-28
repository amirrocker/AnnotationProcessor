## Annotation Processor

Overview:
A simple Annotation Processor for binding views like with Butterknife.
The code is from a tutorial I found on medium. Anyway it sounded very interesting
and I decided to try it out. And here we are. At the moment the bindView annotation
works correctly. I have not yet found the time to find out whether or not the other
annotations really work.
WIP - work in progress repo

Descrioption:
 Test project based on a medium based tutorial on how to roll your own annotation processor.
 See: https://medium.com/@aitorvs/annotation-processing-in-android-studio-7042ccb83024
 Since I can't get the first setup to work I'll keep researching to find a working solution.
 I have found this article:
 https://blog.mindorks.com/android-annotation-processing-tutorial-part-1-a-practical-approach
 where the second part sets up a project structure to implement a Butterknife like processor.
 So lets see if I can solve my problem...
 Update:
 Yes, the new gradle plugin has annotionProcessor implemented so we do not need to use
 "apt" anymore. Instead we can access the TypeElements from the gradle build process and
 even debug the compilation process from inside our IDE.

 Since annotation processing runs in its own separate environment there is only work arounds to
 communicate with the application by other means such as the debugger.
 See:
 https://www.w3ma.com/how-to-debug-an-annotation-processor-in-android-studio/
 also other resources may help...
 https://stablekernel.com/article/the-10-step-guide-to-annotation-processing-in-android-studio/

 Also we need to introduce a square based library called JavaPoet. It is a lib that allows for
 easier type creation. A very good introduction other than the actual documentation is here:
 https://www.baeldung.com/java-poet and https://github.com/square/javapoet

 Using javapoet a new file is created called MainActivity$Binding where the code implementation
 will live. Say a new activity named MapActivity would lead to a MapActivity$Binding class.
 Basic usage of JavaPoet can be found here: https://github.com/square/javapoet
 FunFact: Square has some awesome libraries! Look over them it may be worth your while.

 In Short JavaPoet allows:
 - TypeSpec.Builder => define the class schema
 - addModifiers(modifier) => add private, public or protected accesss
 - addAnnotation => add an annotation to the element.
 - TypeSpec.Builder -> addMethod => adds methods and constructor to class
 - MethodSpec -> addParameter => add parameter type for a method.
 - MethodSpec -> addStatement => defines the statements inside the methods. First define placeholders then
 fill them with values.

WIP