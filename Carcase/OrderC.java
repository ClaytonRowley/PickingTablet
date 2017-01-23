package com.richmondcabinets.claytonrowley.pickingtablet.Carcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 26/07/2016.
 */
public class OrderC implements Serializable {

    public String name;
    public List<Carcase> carcases;
    public int count;
    public boolean picked;

    public OrderC()
    {
        count = 0;
        name = "";
        carcases = new ArrayList<>();
        picked = false;
    }

    public boolean AddCarcase(Carcase c)
    {
        Carcase existingC = FindCarcase(c.product);
        if(existingC.product.equals("NONE"))
        {
            carcases.add(c);
            count++;
            return true;
        }
        else
        {
            existingC.total += c.total;
            return false;
        }
    }

    private Carcase FindCarcase(String carcase)
    {
        Carcase c = new Carcase();
        c.product = "NONE";
        for (Carcase car : carcases) {
            if (car.product.equals(carcase))
                return car;
        }
        return c;
    }

    public void RemoveCarcase(String door)
    {
        Carcase c = FindCarcase(door);
        if(c.total == 1)
            carcases.remove(c);
        else
            c.total --;
    }
}
