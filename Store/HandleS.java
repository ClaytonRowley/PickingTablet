package com.richmondcabinets.claytonrowley.pickingtablet.Store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 15/08/2016.
 */
public class HandleS  implements Serializable{

    public List<OrderS> orders;
    public int count;
    public String key;

    public HandleS()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "c";
    }

    public boolean AddOrder(Store s)
    {
        OrderS existingA = FindOrder(s.order);
        if(existingA.name.equals("NONE"))
        {
            OrderS tempO = new OrderS();
            tempO.name = s.order;
            tempO.AddStore(s);
            orders.add(tempO);
            count++;
            return true;
        }
        else
        {
            existingA.AddStore(s);
            return false;
        }
    }

    public OrderS FindOrder(String order)
    {
        OrderS o = new OrderS();
        o.name = "NONE";
        for(OrderS ord:orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }
}
