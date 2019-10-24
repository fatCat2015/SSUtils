package com.cat.sutils.wheel;

import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.cat.sutils.R;

class WheelLayoutManager extends RecyclerView.LayoutManager {

    private LinearSnapHelper mLinearSnapHelper;

    int mVisibleItemCount;

    int mItemHeight;

    private int mTotalOffsetY;

    private WheelView.ItemTransformer mItemTransformer;

    int mSelectedPosition;

    private RecyclerView mRecyclerView;

    private IWheelViewCalculator wheelViewCalculator;

    private OnSelectedChangListener mOnSelectedChangListener;


    public WheelLayoutManager(int visibleItemCount) {
        if(visibleItemCount<3){
            throw new IllegalArgumentException("visibleItemCount  has to be greater than or equal to 3");
        }
        wheelViewCalculator=new WheelViewCalculator(this);
        this.mVisibleItemCount=visibleItemCount;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mLinearSnapHelper=new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(view);
        mItemHeight= (int) view.getContext().getResources().getDimension(R.dimen.wheel_view_item_height);
        this.mRecyclerView=view;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void setOnSelectedChangListener(OnSelectedChangListener onSelectedChangListener) {
        this.mOnSelectedChangListener = onSelectedChangListener;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        mTotalOffsetY=mSelectedPosition*mItemHeight;
        int startPosition=wheelViewCalculator.calculateStartPosition();
        int top=wheelViewCalculator.calculateStartTop();
        int itemCount=getItemCount();
        for (int i = startPosition; i < itemCount ; i++) {
            if(top>=getHeight()-getPaddingTop()){
                break;
            }
            fillView(recycler,i,-1,getPaddingLeft(),top,getWidth()-getPaddingRight(),top+=mItemHeight);
        }
        transformItems();
    }




    private void fillView(RecyclerView.Recycler recycler,int position,  int index,int left, int top, int right, int bottom){
        View child=recycler.getViewForPosition(position);
        addView(child,index);
        measureChild(child,0,0);
        layoutDecorated(child,left,top,right,bottom);
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int revisedDy=reviseDy(dy);
        offsetChildrenVertical(-revisedDy);
        mTotalOffsetY+=revisedDy;
        if (revisedDy!=0) {
            recycleViews(revisedDy,recycler);
            fillViews(revisedDy,recycler);
        }
        transformItems();
        return revisedDy;
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state==RecyclerView.SCROLL_STATE_IDLE){
            int selectedPosition=getPosition(mLinearSnapHelper.findSnapView(this));
            if(mSelectedPosition!=selectedPosition){
                mSelectedPosition=selectedPosition;
                if(mOnSelectedChangListener!=null){
                    mOnSelectedChangListener.onSelectedChanged((WheelView) mRecyclerView,mSelectedPosition);
                }
            }
        }
    }


    private void fillViews(int dy, RecyclerView.Recycler recycler) {
        if(getChildCount()==0){  //由于是先回收再填充view 当dy滑动距离大于一屏幕的时候 item已经全部被回收了 此时getChildCount()==0
            return;
        }
        View topView=getChildAt(0);
        View bottomView=getChildAt(getChildCount()-1);
        if(dy>0){
            int bottomOfBottomView=getDecoratedBottom(bottomView);
            int bottomSpace=getHeight()-getPaddingBottom()-bottomOfBottomView;
            if(bottomSpace>0){
                int top=bottomOfBottomView;
                int addItemCount=bottomSpace/mItemHeight+1;
                int position=getPosition(bottomView)+1;
                for (int i = 0; i < addItemCount; i++) {
                    if(position>=getItemCount()){
                        break;
                    }
                    fillView(recycler,position,-1,getPaddingLeft(),top,getWidth()-getPaddingRight(),top+=mItemHeight);
                    position++;
                }
            }
        }else{
            int topOfTopView=getDecoratedTop(topView);
            int topSpace=topOfTopView-getPaddingTop();
            if (topSpace>0) {
                int bottom=topOfTopView;
                int addItemCount=topSpace/mItemHeight+1;
                int position=getPosition(topView)-1;
                for (int i = 0; i < addItemCount; i++) {
                    if(position<0){
                        break;
                    }
                    int top=bottom-mItemHeight;
                    fillView(recycler,position,0,getPaddingLeft(),top,getWidth()-getPaddingRight(),bottom);
                    bottom-=mItemHeight;
                    position--;
                }

            }
        }
    }


    private void recycleViews(int dy, RecyclerView.Recycler recycler){
        for (int i = getChildCount()-1; i >=0 ; i--) {
            View childView=getChildAt(i);
            if(dy>0){
                if(getDecoratedBottom(childView)<=getPaddingTop()){
                    removeAndRecycleView(childView,recycler);
                }
            }else{
                if(getDecoratedTop(childView)>=getHeight()-getPaddingBottom()){
                    removeAndRecycleView(childView,recycler);
                }
            }
        }
    }


    private int reviseDy(int dy){
        if(mTotalOffsetY+dy<0){
            dy=-mTotalOffsetY;
        }else if(mTotalOffsetY+dy>(getItemCount()-1)*mItemHeight){
            dy=(getItemCount()-1)*mItemHeight-mTotalOffsetY;
        }
        return dy;
    }


    private void transformItems(){
        if(mItemTransformer!=null){
            for (int i = 0; i < getChildCount(); i++) {
                View childView=getChildAt(i);
                mItemTransformer.transformItem((TextView) childView,wheelViewCalculator.calculateChildViewPosition(childView));
            }
        }
    }


    @Override
    public void scrollToPosition(int position) {
        if(position==mSelectedPosition){
            return;
        }
        position=wheelViewCalculator.revisePosition(position);
        mSelectedPosition=position;
        if(mOnSelectedChangListener!=null){
            mOnSelectedChangListener.onSelectedChanged((WheelView) mRecyclerView,mSelectedPosition);
        }
        requestLayout();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        if(position==mSelectedPosition){
            return;
        }
        position=wheelViewCalculator.revisePosition(position);
        int needScrolledDistance=wheelViewCalculator.calculateScrolledDistance(mSelectedPosition,position);
        recyclerView.smoothScrollBy(0,needScrolledDistance,new AccelerateDecelerateInterpolator());
    }



    public void setItemTransformer(WheelView.ItemTransformer itemTransformer) {
        this.mItemTransformer = itemTransformer;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition=selectedPosition;
    }







}
