package robertapengelly.support.widget;

import  android.content.Context;
import  android.content.res.TypedArray;

class ThemeUtils {

    private ThemeUtils() {}
    
    static int getColorFromAttrRes(int attr, Context context) {
    
        TypedArray a = context.obtainStyledAttributes(new int[] {attr});
        
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    
    }
    
    static float getFloatFromAttrRes(int attrRes, Context context) {
    
        TypedArray a = context.obtainStyledAttributes(new int[] {attrRes});
        
        try {
            return a.getFloat(0, 0);
        } finally {
            a.recycle();
        }
    
    }

}