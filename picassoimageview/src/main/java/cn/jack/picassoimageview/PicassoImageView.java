package cn.jack.picassoimageview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    private static final int PENDING_SIZE = 0;
    int SIZE_ORIGINAL = Integer.MIN_VALUE;
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
    private boolean noFade = false;


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
                if (typedArray.hasValue(R.styleable.PicassoImageView_noFade)) {
                    noFade = typedArray.getBoolean(R.styleable.PicassoImageView_noFade, false);
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
        getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
    }

    private ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            widthPixel = getTargetWidth();
            heightPixel = getTargetHeight();
            if (isViewStateAndSizeValid(widthPixel, heightPixel)) {
                Log.e("getMeasuredHeight", heightPixel + "");
                Log.e("getMeasuredWidth", widthPixel + "");
                if (requestCreator != null) {
                    into();
                }
                getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
            }
            return true;
        }
    };


    private boolean isViewStateAndSizeValid(int width, int height) {
        return isViewStateValid() && isSizeValid(width) && isSizeValid(height);
    }

    private boolean isViewStateValid() {
        // We consider the view state as valid if the view has
        // non-null layout params and a non-zero layout width and height.
        if (getLayoutParams() != null
                && getLayoutParams().width > 0
                && getLayoutParams().height > 0) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return isLaidOut();
        }
        return !isLayoutRequested();
    }

    private boolean isSizeValid(int size) {
        return size > 0 || size == SIZE_ORIGINAL;
    }

    private int getTargetHeight() {
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int layoutParamSize = layoutParams != null ? layoutParams.height : PENDING_SIZE;
        return getTargetDimen(getHeight(), layoutParamSize, verticalPadding);
    }

    private int getTargetWidth() {
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int layoutParamSize = layoutParams != null ? layoutParams.width : PENDING_SIZE;
        return getTargetDimen(getWidth(), layoutParamSize, horizontalPadding);
    }

    private int getTargetDimen(int viewSize, int paramSize, int paddingSize) {
        int adjustedViewSize = viewSize - paddingSize;
        if (isSizeValid(adjustedViewSize)) {
            return adjustedViewSize;
        }

        if (paramSize == PENDING_SIZE) {
            return PENDING_SIZE;
        }

        if (paramSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return SIZE_ORIGINAL;
        } else if (paramSize > 0) {
            return paramSize - paddingSize;
        } else {
            return PENDING_SIZE;
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
            creator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        }
        if (noFade) {
            creator.noFade();
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
        creator.config(Bitmap.Config.RGB_565);
    }


}
