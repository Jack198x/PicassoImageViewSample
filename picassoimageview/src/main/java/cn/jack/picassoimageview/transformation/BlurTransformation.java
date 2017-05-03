package cn.jack.picassoimageview.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;
import android.util.Log;

import com.squareup.picasso.Transformation;

/**
 * Created by Jack on 2016/9/21.
 */

public class BlurTransformation implements Transformation {

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private Context mContext;

    private int mRadius;
    private int mSampling;

    public BlurTransformation(Context context) {
        this(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, int radius) {
        this(context, radius, DEFAULT_DOWN_SAMPLING);
    }

    public BlurTransformation(Context context, int radius, int sampling) {
        mContext = context.getApplicationContext();
        mRadius = radius;
        mSampling = sampling;
    }

    @Override
    public Bitmap transform(Bitmap source) {


        int scaledWidth = source.getWidth() / mSampling;
        int scaledHeight = source.getHeight() / mSampling;

        Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.e("Build.VERSION.SDK_INT",Build.VERSION.SDK_INT+"");
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius);
            } catch (RSRuntimeException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "BlurTransformation(radius=" + mRadius + ", sampling=" + mSampling + ")";
    }
}
