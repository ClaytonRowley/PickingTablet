package com.richmondcabinets.claytonrowley.pickingtablet.Panel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 05/08/2016.
 */
public class PanelP implements Serializable {

    public List<OrderP> orders;
    public int count;
    public String key;

    public PanelP()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "Y";
    }

    public boolean AddOrder(Panel p)
    {
        OrderP existingP = FindOrder(p.order);
        if(existingP.name.equals("NONE"))
        {
            OrderP tempP = new OrderP();
            tempP.name = p.order;
            tempP.AddPanel(p);
            orders.add(tempP);
            count++;
            return true;
        }
        else
        {
            existingP.AddPanel(p);
            return false;
        }
    }

    public OrderP FindOrder(String order)
    {
        OrderP o = new OrderP();
        o.name = "NONE";
        for (OrderP ord : orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }
}
