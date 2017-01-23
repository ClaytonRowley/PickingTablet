package com.richmondcabinets.claytonrowley.pickingtablet.Carcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 26/07/2016.
 */
public class NonShelvesC implements Serializable{

    public List<OrderC> orders;
    public int count;
    public String key;

    public NonShelvesC()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "Q";
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
