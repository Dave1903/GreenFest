package com.thedevlopershome.greenfest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;


import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DecodeBitmapTask extends AsyncTask<Void, Void, Bitmap> {

    private   Context context;
    private final BackgroundBitmapCache cache;
    private final Resources resources;
    private final String bitmapResId;
    private final  int reqWidth;
    private final  int reqHeight;
    private final Reference<Listener> refListener;

    public interface Listener {
        void onPostExecuted(Bitmap bitmap) ;
    }

    public DecodeBitmapTask(Context context, Resources resources, String bitmapResId,
                            int reqWidth, int reqHeight,
                            @NonNull Listener listener)
    {
        this.context=context;
        this.cache = BackgroundBitmapCache.getInstance();
        this.resources = resources;
        this.bitmapResId = bitmapResId;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
        this.refListener = new WeakReference<>(listener);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap cachedBitmap = getBitmapFromUrl(bitmapResId);
        if (cachedBitmap != null) {
           return cachedBitmap;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        final int width = options.outWidth;
        final int height = options.outHeight;

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth
                    && !isCancelled() )
            {
                inSampleSize *= 2;
            }
        }

        if (isCancelled()) {
            return null;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap decodedBitmap;

        try {
             URL url=new URL(bitmapResId);
             HttpURLConnection connection=(HttpURLConnection)url.openConnection();
             connection.connect();
             InputStream inputStream=connection.getInputStream();
             decodedBitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException e){
            decodedBitmap=  BitmapFactory.decodeResource(resources,R.drawable.nit_raipur,options);
        }
        final Bitmap result;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            result = getRoundedCornerBitmap(decodedBitmap,
                    resources.getDimension(R.dimen.card_corner_radius), reqWidth, reqHeight);
            decodedBitmap.recycle();
        } else {
            result = decodedBitmap;
        }

       // cache.addBitmapToBgMemoryCache(Integer.valueOf(bitmapResId), result);
        return result;
    }

    @Override
    final protected void onPostExecute(Bitmap bitmap) {
        final Listener listener = this.refListener.get();
        if (listener != null) {
            listener.onPostExecuted(bitmap);

        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float pixels, int width, int height) {
        final Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int sourceWidth = bitmap.getWidth();
        final int sourceHeight = bitmap.getHeight();

        float xScale = (float) width / bitmap.getWidth();
        float yScale = (float) height / bitmap.getHeight();
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        float left = (width - scaledWidth) / 2;
        float top = (height - scaledHeight) / 2;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        final RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, targetRect, paint);

        return output;
    }

    public   Bitmap getBitmapFromUrl(String src) {
        try {
            return Glide.with(context).load(src).asBitmap().into(reqWidth,reqHeight).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}