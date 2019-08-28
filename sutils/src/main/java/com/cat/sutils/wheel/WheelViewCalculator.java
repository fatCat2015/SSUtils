package com.cat.sutils.wheel;

import android.view.View;

public final class WheelViewCalculator implements IWheelViewCalculator {


    private WheelLayoutManager wheelLayoutManager;

    WheelViewCalculator(WheelLayoutManager wheelLayoutManager) {
        this.wheelLayoutManager = wheelLayoutManager;
    }

    @Override
    public int calculateStartPosition() {
        int startPosition=wheelLayoutManager.mSelectedPosition-wheelLayoutManager.mVisibleItemCount/2;
        if(startPosition<=0){
            startPosition=0;
        }
        return startPosition;
    }

    @Override
    public int calculateStartTop() {
        int startPosition=calculateStartPosition();
        return wheelLayoutManager.getPaddingTop()+(wheelLayoutManager.mVisibleItemCount/2-(wheelLayoutManager.mSelectedPosition-startPosition))*wheelLayoutManager.mItemHeight;
    }

    @Override
    public float calculateChildViewPosition(View childView) {
        int topOfCenterView=wheelLayoutManager.getPaddingTop()+wheelLayoutManager.mVisibleItemCount/2*wheelLayoutManager.mItemHeight;
        return (wheelLayoutManager.getDecoratedTop(childView)-topOfCenterView*1.0F)/wheelLayoutManager.mItemHeight;
    }

    @Override
    public int calculateScrolledDistance(int startPotion, int endPosition) {
        return (endPosition-startPotion)*wheelLayoutManager.mItemHeight;
    }

    @Override
    public int revisePosition(int position) {
        if(position<0){
            position=0;
        }
        if (position>=wheelLayoutManager.getItemCount()) {
            position=wheelLayoutManager.getItemCount()-1;
        }
        return position;
    }
}
