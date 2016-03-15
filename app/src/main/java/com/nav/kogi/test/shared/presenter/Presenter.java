package com.nav.kogi.test.shared.presenter;

/**
 * @author Eduardo Naveda
 */
public interface Presenter<T> {

    void takeView(T view);

    void dropView();

}
