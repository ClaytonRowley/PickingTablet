package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.Panel.Panel;
import com.richmondcabinets.claytonrowley.pickingtablet.Panel.RouteP;

import java.net.PortUnreachableException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoutePickP extends AppCompatActivity {

    LinearLayout lay;
    List<RouteP> routePs = new ArrayList<>();
    String date;
    String userID;

    ProgressBar progressBar;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_pick_p);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date = getIntent().getStringExtra("DATE");
        userID = getIntent().getStringExtra("USER");
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        getSupportActionBar().setTitle(date);

        lay = (LinearLayout)findViewById(R.id.lay);

        CreatePanels();
    }

    private void CreatePanels()
    {
        List<Panel> panels = GetPanels(date);
        routePs = CreateRoutes(panels);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(routePs.size());

        for(RouteP r : routePs)
        {
            final RouteP route = r;
            final Button btnRoute = new Button(this);
            btnRoute.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
            btnRoute.setText(r.name);
            btnRoute.setId(R.id.ProgButt);
            new GetProgress().execute(r.name, "", "");
            btnRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(progressBar.getVisibility() == View.GONE) {
                        PopulateOrders(route);
                    }
                }
            });
            lay.addView(btnRoute);
        }

        Toast toast = Toast.makeText(getApplicationContext(), "Getting Progress", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void PopulateOrders(RouteP route)
    {
        Button temp = new Button(this);
        for (int index = 0; index < lay.getChildCount(); ++index) {
            View nextChild = lay.getChildAt(index);
            int id = nextChild.getId();
            if(id == R.id.ProgButt) {
                Button tempB = (Button) nextChild;
                if(tempB.getText().equals(route.name)) {
                    temp = tempB;
                }
            }
        }
        int color = 0;
        try {
            color = ((ColorDrawable)temp.getBackground()).getColor();
        }
        catch (Exception e) {}
        boolean complete = false;
        if(color == Color.GREEN)
            complete = true;
        Intent intent = new Intent(this, AnalysisPickP.class);
        intent.putExtra("ROUTE", route);
        intent.putExtra("USER", userID);
        intent.putExtra("COMPLETE", complete);
        startActivityForResult(intent, RESULT);
    }

    private String GetProgress(String route)
    {
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WHERE Route = '" + route + "' AND Area = 'PK-PANEL'");
            while(reset.next()) {
                String r = reset.getString("Route").trim();
                return r + ",P";
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return "NO";
    }

    private class GetProgress extends AsyncTask<String, String, String> {
        protected void onPreExectute() {

        }

        protected String doInBackground(String... args) {
            String p = GetPick(args[0]);

            if(p.equals("NO")) {
                p = GetProgress(args[0]);
            }
            return p;
        }

        protected void onPostExecute(String result)
        {
            progressBar.incrementProgressBy(1);
            if(progressBar.getProgress() == progressBar.getMax())
                progressBar.setVisibility(View.GONE);
            String[] temp = result.split(",");
            if(!result.equals("NO"))
            {
                for(int index = 0; index<lay.getChildCount(); ++index) {
                    View nextChild = lay.getChildAt(index);
                    Button b = (Button)nextChild;
                    if(b.getText().equals(temp[0]))
                    {
                        if(temp[1].equals("D")) {
                            b.setBackgroundColor(Color.GREEN);
                            return;
                        }
                        else if (temp[1].equals("P")) {
                            b.setBackgroundColor(Color.YELLOW);
                            return;
                        }
                    }
                }
            }
        }
    }


    private String GetPick(String route) {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Stores Timeline] WHERE Process = '" + route + "' AND UPPER(Status) LIKE '%PANEL%'");
            while (reset.next()) {
                String r = reset.getString("Process").trim();
                return r + ",D";
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return "NO";
    }

    private List<Panel> GetPanels(String date)
    {
        List<Panel> panels = new ArrayList<>();
        try {
            String SPsql = "EXEC [dbo].[CR_Pick_Pane] ?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(300);
            ps.setString(1, date);

            boolean hadResults = ps.execute();

            while(hadResults)
            {
                ResultSet reset = ps.getResultSet();

                while(reset.next()) {
                    String r = reset.getString("Route");
                    String order = reset.getString("Order No");
                    String product = reset.getString("Component");
                    String desc = reset.getString("Description");
                    String anala = reset.getString("Analysis A");
                    String cons = reset.getString("Consumer");
                    String wh = reset.getString("Warehouse");
                    int total = reset.getInt("Total");
                    String scan = reset.getString("Scanned").trim();
                    boolean s = false;
                    if(scan.equals("Y"))
                        s = true;
                    if(product.equals("MKB0074"))
                        total = 0;
                    String boiler = reset.getString("Boiler");
                    boolean b = false;
                    if(boiler.equals("SPCBOILER"))
                        b = true;
                    Panel p = new Panel(r, order, product, desc, anala, cons, wh, total, b, s);
                    panels.add(p);
                }

                hadResults = ps.getMoreResults();
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return panels;
    }

    private List<RouteP> CreateRoutes(List<Panel> panels)
    {
        List<RouteP> routes = new ArrayList<>();

        for(Panel p : panels)
        {
            RouteP temp = FindRoute(routes, p.route);
            if(temp.name.equals("NONE"))
            {
                temp.name = p.route;
                temp.AddPanel(p);
                routes.add(temp);
            }
            else
            {
                temp.AddPanel(p);
            }
        }
        return  routes;
    }

    private RouteP FindRoute(List<RouteP> route, String name)
    {
        for(RouteP r:route)
        {
            if(r.name.equals(name))
                return r;
        }
        RouteP temp = new RouteP();
        temp.name = "NONE";
        return temp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_extra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btb) {
            Intent intent = new Intent(this, BinToBin.class);
            intent.putExtra("CD", false);
            intent.putExtra("USER", userID);
            startActivityForResult(intent, BTB);
            return true;
        }

        if (id == R.id.stkadj) {
            Intent intent = new Intent(this, StkAdj.class);
            intent.putExtra("USER", userID);
            startActivityForResult(intent, STKADJ);
            return true;
        }

        if (id == R.id.ce) {
            ClearError();
            return true;
        }

        if (id == R.id.binenq) {
            Intent intent = new Intent(this, BinEnquiry.class);
            startActivity(intent);
        }

        if (id == R.id.stockloc) {
            Intent intent = new Intent(this, StockLocator.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void ClearError()
    {
        String id = userID.substring(0, 2) + "0001";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM scheme.fsbbm WHERE tran_line = '" + id + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM scheme.fssaextm WHERE tran_line = '" + id + "'";
            stmt.executeUpdate(sql);
            id = "32" + userID.substring(0, 2);
            sql = "DELETE FROM scheme.fscontm WHERE fs_id LIKE '%" + userID.substring(0, 2) + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            lay.removeAllViews();
            CreatePanels();
        }
    }
}
