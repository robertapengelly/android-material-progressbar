package robertapengelly.support.widget;

import  android.content.Context;
import  android.graphics.Canvas;

class HorizontalProgressBackgroundDrawable extends BaseSingleHorizontalProgressDrawable implements ShowBackgroundDrawable {

    private boolean mShow = true;
    
    HorizontalProgressBackgroundDrawable(Context context) {
        super(context);
    }
    
    @Override
    public void draw(Canvas canvas) {
    
        if (mShow)
            super.draw(canvas);
    
    }
    
    @Override
    public boolean getShowBackground() {
        return mShow;
    }
    
    @Override
    public void setShowBackground(boolean show) {
    
        if (mShow != show) {
        
            mShow = show;
            invalidateSelf();
        
        }
    
    }

}