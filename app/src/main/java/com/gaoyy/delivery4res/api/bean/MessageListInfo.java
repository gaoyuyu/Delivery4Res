package com.gaoyy.delivery4res.api.bean;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2017/9/15 0015.
 */

public class MessageListInfo
{


    /**
     * body : {"list":{"currentPage":1,"nextPage":2,"pageSize":1,"pages":6,"previousPage":1,"result":[{"addTime":"2017-09-13 21:13:58","content":"你好你好你好","deleteStatus":false,"id":266,"status":0,"type":0}],"rowCount":6}}
     * errorCode : -1
     * msg : 操作成功
     * success : true
     */

    private BodyBean body;
    private String errorCode;
    private String msg;
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

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
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
         * list : {"currentPage":1,"nextPage":2,"pageSize":1,"pages":6,"previousPage":1,"result":[{"addTime":"2017-09-13 21:13:58","content":"你好你好你好","deleteStatus":false,"id":266,"status":0,"type":0}],"rowCount":6}
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
             * result : [{"addTime":"2017-09-13 21:13:58","content":"你好你好你好","deleteStatus":false,"id":266,"status":0,"type":0}]
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
                 * addTime : 2017-09-13 21:13:58
                 * content : 你好你好你好
                 * deleteStatus : false
                 * id : 266
                 * status : 0
                 * type : 0
                 */

                private String addTime;
                private String content;
                private boolean deleteStatus;
                private int id;
                private int status;
                private int type;

                public String getAddTime()
                {
                    return addTime;
                }

                public void setAddTime(String addTime)
                {
                    this.addTime = addTime;
                }

                public String getContent()
                {
                    return content;
                }

                public void setContent(String content)
                {
                    this.content = content;
                }

                public boolean isDeleteStatus()
                {
                    return deleteStatus;
                }

                public void setDeleteStatus(boolean deleteStatus)
                {
                    this.deleteStatus = deleteStatus;
                }

                public int getId()
                {
                    return id;
                }

                public void setId(int id)
                {
                    this.id = id;
                }

                public int getStatus()
                {
                    return status;
                }

                public void setStatus(int status)
                {
                    this.status = status;
                }

                public int getType()
                {
                    return type;
                }

                public void setType(int type)
                {
                    this.type = type;
                }
            }
        }
    }
}
