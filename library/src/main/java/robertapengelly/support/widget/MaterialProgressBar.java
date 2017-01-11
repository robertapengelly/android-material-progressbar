package robertapengelly.support.widget;

import  android.annotation.SuppressLint;
import  android.annotation.TargetApi;
import  android.content.Context;
import  android.content.res.ColorStateList;
import  android.graphics.PorterDuff;
import  android.graphics.drawable.Drawable;
import  android.graphics.drawable.LayerDrawable;
import  android.os.Build;
import  android.util.AttributeSet;
import  android.util.Log;
import  android.widget.ProgressBar;

import  robertapengelly.support.materialprogressbar.R;
import  robertapengelly.support.materialprogressbar.widget.TintTypedArray;

/** A {@link ProgressBar} subclass that handles tasks related to backported progress drawable. */
public class MaterialProgressBar extends ProgressBar {

    private static final String TAG = MaterialProgressBar.class.getSimpleName();
    
    public static final int PROGRESS_STYLE_CIRCULAR = 0;
    public static final int PROGRESS_STYLE_HORIZONTAL = 1;
    
    private int mProgressStyle;
    
    private TintInfo mProgressTintInfo = new TintInfo();
    
    public MaterialProgressBar(Context context) {
        super(context);
        
        init(context, null, 0, 0);
    
    }
    
    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context, attrs, 0, 0);
    
    }
    
    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        init(context, attrs, defStyleAttr, 0);
    
    }
    
    @TargetApi(21)
    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        init(context, attrs, defStyleAttr, defStyleRes);
    
    }
    
    private void applyIndeterminateTint() {
    
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        
        if (indeterminateDrawable == null)
            return;
        
        if (mProgressTintInfo.mHasIndeterminateTint || mProgressTintInfo.mHasIndeterminateTintMode) {
        
            indeterminateDrawable.mutate();
            applyTintForDrawable(indeterminateDrawable, mProgressTintInfo.mIndeterminateTint,
                mProgressTintInfo.mHasIndeterminateTint, mProgressTintInfo.mIndeterminateTintMode,
                    mProgressTintInfo.mHasIndeterminateTintMode);
        
        }
    
    }
    
    private void applyPrimaryProgressTint() {
    
        if (getProgressDrawable() == null)
            return;
        
        if (mProgressTintInfo.mHasProgressTint || mProgressTintInfo.mHasProgressTintMode) {
        
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.progress, true);
            
            if (target != null)
                applyTintForDrawable(target, mProgressTintInfo.mProgressTint,
                    mProgressTintInfo.mHasProgressTint, mProgressTintInfo.mProgressTintMode,
                        mProgressTintInfo.mHasProgressTintMode);
        
        }
    
    }
    
    private void applyProgressBackgroundTint() {
    
        if (getProgressDrawable() == null)
            return;
        
        if (mProgressTintInfo.mHasProgressBackgroundTint || mProgressTintInfo.mHasProgressBackgroundTintMode) {
        
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.background, false);
            
            if (target != null)
                applyTintForDrawable(target, mProgressTintInfo.mProgressBackgroundTint,
                    mProgressTintInfo.mHasProgressBackgroundTint, mProgressTintInfo.mProgressBackgroundTintMode,
                        mProgressTintInfo.mHasProgressBackgroundTintMode);
        
        }
    
    }
    
    private void applyProgressTints() {
    
        if (getProgressDrawable() == null)
            return;
        
        applyPrimaryProgressTint();
        applyProgressBackgroundTint();
        applySecondaryProgressTint();
    
    }
    
    private void applySecondaryProgressTint() {
    
        if (getProgressDrawable() == null)
            return;
        
        if (mProgressTintInfo.mHasSecondaryProgressTint || mProgressTintInfo.mHasSecondaryProgressTintMode) {
        
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress, false);
            
            if (target != null)
                applyTintForDrawable(target, mProgressTintInfo.mSecondaryProgressTint,
                    mProgressTintInfo.mHasSecondaryProgressTint, mProgressTintInfo.mSecondaryProgressTintMode,
                        mProgressTintInfo.mHasSecondaryProgressTintMode);
        
        }
    
    }
    
    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, ColorStateList tint, boolean hasTint,
        PorterDuff.Mode tintMode, boolean hasTintMode) {
        
        if (hasTint || hasTintMode) {
        
            if (hasTint) {
            
                if (drawable instanceof TintableDrawable)
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintList(tint);
                else {
                
                    Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        drawable.setTintList(tint);
                
                }
            
            }
            
            if (hasTintMode) {
            
                if (drawable instanceof TintableDrawable)
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintMode(tintMode);
                else {
                
                    Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        drawable.setTintMode(tintMode);
                
                }
            
            }
            
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful())
                drawable.setState(getDrawableState());
        
        }
    
    }
    
    private void fixCanvasScalingWhenHardwareAccelerated() {
    
        if (Build.VERSION.SDK_INT < 18)
            if (Build.VERSION.SDK_INT >= 11)
                // Canvas scaling when hardware accelerated results in artifacts on older API levels, so
                // we need to use software rendering
                if (isHardwareAccelerated() && getLayerType() != LAYER_TYPE_SOFTWARE)
                    setLayerType(LAYER_TYPE_SOFTWARE, null);
    
    }
    
    /**
     * Get the current drawable of this ProgressBar.
     *
     * @return The current drawable.
     */
    public Drawable getCurrentDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }
    
    /** {@inheritDoc} */
    @Override
    public ColorStateList getIndeterminateTintList() {
        return mProgressTintInfo.mIndeterminateTint;
    }
    
    /** {@inheritDoc} */
    @Override
    public PorterDuff.Mode getIndeterminateTintMode() {
        return mProgressTintInfo.mIndeterminateTintMode;
    }
    
    /** {@inheritDoc} */
    @Override
    public ColorStateList getProgressBackgroundTintList() {
        return mProgressTintInfo.mProgressBackgroundTint;
    }
    
    /** {@inheritDoc} */
    @Override
    public PorterDuff.Mode getProgressBackgroundTintMode() {
        return mProgressTintInfo.mProgressBackgroundTintMode;
    }
    
    /**
     * Get the progress style of this ProgressBar.
     *
     * @return The progress style.
     */
    public int getProgressStyle() {
        return mProgressStyle;
    }
    
    /** {@inheritDoc} */
    @Override
    public ColorStateList getProgressTintList() {
        return mProgressTintInfo.mProgressTint;
    }
    
    /** {@inheritDoc} */
    @Override
    public PorterDuff.Mode getProgressTintMode() {
        return mProgressTintInfo.mProgressTintMode;
    }
    
    /** {@inheritDoc} */
    @Override
    public ColorStateList getSecondaryProgressTintList() {
        return mProgressTintInfo.mSecondaryProgressTint;
    }
    
    /** {@inheritDoc} */
    @Override
    public PorterDuff.Mode getSecondaryProgressTintMode() {
        return mProgressTintInfo.mSecondaryProgressTintMode;
    }
    
    /**
     * Get whether the current drawable is showing a background. The default is {@code true}.
     *
     * @return Whether the current drawable is showing a background, or {@code false} if the
     *         drawable does not implement {@link ShowBackgroundDrawable}.
     */
    public boolean getShowProgressBackground() {
    
        Drawable drawable = getCurrentDrawable();
        
        if (drawable instanceof ShowBackgroundDrawable)
            return ((ShowBackgroundDrawable) drawable).getShowBackground();
        else
            return false;
    
    }
    
    private Drawable getTintTargetFromProgressDrawable(int layerId, boolean shouldFallback) {
    
        Drawable progressDrawable = getProgressDrawable();
        
        if (progressDrawable == null)
            return null;
        
        
        progressDrawable.mutate();
        Drawable layerDrawable = null;
        
        if (progressDrawable instanceof LayerDrawable)
            layerDrawable = ((LayerDrawable) progressDrawable).findDrawableByLayerId(layerId);
        
        if (layerDrawable == null && shouldFallback)
            layerDrawable = progressDrawable;
        
        return layerDrawable;
    
    }
    
    /**
     * Get whether the current drawable is using an intrinsic padding. The default is {@code true}.
     *
     * @return Whether the current drawable is using an intrinsic padding.
     * @throws IllegalStateException If the current drawable does not implement {@link IntrinsicPaddingDrawable}.
     */
    public boolean getUseIntrinsicPadding() {
    
        Drawable drawable = getCurrentDrawable();
        
        if (drawable instanceof IntrinsicPaddingDrawable)
            return ((IntrinsicPaddingDrawable) drawable).getUseIntrinsicPadding();
        else
            throw new IllegalStateException("Drawable does not implement IntrinsicPaddingDrawable");
    
    }
    
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
            R.styleable.MaterialProgressBar, defStyleAttr, defStyleRes);
        
        mProgressStyle = a.getInt(R.styleable.MaterialProgressBar_mpb_progressStyle, PROGRESS_STYLE_CIRCULAR);
        
        boolean setBothDrawables = a.getBoolean(R.styleable.MaterialProgressBar_mpb_setBothDrawables, false);
        boolean useIntrinsicPadding = a.getBoolean(R.styleable.MaterialProgressBar_mpb_useIntrinsicPadding, true);
        
        boolean showProgressBackground = a.getBoolean(R.styleable.MaterialProgressBar_mpb_showProgressBackground,
            (mProgressStyle == PROGRESS_STYLE_HORIZONTAL));
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTint)) {
        
            mProgressTintInfo.mHasIndeterminateTint = true;
            mProgressTintInfo.mIndeterminateTint = a.getColorStateList(
                R.styleable.MaterialProgressBar_mpb_indeterminateTint);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTintMode)) {
        
            mProgressTintInfo.mHasIndeterminateTintMode = true;
            mProgressTintInfo.mIndeterminateTintMode = DrawableCompat.parseTintMode(a.getInt(
                R.styleable.MaterialProgressBar_mpb_indeterminateTintMode, -1), null);
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTint)) {
        
            mProgressTintInfo.mHasProgressBackgroundTint = true;
            mProgressTintInfo.mProgressBackgroundTint = a.getColorStateList(
                R.styleable.MaterialProgressBar_mpb_progressBackgroundTint);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode)) {
        
            mProgressTintInfo.mHasProgressBackgroundTintMode = true;
            mProgressTintInfo.mProgressBackgroundTintMode = DrawableCompat.parseTintMode(a.getInt(
                R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode, -1), null);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTint)) {
        
            mProgressTintInfo.mHasProgressTint = true;
            mProgressTintInfo.mProgressTint = a.getColorStateList(
                R.styleable.MaterialProgressBar_mpb_progressTint);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTintMode)) {
        
            mProgressTintInfo.mHasProgressTintMode = true;
            mProgressTintInfo.mProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                R.styleable.MaterialProgressBar_mpb_progressTintMode, -1), null);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTint)) {
        
            mProgressTintInfo.mHasSecondaryProgressTint = true;
            mProgressTintInfo.mSecondaryProgressTint = a.getColorStateList(
                R.styleable.MaterialProgressBar_mpb_secondaryProgressTint);
        
        }
        
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode)) {
        
            mProgressTintInfo.mHasSecondaryProgressTintMode = true;
            mProgressTintInfo.mSecondaryProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode, -1), null);
        
        }
        
        a.recycle();

        switch (mProgressStyle) {
        
            case PROGRESS_STYLE_CIRCULAR:
                if (!isIndeterminate() || setBothDrawables)
                    throw new UnsupportedOperationException("Determinate circular drawable is not yet supported");
                else if (!isInEditMode())
                    setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
                
                break;
            case PROGRESS_STYLE_HORIZONTAL:
                if (isIndeterminate() || setBothDrawables)
                    if (!isInEditMode())
                        setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(context));
                
                if (!isIndeterminate() || setBothDrawables)
                    setProgressDrawable(new HorizontalProgressDrawable(context));
                
                break;
            default:
                throw new IllegalArgumentException("Unknown progress style: " + mProgressStyle);
        
        }
        
        setShowProgressBackground(showProgressBackground);
        setUseIntrinsicPadding(useIntrinsicPadding);
    
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        // isHardwareAccelerated() only works when attached to a window.
        fixCanvasScalingWhenHardwareAccelerated();
    
    }
    
    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);
        
        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null)
            applyIndeterminateTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setIndeterminateTintList(ColorStateList tint) {
    
        mProgressTintInfo.mIndeterminateTint = tint;
        mProgressTintInfo.mHasIndeterminateTint = true;
        
        applyIndeterminateTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setIndeterminateTintMode(PorterDuff.Mode tintMode) {
    
        mProgressTintInfo.mIndeterminateTintMode = tintMode;
        mProgressTintInfo.mHasIndeterminateTintMode = true;
        
        applyIndeterminateTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setProgressBackgroundTintList(ColorStateList tint) {
    
        mProgressTintInfo.mProgressBackgroundTint = tint;
        mProgressTintInfo.mHasProgressBackgroundTint = true;
        
        applyProgressBackgroundTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setProgressBackgroundTintMode(PorterDuff.Mode tintMode) {
    
        mProgressTintInfo.mProgressBackgroundTintMode = tintMode;
        mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        
        applyProgressBackgroundTint();
    
    }
    
    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
        
        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null)
            applyProgressTints();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setProgressTintList(ColorStateList tint) {
    
        mProgressTintInfo.mProgressTint = tint;
        mProgressTintInfo.mHasProgressTint = true;
        
        applyPrimaryProgressTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setProgressTintMode(PorterDuff.Mode tintMode) {
    
        mProgressTintInfo.mProgressTintMode = tintMode;
        mProgressTintInfo.mHasProgressTintMode = true;
        
        applyPrimaryProgressTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setSecondaryProgressTintList(ColorStateList tint) {
    
        mProgressTintInfo.mSecondaryProgressTint = tint;
        mProgressTintInfo.mHasSecondaryProgressTint = true;
        
        applySecondaryProgressTint();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setSecondaryProgressTintMode(PorterDuff.Mode tintMode) {
    
        mProgressTintInfo.mSecondaryProgressTintMode = tintMode;
        mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        
        applySecondaryProgressTint();
    
    }
    
    /**
     * Set whether the current drawable should show a background. The default is {@code true}.
     *
     * @param show Whether background should be shown. When {@code false}, does nothing if the
     *             progress drawable does not implement {@link ShowBackgroundDrawable}, otherwise a
     *             {@link IllegalStateException} is thrown.
     * @throws IllegalStateException If {@code show} is {@code true} but the current drawable
     *         does not implement {@link ShowBackgroundDrawable}.
     */
    public void setShowProgressBackground(boolean show) {
    
        Drawable drawable = getCurrentDrawable();
        
        if (drawable instanceof ShowBackgroundDrawable)
            ((ShowBackgroundDrawable) drawable).setShowBackground(show);
        
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        
        if (indeterminateDrawable instanceof ShowBackgroundDrawable)
            ((ShowBackgroundDrawable) indeterminateDrawable).setShowBackground(show);
    
    }
    
    /**
     * Set whether the current drawable should use an intrinsic padding. The default is
     * {@code true}.
     *
     * @param useIntrinsicPadding Whether the current drawable should use its intrinsic padding.
     * @throws IllegalStateException If the current drawable does not implement {@link IntrinsicPaddingDrawable}.
     */
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
    
        Drawable drawable = getCurrentDrawable();
        
        if (drawable instanceof IntrinsicPaddingDrawable)
            ((IntrinsicPaddingDrawable) drawable).setUseIntrinsicPadding(useIntrinsicPadding);
        
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        
        if (indeterminateDrawable instanceof IntrinsicPaddingDrawable)
            ((IntrinsicPaddingDrawable) indeterminateDrawable).setUseIntrinsicPadding(useIntrinsicPadding);
    
    }
    
    private static class TintInfo {
    
        ColorStateList mProgressTint;
        PorterDuff.Mode mProgressTintMode;
        boolean mHasProgressTint;
        boolean mHasProgressTintMode;
        
        ColorStateList mSecondaryProgressTint;
        PorterDuff.Mode mSecondaryProgressTintMode;
        boolean mHasSecondaryProgressTint;
        boolean mHasSecondaryProgressTintMode;
        
        ColorStateList mProgressBackgroundTint;
        PorterDuff.Mode mProgressBackgroundTintMode;
        boolean mHasProgressBackgroundTint;
        boolean mHasProgressBackgroundTintMode;
        
        ColorStateList mIndeterminateTint;
        PorterDuff.Mode mIndeterminateTintMode;
        boolean mHasIndeterminateTint;
        boolean mHasIndeterminateTintMode;
    
    }

}