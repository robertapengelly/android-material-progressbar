# android-material-progressbar

Add a Material ProgressBar on pre-lollipop devices. Supports Android 2.3 API 9 (GINGERBREAD) and up.

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
	        compile 'com.github.robertapengelly:android-material-progressbar:1.0.0'
	}

Implementation:

    Create a layout file with a MaterialProgressBar widget (layout/activity_main.xml)
    
        <?xml version="1.0" encoding="utf-8" ?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/activity_main"
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            android:orientation="vertical"
            android:padding="10dp">
            
            <robertapengelly.support.widget.MaterialProgressBar
                android:indeterminate="true"
                android:layout_height="wrap_content"
                android:layout_width="match_parent"
                app:mpb_progressStyle="horizontal"
                style="@style/Widget.MaterialProgressBar.ProgressBar.Horizontal" />
        
        </LinearLayout>

Use Material ProgressBar without the Android Support Library

    Add colorControlActivated to your style (values/styles.xml)
    
        <style name="Theme" parent="@android:style/Theme.NoTitleBar">
            <item name="colorControlActivated">?attr/colorAccent</item>
        </style>

Use Material ProgressBar with the Android Support Library

    Add colorControlActivated to your style (values/styles.xml)
    
        <style name="Theme" parent="Theme.AppCompat.NoActionBar">
            <item name="colorAccent">@color/colorAccent</item>
        </style>
