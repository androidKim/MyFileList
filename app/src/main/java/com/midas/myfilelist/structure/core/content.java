package com.midas.myfilelist.structure.core;

/**
 * Created by taejun on 2018. 6. 21..
 */

public class content
{
    /*********************** Define ***********************/
    //다운로드상태..
    public static String TYPE_DOWN_BEFORE = "B";//다운로드전
    public static String TYPE_DOWN_ING = "I";//다운로드중
    public static String TYPE_DOWN_COMPLETE = "C";//다운로드완료
    /*********************** Member ***********************/
    public String title = null;//타이틀
    public String url = null;//컨텐츠 url
    //----------------------------------------------
    //
    public content(String title, String url)
    {
        this.title = title;
        this.url = url;

    }
}
