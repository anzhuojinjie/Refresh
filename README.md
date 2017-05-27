


# Refresh

一个万能的下拉刷新上拉加载更多库，下拉刷新，加载更多，理论上支持任意view的下拉刷新，上拉加载库，实现了仿美团、微博、慕课网已经粘性下拉刷新样式



![enter description here][1]![enter description here][2]![enter description here][3]

# 使用方法

step1：在项目gradle根目录下添加

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
step2：在app的gradle根目录下添加

``` 
dependencies {
    compile 'com.github.anzhuojinjie:Refresh:1.0'
}
```

step2：在activity中implements RefreshLayout.RefreshLayoutDelegate
```
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
```

#加载更多只有一种效果：\r\n


![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i1.png)

#仿微博下拉刷新\r\n


![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i11.png)
![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i12.png)
``` 
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
```

#仿美团下拉刷新\r\n


![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i21.png)

``` 
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
```

#仿慕课网下啦刷新\r\n


![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i31.png)
``` 
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
```
#粘性下啦刷新\r\n
![](https://github.com/anzhuojinjie/Refresh/blob/master/img/i41.png)

``` 
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
```






如有疑问欢迎加入
群名称：安卓开发交流群
群   号：599329462

