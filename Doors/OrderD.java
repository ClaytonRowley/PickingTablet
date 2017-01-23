package com.richmondcabinets.claytonrowley.pickingtablet.Doors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 08/07/2016.
 */
public class OrderD implements Serializable{

    public String name;
    public List<Door> doors;
    public int count;
    public boolean picked;

    public OrderD()
    {
        count = 0;
        name = "";
        doors = new ArrayList<>();
        picked = false;
    }

    public boolean AddDoor(Door d)
    {
        Door existingD = FindDoor(d.product);
        if(existingD.product.equals("NONE"))
        {
            doors.add(d);
            count++;
            return true;
        }
        else
        {
            existingD.total += d.total;
            return false;
        }
    }

    private Door FindDoor(String door)
    {
        Door d = new Door();
        d.product = "NONE";
        for(Door doo:doors)
        {
            if(doo.product.equals(door))
                return doo;
        }
        return d;
    }

    public void RemoveDoor(String door)
    {
        Door d = FindDoor(door);
        if(d.total == 1)
            doors.remove(d);
        else
            d.total --;
    }
}
