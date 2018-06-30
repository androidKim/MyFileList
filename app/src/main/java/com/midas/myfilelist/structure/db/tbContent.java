package com.midas.myfilelist.structure.db;


import com.midas.myfilelist.structure.core.content;

/*
content database info
 */
public class tbContent
{
    /*************** Define ***************/

    /*************** Member ***************/
    public String title = null;//파일명
    public String url = null;//
    public String status = null;//다운로드상태
    public long cur_size = 0;
    public long full_size = 0;


    /*************** 생성자 ***************/
    //------------------------------------------------
    //
    public tbContent()
    {

    }
    //------------------------------------------------
    //
    public tbContent(content pInfo)
    {
        if(pInfo == null)
            return;

        this.title = pInfo.title;
        this.url = pInfo.url;
    }

    //------------------------------------------------
    //
    public tbContent(String title, String url, String status, long cur_size, long full_size)
    {
        this.title = title;
        this.url = url;
        this.status = status;
        this.cur_size = cur_size;
        this.full_size = full_size;
    }
}
