package com.richmondcabinets.claytonrowley.pickingtablet.Carcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 17/08/2016.
 */
public class ExprDrawBoxC implements Serializable {

    public List<OrderC> orders;
    public int count;
    public String key;

    public ExprDrawBoxC()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "r";
    }

    public boolean AddOrder(Carcase c)
    {
        OrderC existingO = FindOrder(c.order);
        if(existingO.name.equals("NONE"))
        {
            OrderC tempO = new OrderC();
            tempO.name = c.order;
            tempO.AddCarcase(c);
            orders.add(tempO);
            count++;
            return true;
        }
        else
        {
            existingO.AddCarcase(c);
            return false;
        }
    }

    public OrderC FindOrder(String order)
    {
        OrderC o = new OrderC();
        o.name = "NONE";
        for(OrderC ord:orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }

    public void RemoveCarcase(String door, String order)
    {
        OrderC o = FindOrder(order);
        o.RemoveCarcase(door);
    }
}
