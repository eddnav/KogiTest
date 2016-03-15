package com.nav.kogi.test.shared.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author Eduardo Naveda
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface Fragments {
}
