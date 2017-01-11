# android-material-progressbar

Add a ripple drawable to your view on pre-lollipop devices. Supports Android 2.3 API 9 (GINGERBREAD) and up.
For more information about ripple drawables visit https://developer.android.com/reference/android/graphics/drawable/RippleDrawable.html

Usage:

    Step 1. Add the JitPack repository to your build file
    
    Add it in your root build.gradle at the end of repositories:
    
    allprojects {
		  repositories {
			  ...
			  maven { url 'https://jitpack.io' }
		  }
	}
    
    Step 2. Add the dependency
    
    dependencies {
	        compile 'com.github.robertapengelly:android-ripple-drawable:1.0.4'
	}

Basic Implementation:

    Create a ripple drawable file (drawable/ripple.xml)
    
        <?xml version="1.0" encoding="utf-8" ?>
        <ripple xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            android:color="@color/ripple_material_dark"
            tools:targetApi="lollipop"> <!-- Optional to suppress the API warning. -->
            
            <item>
                <shape android:shape="rectangle">
                    <corners android:radius="5dp" />
                    <solid android:color="#424242" />
                    <stroke android:color="#bdbdbd" android:width="1dp" />
                </shape>
            </item>
        
        </ripple>
    
    Add the imports to your class
    
        import robertapengelly.support.graphics.drawable.LollipopDrawable;
        import robertapengelly.support.graphics.drawable.LollipopDrawablesCompat;
        import robertapengelly.support.view.DrawableHotspotTouch;
    
    Add the ripple as your views background
    
        View view = findViewById(R.id.your_view);
        view.setBackgroundDrawable(LollipopDrawablesCompat.getDrawable(getResources(), R.drawable.ripple, getTheme()));
        view.setClickable(true);
        view.setOnClickListener(this);
        view.setOnTouchListener(new DrawableHotspotTouch((LollipopDrawable) view.getBackground()));
