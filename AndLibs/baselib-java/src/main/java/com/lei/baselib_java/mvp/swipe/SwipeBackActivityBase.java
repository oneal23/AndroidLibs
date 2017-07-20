package com.lei.baselib_java.mvp.swipe;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
      SwipeBackLayout getSwipeBackLayout();

      void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
      void scrollToFinishActivity();

}
