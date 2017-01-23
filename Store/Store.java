package com.richmondcabinets.claytonrowley.pickingtablet.Store;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 15/08/2016.
 */
public class Store implements Serializable{

    public String route;
    public String order;
    public String product;
    public String desc;
    public String analysisA;
    public String warehouse;
    public int total;
    public boolean scanned;

    public Store()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        analysisA = "";
        warehouse = "";
        total = 0;
        scanned = false;
    }

    public Store(String r, String o, String p, String d, String a, String w, int t, boolean s)
    {
        route = r;
        order = o;
        product = p;
        desc = d;
        analysisA = a;
        warehouse = w;
        total = t;
        scanned = s;
    }
}
