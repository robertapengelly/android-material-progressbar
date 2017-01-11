package robertapengelly.support.widget;

import  android.content.Context;
import  android.graphics.Canvas;
import  android.graphics.drawable.Animatable;
import  android.os.Build;

import  robertapengelly.support.animation.Animator;
import  robertapengelly.support.materialprogressbar.R;

abstract class BaseIndeterminateProgressDrawable extends BaseProgressDrawable implements Animatable {

    Animator[] mAnimators;
    
    BaseIndeterminateProgressDrawable(Context context) {
    
        int colorControlActivated = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated, context);
        
        if ((colorControlActivated == 0) && Build.VERSION.SDK_INT >= 21)
            colorControlActivated = ThemeUtils.getColorFromAttrRes(android.R.attr.colorControlActivated, context);
        
        // setTint() has been overridden for compatibility; DrawableCompat won't work because
        // wrapped Drawable won't be Animatable.
        setTint(colorControlActivated);
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (isStarted())
            invalidateSelf();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
    
        for (Animator animator : mAnimators)
            if (animator.isRunning())
                return true;
        
        return false;
    
    }
    
    private boolean isStarted() {
    
        for (Animator animator : mAnimators)
            if (animator.isStarted())
                return true;
        
        return false;
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void start() {
    
        if (isStarted())
            return;
        
        for (Animator animator : mAnimators)
            animator.start();
        
        invalidateSelf();
    
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
    
        for (Animator animator : mAnimators)
            animator.end();
    
    }

}