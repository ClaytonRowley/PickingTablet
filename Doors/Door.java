package com.richmondcabinets.claytonrowley.pickingtablet.Doors;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 08/07/2016.
 */
public class Door implements Serializable{

    public String route;
    public String order;
    public String product;
    public String desc;
    public String drawing;
    public String analysisA;
    public String consumerUnit;
    public String warehouse;
    public boolean drill;
    public int total;
    public boolean scanned;

    public Door()
    {
        route = "";
        order = "";
        product = "";
        desc = "";
        drawing = "";
        analysisA = "";
        consumerUnit = "";
        warehouse = "";
        drill = false;
        total = 0;
        scanned = false;
    }

    public Door(String r, String o, String p, String d, String dr, String a, String c, String w, boolean dri, int t, boolean s) {
        route = r;
        order = o;
        product = p;
        desc = d;
        drawing = dr;
        analysisA = a;
        consumerUnit = c;
        warehouse = w;
        drill = dri;
        total = t;
        scanned = s;
    }
}
