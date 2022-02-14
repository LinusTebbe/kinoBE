package Util;

import Database.AbstractRepository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    String name();
    Class<? extends AbstractRepository<?>> repository();
}
