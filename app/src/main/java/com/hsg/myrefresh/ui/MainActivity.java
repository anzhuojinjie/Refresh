package com.hsg.myrefresh.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hsg.myrefresh.R;
import com.hsg.myrefresh.adapter.MyAdapter;
import com.hsg.myrefresh.bean.Student;
import com.hsg.refreshlibrary.MeiTuanRefreshViewHolder;
import com.hsg.refreshlibrary.MoocRefreshViewHolder;
import com.hsg.refreshlibrary.RefreshLayout;
import com.hsg.refreshlibrary.StickinessRefreshViewHolder;
import com.hsg.refreshlibrary.WeiBoRefreshViewHolder;
import com.zhy.autolayout.AutoLayoutActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AutoLayoutActivity implements RefreshLayout.RefreshLayoutDelegate{

    private RefreshLayout refreshLayout;
    private ListView mLV;
    private List<Student> mList;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TintBarUtil.setTransparent(this);

        refreshLayout = ((RefreshLayout) findViewById(R.id.refresh_layout));
        mLV = ((ListView) findViewById(R.id.lv));

        mList = new ArrayList<>();
        mAdapter = new MyAdapter(this, mList);
        mLV.setAdapter(mAdapter);

        initData();

//        initRefreshLayout();//仿微博刷新
//        initMeiTuanRefreshLayout();//仿美团刷新
//        initMoocRefreshViewHolder();//仿慕课网刷新
        initStickinessRefreshLayout();//粘性下啦刷新
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            final Student s = new Student(i,"下啦刷新 - Refresh" + i, "身份证 ：1101999999999999999" + i);
            mList.add(s);
        }
        mAdapter.setData(mList);
    }

    //仿微博下拉刷新
    private void initRefreshLayout(){
        // 为RefreshLayout 设置代理
        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        WeiBoRefreshViewHolder refreshViewHolder = new WeiBoRefreshViewHolder(this, true);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在java多时的文本
        refreshViewHolder.setReleaseRefreshText("释放更新   上次更新时间："+ new SimpleDateFormat("MM月dd日 HH分mm秒").format(System.currentTimeMillis()));
        refreshViewHolder.setLoadingMoreText("正在加载更多。。。");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.colorAccent);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.colorAccent);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
        // 设置下拉刷新和上拉加载更多的风格
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    //仿美团下拉刷新
    private void initMeiTuanRefreshLayout(){
        // 为RefreshLayout 设置代理
        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        MeiTuanRefreshViewHolder refreshViewHolder = new MeiTuanRefreshViewHolder(this, true);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载更多。。。");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.colorAccent);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.colorAccent);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
        // 设置下拉刷新和上拉加载更多的风格
        //设置下拉过程中的图片资源
        refreshViewHolder.setPullDownImageResource(R.mipmap.ic_launcher);
        //设置进入释放刷新状态时的动画资源
        refreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_loding);
        //设置正在刷新时的动画资源
        refreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_loding);
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    };

    //仿慕课网下啦刷新
    private void initMoocRefreshViewHolder(){
        // 为RefreshLayout 设置代理
        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        MoocRefreshViewHolder refreshViewHolder = new MoocRefreshViewHolder(this, true);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载更多。。。");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.colorAccent);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.colorAccent);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
        // 设置原始图片资源
        refreshViewHolder.setOriginalImage(R.mipmap.ic_launcher);
        //设置最终生成图片的填充颜色资源
        refreshViewHolder.setUltimateColor(R.color.colorPrimary);
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }
    //粘性下啦刷新
    private void initStickinessRefreshLayout(){
        // 为RefreshLayout 设置代理
        refreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        StickinessRefreshViewHolder refreshViewHolder = new StickinessRefreshViewHolder(this, true);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载更多。。。");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.colorAccent);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.colorAccent);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
        // 设置旋转图片资源
        refreshViewHolder.setRotateImage(R.mipmap.ic_launcher);
        //设置黏性颜色资源
        refreshViewHolder.setStickinessColor(R.color.colorPrimary);
        refreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    public void onRefreshLayoutBeginRefreshing(final RefreshLayout refreshLayout) {
        // 在这里加载最新数据

        if (true) {//true改为判断网络
            // 如果网络可用，则加载网络数据
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // 加载完毕后在 UI 线程结束下拉刷新
                    refreshLayout.endRefreshing();
                    List<Student> newList = new ArrayList<Student>();
                    for (int i = 0; i < 5; i++) {
                        final Student s = new Student(i,"下啦刷新 - REFRESH" + System.currentTimeMillis(), "身份证 ：刷新" + System.currentTimeMillis());
                        newList.add(s);
                    }

                    mList.addAll(0, newList);
                    mAdapter.setData(mList);
                }
            }.execute();
        } else {
            // 网络不可用，结束下拉刷新
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onRefreshLayoutBeginLoadingMore(final RefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
        if (true) {//true改为判断网络
            // 如果网络可用，则异步加载网络数据，并返回 true，显示正在加载更多
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    // 加载完毕后在 UI 线程结束加载更多
                    refreshLayout.endLoadingMore();

                    for (int i = 0; i < 5; i++) {
                        final Student s = new Student(i,"加载更多 -LOADMORE" + System.currentTimeMillis(), "身份证 ：加载" + System.currentTimeMillis());
                        mList.add(s);
                    }
                    mAdapter.setData(mList);
                }
            }.execute();
            return true;
        } else {
            // 网络不可用，返回 false，不显示正在加载更多
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在 activity 的 onStart 方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        refreshLayout.beginRefreshing();
    }

    // 通过代码方式控制进入加载更多状态
    public void beginLoadingMore() {
        refreshLayout.beginLoadingMore();
    }
}
