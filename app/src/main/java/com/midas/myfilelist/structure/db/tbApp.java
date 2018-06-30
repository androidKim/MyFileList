package com.midas.myfilelist.structure.db;


/*
content database info
 */
public class tbApp
{
    /*************** Define ***************/

    /*************** Member ***************/
    public String status = null;//서비스중 여부

    /*************** 생성자 ***************/
    //------------------------------------------------
    //
    public tbApp()
    {

    }
    //------------------------------------------------
    //
    public tbApp(String status)
    {
        this.status = status;
    }
}
