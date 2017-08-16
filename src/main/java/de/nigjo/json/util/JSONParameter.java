package de.nigjo.json.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface JSONParameter
{
  public String name() default "";

  public boolean requiered() default false;

  public boolean nullable() default false;

}
