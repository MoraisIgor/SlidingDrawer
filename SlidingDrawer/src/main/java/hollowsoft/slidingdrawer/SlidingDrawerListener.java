/*
 * Copyright 2014 Igor Morais
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

/**
 * <p> Listener invoked when the drawer is closed. </p>
 *
 * @author Igor Morais
 */
public interface SlidingDrawerListener {

    /**
     * Invoked when the user starts dragging the drawer handle.
     */
    void onScrollStarted();

    /**
     * Invoked when the user stops dragging the drawer handle.
     */
    void onScrollEnded();


    /**
     * Invoked when the drawer becomes fully open.
     */
    void onDrawerOpened();

    /**
     * Invoked just before the drawer starts opening.
     */
    void onDrawerWillOpen();

    /**
     * Invoked when the drawer becomes fully closed.
     */
    void onDrawerClosed();


    /**
     * Invoked just before the drawer start closing.
     */
    void onDrawerWilClose();
}
