package com.richmondcabinets.claytonrowley.pickingtablet;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 21/07/2016.
 */
public class ProductListCD {

    public List<String> products;

    public ProductListCD()
    {
        products = new ArrayList<>();
    }

    public void PopulateProducts(String bin)
    {
        products.clear();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT scheme.stquem.product, SUM(scheme.stquem.quantity) AS quantity, scheme.stockm.long_description FROM scheme.stquem INNER JOIN scheme.stockm ON scheme.stquem.warehouse = scheme.stockm.warehouse AND scheme.stquem.product = scheme.stockm.product WHERE scheme.stquem.bin_number = '"+bin+"' AND quantity != 0 GROUP BY scheme.stquem.product, scheme.stockm.long_description");
            while (reset.next()) {
                String pr = reset.getString("product").trim();
                String des = reset.getString("long_description").trim();
                int qu = reset.getInt("quantity");
                products.add(pr + ", " + des + ", " + Integer.toString(qu));
            }
        }
        catch (Exception e)
        {
            Log.w("Error", e.getMessage());
        }
    }
}
