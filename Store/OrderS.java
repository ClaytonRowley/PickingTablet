package com.richmondcabinets.claytonrowley.pickingtablet.Store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 15/08/2016.
 */
public class OrderS implements Serializable {

    public String name;
    public List<Store> stores;
    public int count;
    public boolean picked;

    public OrderS()
    {
        count = 0;
        name = "";
        stores = new ArrayList<>();
        picked = false;
    }

    public boolean AddStore(Store s)
    {
        Store existingS = FindStore(s.product);
        if(existingS.product.equals("NONE"))
        {
            stores.add(s);
            count++;
            return true;
        }
        else
        {
            existingS.total += s.total;
            return false;
        }
    }

    private Store FindStore(String store)
    {
        Store s = new Store();
        s.product = "NONE";
        for (Store sto : stores) {
            if(sto.product.equals(store))
                return sto;
        }
        return s;
    }
}
