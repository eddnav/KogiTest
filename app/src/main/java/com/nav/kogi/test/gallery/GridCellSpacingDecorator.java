package com.nav.kogi.test.gallery;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds spacing decorations around the cells of a RecycleView, suitable when using {@link GridLayoutManager}
 *
 * @author Eduardo Naveda
 */
public class GridCellSpacingDecorator extends RecyclerView.ItemDecoration {

    private int spacing;

    public GridCellSpacingDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int span = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

        int position = parent.getChildAdapterPosition(view);
        int column = position % span;

        outRect.left = spacing - column * spacing / span;
        outRect.right = (column + 1) * spacing / span;

        if (position < span) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing;
    }

}