![1] EnhancedSlidingDrawer
==========================

This is a fork from [Igor Morais SlidingDrawer project](https://github.com/MoraisIgor/SlidingDrawer).

SlidingDrawer hides content out of the screen and allows the user to drag a handle to bring the content on screen. SlidingDrawer can be used vertically or horizontally.

A special widget composed of two children views: the handle, that the users drags, and the content, attached to the handle and dragged with it.

SlidingDrawer should be used as an overlay inside layouts. This means SlidingDrawer should only be used inside of a FrameLayout or a RelativeLayout for instance.

The size of the SlidingDrawer defines how much space the content will occupy once slid out so SlidingDrawer should usually use `match_parent` for both its dimensions.

Have a look at the example application, it shows examples of vertical and horizontal sliding drawers as well as an attempt to mimic the iOS drawers.


Setup
-----

Put a new build rule on your `gradle.build` file. For example:

```
dependencies {
    
    compile 'com.github.moraisigor:slidingdrawer:1.5.5'
}
```

Example
-------

##### In Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              xmlns:drawer="http://schemas.android.com/apk/res-auto"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <hollowsoft.slidingdrawer.SlidingDrawer
        android:id="@+id/drawer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        drawer:handle="@+id/handle"
        drawer:content="@+id/content">
        
        <TextView
            android:id="@+id/handle"
            android:layout_width="match_parent"
            android:layout_height="75dp"
            android:background="#01579B"
            android:gravity="center"
            android:text="Handle"/>
            
        <TextView
            android:id="@+id/content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="#00ACC1"
            android:gravity="center"
            android:text="Content"/>
    
    </hollowsoft.slidingdrawer.SlidingDrawer>
    
</LinearLayout>
```

Set the width (or height) of the SlidingDrawer to `wrap_content` if you want it to deploy only as necessary to show the content (vs. completely up or left).

If you want the handle to change while depending on the drawer status (moving, closed, open), use a state drawable.
The state of the handle view is set to `activated` while the drawer moves and to `selected` when the slider if fully opened.


##### In Code

```java
public class DrawerActivity extends Activity implements SlidingDrawerListener {
                                                
    private static final String TAG = "DrawerActivity";
    
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        setContentView(R.layout.drawer_activity);
        
        final SlidingDrawer drawer = (SlidingDrawer) view.findViewById(R.id.drawer);
        
        drawer.setSlidingDrawerListener(this);
    }
    
    @Override
    public void onDrawerOpened() {
        Logger.logInfo(TAG, "Drawer is opened");
    }

    @Override
    public void onDrawerWillOpen() {
        Logger.logInfo(TAG, "Drawer will open");
    }

    @Override
    public void onDrawerClosed() {
        Logger.logInfo(TAG, "Drawer is closed");
    }

    @Override
    public void onDrawerWilClose() {
        Logger.logInfo(TAG, "Drawer will close");
    }

    @Override
    public void onScrollStarted() {
        Logger.logInfo(TAG, "Scroll Started");
    }

    @Override
    public void onScrollEnded() {
        Logger.logInfo(TAG, "Scroll Ended");
    }
}
```



Contact
-------

* [Igor Morais](http://igormorais.com) :)
* [Iron Bird Software](http://www.ironbirdsoftware.com)


License
-------

```
Copyright (C) 2016 IronBird Software - Jean-Michel Cazaux
Copyright 2014 Igor Morais
Copyright (C) 2008 The Android Open Source Project
    
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: https://raw.githubusercontent.com/MoraisIgor/SlidingDrawer/master/Asset/Icon.png
