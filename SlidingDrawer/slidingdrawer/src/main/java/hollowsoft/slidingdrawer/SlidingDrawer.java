/*
 * Copyright 2014 Igor Morais
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hollowsoft.slidingdrawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * <p> SlidingDrawer hides content out of the screen and allows the user to drag a handle
 * to bring the content on screen. SlidingDrawer can be used vertically or horizontally. </p>
 *
 * <p> A special widget composed of two children views: the handle, that the users drags,
 * and the content, attached to the handle and dragged with it. </p>
 *
 * <p> SlidingDrawer should be used as an overlay inside layouts. This means SlidingDrawer
 * should only be used inside of a FrameLayout or a RelativeLayout for instance. </p>
 *
 * <p> The size of the SlidingDrawer defines how much space the content will occupy once slid
 * out so SlidingDrawer should usually use match_parent for both its dimensions. </p>
 *
 * <p> Inside an XML layout, SlidingDrawer must define the id of the handle and of the
 * content: </p>
 *
 * <pre class="prettyprint">
 * &lt;hollowsoft.slidingdrawer.SlidingDrawer
 *     android:id="@+id/drawer"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:handle="@+id/handle"
 *     android:content="@+id/content"&gt;
 *
 * &lt;ImageView
 *     android:id="@id/handle"
 *     android:layout_width="88dip"
 *     android:layout_height="44dip"/&gt;
 *
 * &lt;GridView
 *     android:id="@id/content"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"/&gt;
 *
 * &lt;/hollowsoft.slidingdrawer.SlidingDrawer&gt;
 * </pre>
 *
 * @author Igor Morais
 */
public class SlidingDrawer extends ViewGroup {

    private static final int TAP_THRESHOLD = 6;
    private static final float MAX_TAP_VELOCITY = 100.0f;
    private static final float MAX_MINOR_VELOCITY = 150.0f;
    private static final float MAX_MAJOR_VELOCITY = 200.0f;
    private static final float MAX_ACCELERATION = 2000.0f;
    private static final int VELOCITY_UNITS = 1000;

    private static final int ANIMATION_FRAME_DURATION = 1000 / 60;

    private static final int DRAWER_EXPANDED = 501;
    private static final int DRAWER_COLLAPSED = 502;

    private static final int ORIENTATION_VERTICAL = 1;

    private int tapThreshold;
    private int maxTapVelocity;
    private int maxMinorVelocity;
    private int maxMajorVelocity;
    private int maxAcceleration;
    private int velocityUnits;

    private float animationAcceleration;
    private float animationVelocity;
    private float animationPosition;
    private long animationLastTime;
    private int touchDelta;

    private VelocityTracker velocityTracker;

    private final Rect rectFrame = new Rect();
    private final Rect rectInvalidate = new Rect();

    private boolean tracking;
    private boolean animating;

    private boolean locked;
    private boolean expanded;

    private int handleWidth;
    private int handleHeight;

    /**
     * Styleable.
     */
    private boolean animateOnClick;
    private boolean allowSingleTap;
    private int topOffset;
    private int bottomOffset;
    private boolean vertical;
    private int handleId;
    private int contentId;

    private View viewHandle;
    private View viewContent;

    private OnDrawerOpenListener onDrawerOpenListener;
    private OnDrawerCloseListener onDrawerCloseListener;
    private OnDrawerScrollListener onDrawerScrollListener;

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context The applications environment.
     * @param attributeSet The attributes defined in XML.
     */
    public SlidingDrawer(final Context context, final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context The applications environment.
     * @param attributeSet The attributes defined in XML.
     * @param defStyleAttr An attribute in the current theme that contains a reference
     *        to a style resource that supplies default values for the view.
     *        Can be 0 to not look for defaults.
     */
    public SlidingDrawer(final Context context, final AttributeSet attributeSet, final int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        loadStyleable(context, attributeSet, defStyleAttr, 0);
    }

    /**
     * Creates a new SlidingDrawer from a specified set of attributes defined in XML.
     *
     * @param context The applications environment.
     * @param attributeSet The attributes defined in XML.
     * @param defStyleAttr An attribute in the current theme that contains a reference
     *        to a style resource that supplies default values for the view.
     *        Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that supplies
     *        default values for the view, used only if defStyleAttr is 0
     *        or can not be found in the theme. Can be 0 to not look for defaults.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingDrawer(final Context context, final AttributeSet attributeSet, final int defStyleAttr, final int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);

        loadStyleable(context, attributeSet, defStyleAttr, defStyleRes);
    }

    private void loadStyleable(final Context context, final AttributeSet attributeSet, final int defStyleAttr,
                               final int defStyleRes) {

        final TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
                                                                     R.styleable.SlidingDrawer,
                                                                     defStyleAttr,
                                                                     defStyleRes);

        animateOnClick = typedArray.getBoolean(R.styleable.SlidingDrawer_animateOnClick, true);
        allowSingleTap = typedArray.getBoolean(R.styleable.SlidingDrawer_allowSingleTap, true);
        topOffset = (int) typedArray.getDimension(R.styleable.SlidingDrawer_topOffset, 0.0f);
        bottomOffset = (int) typedArray.getDimension(R.styleable.SlidingDrawer_bottomOffset, 0.0f);

        vertical = typedArray.getInt(R.styleable.SlidingDrawer_android_orientation, ORIENTATION_VERTICAL) == ORIENTATION_VERTICAL;

        handleId = typedArray.getResourceId(R.styleable.SlidingDrawer_handle, 0);

        if (handleId == 0) {
            throw new IllegalArgumentException("The handle attribute is required and must refer to a valid child.");
        }

        contentId = typedArray.getResourceId(R.styleable.SlidingDrawer_content, 0);

        if (contentId == 0) {
            throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
        }

        if (handleId == contentId) {
            throw new IllegalArgumentException("The content and handle attributes must refer to different children.");
        }

        typedArray.recycle();

        final float density = getResources().getDisplayMetrics().density;

        tapThreshold = (int) (TAP_THRESHOLD * density + 0.5f);
        maxTapVelocity = (int) (MAX_TAP_VELOCITY * density + 0.5f);
        maxMinorVelocity = (int) (MAX_MINOR_VELOCITY * density + 0.5f);
        maxMajorVelocity = (int) (MAX_MAJOR_VELOCITY * density + 0.5f);
        maxAcceleration = (int) (MAX_ACCELERATION * density + 0.5f);
        velocityUnits = (int) (VELOCITY_UNITS * density + 0.5f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        viewHandle = findViewById(handleId);

        if (viewHandle == null) {
            throw new IllegalArgumentException("The handle attribute is must refer to an existing child.");
        }

        viewHandle.setOnClickListener(new DrawerToggle());

        viewContent = findViewById(contentId);

        if (viewContent == null) {
            throw new IllegalArgumentException("The content attribute is must refer to an existing child.");
        }

        viewContent.setVisibility(View.GONE);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        final int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("The Drawer cannot have unspecified dimensions.");
        }

        measureChild(viewHandle, widthMeasureSpec, heightMeasureSpec);

        if (vertical) {

            final int height = heightSpecSize - viewHandle.getMeasuredHeight() - topOffset;

            viewContent.measure(MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.EXACTLY),
                                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        } else {

            final int width = widthSpecSize - viewHandle.getMeasuredWidth() - topOffset;

            viewContent.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                                MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {

        if (!tracking) {

            final int width = right - left;
            final int height = bottom - top;

            int childLeft;
            int childTop;

            int childWidth = viewHandle.getMeasuredWidth();
            int childHeight = viewHandle.getMeasuredHeight();

            if (vertical) {

                childLeft = (width - childWidth) / 2;
                childTop = expanded ? topOffset : height - childHeight + bottomOffset;

                viewContent.layout(0,
                                   topOffset + childHeight,
                                   viewContent.getMeasuredWidth(),
                                   topOffset + childHeight + viewContent.getMeasuredHeight());

            } else {

                childLeft = expanded ? topOffset : width - childWidth + bottomOffset;
                childTop = (height - childHeight) / 2;

                viewContent.layout(topOffset + childWidth,
                                   0,
                                   topOffset + childWidth + viewContent.getMeasuredWidth(),
                                   viewContent.getMeasuredHeight());
            }

            viewHandle.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

            handleWidth = viewHandle.getWidth();
            handleHeight = viewHandle.getHeight();
        }
    }

    @Override
    protected void dispatchDraw(@NonNull final Canvas canvas) {
        final long drawingTime = getDrawingTime();

        drawChild(canvas, viewHandle, drawingTime);

        if (tracking || animating) {

            final Bitmap bitmap = viewContent.getDrawingCache();

            if (bitmap == null) {

                canvas.save();

                canvas.translate(vertical ? 0 : viewHandle.getLeft() - topOffset,
                                 vertical ? viewHandle.getTop() - topOffset : 0);

                drawChild(canvas, viewContent, drawingTime);

                canvas.restore();

            } else {

                if (vertical) {
                    canvas.drawBitmap(bitmap, 0, viewHandle.getBottom(), null);

                } else {
                    canvas.drawBitmap(bitmap, viewHandle.getRight(), 0, null);
                }
            }

        } else if (expanded) {

            drawChild(canvas, viewContent, drawingTime);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {

        if (locked) {
            return false;
        }

        final int action = event.getAction();

        final float x = event.getX();
        final float y = event.getY();

        viewHandle.getHitRect(rectFrame);

        if (!tracking && !rectFrame.contains((int) x, (int) y)) {
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {

            tracking = true;

            viewHandle.setPressed(true);

            prepareContent();

            if (onDrawerScrollListener != null) {
                onDrawerScrollListener.onScrollStarted();
            }

            if (vertical) {

                final int top = viewHandle.getTop();

                touchDelta = (int) y - top;

                prepareTracking(top);

            } else {

                final int left = viewHandle.getLeft();

                touchDelta = (int) x - left;

                prepareTracking(left);
            }

            velocityTracker.addMovement(event);
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {

        if (locked) {
            return true;
        }

        if (tracking) {

            velocityTracker.addMovement(event);

            final int action = event.getAction();

            switch (action) {

                case MotionEvent.ACTION_MOVE:

                    moveHandle((int) (vertical ? event.getY() : event.getX()) - touchDelta);

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    velocityTracker.computeCurrentVelocity(velocityUnits);

                    float xVelocity = velocityTracker.getXVelocity();
                    float yVelocity = velocityTracker.getYVelocity();

                    boolean negative;

                    if (vertical) {

                        negative = yVelocity < 0;

                        if (xVelocity < 0) {
                            xVelocity = -xVelocity;
                        }

                        if (xVelocity > maxMinorVelocity) {
                            xVelocity = maxMinorVelocity;
                        }

                    } else {

                        negative = xVelocity < 0;

                        if (yVelocity < 0) {
                            yVelocity = -yVelocity;
                        }

                        if (yVelocity > maxMinorVelocity) {
                            yVelocity = maxMinorVelocity;
                        }
                    }

                    float velocity = (float) Math.hypot(xVelocity, yVelocity);

                    if (negative) {
                        velocity = -velocity;
                    }

                    final int top = viewHandle.getTop();
                    final int left = viewHandle.getLeft();

                    if (Math.abs(velocity) < maxTapVelocity) {

                        if (vertical ? (expanded && top < tapThreshold + topOffset) || (!expanded && top > bottomOffset + getBottom() - getTop() - handleHeight - tapThreshold) :
                                       (expanded && left < tapThreshold + topOffset) || (!expanded && left > bottomOffset + getRight() - getLeft() - handleWidth - tapThreshold)) {

                            if (allowSingleTap) {

                                playSoundEffect(SoundEffectConstants.CLICK);

                                if (expanded) {
                                    animateClose(vertical ? top : left);

                                } else {
                                    animateOpen(vertical ? top : left);
                                }

                            } else {

                                performFling(vertical ? top : left, velocity, false);
                            }

                        } else {

                            performFling(vertical ? top : left, velocity, false);
                        }

                    } else {

                        performFling(vertical ? top : left, velocity, false);
                    }

                    break;
            }
        }

        return tracking || animating || super.onTouchEvent(event);
    }

    private void animateOpen(final int position) {
        prepareTracking(position);

        performFling(position, -maxAcceleration, true);
    }

    private void animateClose(final int position) {
        prepareTracking(position);

        performFling(position, maxAcceleration, true);
    }

    private void prepareTracking(final int position) {

        tracking = true;

        velocityTracker = VelocityTracker.obtain();

        if (expanded) {

            if (animating) {
                animating = false;

                removeCallbacks(handler);
            }

            moveHandle(position);

        } else {

            animationAcceleration = maxAcceleration;
            animationVelocity = maxMajorVelocity;
            animationPosition = bottomOffset + (vertical ? getHeight() - handleHeight : getWidth() - handleWidth);

            moveHandle((int) animationPosition);

            animating = true;

            removeCallbacks(handler);

            animationLastTime = SystemClock.uptimeMillis();

            animating = true;
        }
    }

    private void stopTracking() {

        viewHandle.setPressed(false);

        tracking = false;

        if (onDrawerScrollListener != null) {
            onDrawerScrollListener.onScrollEnded();
        }

        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
    }

    private void performFling(final int position, final float velocity, final boolean always) {

        animationVelocity = velocity;
        animationPosition = position;

        if (expanded) {

            if (always || (velocity > maxMajorVelocity || (position > topOffset + (vertical ? handleHeight : handleWidth) && velocity > -maxMajorVelocity))) {

                animationAcceleration = maxAcceleration;

                if (velocity < 0) {
                    animationVelocity = 0;
                }

            } else {

                animationAcceleration = -maxAcceleration;

                if (velocity > 0) {
                    animationVelocity = 0;
                }
            }

        } else {

            if (!always && (velocity > maxMajorVelocity || (position > (vertical ? getHeight() : getWidth()) / 2 && velocity > -maxMajorVelocity))) {

                animationAcceleration = maxAcceleration;

                if (velocity < 0) {
                    animationVelocity = 0;
                }

            } else {

                animationAcceleration = -maxAcceleration;

                if (velocity > 0) {
                    animationVelocity = 0;
                }
            }
        }

        animationLastTime = SystemClock.uptimeMillis();

        animating = true;

        removeCallbacks(handler);

        postDelayed(handler, ANIMATION_FRAME_DURATION);

        stopTracking();
    }

    private void incrementAnimation() {

        final long now = SystemClock.uptimeMillis();

        final float time = (now - animationLastTime) / 1000.0f;

        final float acceleration = animationAcceleration;
        final float velocity = animationVelocity;
        final float position = animationPosition;

        animationVelocity = velocity + (acceleration * time);
        animationPosition = position + (velocity * time) + (0.5f * acceleration * time * time);

        animationLastTime = now;
    }

    private void doAnimation() {

        if (animating) {

            incrementAnimation();

            if (animationPosition >= bottomOffset + (vertical ? getHeight() : getWidth()) - 1) {
                animating = false;

                closeDrawer();

            } else if (animationPosition < topOffset) {
                animating = false;

                openDrawer();

            } else {

                moveHandle((int) animationPosition);

                postDelayed(handler, ANIMATION_FRAME_DURATION);
            }
        }
    }

    private void prepareContent() {

        if (!animating) {

            if (viewContent.isLayoutRequested()) {

                if (vertical) {

                    final int height = getBottom() - getTop() - handleHeight - topOffset;

                    viewContent.measure(MeasureSpec.makeMeasureSpec(getRight() - getLeft(), MeasureSpec.EXACTLY),
                                        MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

                    viewContent.layout(0,
                                       topOffset + handleHeight,
                                       viewContent.getMeasuredWidth(),
                                       topOffset + handleHeight + viewContent.getMeasuredHeight());

                } else {

                    final int childWidth = viewHandle.getWidth();

                    final int width = getRight() - getLeft() - childWidth - topOffset;

                    viewContent.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                                        MeasureSpec.makeMeasureSpec(getBottom() - getTop(), MeasureSpec.EXACTLY));

                    viewContent.layout(childWidth + topOffset,
                                       0,
                                       topOffset + childWidth + viewContent.getMeasuredWidth(),
                                       viewContent.getMeasuredHeight());
                }
            }

            viewContent.getViewTreeObserver().dispatchOnPreDraw();

            if (!viewContent.isHardwareAccelerated()) {
                viewContent.buildDrawingCache();
            }

            viewContent.setVisibility(View.GONE);
        }
    }

    private void openDrawer() {
        moveHandle(DRAWER_EXPANDED);

        viewContent.setVisibility(View.VISIBLE);

        if (!expanded) {
            expanded = true;

            if (onDrawerOpenListener != null) {
                onDrawerOpenListener.onDrawerOpened();
            }
        }
    }

    private void closeDrawer() {
        moveHandle(DRAWER_COLLAPSED);

        viewContent.setVisibility(View.GONE);
        viewContent.destroyDrawingCache();

        if (expanded) {
            expanded = false;

            if (onDrawerCloseListener != null) {
                onDrawerCloseListener.onDrawerClosed();
            }
        }
    }

    private void moveHandle(final int position) {

        if (vertical) {

            if (position == DRAWER_EXPANDED) {
                viewHandle.offsetTopAndBottom(topOffset - viewHandle.getTop());

                invalidate();

            } else if (position == DRAWER_COLLAPSED) {
                viewHandle.offsetTopAndBottom(bottomOffset + getBottom() - getTop() - handleHeight - viewHandle.getTop());

                invalidate();

            } else {

                final int top = viewHandle.getTop();

                int deltaY = position - top;

                if (position < topOffset) {
                    deltaY = topOffset - top;

                } else if (deltaY > bottomOffset + getBottom() - getTop() - handleHeight - top) {
                    deltaY = bottomOffset + getBottom() - getTop() - handleHeight - top;
                }

                viewHandle.offsetTopAndBottom(deltaY);
                viewHandle.getHitRect(rectFrame);

                rectInvalidate.set(rectFrame);

                rectInvalidate.union(rectFrame.left,
                                     rectFrame.top - deltaY,
                                     rectFrame.right,
                                     rectFrame.bottom - deltaY);

                rectInvalidate.union(0,
                                     rectFrame.bottom - deltaY,
                                     getWidth(),
                                     rectFrame.bottom - deltaY + viewContent.getHeight());

                invalidate(rectInvalidate);
            }

        } else {

            if (position == DRAWER_EXPANDED) {

                viewHandle.offsetLeftAndRight(topOffset - viewHandle.getLeft());

                invalidate();

            } else if (position == DRAWER_COLLAPSED) {

                viewHandle.offsetLeftAndRight(bottomOffset + getRight() - getLeft() - handleWidth - viewHandle.getLeft());

                invalidate();

            } else {

                final int left = viewHandle.getLeft();

                int deltaX = position - left;

                if (position < topOffset) {
                    deltaX = topOffset - left;

                } else if (deltaX > bottomOffset + getRight() - getLeft() - handleWidth - left) {
                    deltaX = bottomOffset + getRight() - getLeft() - handleWidth - left;
                }

                viewHandle.offsetLeftAndRight(deltaX);
                viewHandle.getHitRect(rectFrame);

                rectInvalidate.set(rectFrame);

                rectInvalidate.union(rectFrame.left - deltaX,
                                     rectFrame.top,
                                     rectFrame.right - deltaX,
                                     rectFrame.bottom);

                rectInvalidate.union(rectFrame.right - deltaX,
                                     0,
                                     rectFrame.right - deltaX + viewContent.getWidth(),
                                     getHeight());

                invalidate(rectInvalidate);
            }
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(@NonNull final AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);

        event.setClassName(SlidingDrawer.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull final AccessibilityNodeInfo nodeInfo) {
        super.onInitializeAccessibilityNodeInfo(nodeInfo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            nodeInfo.setClassName(SlidingDrawer.class.getName());
        }
    }

    /**
     * Indicates whether the drawer is scrolling or flinging.
     *
     * @return True if the drawer is scroller or flinging, false otherwise.
     */
    public final boolean isMoving() {
        return tracking || animating;
    }

    /**
     * Indicates whether the drawer is currently fully opened.
     *
     * @return True if the drawer is opened, false otherwise.
     */
    public final boolean isOpened() {
        return expanded;
    }

    /**
     * Locks the SlidingDrawer so that touch events are ignores.
     *
     * @see #unlock()
     */
    public final void lock() {
        locked = true;
    }

    /**
     * Unlocks the SlidingDrawer so that touch events are processed.
     *
     * @see #lock()
     */
    public final void unlock() {
        locked = false;
    }

    /**
     * Toggles the drawer open and close with an animation.
     *
     * @see #animateOpen()
     * @see #animateClose()
     * @see #toggle()
     * @see #open()
     * @see #close()
     */
    public void animateToggle() {

        if (expanded) {
            animateClose();

        } else {
            animateOpen();
        }
    }

    /**
     * Opens the drawer with an animation.
     *
     * @see #animateToggle()
     * @see #animateClose()
     * @see #toggle()
     * @see #open()
     * @see #close()
     */
    public void animateOpen() {
        prepareContent();

        if (onDrawerScrollListener != null) {
            onDrawerScrollListener.onScrollStarted();
        }

        animateOpen(vertical ? viewHandle.getTop() : viewHandle.getLeft());

        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);

        if (onDrawerScrollListener != null) {
            onDrawerScrollListener.onScrollEnded();
        }
    }

    /**
     * Closes the drawer with an animation.
     *
     * @see #animateToggle()
     * @see #animateOpen()
     * @see #toggle()
     * @see #open()
     * @see #close()
     */
    public void animateClose() {
        prepareContent();

        if (onDrawerScrollListener != null) {
            onDrawerScrollListener.onScrollStarted();
        }

        animateClose(vertical ? viewHandle.getTop() : viewHandle.getLeft());

        if (onDrawerScrollListener != null) {
            onDrawerScrollListener.onScrollEnded();
        }
    }

    /**
     * Toggles the drawer open and close. Takes effect immediately.
     *
     * @see #animateToggle()
     * @see #animateOpen()
     * @see #animateClose()
     * @see #open()
     * @see #close()
     */
    public void toggle() {

        if (expanded) {
            closeDrawer();

        } else {
            openDrawer();
        }

        invalidate();
        requestLayout();
    }

    /**
     * Opens the drawer immediately.
     *
     * @see #animateToggle()
     * @see #animateOpen()
     * @see #animateClose()
     * @see #toggle()
     * @see #close()
     */
    public void open() {
        openDrawer();

        invalidate();
        requestLayout();

        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    /**
     * Closes the drawer immediately.
     *
     * @see #animateToggle()
     * @see #animateOpen()
     * @see #animateClose()
     * @see #toggle()
     * @see #open()
     */
    public void close() {
        closeDrawer();

        invalidate();
        requestLayout();
    }

    /**
     * Returns the handle of the drawer.
     *
     * @return The View representing the handle of the drawer,
     *         identified by the "handle" id in XML.
     */
    public final View getHandle() {
        return viewHandle;
    }

    /**
     * Returns the content of the drawer.
     *
     * @return The View representing the content of the drawer,
     *         identified by the "content" id in XML.
     */
    public final View getContent() {
        return viewContent;
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes open.
     *
     * @param onDrawerOpenListener The listener to be notified when the drawer is opened.
     */
    public final void setOnDrawerOpenListener(OnDrawerOpenListener onDrawerOpenListener) {
        this.onDrawerOpenListener = onDrawerOpenListener;
    }

    /**
     * Sets the listener that receives a notification when the drawer becomes close.
     *
     * @param onDrawerCloseListener The listener to be notified when the drawer is closed.
     */
    public final void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        this.onDrawerCloseListener = onDrawerCloseListener;
    }

    /**
     * <p> Sets the listener that receives a notification when the drawer starts or ends a scroll. </p>
     * <p> A fling is considered as a scroll. A fling will also trigger a drawer opened or drawer closed event. </p>
     *
     * @param onDrawerScrollListener The listener to be notified when scrolling starts or stops.
     */
    public final void setOnDrawerScrollListener(OnDrawerScrollListener onDrawerScrollListener) {
        this.onDrawerScrollListener = onDrawerScrollListener;
    }

    private final Runnable handler = new Runnable() {

        @Override
        public void run() {

            doAnimation();
        }
    };

    private class DrawerToggle implements OnClickListener {

        @Override
        public void onClick(final View view) {

            if (!locked) {

                if (animateOnClick) {
                    animateToggle();

                } else {
                    toggle();
                }
            }
        }
    }
}
