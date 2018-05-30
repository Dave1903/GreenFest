package com.thedevlopershome.greenfest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;




public class SliderCard extends RecyclerView.ViewHolder implements DecodeBitmapTask.Listener {

    private static int viewWidth = 0;
    private static int viewHeight = 0;
    Context context;
    private final ImageView imageView;

    private DecodeBitmapTask task;

    public SliderCard(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    void setContent(Context context, final String resId) {
        this.context=context;
        if (viewWidth == 0) {
            itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    viewWidth = itemView.getWidth();
                    viewHeight = itemView.getHeight();
                    loadBitmap(resId);
                }
            });
        } else {
            loadBitmap(resId);
        }
    }

    void clearContent() {
        if (task != null) {
            task.cancel(true);
        }
    }

    private void loadBitmap(String resId) {
        task = new DecodeBitmapTask(context,itemView.getResources(), resId, viewWidth, viewHeight, this);
        task.execute();
    }

    @Override
    public void onPostExecuted(Bitmap bitmap)   {

        imageView.setImageBitmap(bitmap);
    }

}