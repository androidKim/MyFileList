package com.midas.myfilelist.core;

import android.content.Context;

import com.midas.myfilelist.MyApp;
import com.midas.myfilelist.structure.core.content;
import com.midas.myfilelist.structure.function.get_data.res_get_data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by taejun on 2018. 6. 20..
 */

public class WebReqCtrl
{

    /************************* Define *************************/
    public static String PAGE_URL = "";

    /************************* Member *************************/
    public MyApp m_App = null;
    public Context m_Context = null;
    public ArrayList<String> m_ContentList = new ArrayList<>();


    /************************* 생성자 *************************/
    //----------------------------------------------------------
    //
    public WebReqCtrl()
    {

    }
    //----------------------------------------------------------
    //
    public WebReqCtrl(MyApp myApp, Context pContext)
    {
        this.m_App = myApp;
        this.m_Context = pContext;
    }


    /************************* user function *************************/
    //----------------------------------------------------------
    //request
    private Document requestApi(String strUrl)
    {
        if(strUrl == null)
            return null;

        Document doc = null;

        try
        {
            URL url = new URL(strUrl);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //----------------------------------------------------------
    //get data
    public res_get_data getData()
    {
        res_get_data response = new res_get_data();

        Document doc = requestApi(PAGE_URL);

        String s = "";
        NodeList nodeList = doc.getElementsByTagName("content");

        for(int i = 0; i< nodeList.getLength(); i++)
        {

            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;

            NodeList idx = fstElmnt.getElementsByTagName("title");
            s += "title = "+  idx.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList gugun = fstElmnt.getElementsByTagName("url");
            s += "url = "+  gugun.item(0).getChildNodes().item(0).getNodeValue() +"\n";


            content pInfo = new content(idx.item(0).getChildNodes().item(0).getNodeValue(), gugun.item(0).getChildNodes().item(0).getNodeValue());
            response.contentList.add(pInfo);
        }

        return response;
    }
}
