package com.richmondcabinets.claytonrowley.pickingtablet.Trim;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 09/08/2016.
 */
public class RouteT implements Serializable {

    public String name;
    public TrimT trimT;
    int drop;

    public RouteT()
    {
        name = "";
        trimT = new TrimT();
        drop = 0;
    }

    public void AddTrim(Trim t)
    {
        trimT.AddOrder(t);
    }

    public void RemoveTrim(String trim, String order)
    {
        trimT.RemoveTrim(trim, order);
    }
}
