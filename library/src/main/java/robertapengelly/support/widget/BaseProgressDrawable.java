package robertapengelly.support.widget;

abstract class BaseProgressDrawable extends BasePaintDrawable implements IntrinsicPaddingDrawable {

    boolean mUseIntrinsicPadding = true;
    
    /** {@inheritDoc} */
    @Override
    public boolean getUseIntrinsicPadding() {
        return mUseIntrinsicPadding;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
    
        if (mUseIntrinsicPadding != useIntrinsicPadding) {
        
            mUseIntrinsicPadding = useIntrinsicPadding;
            invalidateSelf();
        
        }
    
    }

}