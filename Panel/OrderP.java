package com.richmondcabinets.claytonrowley.pickingtablet.Panel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 05/08/2016.
 */
public class OrderP implements Serializable {

    public String name;
    public List<Panel> panels;
    public int count;
    public boolean picked;

    public OrderP()
    {
        count = 0;
        name = "";
        panels = new ArrayList<>();
        picked = false;
    }

    public boolean AddPanel(Panel p)
    {
        Panel existingP = FindPanel(p.product);
        if(existingP.product.equals("NONE"))
        {
            panels.add(p);
            count++;
            return true;
        }
        else
        {
            existingP.total += p.total;
            return false;
        }
    }

    public Panel FindPanel(String panel)
    {
        Panel p = new Panel();
        p.product = "NONE";
        for (Panel pan : panels) {
            if (pan.product.equals(panel))
                return pan;
        }
        return p;
    }
}
