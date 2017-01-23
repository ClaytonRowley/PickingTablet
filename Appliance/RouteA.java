package com.richmondcabinets.claytonrowley.pickingtablet.Appliance;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 03/08/2016.
 */
public class RouteA implements Serializable{

    public String name;
    public ApplianceA applianceA;
    public SinkA sinkA;
    int drop;

    public RouteA()
    {
        name = "";
        applianceA = new ApplianceA();
        sinkA = new SinkA();
        drop = 0;
    }

    public void AddAppliance(Appliance a)
    {
        switch (a.analysisA)
        {
            case "APPLIANCE":
                applianceA.AddOrder(a);
                break;
            case "SINK":
                sinkA.AddOrder(a);
                break;
        }
    }
}
