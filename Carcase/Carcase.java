package com.richmondcabinets.claytonrowley.pickingtablet.Carcase;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 26/07/2016.
 */
public class Carcase implements Serializable{

    public String route;
    public String order;
    public String product;
    public String desc;
    public String drawing;
    public String analysisA;
    public String analysisD;
    public String analysisE;
    public String category;
    public String consumer;
    public String warehouse;
    public int total;
    public String edged;
    public boolean scanned;

    public Carcase()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        drawing = "";
        analysisA = "";
        analysisD = "";
        analysisE = "";
        category = "";
        consumer = "";
        warehouse = "";
        total = 0;
        edged = "NO";
        scanned = false;
    }

    public Carcase(String r, String o, String p, String d, String dr, String a, String ad, String e, String c, String co, String w, int t, boolean s)
    {
        route = r;
        order = o;
        product = p;
        desc = d;
        drawing = dr;
        analysisA = a;
        analysisD = ad;
        analysisE = e;
        category = c;
        consumer = co;
        warehouse = w;
        total = t;
        edged = "NO";
        scanned = s;
    }
}
