package com.ciklum.gitlum.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This custom annotation is used to inform the developer, that the class, decorated with {@link com.ciklum.gitlum.annotations.UseCase},
 * represents an implementation of the <a href="https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html">use case design pattern</a>.
 * The annotation is alias for the {@link org.springframework.stereotype.Component} stereotype annotation and
 * should be used only on classes, which must be managed by the Spring Context.
 * @author Ventsislav Stoevski
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface UseCase {
	@AliasFor(annotation = Component.class)
	String value() default "";
}