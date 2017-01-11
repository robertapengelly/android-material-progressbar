package robertapengelly.support.materialprogressbar.widget;

import  android.content.Context;
import  android.content.res.ColorStateList;
import  android.content.res.Resources;
import  android.content.res.TypedArray;
import  android.graphics.drawable.Drawable;
import  android.os.Build;
import  android.util.AttributeSet;
import  android.util.TypedValue;

import  robertapengelly.support.materialprogressbar.content.res.ResourcesCompat;

/** A class that wraps a {@link android.content.res.TypedArray} and provides the same public API surface. */
public class TintTypedArray {

    private final Context mContext;
    private final TypedArray mWrapped;
    
    private TypedValue mTypedValue;
    
    public static TintTypedArray obtainStyledAttributes(Context context, int resid, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(resid, attrs));
    }
    
    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set,
        int[] attrs, int defStyleAttr, int defStyleRes) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes));
    }

    private TintTypedArray(Context context, TypedArray array) {
    
        mContext = context;
        mWrapped = array;
    
    }
    
    public boolean getBoolean(int index, boolean defValue) {
        return mWrapped.getBoolean(index, defValue);
    }
    
    public ColorStateList getColorStateList(int index) {
    
        if (mWrapped.hasValue(index)) {
        
            final int resourceId = mWrapped.getResourceId(index, 0);
            
            if (resourceId != 0) {
            
                final ColorStateList value = ResourcesCompat.getColorStateList(mContext, resourceId);
                
                if (value != null)
                    return value;
            
            }
        
        }
        
        return mWrapped.getColorStateList(index);
    
    }
    
    public int getInt(int index, int defValue) {
        return mWrapped.getInt(index, defValue);
    }
    
    public boolean hasValue(int index) {
        return mWrapped.hasValue(index);
    }
    
    public void recycle() {
        mWrapped.recycle();
    }

}