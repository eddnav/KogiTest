package com.nav.kogi.test.shared.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author Eduardo Naveda
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForFragment {
}
