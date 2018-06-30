package com.midas.myfilelist.ui.act;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.midas.myfilelist.MyApp;
import com.midas.myfilelist.R;
import com.midas.myfilelist.structure.core.content;
import com.midas.myfilelist.structure.db.tbApp;
import com.midas.myfilelist.structure.db.tbContent;
import com.midas.myfilelist.structure.function.get_data.res_get_data;
import com.midas.myfilelist.ui.adapter.ListAdapter;

import java.util.ArrayList;

public class ActMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /******************* Define *******************/

    /******************* Member *******************/
    public MyApp m_App = null;
    public Context m_Context = null;
    public res_get_data m_ResGetData = null;
    public ListAdapter m_Adapter = null;
    /******************* Controller *******************/
    public SwipeRefreshLayout m_ly_SwipeRefresh = null;
    public ListView m_ListlView = null;

    /******************* System Function *******************/
    //-------------------------------------------------------
    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        m_Context = this;
        m_App = new MyApp(m_Context);
        initValue();
        recvIntentData();
        String strTest = m_App.m_SpCtrl.getSpRunStatus();
        setInitLayout();
    }
    //-------------------------------------------------------
    //
    @Override
    protected void onResume()
    {
        super.onResume();
        tbApp pAppInfo = m_App.m_DbCtrl.getStatusInfo(m_App.m_DbCtrl.m_Db);//app status info;
        if(pAppInfo == null)
            return;

        if(m_Adapter != null)
        {
            m_Adapter.setStatusInfo(pAppInfo);
            m_Adapter.notifyDataSetChanged();
        }
    }

    /******************* User Function *******************/
    //-------------------------------------------------------
    //
    public void initValue()
    {

    }

    //-------------------------------------------------------
    //
    public void recvIntentData()
    {

    }

    //-------------------------------------------------------
    //
    public void setInitLayout()
    {
        m_ly_SwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.ly_SwipeRefresh);
        m_ListlView = (ListView)findViewById(R.id.listView);

        //event..
        m_ly_SwipeRefresh.setOnRefreshListener(this);

        settingView();
        getXmlDataProc();
    }

    //-------------------------------------------------------
    //
    public void settingView()
    {
        getXmlDataProc();
    }

    //-------------------------------------------------------
    //get Web Xml Data
    public void getXmlDataProc()
    {
        if(true)   //check network
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //doInBackground..
                    m_ResGetData = m_App.m_WebReqCtrl.getData();

                    if(m_ResGetData != null)
                    {
                        //Main UiThread..
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                settingListView(m_ResGetData);
                            }
                        });
                    }
                    else
                    {

                    }
                }
            }).start();
        }
        else
        {

        }
    }

    //-------------------------------------------------------
    //
    public void settingListView(res_get_data pRes)
    {
        if(pRes == null)
            return;

        if(pRes.contentList == null)
            return;

        //setDataToLocal
        for(int i=0; i<pRes.contentList.size(); i++)
        {
            content pInfo  = pRes.contentList.get(i);
            tbContent dbInfo = new tbContent(pInfo);

            //insert
            m_App.m_DbCtrl.setContentInfo(m_App.m_DbCtrl.m_Db, dbInfo);
        }

        //getDataFromLocal
        ArrayList<tbContent> pArray = m_App.m_DbCtrl.getContentList(m_App.m_DbCtrl.m_Db);
        tbApp pAppInfo = m_App.m_DbCtrl.getStatusInfo(m_App.m_DbCtrl.m_Db);//app status info
        m_Adapter = new ListAdapter(m_Context, 0, pArray, m_App, pAppInfo);
        m_ListlView.setAdapter(m_Adapter);
        m_Adapter.notifyDataSetChanged();
    }
    //-------------------------------------------------------
    //
    public void setRefresh()
    {
        m_ResGetData = null;//noti list
        m_Adapter = null;
        getXmlDataProc();
        m_ly_SwipeRefresh.setRefreshing(false);
    }
    /************************* listener *************************/
    //-------------------------------------------------------
    //swiperListener callback
    @Override
    public void onRefresh()
    {
        setRefresh();
    }
}
