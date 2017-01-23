package com.richmondcabinets.claytonrowley.pickingtablet.Trim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 09/08/2016.
 */
public class OrderT implements Serializable {

    public String name;
    public List<Trim> trims;
    public int count;
    public boolean picked;

    public OrderT()
    {
        count = 0;
        name = "";
        trims = new ArrayList<>();
        picked = false;
    }

    public boolean AddTrim(Trim t)
    {
        Trim existingT = FindTrim(t.product);
        if(existingT.product.equals("NONE"))
        {
            trims.add(t);
            count++;
            return true;
        }
        else
        {
            existingT.total += t.total;
            return false;
        }
    }

    public Trim FindTrim(String trim)
    {
        Trim t = new Trim();
        t.product = "NONE";
        for (Trim tri : trims) {
            if (tri.product.equals(trim))
                return tri;
        }
        return t;
    }

    public void RemoveTrim(String trim)
    {
        Trim t = FindTrim(trim);
        if(t.total == 1)
            trims.remove(t);
        else
            t.total --;
    }
}
