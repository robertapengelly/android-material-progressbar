package robertapengelly.support.widget;

import  android.content.res.ColorStateList;
import  android.graphics.Canvas;
import  android.graphics.Color;
import  android.graphics.ColorFilter;
import  android.graphics.PixelFormat;
import  android.graphics.PorterDuff;
import  android.graphics.PorterDuffColorFilter;
import  android.graphics.Rect;
import  android.graphics.drawable.Drawable;

abstract class BaseDrawable extends Drawable implements TintableDrawable {

    private ColorFilter mColorFilter;
    private PorterDuffColorFilter mTintFilter;
    private ColorStateList mTintList;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
    
    private DummyConstantState mConstantState = new DummyConstantState();
    
    int mAlpha = 0xFF;
    
    /** {@inheritDoc} */
    @Override
    public void draw(Canvas canvas) {
    
        Rect bounds = getBounds();
        
        if ((bounds.height() == 0) || (bounds.width() == 0))
            return;
        
        int saveCount = canvas.save();
        canvas.translate(bounds.left, bounds.top);
        
        onDraw(canvas, bounds.width(), bounds.height());
        canvas.restoreToCount(saveCount);
    
    }
    
    @Override
    public int getAlpha() {
        return mAlpha;
    }
    
    /** {@inheritDoc} */
    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }
    
    ColorFilter getColorFilterForDrawing() {
        return ((mColorFilter != null) ? mColorFilter : mTintFilter);
    }
    
    // Workaround LayerDrawable.ChildDrawable which calls getConstantState().newDrawable()
    // without checking for null.
    // We are never inflated from XML so the protocol of ConstantState does not apply to us. In
    // order to make LayerDrawable happy, we return ourselves from DummyConstantState.newDrawable().
    
    @Override
    public ConstantState getConstantState() {
        return mConstantState;
    }
    
    /** {@inheritDoc} */
    @Override
    public int getOpacity() {
        // Be safe.
        return PixelFormat.TRANSLUCENT;
    }
    
    @Override
    public boolean isStateful() {
        return ((mTintList != null) && mTintList.isStateful());
    }
    
    abstract void onDraw(Canvas canvas, int width, int height);
    
    @Override
    protected boolean onStateChange(int[] state) {
        return updateTintFilter();
    }
    
    /** {@inheritDoc} */
    @Override
    public void setAlpha(int alpha) {
    
        if (mAlpha != alpha) {
        
            mAlpha = alpha;
            invalidateSelf();
        
        }
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    
        mColorFilter = colorFilter;
        invalidateSelf();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }
    
    /** {@inheritDoc} */
    @Override
    public void setTintList(ColorStateList tint) {
    
        mTintList = tint;
        
        if (updateTintFilter())
            invalidateSelf();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
    
        mTintMode = tintMode;
        
        if (updateTintFilter())
            invalidateSelf();
    
    }
    
    private boolean updateTintFilter() {
    
        if ((mTintList == null) || (mTintMode == null)) {
        
            boolean hadTintFilter = mTintFilter != null;
            mTintFilter = null;
            
            return hadTintFilter;
        
        }
        
        int tintColor = mTintList.getColorForState(getState(), Color.TRANSPARENT);
        
        // They made PorterDuffColorFilter.setColor() and setMode() @hide.
        mTintFilter = new PorterDuffColorFilter(tintColor, mTintMode);
        return true;
    
    }
    
    private class DummyConstantState extends ConstantState {
    
        @Override
        public int getChangingConfigurations() {
            return 0;
        }
        
        @Override
        public Drawable newDrawable() {
            return BaseDrawable.this;
        }
    
    }

}