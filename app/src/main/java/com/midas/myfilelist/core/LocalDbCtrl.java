package com.midas.myfilelist.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.midas.myfilelist.structure.db.tbApp;
import com.midas.myfilelist.structure.db.tbContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LocalDbCtrl
{
    /**************************** Define ***************************/
    //Local Database
    final private static String ROOT_DIR = "/data/data/com.midas.myfilelist/databases/";
    final private static String LOCAL_DB_NAME = "mydb";
    final private static int LOCAL_DB_VERSION = 1;

    //table
    final public static String TB_APP = "tb_app";//
    final public static String TB_CONTENT = "tb_content";//
    /**************************** Member ***************************/
    public SQLiteDatabase m_Db = null;
    public LocalDatabaseHelper m_DbHelper = null;
    //---------------------------------------------------------------------------------------------------
    // Local DB Helper
    public LocalDbCtrl()
    {

    }

    public LocalDbCtrl(Context pContext)
    {
        Open(pContext);
    }

    //---------------------------------------------------------------------------------------------------
    // OpenLocalDb
    public boolean Open(Context context)
    {
        if (context == null)
            return false;

        m_DbHelper = new LocalDatabaseHelper(context);
        m_Db = m_DbHelper.getWritableDatabase();

        return isOpen();
    }

    //---------------------------------------------------------------------------------------------------
    // Check LocalDb Open
    public boolean isOpen()
    {
        if (m_DbHelper == null || m_Db == null || m_Db.isOpen() == false)
            return false;

        return true;
    }

    //---------------------------------------------------------------------------------------------------
    // Check LocalDb Close
    public boolean Close()
    {
        if (m_DbHelper == null)
            return false;

        m_DbHelper.close();
        m_DbHelper = null;
        m_Db = null;

        return true;
    }
    //---------------------------------------------------------------------------------------------------
    //
    public static void setDatabases(Context context)
    {
        File folder = new File(ROOT_DIR);
        if(folder.exists())
        {

        }
        else
        {
            folder.mkdirs();
        }
        AssetManager assetManager = context.getResources().getAssets(); //ctx가 없으면 assets폴더를 찾지 못한다.
        File outfile = new File(ROOT_DIR + LOCAL_DB_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try
        {
            is = assetManager.open(LOCAL_DB_NAME, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0)
            {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            }
            else
            {

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //---------------------------------------------------------------------------------------------------
    // Local DB Helper
    private static class LocalDatabaseHelper extends SQLiteOpenHelper
    {
        //---------------------------------------------------------------------------------------------------
        //
        public LocalDatabaseHelper(Context context)
        {
            super(context, LOCAL_DB_NAME, null, LOCAL_DB_VERSION);
            setDatabases(context);
        }

        //---------------------------------------------------------------------------------------------------
        // 최초 DB 파일 생성
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(String.format("CREATE TABLE %s ("
                            + "status TEXT"
                            + ");",
                            TB_APP));

            db.execSQL(String.format("CREATE TABLE %s ("
                            + "title TEXT, "
                            + "url TEXT, "
                            + "status TEXT, "
                            + "cur_size INTEGER, "
                            + "full_size INTEGER"
                            + ");",
                            TB_CONTENT));
        }

        //---------------------------------------------------------------------------------------------------
        // DB 업그레이드
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // Clear All Data
            DropAllTables(db);

            // Create All New Table
            onCreate(db);
        }

        //---------------------------------------------------------------------------------------------------
        //
        public void DropAllTables(SQLiteDatabase db)
        {
            // System
            db.execSQL(String.format("DROP TABLE IF EXISTS %s;", TB_APP));
            db.execSQL(String.format("DROP TABLE IF EXISTS %s;", TB_CONTENT));
        }
    }

    /**************************** Query Function ***************************/
    //---------------------------------------------------------------------------------------------------
    // Insert
    public static boolean setContentInfo(SQLiteDatabase db, tbContent info)
    {
        if(db == null || info == null)
            return false;

        if(existContentInfo(db, info))//Exist Update
        {
            return updateMemberInfo(db, info);
        }
        else//Insert
        {
            return insertContent(db, info);
        }
    }
    //---------------------------------------------------------------------------------------------------
    // Exist Info
    private  static boolean existContentInfo(SQLiteDatabase db, tbContent info)
    {
        if(db == null || info == null)
            return false;

        String query = String.format("select * from %s where title='%s'", TB_CONTENT, info.title);

        return Exists(db, query);
    }
    //---------------------------------------------------------------------------------------------------
    // Exist
    public static boolean Exists(SQLiteDatabase db, String strQuery)
    {
        if(db == null || strQuery == null || strQuery.length() <= 0)
            return false;

        boolean bExists = true;
        Cursor result = db.rawQuery(strQuery, null);

        try
        {
            if(result == null || result.getCount() <= 0)
            {
                bExists = false;
            }
        }
        finally
        {
            result.close();
        }

        return bExists;
    }
    //---------------------------------------------------------------------------------------------------
    // get content list
    public static ArrayList<tbContent> getContentList(SQLiteDatabase db)
    {
        String query = String.format("select * from %s", TB_CONTENT);

        ArrayList<tbContent> arrResult = null;//Result

        Cursor result = db.rawQuery(query, null);
        try
        {
            if(result == null)
                return null;

            if(result.moveToFirst() == false)
                return null;

            arrResult = new ArrayList<tbContent>();
            tbContent pInfo = null;

            pInfo = new tbContent(result.getString(0), result.getString(1), result.getString(2), result.getLong(3), result.getLong(4));
            arrResult.add(pInfo);

            while(result.moveToNext())
            {
                pInfo = new tbContent(result.getString(0), result.getString(1), result.getString(2), result.getLong(3), result.getLong(4));
                arrResult.add(pInfo);
            }
        }
        finally
        {
            result.close();
        }
        return arrResult;
    }

    //---------------------------------------------------------------------------------------------------
    // Insert
    private static boolean insertContent(SQLiteDatabase db, tbContent info)
    {
        if (db == null || info == null)
            return false;

        String query = String.format("insert into %s values('%s', '%s', '%s', %d, %d);",
                TB_CONTENT, info.title, info.url, info.status, info.cur_size, info.full_size);

        db.execSQL(query);

        return true;
    }

    //---------------------------------------------------------------------------------------------------
    // Update
    private static boolean updateMemberInfo(SQLiteDatabase db, tbContent info)
    {
        if(db == null || info == null)
            return false;

        String query = String.format("update %s set url='%s', status='%s', cur_size=%d, full_size=%d where title='%s';",
                TB_CONTENT, info.url, info.status, info.cur_size, info.full_size, info.title);

        db.execSQL(query);

        return true;
    }

    //---------------------------------------------------------------------------------------------------
    //get Status
    public static Cursor getStatusCursorInfo(SQLiteDatabase db, Context pContext, Uri uri)
    {
        String query = String.format("select * from %s limit 0,1;", TB_APP);

        Cursor result = db.rawQuery(query, null);
        try
        {
            if(result == null)
                return null;

            if(result.moveToFirst() == false)
                return null;

            result.setNotificationUri(pContext.getContentResolver(), uri);
        }
        finally
        {
            //result.close();
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------
    //get Status
    public static tbApp getStatusInfo(SQLiteDatabase db)
    {
        String query = String.format("select * from %s limit 0,1;", TB_APP);

        tbApp pResult = null;

        Cursor result = db.rawQuery(query, null);
        try
        {
            if(result == null)
                return null;

            if(result.moveToFirst() == false)
                return null;

            pResult = new tbApp(result.getString(0));
        }
        finally
        {
            result.close();
        }
        return pResult;
    }

    //---------------------------------------------------------------------------------------------------
    //
    public static boolean setStatusInfo(SQLiteDatabase db, tbApp info)
    {
        if(db == null || info == null)
            return false;

        if(existStatus(db, info))//Exist Update
        {
            return updateStatus(db, info);
        }
        else//Insert
        {
            return insertStatus(db, info);
        }
    }

    //---------------------------------------------------------------------------------------------------
    // Exist Info
    private  static boolean existStatus(SQLiteDatabase db, tbApp info)
    {
        if(db == null || info == null)
            return false;

        String query = String.format("select * from %s limit 0, 1;", TB_APP);

        return Exists(db, query);
    }
    //---------------------------------------------------------------------------------------------------
    //
    private static boolean insertStatus(SQLiteDatabase db, tbApp info)
    {
        if (db == null || info == null)
            return false;

        String query = String.format("insert into %s values('%s');", TB_APP, info.status);

        db.execSQL(query);

        return true;
    }

    //---------------------------------------------------------------------------------------------------
    // Update
    private static boolean updateStatus(SQLiteDatabase db, tbApp info)
    {
        if(db == null || info == null)
            return false;

        String query = String.format("update %s set status='%s';", TB_APP, info.status);

        db.execSQL(query);
        return true;
    }
}
