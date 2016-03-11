package com.nav.kogi.test.shared.util;

import android.content.Context;
import android.os.Build;

/**
 * @author Eduardo Naveda
 */
public class AndroidUtil {

    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resId);
        } else
            //noinspection deprecation
            return context.getResources().getColor(resId);
    }

}
