package com.hsg.refreshlibrary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;

/**
 * 作者:Joe 邮件:963893628@qq.com
 * 创建时间:17/07/15
 */
public class StickyNavLayout extends LinearLayout {
    private View mHeaderView;
    private View mNavView;
    private View mContentView;

    private View mDirectNormalView;
    private RecyclerView mDirectRecyclerView;
    private AbsListView mDirectAbsListView;
    private ScrollView mDirectScrollView;
    private WebView mDirectWebView;
    private ViewPager mDirectViewPager;

    private View mNestedContentView;
    private View mNestedNormalView;
    private RecyclerView mNestedRecyclerView;
    private AbsListView mNestedAbsListView;
    private ScrollView mNestedScrollView;
    private WebView mNestedWebView;

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    private boolean mIsInControl = true;

    private float mLastDispatchY;
    private float mLastTouchY;

    public RefreshLayout mRefreshLayout;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mOverScroller = new OverScroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    public void setOrientation(int orientation) {
        if (VERTICAL == orientation) {
            super.setOrientation(VERTICAL);
        }
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 3) {
            throw new IllegalStateException(StickyNavLayout.class.getSimpleName() + "必须有且只有三个子控件");
        }

        mHeaderView = getChildAt(0);
        mNavView = getChildAt(1);
        mContentView = getChildAt(2);

        if (mContentView instanceof AbsListView) {
            mDirectAbsListView = (AbsListView) mContentView;
            mDirectAbsListView.setOnScrollListener(mLvOnScrollListener);
        } else if (mContentView instanceof RecyclerView) {
            mDirectRecyclerView = (RecyclerView) mContentView;
            mDirectRecyclerView.addOnScrollListener(mRvOnScrollListener);
        } else if (mContentView instanceof ScrollView) {
            mDirectScrollView = (ScrollView) mContentView;
        } else if (mContentView instanceof WebView) {
            mDirectWebView = (WebView) mContentView;
        } else if (mContentView instanceof ViewPager) {
            mDirectViewPager = (ViewPager) mContentView;
            mDirectViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    regetNestedContentView();
                }
            });
        } else {
            mDirectNormalView = mContentView;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(mContentView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - getNavViewHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(0, mOverScroller.getCurrY());
            invalidate();
        }
    }

    public void fling(int velocityY) {
        mOverScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, getHeaderViewHeight());
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }

        int headerViewHeight = getHeaderViewHeight();
        if (y > headerViewHeight) {
            y = headerViewHeight;
        }

        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    /**
     * 获取头部视图高度，包括topMargin和bottomMargin
     *
     * @return
     */
    private int getHeaderViewHeight() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        return mHeaderView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    /**
     * 获取导航视图的高度，包括topMargin和bottomMargin
     *
     * @return
     */
    private int getNavViewHeight() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mNavView.getLayoutParams();
        return mNavView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    /**
     * 头部视图是否已经完全隐藏
     *
     * @return
     */
    private boolean isHeaderViewCompleteInvisible() {
        // 0表示x，1表示y
        int[] location = new int[2];
        getLocationOnScreen(location);
        int contentOnScreenTopY = location[1] + getPaddingTop();

        mNavView.getLocationOnScreen(location);
        MarginLayoutParams params = (MarginLayoutParams) mNavView.getLayoutParams();
        int navViewTopOnScreenY = location[1] - params.topMargin;

        if (navViewTopOnScreenY == contentOnScreenTopY) {
            return true;
        } else {
            return false;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentTouchY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDispatchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastDispatchY;
                mLastDispatchY = currentTouchY;
                if (isContentViewToTop() && isHeaderViewCompleteInvisible()) {
                    if (differentY >= 0 && !mIsInControl) {
                        mIsInControl = true;

                        return resetDispatchTouchEvent(ev);
                    }

                    if (differentY <= 0 && mIsInControl) {
                        mIsInControl = false;

                        return resetDispatchTouchEvent(ev);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean resetDispatchTouchEvent(MotionEvent ev) {
        MotionEvent newEvent = MotionEvent.obtain(ev);

        ev.setAction(MotionEvent.ACTION_CANCEL);
        dispatchTouchEvent(ev);

        newEvent.setAction(MotionEvent.ACTION_DOWN);
        return dispatchTouchEvent(newEvent);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float currentTouchY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                if (Math.abs(differentY) > mTouchSlop) {
                    if (!isHeaderViewCompleteInvisible() || (isContentViewToTop() && isHeaderViewCompleteInvisible() && mIsInControl)) {
                        mLastTouchY = currentTouchY;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);

        float currentTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }

                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                mLastTouchY = currentTouchY;
                if (Math.abs(differentY) > 0) {
                    scrollBy(0, (int) -differentY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    public boolean isContentViewToTop() {
        if (mDirectNormalView != null) {
            return true;
        }

        if (RefreshScrollingUtil.isScrollViewOrWebViewToTop(mDirectWebView)) {
            return true;
        }

        if (RefreshScrollingUtil.isScrollViewOrWebViewToTop(mDirectScrollView)) {
            return true;
        }

        if (RefreshScrollingUtil.isAbsListViewToTop(mDirectAbsListView)) {
            return true;
        }

        if (RefreshScrollingUtil.isRecyclerViewToTop(mDirectRecyclerView)) {
            return true;
        }

        if (mDirectViewPager != null) {
            return isViewPagerContentViewToTop();
        }

        return false;
    }

    private boolean isViewPagerContentViewToTop() {
        if (mNestedContentView == null) {
            regetNestedContentView();
        }

        if (mDirectNormalView != null) {
            return true;
        }

        if (RefreshScrollingUtil.isScrollViewOrWebViewToTop(mNestedWebView)) {
            return true;
        }

        if (RefreshScrollingUtil.isScrollViewOrWebViewToTop(mNestedScrollView)) {
            return true;
        }

        if (RefreshScrollingUtil.isAbsListViewToTop(mNestedAbsListView)) {
            return true;
        }

        if (RefreshScrollingUtil.isRecyclerViewToTop(mNestedRecyclerView)) {
            return true;
        }

        return false;
    }

    /**
     * 重新获取嵌套的内容视图
     */
    private void regetNestedContentView() {
        int currentItem = mDirectViewPager.getCurrentItem();
        PagerAdapter adapter = mDirectViewPager.getAdapter();
        if (adapter instanceof FragmentPagerAdapter || adapter instanceof FragmentStatePagerAdapter) {
            Fragment item = (Fragment) adapter.instantiateItem(mDirectViewPager, currentItem);
            mNestedContentView = item.getView();

            // 清空之前的
            mNestedNormalView = null;
            mNestedAbsListView = null;
            mNestedRecyclerView = null;
            mNestedScrollView = null;
            mNestedWebView = null;

            if (mNestedContentView instanceof AbsListView) {
                mNestedAbsListView = (AbsListView) mNestedContentView;
                mNestedAbsListView.setOnScrollListener(mLvOnScrollListener);

                if (!isHeaderViewCompleteInvisible()) {
                    mNestedAbsListView.setSelection(0);
                }
            } else if (mNestedContentView instanceof RecyclerView) {
                mNestedRecyclerView = (RecyclerView) mNestedContentView;
                mNestedRecyclerView.removeOnScrollListener(mRvOnScrollListener);
                mNestedRecyclerView.addOnScrollListener(mRvOnScrollListener);

                if (!isHeaderViewCompleteInvisible()) {
                    mNestedRecyclerView.scrollToPosition(0);
                }
            } else if (mNestedContentView instanceof ScrollView) {
                mNestedScrollView = (ScrollView) mNestedContentView;

                if (!isHeaderViewCompleteInvisible()) {
                    mNestedScrollView.scrollTo(mNestedScrollView.getScrollX(), 0);
                }
            } else if (mNestedContentView instanceof WebView) {
                mNestedWebView = (WebView) mNestedContentView;

                if (!isHeaderViewCompleteInvisible()) {
                    mNestedWebView.scrollTo(mNestedWebView.getScrollX(), 0);
                }
            } else {
                mNestedNormalView = mNestedContentView;
            }
        } else {
            throw new IllegalStateException(StickyNavLayout.class.getSimpleName() + "的第三个子控件为ViewPager时，其adapter必须是FragmentPagerAdapter或者FragmentStatePagerAdapter");
        }
    }

    public void setRefreshLayout(RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

    private RecyclerView.OnScrollListener mRvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if ((newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING) && mRefreshLayout != null && mRefreshLayout.shouldHandleRecyclerViewLoadingMore(recyclerView)) {
                mRefreshLayout.beginLoadingMore();
            }
        }
    };

    private AbsListView.OnScrollListener mLvOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            if ((scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) && mRefreshLayout != null && mRefreshLayout.shouldHandleAbsListViewLoadingMore(absListView)) {
                mRefreshLayout.beginLoadingMore();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    public boolean shouldHandleLoadingMore() {
        if (mRefreshLayout == null) {
            return false;
        }

        if (mDirectNormalView != null) {
            return true;
        }

        if (RefreshScrollingUtil.isWebViewToBottom(mDirectWebView)) {
            return true;
        }

        if (RefreshScrollingUtil.isScrollViewToBottom(mDirectScrollView)) {
            return true;
        }

        if (mDirectAbsListView != null) {
            return mRefreshLayout.shouldHandleAbsListViewLoadingMore(mDirectAbsListView);
        }

        if (mDirectRecyclerView != null) {
            return mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mDirectRecyclerView);
        }

        if (mDirectViewPager != null) {
            if (mNestedContentView == null) {
                regetNestedContentView();
            }

            if (mNestedNormalView != null) {
                return true;
            }

            if (RefreshScrollingUtil.isWebViewToBottom(mNestedWebView)) {
                return true;
            }

            if (RefreshScrollingUtil.isScrollViewToBottom(mNestedScrollView)) {
                return true;
            }

            if (mNestedAbsListView != null) {
                return mRefreshLayout.shouldHandleAbsListViewLoadingMore(mNestedAbsListView);
            }

            if (mNestedRecyclerView != null) {
                return mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mNestedRecyclerView);
            }
        }

        return false;
    }

    public void scrollToBottom() {
        RefreshScrollingUtil.scrollToBottom(mDirectScrollView);
        RefreshScrollingUtil.scrollToBottom(mDirectRecyclerView);
        RefreshScrollingUtil.scrollToBottom(mDirectAbsListView);

        if (mDirectViewPager != null) {
            if (mNestedContentView == null) {
                regetNestedContentView();
            }
            RefreshScrollingUtil.scrollToBottom(mNestedScrollView);
            RefreshScrollingUtil.scrollToBottom(mNestedRecyclerView);
            RefreshScrollingUtil.scrollToBottom(mNestedAbsListView);
        }
    }
}