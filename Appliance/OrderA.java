package com.richmondcabinets.claytonrowley.pickingtablet.Appliance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 03/08/2016.
 */
public class OrderA implements Serializable {

    public String name;
    public List<Appliance> appliances;
    public int count;
    public boolean picked;

    public OrderA()
    {
        count = 0;
        name = "";
        appliances = new ArrayList<>();
        picked = false;
    }

    public boolean AddAppliance(Appliance a)
    {
        Appliance existingA = FindAppliance(a.product);
        if(existingA.product.equals("NONE"))
        {
            appliances.add(a);
            count++;
            return true;
        }
        else
        {
            existingA.total += a.total;
            return false;
        }
    }

    private Appliance FindAppliance(String appliance)
    {
        Appliance a = new Appliance();
        a.product = "NONE";
        for (Appliance app : appliances) {
            if (app.product.equals(appliance))
                return app;
        }
        return a;
    }
}
