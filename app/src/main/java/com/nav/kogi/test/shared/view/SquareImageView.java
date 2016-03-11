package com.nav.kogi.test.shared.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Eduardo Naveda
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(width, width);
    }

}
