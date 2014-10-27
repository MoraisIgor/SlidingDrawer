![1] SlidingDrawer
==================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SlidingDrawer-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/765)

This widget has ported and improved from the `Android Open Source Project`.

SlidingDrawer hides content out of the screen and allows the user to drag a handle to bring the content on screen.
SlidingDrawer can be used vertically or horizontally.

A special widget composed of two children views: the handle, that the users drags, and the content, attached to the handle and dragged with it. SlidingDrawer should be used as an overlay inside layouts. This means SlidingDrawer should only be used inside of a `FrameLayout` or a `RelativeLayout` for instance.

The size of the SlidingDrawer defines how much space the content will occupy once slid out so SlidingDrawer should usually use `match_parent` for both its dimensions.

Inside a XML layout, SlidingDrawer must define the id of the handle and of the content.

![2] &nbsp; ![3]

See more here [SlidingDrawer](http://developer.android.com/reference/android/widget/SlidingDrawer.html).


Setup
-----

You can clone this repository like this.

`git clone https://github.com/MoraisIgor/SlidingDrawer.git`

And add it to your project reference.

For more detailed information, look here [Referencing a library project](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject).


### Gradle build

To install the sample application to your device run the following task:

```
$ ./gradlew installDebug
```

To deploy the library to your local Maven repository run the following task:

```
$ ./gradlew install
```

Then, to use the library in your project add the following to your `build.gradle`:

```groovy
dependencies {
    compile 'com.hollowsoft.library:slidingdrawer:1.3.4'
}
```


Example
-------

##### In The Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/holo_red_dark"
    tools:context=".view.MainScreen" >

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="@string/activity"
        android:textColor="@android:color/white"
        android:textSize="25sp" />

    <com.hollowsoft.library.slidingdrawer.SlidingDrawer
        android:id="@+id/sliding_drawer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:content="@+id/drawer_content"
        app:handle="@+id/drawer_handle" >

        <TextView
            android:id="@+id/drawer_handle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/holo_blue_dark"
            android:gravity="center"
            android:paddingBottom="15dp"
            android:paddingTop="15dp"
            android:text="@string/handle"
            android:textColor="@android:color/white"
            android:textSize="25sp" />

        <TextView
            android:id="@+id/drawer_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_green_dark"
            android:gravity="center"
            android:text="@string/content"
            android:textColor="@android:color/white"
            android:textSize="25sp" />
    </com.hollowsoft.library.slidingdrawer.SlidingDrawer>

</RelativeLayout>
```

##### In The Code

```java
public final class MainScreen extends Activity implements OnDrawerScrollListener, OnDrawerOpenListener,
		OnDrawerCloseListener {

	/**
	 *
	 */
	private static final String TAG = "SlidingDrawer";

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);

		setContentView(R.layout.main_screen);

		final SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.sliding_drawer);

		slidingDrawer.setOnDrawerScrollListener(this);
		slidingDrawer.setOnDrawerOpenListener(this);
		slidingDrawer.setOnDrawerCloseListener(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hollowsoft.library.slidingdrawer.OnDrawerScrollListener#onScrollStarted
	 * ()
	 */
	@Override
	public void onScrollStarted() {
		Log.i(TAG, "onScrollStarted()");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hollowsoft.library.slidingdrawer.OnDrawerScrollListener#onScrollEnded
	 * ()
	 */
	@Override
	public void onScrollEnded() {
		Log.i(TAG, "onScrollEnded()");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hollowsoft.library.slidingdrawer.OnDrawerOpenListener#onDrawerOpened
	 * ()
	 */
	@Override
	public void onDrawerOpened() {
		Log.i(TAG, "onDrawerOpened()");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hollowsoft.library.slidingdrawer.OnDrawerCloseListener#onDrawerClosed
	 * ()
	 */
	@Override
	public void onDrawerClosed() {
		Log.i(TAG, "onDrawerClosed()");
	}
}
```

For more detailed information, look at sample application.


Thanks
------

Awesome tools for code quality improvement:

* [Android Lint](http://developer.android.com/tools/help/lint.html)
* [Checkstyle](http://checkstyle.sourceforge.net)
* [FindBugs](http://findbugs.sourceforge.net)
* [PMD](http://pmd.sourceforge.net)


Contact
-------

* [GitHub Page](http://moraisigor.github.io)
* [Home Page](http://igormorais.com)


License
-------

```
    Copyright (c) 2014 HollowSoft @IgorMorais

    Licensed under the Apache License, Version 2.0 (the “License”);
    you may not use this file except in compliance with the License.

         You may obtain a copy of the License at
         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an “AS IS” BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```

```
    Copyright (C) 2008 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the “License”);
    you may not use this file except in compliance with the License.

         You may obtain a copy of the License at
         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an “AS IS” BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```


[1]: https://raw.githubusercontent.com/MoraisIgor/SlidingDrawer/master/raw/android.png
[2]: https://raw.githubusercontent.com/MoraisIgor/SlidingDrawer/master/raw/1.png
[3]: https://raw.githubusercontent.com/MoraisIgor/SlidingDrawer/master/raw/2.png
