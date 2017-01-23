package com.richmondcabinets.claytonrowley.pickingtablet.Panel;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 05/08/2016.
 */
public class RouteP implements Serializable{

    public String name;
    public PanelP panelP;
    public BoilerP boilerP;
    int drop;

    public RouteP()
    {
        name = "";
        panelP = new PanelP();
        boilerP = new BoilerP();
        drop = 0;
    }

    public void AddPanel(Panel p)
    {
        if(p.boiler)
            boilerP.AddOrder(p);
        else
            panelP.AddOrder(p);
    }
}
