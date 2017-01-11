package robertapengelly.support.materialprogressbar.content.res;

import  android.content.Context;
import  android.content.res.ColorStateList;
import  android.content.res.Configuration;
import  android.content.res.Resources;
import  android.graphics.drawable.Drawable;
import  android.os.Build;
import  android.util.Log;
import  android.util.SparseArray;
import  android.util.TypedValue;

import  java.util.WeakHashMap;

import  org.xmlpull.v1.XmlPullParser;

/** Class for accessing an application's resources through AppCompat, and thus any backward compatible functionality. */
public final class ResourcesCompat {

    private static final String LOG_TAG = "ResourcesCompat";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    
    private static final Object sColorStateCacheLock = new Object();
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<>(0);
    
    private ResourcesCompat() {}
    
    private static void addColorStateListToCache(Context context, int resId, ColorStateList value) {
    
        synchronized (sColorStateCacheLock) {
        
            SparseArray<ColorStateListCacheEntry> entries = sColorStateCaches.get(context);
            
            if (entries == null) {
            
                entries = new SparseArray<>();
                sColorStateCaches.put(context, entries);
            
            }
            
            entries.append(resId, new ColorStateListCacheEntry(value, context.getResources().getConfiguration()));
        
        }
    
    }
    
    private static ColorStateList getCachedColorStateList(Context context, int resId) {
    
        synchronized (sColorStateCacheLock) {
        
            final SparseArray<ColorStateListCacheEntry> entries = sColorStateCaches.get(context);
            
            if ((entries != null) && (entries.size() > 0)) {
            
                final ColorStateListCacheEntry entry = entries.get(resId);
                
                if (entry != null) {
                
                    if (entry.configuration.equals(context.getResources().getConfiguration()))
                        // If the current configuration matches the entry's, we can use it
                        return entry.value;
                    else
                        // Otherwise we'll remove the entry
                        entries.remove(resId);
                
                }
            
            }
        
        }
        
        return null;
    
    }
    
    /**
     * Returns the {@link ColorStateList} from the given resource. The resource can include
     * themeable attributes, regardless of API level.
     *
     * @param context context to inflate against
     * @param resId   the resource identifier of the ColorStateList to retrieve
     */
    public static ColorStateList getColorStateList(Context context, int resId) {
    
        if (Build.VERSION.SDK_INT >= 23)
            // On M+ we can use the framework
            return context.getColorStateList(resId);
        
        // Before that, we'll try handle it ourselves
        ColorStateList csl = getCachedColorStateList(context, resId);
        
        if (csl != null)
            return csl;
        
        // Cache miss, so try and inflate it ourselves
        csl = inflateColorStateList(context, resId);
        
        if (csl != null) {
        
            // If we inflated it, add it to the cache and return
            addColorStateListToCache(context, resId, csl);
            return csl;
        
        }
        
        // If we reach here then we couldn't inflate it, so let the framework handle it
        //noinspection deprecation
        return context.getResources().getColorStateList(resId);
    
    }
    
    private static TypedValue getTypedValue() {
    
        TypedValue tv = TL_TYPED_VALUE.get();
        
        if (tv == null) {
        
            tv = new TypedValue();
            TL_TYPED_VALUE.set(tv);
        
        }
        
        return tv;
    
    }
    
    /** Inflates a {@link ColorStateList} from resources, honouring theme attributes. */
    private static ColorStateList inflateColorStateList(Context context, int resId) {
    
        if (isColorInt(context, resId))
            // The resource is a color int, we can't handle it so return null
            return null;
        
        final Resources r = context.getResources();
        final XmlPullParser xml = r.getXml(resId);
        
        try {
            return ColorStateListInflater.createFromXml(r, xml, context.getTheme());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to inflate ColorStateList, leaving it to the framework", e);
        }
        
        return null;
    
    }
    
    private static boolean isColorInt(Context context, int resId) {
    
        final Resources r = context.getResources();
        
        final TypedValue value = getTypedValue();
        r.getValue(resId, value, true);
        
        return ((value.type >= TypedValue.TYPE_FIRST_COLOR_INT) && (value.type <= TypedValue.TYPE_LAST_COLOR_INT));
    
    }
    
    private static class ColorStateListCacheEntry {
    
        final Configuration configuration;
        final ColorStateList value;
        
        ColorStateListCacheEntry(ColorStateList value, Configuration configuration) {
        
            this.configuration = configuration;
            this.value = value;
        
        }
    
    }

}