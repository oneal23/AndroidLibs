package com.lei.baselib_java.mvp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lei.baselib_java.R;
import com.lei.baselib_java.ui.recycler.EasyRecyclerView;
import com.lei.baselib_java.ui.recycler.adapter.RecyclerArrayAdapter;
import com.lei.baselib_java.ui.recycler.decoration.DividerDecoration;
import com.lei.baselib_java.ui.recycler.decoration.SpaceDecoration;
import com.lei.baselib_java.utils.NetworkUtil;
import com.lei.baselib_java.utils.Utils;

/**
 * Created by rymyz on 2017/5/9.
 */

public abstract class BaseListMvpFragment<P extends IBasePresenter> extends BaseMvpFragment<P> implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    protected EasyRecyclerView recyclerView;
    protected RecyclerArrayAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    private Handler handler = new Handler(Looper.getMainLooper());

    protected boolean useRefresh = true;
    protected boolean useMore = true;

    /**
     * 设置LayoutManager
     */
    public abstract void setLayoutManager();

    /**
     * 设置Adapter
     */
    public abstract void setAdapter();

    /**
     * 点击事件
     *
     * @param position
     */
    public abstract void onRecyclerItemClick(int position);

    /**
     * 长按事件
     *
     * @param position
     */
    public abstract void onRecyclerItemLongClick(int position);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container,false);

        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recyclerView);
        setAdapter();

        recyclerView.setAdapterWithProgress(adapter);

        setLayoutManager();
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (setDecoration() != null) {
            recyclerView.addItemDecoration(setDecoration());
        }

        setPadding();
        useRefreshOrMore();

        if (useMore) {
            adapter.setMore(R.layout.view_more, this);
            adapter.setNoMore(R.layout.view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
                @Override
                public void onNoMoreShow() {
                    adapter.resumeMore();
                }

                @Override
                public void onNoMoreClick() {
                    adapter.resumeMore();
                }
            });
        }

        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onRecyclerItemClick(position);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                onRecyclerItemLongClick(position);

                return true;
            }
        });

        if (useRefresh) {
            recyclerView.setRefreshListener(this);
        }

        return view;
    }

    @Override
    protected void initData() {
        onRefresh();
    }

    public abstract RecyclerView.ItemDecoration setDecoration();

    public void useRefreshOrMore() {
    }

    public void setPadding() {
    }

    /**
     * 当设置为Linear时，手动调用此方法
     */
    public DividerDecoration getDividerDecoration(int dp) {
        DividerDecoration itemDecoration = new DividerDecoration(Color.alpha(0), (int) Utils.convertDpToPixel(dp, getActivity()));
        itemDecoration.setDrawLastItem(false);

        return itemDecoration;
    }

    public DividerDecoration getDividerDecoration(int dp, int color) {
        DividerDecoration itemDecoration = new DividerDecoration(color, (int) Utils.convertDpToPixel(dp, getActivity()));
        itemDecoration.setDrawLastItem(false);

        return itemDecoration;
    }

    /**
     * 当设置为Grid或者瀑布流时，手动调用此方法
     */
    public SpaceDecoration getSpaceDecoration() {
        SpaceDecoration itemDecoration = new SpaceDecoration((int) Utils.convertDpToPixel(8f, getActivity()));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);

        return itemDecoration;
    }

    /**
     * 刷新时进行的操作
     */
    public abstract void refresh();

    /**
     * 加载更多时进行的操作
     */
    public abstract void loadMore();

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!NetworkUtil.isAvailable(getActivity())) {
                    showError("网络连接异常，请检查您的网络状态");
//                    adapter.clear();
                    if (useMore) {
                        adapter.pauseMore();
                    }
                    if (adapter.getCount() == 0) {
                        recyclerView.showEmpty();
                    }
                    return;
                }
                refresh();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtil.isAvailable(getActivity())) {
                    showError("网络连接异常，请检查您的网络状态");
                    adapter.pauseMore();
                    if (useMore) {
                        recyclerView.showError();
                    }
                    return;
                }
                loadMore();
            }
        }, 500);
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        recyclerView.setRefreshing(false);
    }
}
