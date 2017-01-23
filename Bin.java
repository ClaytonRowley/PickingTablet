package com.richmondcabinets.claytonrowley.pickingtablet;

/**
 * Created by Clayton.Rowley on 11/07/2016.
 */
public class Bin {

    public String name;
    public int quantity;

    public Bin()
    {
        name = "";
        quantity = 0;
    }

    public Bin(String n, int q)
    {
        name = n;
        quantity = q;
    }
}
