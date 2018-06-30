package com.midas.myfilelist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.midas.myfilelist.MyApp;
import com.midas.myfilelist.R;
import com.midas.myfilelist.structure.db.tbApp;
import com.midas.myfilelist.structure.db.tbContent;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter
{
    /****************************** Define ******************************/

    /****************************** Member ******************************/
    private MyApp m_App = null;
    private Context m_Context = null;
    private LayoutInflater m_LayoutInflater = null;
    private ListAdapter m_This = null;
    private ArrayList<tbContent> m_Items = null;
    private tbApp m_AppInfo = null;
    /****************************** Event ******************************/
    //---------------------------------------------------------------------------------------------------
    //
    public ListAdapter(Context context, int resource, ArrayList<tbContent> objects, MyApp pApp, tbApp pInfo)
    {
        super(context, resource, objects);
        this.m_App = pApp;
        this.m_Context = context;
        this.m_This = this;
        this.m_Items = objects;
        this.m_LayoutInflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.m_AppInfo = pInfo;
    }

    //---------------------------------------------------------------------------------------------------
    //
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        View pView = convertView;

        // Get Item Data
        tbContent pInfo = m_Items.get(position);

        if( pInfo == null )
            return pView;

        if(pView == null)
        {
            pView = m_LayoutInflater.inflate(R.layout.row_list_item, parent, false);
            holder = new ViewHolder();
            pView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)pView.getTag();
            holder.init();
        }
        settingRowView(pView, pInfo, holder);

        return pView;
    }
    /****************************** Function ******************************/
    //---------------------------------------------------------------------------------------------------
    // set Contents Data
    public void setData(tbContent pInfo)
    {
        m_Items.add(pInfo);
    }
    //---------------------------------------------------------------------------------------------------
    // AddData
    public void addData(ArrayList<tbContent> array)
    {
        m_Items.addAll(array);
    }

    //---------------------------------------------------------------------------------------------------
    // Refresh UI
    public void RefreshList()
    {
        m_This.notifyDataSetChanged();
    }
    //---------------------------------------------------------------------------------------------------
    //
    public void settingRowView(View pView, tbContent pInfo, ViewHolder holder)
    {
        //null Check
        if(pView==null || pInfo==null)
            return;

        holder.tv_Title = (TextView)pView.findViewById(R.id.tv_Title);
        holder.tv_Status = (TextView)pView.findViewById(R.id.tv_Status);
        holder.btn_Download = (Button)pView.findViewById(R.id.btn_Download);
        holder.v_Dim = (View)pView.findViewById(R.id.v_Dim);

        if(pInfo.title != null)
            holder.tv_Title.setText(pInfo.title);

        holder.btn_Download.setTag(pInfo);
        holder.btn_Download.setOnClickListener(onClickDownload);


        //
        if(m_AppInfo != null)
        {
            if(m_AppInfo.status != null)
            {
                if(m_AppInfo.status.equals("Y"))
                {
                    holder.v_Dim.setVisibility(View.VISIBLE);
                    holder.btn_Download.setClickable(false);
                }
                else
                {
                    holder.v_Dim.setVisibility(View.GONE);
                    holder.btn_Download.setClickable(true);
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------------
    //
    public void setStatusInfo(tbApp pInfo)
    {
        if(pInfo == null)
            return;

        this.m_AppInfo = pInfo;
    }
    /**************************** Listener ****************************/
    //---------------------------------------------------------------------------------------------------
    //
    View.OnClickListener onClickDownload = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            tbContent pInfo = (tbContent)view.getTag();

            if(pInfo == null)
                return;

            String title = pInfo.title;
            String url = pInfo.url;

            //open downloaderApp and Send url
            Intent pIntent =  m_Context.getPackageManager().getLaunchIntentForPackage("com.midas.mydownloader");
            pIntent.putExtra("title", title);
            pIntent.putExtra("url", url);
            m_Context.startActivity(pIntent);
        }
    };
    /**************************** interface ****************************/
    /**************************** InnerClass ****************************/
    //---------------------------------------------------------------------------------------------------
    //
    static class ViewHolder
    {
        View v_Dim;
        TextView tv_Title;
        TextView tv_Status;
        Button btn_Download;

        public void init()
        {
            if (tv_Title != null)
                tv_Title.setText(null);

            if(tv_Status != null)
                tv_Status.setText(null);
        }
    }
}
