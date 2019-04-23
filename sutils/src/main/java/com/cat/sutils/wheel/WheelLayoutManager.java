package com.cat.sutils.wheel;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cat.sutils.R;


public class WheelLayoutManager extends RecyclerView.LayoutManager {

    private LinearSnapHelper mLinearSnapHelper;

    private int mVisibleItemCount;

    private int mItemHeight;

    private int mTotalOffsetY;

    private ItemTransformer mItemTransformer;

    private int mSelectedPosition;



    public WheelLayoutManager(int visibleItemCount) {
        this.mVisibleItemCount=visibleItemCount;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mLinearSnapHelper=new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(view);
        mItemHeight= (int) view.getContext().getResources().getDimension(R.dimen.wheel_view_item_height);
    }



    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        reset(recycler);
        if(mVisibleItemCount>=3){
            int top=mVisibleItemCount/2*mItemHeight+getPaddingTop();
            int itemCount=getItemCount();
            for (int i = 0; i < itemCount ; i++) {
                if(top>=getHeight()-getPaddingTop()){
                    break;
                }
                fillView(recycler,i,-1,getPaddingLeft(),top,getWidth()-getPaddingRight(),top+=mItemHeight);
            }
            transformItems();
        }
    }


    private void reset(RecyclerView.Recycler recycler){
        detachAndScrapAttachedViews(recycler);
        mSelectedPosition=0;
        mTotalOffsetY=0;
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
                //选择发生了变化
            }
        }
    }


    private void fillViews(int dy, RecyclerView.Recycler recycler) {
        if(getChildCount()==0){
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
                mItemTransformer.transformItem((TextView) childView,calculatePosition(childView));
            }
        }
    }

    private float calculatePosition(View childView){
        int topOfCenterView=getPaddingTop()+mVisibleItemCount/2*mItemHeight;
        return (getDecoratedTop(childView)-topOfCenterView*1.0F)/mItemHeight;
    }




    public void setItemTransformer(ItemTransformer itemTransformer) {
        this.mItemTransformer = itemTransformer;
    }


    public interface ItemTransformer{
        void transformItem(TextView childView, float position);
    }





}
