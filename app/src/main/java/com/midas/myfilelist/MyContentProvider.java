package com.midas.myfilelist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.midas.myfilelist.structure.db.tbApp;
import com.midas.myfilelist.structure.db.tbContent;

public class MyContentProvider extends ContentProvider
{
    /************************ Define ************************/
    public static final String PROVIDER_NAME = "com.midas.myfilelist.MyContentProvider";
    public static final int GET_STATUS = 1;
    public static final int SET_STATUS = 2;
    public static final int SET_CONTENT = 3;
    public static UriMatcher m_UriMatcher = null;
    /************************ Member ************************/
    private MyApp m_App = null;
    private Context m_Context = null;

    @Override
    public boolean onCreate()
    {
        m_UriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        m_UriMatcher.addURI(PROVIDER_NAME, "get_status", GET_STATUS);
        m_UriMatcher.addURI(PROVIDER_NAME, "set_status", SET_STATUS);
        m_UriMatcher.addURI(PROVIDER_NAME, "set_content", SET_CONTENT);

        m_Context = getContext();
        m_App = new MyApp(m_Context);

        return (m_App.m_DbCtrl.m_Db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String strStatus, @Nullable String[] strings1, @Nullable String s1)
    {
        switch (m_UriMatcher.match(uri))
        {
            case GET_STATUS:
                Cursor pCursor = m_App.m_DbCtrl.getStatusCursorInfo(m_App.m_DbCtrl.m_Db, getContext(), uri);
                if(pCursor != null)
                    return pCursor;
                break;
            case SET_STATUS:
                if(strStatus == null)
                    strStatus = "N";

                tbApp pInfo = new tbApp(strStatus);
                m_App.m_DbCtrl.setStatusInfo(m_App.m_DbCtrl.m_Db, pInfo);
                break;
            case SET_CONTENT:
                if(strings == null)
                    return null;

                //public tbContent(String title, String url, String status, long cur_size, long full_size)
                String title = strings[0];
                String url = strings[1];
                String status = strings[2];
                String cur_size = strings[3];
                String full_size = strings[4];

                tbContent contentInfo = new tbContent();

                m_App.m_DbCtrl.setContentInfo(m_App.m_DbCtrl.m_Db, contentInfo);
                break;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings)
    {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings)
    {
        return 0;
    }
}
