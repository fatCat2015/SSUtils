package com.cat.sutils.wheel;

import android.view.View;

public interface IWheelViewCalculator {

    /**
     * 计算出开始item的position
     * @return
     */
    int calculateStartPosition();

    /**
     * 计算出开始item的top
     * @return
     */
    int calculateStartTop();

    /**
     * 计算childView相对于中心view的距离
     * @param childView
     * @return
     */
    float calculateChildViewPosition(View childView);

    /**
     * 计算需要滚动的距离
     * @param startPotion
     * @param endPosition
     * @return
     */
    int calculateScrolledDistance(int startPotion,int endPosition);


    /**
     *  重新获取position 防止不合法position
     * @param position
     * @return
     */
    int revisePosition(int position);
}
