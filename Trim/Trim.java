package com.richmondcabinets.claytonrowley.pickingtablet.Trim;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 09/08/2016.
 */
public class Trim implements Serializable{

    public String route;
    public String order;
    public String product;
    public String desc;
    public String analysisA;
    public String consumerUnit;
    public String warehouse;
    public int total;
    public boolean scanned;

    public Trim()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        analysisA = "";
        consumerUnit = "";
        warehouse = "";
        total = 0;
        scanned = false;
    }

    public Trim(String r, String o, String p, String d, String a, String c, String w, int t, boolean s)
    {
        route = r;
        order = o;
        product = p;
        desc = d;
        analysisA = a;
        consumerUnit = c;
        warehouse = w;
        total = t;
        scanned = s;
    }
}
