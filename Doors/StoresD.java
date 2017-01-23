package com.richmondcabinets.claytonrowley.pickingtablet.Doors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 08/07/2016.
 */
public class StoresD implements Serializable{

    public List<OrderD> orders;
    public int count;
    public String key;

    public StoresD()
    {
        key = "K";
        count = 0;
        orders = new ArrayList<>();
    }

    public boolean AddOrder(Door d)
    {
        OrderD existingO = FindOrder(d.order);
        if(existingO.name.equals("NONE"))
        {
            OrderD tempO = new OrderD();
            tempO.name = d.order;
            tempO.AddDoor(d);
            orders.add(tempO);
            count++;
            return true;
        }
        else
        {
            existingO.AddDoor(d);
            return false;
        }
    }

    private OrderD FindOrder(String order)
    {
        OrderD o = new OrderD();
        o.name = "NONE";
        for(OrderD ord: orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }

    public void RemoveDoor(String door, String order)
    {
        OrderD o = FindOrder(order);
        o.RemoveDoor(door);
    }
}
