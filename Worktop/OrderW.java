package com.richmondcabinets.claytonrowley.pickingtablet.Worktop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 02/08/2016.
 */
public class OrderW implements Serializable {

    public String name;
    public List<Worktop> worktops;
    public int count;
    public boolean picked;

    public OrderW()
    {
        count = 0;
        name = "";
        worktops = new ArrayList<>();
        picked = false;
    }

    public boolean AddWorktop(Worktop w)
    {
        Worktop existingW = FindWorktop(w.product);
        if(existingW.product.equals("NONE"))
        {
            worktops.add(w);
            count++;
            return true;
        }
        else
        {
            existingW.total += w.total;
            return false;
        }
    }

    private Worktop FindWorktop(String worktop)
    {
        Worktop w = new Worktop();
        w.product = "NONE";
        for (Worktop wor : worktops) {
            if (wor.product.equals(worktop))
                return wor;
        }
        return w;
    }

    public void RemoveWorktop(String worktop)
    {
        Worktop w = FindWorktop(worktop);
        if(w.total == 1)
            worktops.remove(w);
        else
            w.total --;
    }
}
