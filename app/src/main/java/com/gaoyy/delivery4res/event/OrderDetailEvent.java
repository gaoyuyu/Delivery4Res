package com.gaoyy.delivery4res.event;

/**
 * Created by gaoyy on 2017/12/3 0003.
 */

public class OrderDetailEvent
{
    private int tag;

    public OrderDetailEvent(int tag)
    {
        this.tag = tag;
    }

    public int getTag()
    {
        return tag;
    }

}
