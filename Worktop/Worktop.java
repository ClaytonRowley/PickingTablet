package com.richmondcabinets.claytonrowley.pickingtablet.Worktop;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 02/08/2016.
 */
public class Worktop implements Serializable {

    public String route;
    public String order;
    public String product;
    public String desc;
    public String analysisA;
    public String consumer;
    public String warehouse;
    public int total;
    public String cutDown;
    public boolean scanned;

    public Worktop()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        analysisA = "";
        consumer = "";
        warehouse = "";
        total = 0;
        cutDown = "";
        scanned = false;
    }

    public Worktop(String r, String o, String p, String d, String a, String c, String w, int t, String cd, boolean s)
    {
        route = r;
        order = o;
        product = p;
        desc = d;
        analysisA = a;
        consumer = c;
        warehouse = w;
        total = t;
        cutDown = cd;
        scanned = s;
    }
}
