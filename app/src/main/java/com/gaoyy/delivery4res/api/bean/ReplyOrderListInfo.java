package com.gaoyy.delivery4res.api.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaoyy on 2017/9/12 0012.
 */

public class ReplyOrderListInfo
{

    /**
     * body : {"list":{"currentPage":1,"nextPage":2,"pageSize":1,"pages":6,"previousPage":1,"result":[{"addTime":"2017-09-08 02:33:06","buyerMobile":"13069049021","buyerName":"用户1","courier_id":1,"courier_statistics_id":null,"deleteStatus":false,"gcs":[{"addTime":"2017-09-08 02:30:16","cart_type":null,"count":1,"deleteStatus":false,"goods_id":98584,"goods_name":"可可蛋糕","goods_photo":null,"id":230328,"price":89,"spec_info":null}],"goodsCount":1,"goods_amount":89,"id":255,"is_courierstatistics":false,"is_platformstatistics":false,"makingTime":1504852395000,"orderEva":"不好","orderEvas":[],"orderReply":null,"order_id":"3282820170908023306","platform_id":null,"sendingTime":1504852460000,"takingTime":1504852395000,"totalPrice":41.26}],"rowCount":6}}
     * errorCode : -1
     * success : true
     */

    private BodyBean body;
    private String errorCode;
    private boolean success;

    public BodyBean getBody()
    {
        return body;
    }

    public void setBody(BodyBean body)
    {
        this.body = body;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public static class BodyBean
    {
        /**
         * list : {"currentPage":1,"nextPage":2,"pageSize":1,"pages":6,"previousPage":1,"result":[{"addTime":"2017-09-08 02:33:06","buyerMobile":"13069049021","buyerName":"用户1","courier_id":1,"courier_statistics_id":null,"deleteStatus":false,"gcs":[{"addTime":"2017-09-08 02:30:16","cart_type":null,"count":1,"deleteStatus":false,"goods_id":98584,"goods_name":"可可蛋糕","goods_photo":null,"id":230328,"price":89,"spec_info":null}],"goodsCount":1,"goods_amount":89,"id":255,"is_courierstatistics":false,"is_platformstatistics":false,"makingTime":1504852395000,"orderEva":"不好","orderEvas":[],"orderReply":null,"order_id":"3282820170908023306","platform_id":null,"sendingTime":1504852460000,"takingTime":1504852395000,"totalPrice":41.26}],"rowCount":6}
         */

        private ListBean list;

        public ListBean getList()
        {
            return list;
        }

        public void setList(ListBean list)
        {
            this.list = list;
        }

        public static class ListBean
        {
            /**
             * currentPage : 1
             * nextPage : 2
             * pageSize : 1
             * pages : 6
             * previousPage : 1
             * result : [{"addTime":"2017-09-08 02:33:06","buyerMobile":"13069049021","buyerName":"用户1","courier_id":1,"courier_statistics_id":null,"deleteStatus":false,"gcs":[{"addTime":"2017-09-08 02:30:16","cart_type":null,"count":1,"deleteStatus":false,"goods_id":98584,"goods_name":"可可蛋糕","goods_photo":null,"id":230328,"price":89,"spec_info":null}],"goodsCount":1,"goods_amount":89,"id":255,"is_courierstatistics":false,"is_platformstatistics":false,"makingTime":1504852395000,"orderEva":"不好","orderEvas":[],"orderReply":null,"order_id":"3282820170908023306","platform_id":null,"sendingTime":1504852460000,"takingTime":1504852395000,"totalPrice":41.26}]
             * rowCount : 6
             */

            private int currentPage;
            private int nextPage;
            private int pageSize;
            private int pages;
            private int previousPage;
            private int rowCount;
            private LinkedList<ResultBean> result;

            public int getCurrentPage()
            {
                return currentPage;
            }

            public void setCurrentPage(int currentPage)
            {
                this.currentPage = currentPage;
            }

            public int getNextPage()
            {
                return nextPage;
            }

            public void setNextPage(int nextPage)
            {
                this.nextPage = nextPage;
            }

            public int getPageSize()
            {
                return pageSize;
            }

            public void setPageSize(int pageSize)
            {
                this.pageSize = pageSize;
            }

            public int getPages()
            {
                return pages;
            }

            public void setPages(int pages)
            {
                this.pages = pages;
            }

            public int getPreviousPage()
            {
                return previousPage;
            }

            public void setPreviousPage(int previousPage)
            {
                this.previousPage = previousPage;
            }

            public int getRowCount()
            {
                return rowCount;
            }

            public void setRowCount(int rowCount)
            {
                this.rowCount = rowCount;
            }

            public LinkedList<ResultBean> getResult()
            {
                return result;
            }

            public void setResult(LinkedList<ResultBean> result)
            {
                this.result = result;
            }

            public static class ResultBean
            {
                /**
                 * addTime : 2017-09-08 02:33:06
                 * buyerMobile : 13069049021
                 * buyerName : 用户1
                 * courier_id : 1
                 * courier_statistics_id : null
                 * deleteStatus : false
                 * gcs : [{"addTime":"2017-09-08 02:30:16","cart_type":null,"count":1,"deleteStatus":false,"goods_id":98584,"goods_name":"可可蛋糕","goods_photo":null,"id":230328,"price":89,"spec_info":null}]
                 * goodsCount : 1
                 * goods_amount : 89
                 * id : 255
                 * is_courierstatistics : false
                 * is_platformstatistics : false
                 * makingTime : 1504852395000
                 * orderEva : 不好
                 * orderEvas : []
                 * orderReply : null
                 * order_id : 3282820170908023306
                 * platform_id : null
                 * sendingTime : 1504852460000
                 * takingTime : 1504852395000
                 * totalPrice : 41.26
                 */

                private String addTime;
                private String buyerMobile;
                private String buyerName;
                private int courier_id;
                private Object courier_statistics_id;
                private boolean deleteStatus;
                private int goodsCount;
                private Object goods_amount;
                private int id;
                private boolean is_courierstatistics;
                private boolean is_platformstatistics;
                private long makingTime;
                private String orderEva;
                private Object orderReply;
                private String order_id;
                private Object platform_id;
                private long sendingTime;
                private long takingTime;
                private double totalPrice;
                private List<GcsBean> gcs;
                private List<?> orderEvas;

                public String getAddTime()
                {
                    return addTime;
                }

                public void setAddTime(String addTime)
                {
                    this.addTime = addTime;
                }

                public String getBuyerMobile()
                {
                    return buyerMobile;
                }

                public void setBuyerMobile(String buyerMobile)
                {
                    this.buyerMobile = buyerMobile;
                }

                public String getBuyerName()
                {
                    return buyerName;
                }

                public void setBuyerName(String buyerName)
                {
                    this.buyerName = buyerName;
                }

                public int getCourier_id()
                {
                    return courier_id;
                }

                public void setCourier_id(int courier_id)
                {
                    this.courier_id = courier_id;
                }

                public Object getCourier_statistics_id()
                {
                    return courier_statistics_id;
                }

                public void setCourier_statistics_id(Object courier_statistics_id)
                {
                    this.courier_statistics_id = courier_statistics_id;
                }

                public boolean isDeleteStatus()
                {
                    return deleteStatus;
                }

                public void setDeleteStatus(boolean deleteStatus)
                {
                    this.deleteStatus = deleteStatus;
                }

                public int getGoodsCount()
                {
                    return goodsCount;
                }

                public void setGoodsCount(int goodsCount)
                {
                    this.goodsCount = goodsCount;
                }

                public Object getGoods_amount()
                {
                    return goods_amount;
                }

                public void setGoods_amount(Object goods_amount)
                {
                    this.goods_amount = goods_amount;
                }

                public int getId()
                {
                    return id;
                }

                public void setId(int id)
                {
                    this.id = id;
                }

                public boolean isIs_courierstatistics()
                {
                    return is_courierstatistics;
                }

                public void setIs_courierstatistics(boolean is_courierstatistics)
                {
                    this.is_courierstatistics = is_courierstatistics;
                }

                public boolean isIs_platformstatistics()
                {
                    return is_platformstatistics;
                }

                public void setIs_platformstatistics(boolean is_platformstatistics)
                {
                    this.is_platformstatistics = is_platformstatistics;
                }

                public long getMakingTime()
                {
                    return makingTime;
                }

                public void setMakingTime(long makingTime)
                {
                    this.makingTime = makingTime;
                }

                public String getOrderEva()
                {
                    return orderEva;
                }

                public void setOrderEva(String orderEva)
                {
                    this.orderEva = orderEva;
                }

                public Object getOrderReply()
                {
                    return orderReply;
                }

                public void setOrderReply(Object orderReply)
                {
                    this.orderReply = orderReply;
                }

                public String getOrder_id()
                {
                    return order_id;
                }

                public void setOrder_id(String order_id)
                {
                    this.order_id = order_id;
                }

                public Object getPlatform_id()
                {
                    return platform_id;
                }

                public void setPlatform_id(Object platform_id)
                {
                    this.platform_id = platform_id;
                }

                public long getSendingTime()
                {
                    return sendingTime;
                }

                public void setSendingTime(long sendingTime)
                {
                    this.sendingTime = sendingTime;
                }

                public long getTakingTime()
                {
                    return takingTime;
                }

                public void setTakingTime(long takingTime)
                {
                    this.takingTime = takingTime;
                }

                public double getTotalPrice()
                {
                    return totalPrice;
                }

                public void setTotalPrice(double totalPrice)
                {
                    this.totalPrice = totalPrice;
                }

                public List<GcsBean> getGcs()
                {
                    return gcs;
                }

                public void setGcs(List<GcsBean> gcs)
                {
                    this.gcs = gcs;
                }

                public List<?> getOrderEvas()
                {
                    return orderEvas;
                }

                public void setOrderEvas(List<?> orderEvas)
                {
                    this.orderEvas = orderEvas;
                }

                public static class GcsBean
                {
                    /**
                     * addTime : 2017-09-08 02:30:16
                     * cart_type : null
                     * count : 1
                     * deleteStatus : false
                     * goods_id : 98584
                     * goods_name : 可可蛋糕
                     * goods_photo : null
                     * id : 230328
                     * price : 89
                     * spec_info : null
                     */

                    private String addTime;
                    private Object cart_type;
                    private int count;
                    private boolean deleteStatus;
                    private int goods_id;
                    private String goods_name;
                    private Object goods_photo;
                    private int id;
                    private Object price;
                    private Object spec_info;

                    public String getAddTime()
                    {
                        return addTime;
                    }

                    public void setAddTime(String addTime)
                    {
                        this.addTime = addTime;
                    }

                    public Object getCart_type()
                    {
                        return cart_type;
                    }

                    public void setCart_type(Object cart_type)
                    {
                        this.cart_type = cart_type;
                    }

                    public int getCount()
                    {
                        return count;
                    }

                    public void setCount(int count)
                    {
                        this.count = count;
                    }

                    public boolean isDeleteStatus()
                    {
                        return deleteStatus;
                    }

                    public void setDeleteStatus(boolean deleteStatus)
                    {
                        this.deleteStatus = deleteStatus;
                    }

                    public int getGoods_id()
                    {
                        return goods_id;
                    }

                    public void setGoods_id(int goods_id)
                    {
                        this.goods_id = goods_id;
                    }

                    public String getGoods_name()
                    {
                        return goods_name;
                    }

                    public void setGoods_name(String goods_name)
                    {
                        this.goods_name = goods_name;
                    }

                    public Object getGoods_photo()
                    {
                        return goods_photo;
                    }

                    public void setGoods_photo(Object goods_photo)
                    {
                        this.goods_photo = goods_photo;
                    }

                    public int getId()
                    {
                        return id;
                    }

                    public void setId(int id)
                    {
                        this.id = id;
                    }

                    public Object getPrice()
                    {
                        return price;
                    }

                    public void setPrice(Object price)
                    {
                        this.price = price;
                    }

                    public Object getSpec_info()
                    {
                        return spec_info;
                    }

                    public void setSpec_info(Object spec_info)
                    {
                        this.spec_info = spec_info;
                    }
                }
            }
        }
    }
}
