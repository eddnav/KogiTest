package com.nav.kogi.test.gallery;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Since RxJava base schedulers can't be reset (as RxAndroid's via {@link RxAndroidPlugins#reset()}), overriding them through this class
 * ensures that tests that imply their use won't crash by initializing them multiple times.
 * <p/>
 * Note: after RxJava initializes the Schedulers class, overriding the schedulers won't be possible anymore. If another test initializes them
 * before this class overrides them, tests will most likely not work as expected as they will be using the default schedulers.
 *
 * @author Eduardo Naveda
 */
public class SchedulerOverrider {

    private static boolean overridden = false;

    public static void withImmediate() {
        if (!overridden) {
            try {
                RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
                    @Override
                    public Scheduler getIOScheduler() {
                        return Schedulers.immediate();
                    }

                    @Override
                    public Scheduler getComputationScheduler() {
                        return Schedulers.immediate();
                    }

                    @Override
                    public Scheduler getNewThreadScheduler() {
                        return Schedulers.immediate();
                    }
                });
                overridden = true;
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Schedulers class already initialized: always override schedulers with overrideWith()");
            }
        }
    }

}