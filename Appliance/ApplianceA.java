package com.richmondcabinets.claytonrowley.pickingtablet.Appliance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 03/08/2016.
 */
public class ApplianceA implements Serializable {

    public List<OrderA> orders;
    public int count;
    public String key;

    public ApplianceA()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "W";
    }

    public boolean AddOrder(Appliance a)
    {
        OrderA existingA = FindOrder(a.order);
        if(existingA.name.equals("NONE"))
        {
            OrderA tempO = new OrderA();
            tempO.name = a.order;
            tempO.AddAppliance(a);
            orders.add(tempO);
            count++;
            return true;
        }
        else
        {
            existingA.AddAppliance(a);
            return false;
        }
    }

    public OrderA FindOrder(String order)
    {
        OrderA o = new OrderA();
        o.name = "NONE";
        for(OrderA ord:orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }
}
