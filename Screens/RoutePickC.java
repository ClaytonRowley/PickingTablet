package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.Carcase;
import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.RouteC;
import com.richmondcabinets.claytonrowley.pickingtablet.Screens.R;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RoutePickC extends AppCompatActivity {

    LinearLayout lay;
    List<RouteC> routeCs = new ArrayList<>();
    List<String> removal = new ArrayList<>();
    String date;
    String userID;

    ProgressBar progressBar;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_pick_c);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date = getIntent().getStringExtra("DATE");
        userID = getIntent().getStringExtra("USER");
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        getSupportActionBar().setTitle(date);

        lay = (LinearLayout)findViewById(R.id.lay);

        CreateCarcase();
    }

    private void CreateCarcase()
    {
        List<Carcase> carcases = GetCarcases(date);
        routeCs = CreateRoutes(carcases);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(routeCs.size());

        for(RouteC r:routeCs)
        {
            final RouteC route = r;
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
        Toast toast = Toast.makeText(getApplicationContext(), "Getting progress", Toast.LENGTH_SHORT);
        toast.show();
    }

    private class GetProgress extends AsyncTask<String, String, String> {
        protected  void onPreExecute() {

        }

        protected String doInBackground(String... args) {
            String p = GetPick(args[0]);

            if(p.equals("NO"))
            {
                p = GetProgress(args[0]);
            }
            return p;
        }

        protected void onPostExecute(String result) {
            progressBar.incrementProgressBy(1);
            if(progressBar.getProgress() == progressBar.getMax())
                progressBar.setVisibility(View.GONE);
            String[] temp = result.split(",");
            if(!result.equals("NO"))
            {
                for(int index=0; index<lay.getChildCount(); ++index) {
                    View nextChild = lay.getChildAt(index);
                    Button b = (Button)nextChild;
                    if(b.getText().equals(temp[0]))
                    {
                        if(temp[1].equals("D")) {
                            b.setBackgroundColor(Color.GREEN);
                            return;
                        }
                        else if(temp[1].equals("P")) {
                            b.setBackgroundColor(Color.YELLOW);
                            return;
                        }
                    }
                }
            }
        }
    }

    private String GetPick(String route)
    {
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Stores Timeline] WHERE Process = '" + route + "' AND UPPER(Status) LIKE '%CARC%'");
            while(reset.next()) {
                String r = reset.getString("Process").trim();
                return r + ",D";
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

    private String GetProgress(String route)
    {
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WHERE Route = '" + route + "' AND Area = 'PK-CARCS'");
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

    private void PopulateOrders(RouteC route)
    {
        Button temp = new Button(this);
        for (int index = 0; index < lay.getChildCount(); ++index) {
            View nextChild = lay.getChildAt(index);
            int id = nextChild.getId();
            if (id == R.id.ProgButt) {
                Button tempB = (Button) nextChild;
                if (tempB.getText().equals(route.name)) {
                    temp = tempB;
                }
            }
        }
        int color = 0;
        try{
            color = ((ColorDrawable)temp.getBackground()).getColor();
        }
        catch (Exception e) {}
        boolean complete = false;
        if(color == Color.GREEN)
            complete = true;
        Intent intent = new Intent(this, AnalysisPickC.class);
        intent.putExtra("ROUTE", route);
        intent.putExtra("USER", userID);
        intent.putExtra("COMPLETE", complete);
        startActivityForResult(intent, RESULT);
    }

    private List<RouteC> CreateRoutes(List<Carcase> carcases)
    {
        List<RouteC> routes = new ArrayList<>();

        for(Carcase c:carcases)
        {
            RouteC temp = FindRoute(routes, c.route);
            if(temp.name.equals("NONE"))
            {
                temp.name = c.route;
                temp.AddCarcase(c);
                routes.add(temp);
            }
            else
            {
                temp.AddCarcase(c);
            }
        }

        for(String s:removal)
        {
            String[] temp = s.split(",");
            RouteC c = FindRoute(routes, temp[3]);
            c.RemoveCarcase(temp[1], temp[2], temp[0]);
        }

        return routes;
    }

    private RouteC FindRoute(List<RouteC> route, String name)
    {
        for(RouteC r:route)
        {
            if(r.name.equals(name))
                return r;
        }
        RouteC temp = new RouteC();
        temp.name = "NONE";
        return temp;
    }

    private List<Carcase> GetCarcases(String date)
    {
        List<Carcase> carcases = new ArrayList<>();
        try {
            String SPsql = "EXEC [dbo].[CR_Pick_Carc] ?";
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
                    String r = reset.getString("Route").trim();
                    String order = reset.getString("Order No").trim();
                    String product = reset.getString("Component").trim();
                    String desc = reset.getString("Description").trim();
                    String draw = reset.getString("Drawing").trim();
                    String anala = reset.getString("Analysis A").trim();
                    String anald = reset.getString("Analysis D").trim();
                    String anale = reset.getString("Analysis E").trim();
                    String consumer = reset.getString("Consumer Unit").trim();
                    String wh = reset.getString("Warehouse").trim();
                    String scan = reset.getString("Scanned").trim();
                    boolean s = false;
                    if(scan.equals("Y"))
                        s = true;
                    int qty = reset.getInt("Total");
                    int cQty = reset.getInt("Cut Total");
                    String cat = GetCategory(draw, anala, anale);
                    if(cat.equals("ERROR"))
                        cat = "D";
                    String cutDown = reset.getString("Cut Down").trim();
                    if(!cutDown.equals("X"))
                        for(int a = 0; a<cQty; a++)
                            removal.add(order + "," + cutDown + "," + cat + "," + r);
                    if(!cat.equals("NOPE")) {
                        Carcase c = new Carcase(r, order, product, desc, draw, anala, anald, anale, cat, consumer, wh, qty, s);
                        carcases.add(c);
                    }
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
        return carcases;
    }

    private String GetCategory(String drawing, String analysisA, String analysisE)
    {
        if(drawing.equals("STORES"))
            return "CARRIER";
        if(drawing.equals("FLAT UNITS"))
        {
            if (analysisA.equals("DRAWPANEL"))
                return "FLAT DRAW";
            if (analysisA.equals("TOPBOTT"))
                return "FLAT TOP";
            if (analysisA.equals("ENDPANEL"))
                return "FLAT END";
            /*if (analysisA.equals("OTHERPANEL"))
            {
                if (analysisE.contains("RAIL"))
                    return "FLAT RAIL";
                if (analysisE.contains("SHELF"))
                    return "FLAT SHELVES";
                if (analysisE.contains("BACK"))
                    return "FLAT BACK";
                if(analysisE.equals("HARDBOARD"))
                    return "FLAT OTHER";
                if(analysisE.equals("FILLER"))
                    return "FLAT OTHER";
                if(analysisE.equals("MUNTINS"))
                    return "FLAT OTHER";
            }*/
            if (analysisA.equals("RAIL"))
                return "FLAT RAIL";
            if (analysisA.equals("SHELF"))
                return "FLAT SHELVES";
            if (analysisA.equals("BACK"))
                return "FLAT BACK";
            if (analysisA.equals("HARDBOARD"))
                return "FLAT OTHER";
            if (analysisA.equals("FILLER"))
                return "FLAT OTHER";
            if (analysisA.equals("MUNTINS"))
                return "FLAT OTHER";
            return "FLAT OTHER";
        }
        else if(!drawing.contains("EXPRESS")){
            if(analysisA.equals("HARDBOARD"))
                return "NOPE";
            if(analysisA.equals("MUNTINS"))
                return "NOPE";
            if (analysisA.equals("ENDPANEL")) {
                switch (drawing) {
                    case "LINE UNITS":
                        return "LINE END";
                    case "NON LINE UNITS":
                        return "NON END";
                    case "NON LINE FEATURES":
                        return "NON END";
                }
            }
            if (analysisA.equals("TOPBOTT")) {
                switch (drawing) {
                    case "LINE UNITS":
                        return "LINE TOP";
                    case "NON LINE UNITS":
                        return "NON TOP";
                    case "NON LINE FEATURES":
                        return "NON TOP";
                }
            }
            /*if (analysisA.equals("OTHERPANEL")) {
                if (analysisE.contains("RAIL")) {
                    return "RAIL";
                }
                if (analysisE.contains("SHELF")) {
                    switch (drawing) {
                        case "LINE UNITS":
                            return "LINE SHELVES";
                        case "NON LINE UNITS":
                            return "NON SHELVES";
                        case "NON LINE FEATURES":
                            return "NON SHELVES";
                    }
                }
                if (analysisE.contains("BACK")) {
                    return "BACK";
                } else {
                    switch (drawing) {
                        case "LINE UNITS":
                            return "LINE OTHER";
                        case "NON LINE UNITS":
                            return "NON OTHER";
                        case "NON LINE FEATURES":
                            return "NON OTHER";
                    }
                }
            }*/
            if (analysisA.equals("DRAWPANEL"))
                return "DRAW";
            if (analysisA.equals("RAIL"))
                return "RAIL";
            if (analysisA.equals("SHELF")) {
                switch (drawing) {
                    case "LINE UNITS":
                        return "LINE SHELVES";
                    case "NON LINE UNITS":
                        return "NON SHELVES";
                    case "NON LINE FEATURES":
                        return "NON SHELVES";
                }
            }
            if (analysisA.equals("BACK"))
                return "BACK";
            else
            {
                switch (drawing) {
                    case "LINE UNITS":
                        return "LINE OTHER";
                    case "NON LINE UNITS":
                        return "NON OTHER";
                    case "NON LINE FEATURES":
                        return "NON OTHER";
                }
            }
            switch (drawing) {
                case "LINE UNITS":
                    return "LINE OTHER";
                case "NON LINE UNITS":
                    return "NON OTHER";
            }
        }
        else if(drawing.contains("EXPRESS"))
        {
            if (analysisA.equals("DRAWPANEL"))
                return "EXPR DRAW";
            if (analysisA.equals("TOPBOTT"))
                return "EXPR TOP";
            if (analysisA.equals("ENDPANEL"))
                return "EXPR END";
            /*if (analysisA.equals("OTHERPANEL"))
            {
                if (analysisE.contains("RAIL"))
                    return "EXPR RAIL";
                if (analysisE.contains("SHELF"))
                    return "EXPR SHELVES";
                if (analysisE.contains("BACK"))
                    return "EXPR BACK";
                if(analysisE.equals("HARDBOARD"))
                    return "EXPR OTHER";
                if(analysisE.equals("FILLER"))
                    return "EXPR OTHER";
                if(analysisE.equals("MUNTINS"))
                    return "EXPR OTHER";
            }*/
            if (analysisA.equals("RAIL"))
                return "EXPR RAIL";
            if (analysisA.equals("SHELF"))
                return "EXPR SHELVES";
            if (analysisA.equals("BACK"))
                return "EXPR BACK";
            if (analysisA.equals("HARDBOARD"))
                return "EXPR OTHER";
            if (analysisA.equals("FILLER"))
                return "EXPR OTHER";
            if (analysisA.equals("MUNTINS"))
                return "EXPR OTHER";
            return "EXPR OTHER";
        }
        return "LINE OTHER";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            lay.removeAllViews();
            CreateCarcase();
        }
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

}
