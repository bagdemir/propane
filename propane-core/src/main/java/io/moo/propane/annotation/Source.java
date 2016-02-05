package io.moo.propane.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.moo.propane.extractors.DefaultComponentIdExtractor;
import io.moo.propane.extractors.DefaultContextExtractor;
import io.moo.propane.extractors.TokenExtractor;

/**
 * Created by bagdemir on 25/03/15.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Source {
  String url() default "";
  Class<? extends TokenExtractor> contextExtractor() default DefaultContextExtractor.class;
  Class<? extends TokenExtractor> componentIdExtractor() default DefaultComponentIdExtractor.class;
}
