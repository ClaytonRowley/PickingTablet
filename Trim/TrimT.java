package com.richmondcabinets.claytonrowley.pickingtablet.Trim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 09/08/2016.
 */
public class TrimT implements Serializable {

    public List<OrderT> orders;
    public int count;
    public String key;

    public TrimT()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "Z";
    }

    public boolean AddOrder(Trim t)
    {
        OrderT existingT = FindOrder(t.order);
        if(existingT.name.equals("NONE"))
        {
            OrderT tempT = new OrderT();
            tempT.name = t.order;
            tempT.AddTrim(t);
            orders.add(tempT);
            count++;
            return true;
        }
        else
        {
            existingT.AddTrim(t);
            return false;
        }
    }

    public OrderT FindOrder(String order)
    {
        OrderT o = new OrderT();
        o.name = "NONE";
        for (OrderT ord : orders)
        {
            if (ord.name.equals(order))
                return ord;
        }
        return o;
    }

    public void RemoveTrim(String trim, String order) {
        OrderT o = FindOrder(order);
        o.RemoveTrim(trim);
    }
}
