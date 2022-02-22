package Util;

import Models.AbstractEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOneRelation {
    String remoteField();
    Class<? extends AbstractEntity> targetClass();
}
