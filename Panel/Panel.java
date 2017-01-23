package com.richmondcabinets.claytonrowley.pickingtablet.Panel;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 05/08/2016.
 */
public class Panel implements Serializable{

    public String route;
    public String order;
    public String product;
    public String desc;
    public String analysisA;
    public String consumerUnit;
    public String warehouse;
    public boolean boiler;
    public int total;
    public boolean scanned;

    public Panel()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        analysisA = "";
        consumerUnit = "";
        warehouse = "";
        total = 0;
        boiler = false;
        scanned = false;
    }

    public Panel(String r, String o, String p, String d, String a, String c, String w, int t, boolean b, boolean s)
    {
        route = r;
        order = o;
        product = p;
        desc = d;
        analysisA = a;
        consumerUnit = c;
        warehouse = w;
        total = t;
        boiler = b;
        scanned = s;
    }
}
