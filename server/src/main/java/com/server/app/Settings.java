package com.server.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Settings
{
    File                   file = null;
    DocumentBuilderFactory dbf  = null;
    DocumentBuilder        db   = null;
    Document               dc   = null;

    Map<String, String>  s_map;
    Map<String, Integer> i_map;

    static String path = "settings.xml";

    public Settings()
    {
        this(path);
    }

    public Settings(String ph)
    {
        if (ph == null || ph.length() < 1)
            ph = path;

        try {
            File file = new File(ph);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dc = db.parse(file);

            Node n = dc.getElementsByTagName("settings").item(0);

            NodeList snodes = n.getChildNodes();

            s_map = new HashMap<String, String>();
            i_map = new HashMap<String, Integer>();

            for(int i = 0; i < ((NodeList) snodes).getLength(); i++)
            {
                Node sn = snodes.item(i);

                String name = sn.getNodeName();
                String cont = sn.getTextContent();

                if (sn.hasAttributes()) {
                    Node an = sn.getAttributes().getNamedItem("type");
                } else {
                    s_map.put(name, cont);
                }
            }

        } catch(Exception e) {

        }
    }
}
