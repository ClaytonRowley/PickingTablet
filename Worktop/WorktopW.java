package com.richmondcabinets.claytonrowley.pickingtablet.Worktop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 02/08/2016.
 */
public class WorktopW implements Serializable {

    public List<OrderW> orders;
    public int count;
    public String key;

    public WorktopW()
    {
        count = 0;
        orders = new ArrayList<>();
        key = "a";
    }

    public boolean AddOrder(Worktop w)
    {
        OrderW existingW = FindOrder(w.order);
        if(existingW.name.equals("NONE"))
        {
            OrderW tempO = new OrderW();
            tempO.name = w.order;
            tempO.AddWorktop(w);
            orders.add(tempO);
            count++;
            return true;
        }
        else
        {
            existingW.AddWorktop(w);
            return false;
        }
    }

    public OrderW FindOrder(String order)
    {
        OrderW o = new OrderW();
        o.name = "NONE";
        for(OrderW ord:orders)
        {
            if(ord.name.equals(order))
                return ord;
        }
        return o;
    }

    public void RemoveWorktop(String worktop, String order) {
        OrderW o = FindOrder(order);
        o.RemoveWorktop(worktop);
    }
}
