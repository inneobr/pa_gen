package br.coop.integrada.api.pa.aplication.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ComparaObjeto {
    public boolean verifica() default true;    
    public String nome() default "";
}
