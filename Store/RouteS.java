package com.richmondcabinets.claytonrowley.pickingtablet.Store;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 15/08/2016.
 */
public class RouteS implements Serializable{

    public String name;
    public StoreS storeS;
    public HandleS handleS;
    int drop;

    public RouteS()
    {
        name = "";
        storeS = new StoreS();
        handleS = new HandleS();
        drop = 0;
    }

    public void AddStore(Store s)
    {
        switch (s.analysisA)
        {
            case "HANDLE":
                handleS.AddOrder(s);
                break;
            default:
                storeS.AddOrder(s);
                break;
        }
    }
}
