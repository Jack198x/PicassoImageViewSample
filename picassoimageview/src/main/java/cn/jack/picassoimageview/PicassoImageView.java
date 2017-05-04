package cn.jack.picassoimageview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

import cn.jack.picassoimageview.transformation.BlurTransformation;
import cn.jack.picassoimageview.transformation.RoundedTransformationBuilder;

/**
 * Created by Jack on 2017/5/2.
 */

@SuppressLint("AppCompatCustomView")
public class PicassoImageView extends ImageView {
    private RequestCreator requestCreator;
    private int widthPixel = 0;
    private int heightPixel = 0;
    private int holderResId = 0;
    private int errorResId = 0;
    private Transformation transformation = null;
    private int transformationType = 0;
    private int cornerRadius = 0;
    private int borderWidth = 0;
    private int borderColor = Color.parseColor("#FFFFFF");
    private int blurRadius = 1;
    private int blurSampling = 1;
    private boolean loadWithNoCache = false;


    public PicassoImageView(Context context) {
        super(context);
        init(context, null);
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
            heightPixel = getMeasuredHeight();
            widthPixel = getMeasuredWidth();
            if (requestCreator != null) {
                into();
            }
        }
    }


    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PicassoImageView);
            try {
                if (typedArray.hasValue(R.styleable.PicassoImageView_imageResource)) {
                    int resourceId = typedArray.getResourceId(R.styleable.PicassoImageView_imageResource, 0);
                    requestCreator = Picasso.with(getContext()).load(resourceId);
                }
                if (typedArray.hasValue(R.styleable.PicassoImageView_placeholderImageResource)) {
                    holderResId = typedArray.getResourceId(R.styleable.PicassoImageView_placeholderImageResource, 0);
                }
                if (typedArray.hasValue(R.styleable.PicassoImageView_errorImageResource)) {
                    errorResId = typedArray.getResourceId(R.styleable.PicassoImageView_errorImageResource, 0);
                }
                if (typedArray.hasValue(R.styleable.PicassoImageView_loadWithNoCache)) {
                    loadWithNoCache = typedArray.getBoolean(R.styleable.PicassoImageView_loadWithNoCache, false);
                }

                if (typedArray.hasValue(R.styleable.PicassoImageView_transformation)) {
                    transformationType = typedArray.getInt(R.styleable.PicassoImageView_transformation, 0);
                }
                if (transformationType == 0) {
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderWidth)) {
                        borderWidth = typedArray.getDimensionPixelSize(R.styleable.PicassoImageView_borderWidth, 0);
                    }
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderColor)) {
                        borderColor = typedArray.getColor(R.styleable.PicassoImageView_borderColor, Color.parseColor("#FFFFFF"));
                    }
                    transformation = new RoundedTransformationBuilder()
                            .borderColor(borderColor)
                            .borderWidth(borderWidth)
                            .oval(false)
                            .build();
                }
                if (transformationType == 1) {
                    //round
                    if (typedArray.hasValue(R.styleable.PicassoImageView_roundedCornerRadius)) {
                        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.PicassoImageView_roundedCornerRadius, 0);
                    }
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderWidth)) {
                        borderWidth = typedArray.getDimensionPixelSize(R.styleable.PicassoImageView_borderWidth, 0);
                    }
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderColor)) {
                        borderColor = typedArray.getColor(R.styleable.PicassoImageView_borderColor, Color.parseColor("#FFFFFF"));
                    }
                    transformation = new RoundedTransformationBuilder()
                            .borderColor(borderColor)
                            .cornerRadius(cornerRadius)
                            .borderWidth(borderWidth)
                            .oval(false)
                            .build();

                }
                if (transformationType == 2) {
                    //oval
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderWidth)) {
                        borderWidth = typedArray.getDimensionPixelSize(R.styleable.PicassoImageView_borderWidth, 0);
                    }
                    if (typedArray.hasValue(R.styleable.PicassoImageView_borderColor)) {
                        borderColor = typedArray.getColor(R.styleable.PicassoImageView_borderColor, Color.parseColor("#FFFFFF"));
                    }
                    transformation = new RoundedTransformationBuilder()
                            .borderColor(borderColor)
                            .borderWidth(borderWidth)
                            .oval(true)
                            .build();
                }
                if (transformationType == 3) {
                    //blur
                    if (typedArray.hasValue(R.styleable.PicassoImageView_blurRadius)) {
                        blurRadius = typedArray.getInt(R.styleable.PicassoImageView_blurRadius, 1);
                    }
                    if (typedArray.hasValue(R.styleable.PicassoImageView_blurSampling)) {
                        blurSampling = typedArray.getInt(R.styleable.PicassoImageView_blurSampling, 1);
                    }
                    transformation = new BlurTransformation(getContext(), blurRadius, blurSampling);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public void setPicassoImageResource(@DrawableRes int resId) {
        requestCreator = Picasso.with(getContext()).load(resId);
        into();
    }

    public void setPicassoImageResource(@DrawableRes int resId, @DrawableRes int holderResId, @DrawableRes int errorResId) {
        requestCreator = Picasso.with(getContext()).load(resId);
        this.holderResId = holderResId;
        this.errorResId = errorResId;
        into();
    }

    public void setPicassoImageFile(File file) {
        requestCreator = Picasso.with(getContext()).load(file);
        into();
    }

    public void setPicassoImageFile(File file, @DrawableRes int holderResId, @DrawableRes int errorResId) {
        requestCreator = Picasso.with(getContext()).load(file);
        this.holderResId = holderResId;
        this.errorResId = errorResId;
        into();
    }

    public void setPicassoImageUri(Uri uri) {
        requestCreator = Picasso.with(getContext()).load(uri);
        into();
    }

    public void setPicassoImageUri(Uri uri, @DrawableRes int holderResId, @DrawableRes int errorResId) {
        requestCreator = Picasso.with(getContext()).load(uri);
        this.holderResId = holderResId;
        this.errorResId = errorResId;
        into();
    }

    public void setPicassoImageUrl(String url) {
        requestCreator = Picasso.with(getContext()).load(TextUtils.isEmpty(url) ? "http://????" : url);
        into();
    }

    public void setPicassoImageUrl(String url, @DrawableRes int holderResId, @DrawableRes int errorResId) {
        requestCreator = Picasso.with(getContext()).load(url);
        this.holderResId = holderResId;
        this.errorResId = errorResId;
        into();
    }


    private void into() {
        updateRequestCreator(requestCreator);
        if (heightPixel > 0 && widthPixel > 0) {
            requestCreator.resize(widthPixel, heightPixel).onlyScaleDown().into(this);
        } else {
            requestCreator.into(this);
        }
    }


    private void updateRequestCreator(RequestCreator creator) {
        if (creator == null) {
            return;
        }
        if (loadWithNoCache) {
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        }
        if (transformation != null) {
            creator.transform(transformation);
        }
        if (holderResId > 0) {
            creator.placeholder(holderResId);
        }
        if (errorResId > 0) {
            creator.error(errorResId);
        }
    }


}
