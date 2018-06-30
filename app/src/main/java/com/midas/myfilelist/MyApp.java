package com.midas.myfilelist;

import android.app.Application;
import android.content.Context;

import com.midas.myfilelist.core.LocalDbCtrl;
import com.midas.myfilelist.core.SharedPrefCtrl;
import com.midas.myfilelist.core.WebReqCtrl;

/**
 * Created by taejun on 2018. 6. 20..
 */

public class MyApp extends Application
{

    /************************* Define *************************/

    /************************* Member *************************/
    public MyApp m_This = null;
    public Context m_Context = null;
    public WebReqCtrl m_WebReqCtrl = null;
    public SharedPrefCtrl m_SpCtrl = null;
    public LocalDbCtrl m_DbCtrl = null;
    public boolean m_bInit = false;
    /************************* Controller *************************/

    /************************* System Function *************************/
    //--------------------------------------------------------------
    //
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    /************************* 생성자 *************************/
    //--------------------------------------------------------------
    //
    public MyApp()
    {

    }
    //--------------------------------------------------------------
    //
    public MyApp(Context pContext)
    {
        if(pContext == null)
            return;

        if(m_This == null)
        {
            m_This = this;
            m_Context = pContext;
            init();
        }
    }
    /************************* User Function *************************/
    //--------------------------------------------------------------
    //
    public void init()
    {
        if(m_bInit == false)
        {
            if(m_WebReqCtrl == null)
                m_WebReqCtrl = new WebReqCtrl();

            if(m_SpCtrl == null)
            {
                m_SpCtrl = new SharedPrefCtrl();
                m_SpCtrl.Init(m_Context);
            }

            if (m_DbCtrl == null)
            {
                m_DbCtrl = new LocalDbCtrl();//Open Local Db
                m_DbCtrl.Open(m_Context);
            }

            m_bInit = true;
        }
    }

}
