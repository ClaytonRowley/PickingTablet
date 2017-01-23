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

import com.richmondcabinets.claytonrowley.pickingtablet.Doors.RouteD;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.widget.ProgressBar;

public class AnalysisPickD extends AppCompatActivity {

    LinearLayout lay;
    RouteD routeD;
    String userID;

    boolean routeComplete = false;

    int pickListCount = 0;
    int pickListComplete = 0;

    ProgressBar progressBar;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_pick_d);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        routeD = (RouteD)getIntent().getSerializableExtra("ROUTE");
        userID = getIntent().getStringExtra("USER");
        getSupportActionBar().setTitle(routeD.name);

        lay = (LinearLayout)findViewById(R.id.lay);
        progressBar = (ProgressBar)findViewById(R.id.pB);

        routeComplete = getIntent().getBooleanExtra("COMPLETE", false);

        progressBar.setMax(11);

        DrawAnalysis();
    }


    private void DrawAnalysis()
    {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        pickListCount = 0;
        pickListComplete = 0;

        if(routeD.doorD.count != 0)
            AddButton("Doors", routeD.doorD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.expressQD.count != 0)
            AddButton("Express Qs", routeD.expressQD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.flatUnitsD.count != 0)
            AddButton("Flat Units", routeD.flatUnitsD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.lineUnitsD.count != 0)
            AddButton("Line Units", routeD.lineUnitsD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.lShapeCBasesD.count != 0)
            AddButton("LShape C-Bases", routeD.lShapeCBasesD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.nonLineFeaturesD.count != 0)
            AddButton("Non Line Features", routeD.nonLineFeaturesD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.nonLineUnitsD.count != 0)
            AddButton("Non Line Units", routeD.nonLineUnitsD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.nonStoresD.count != 0)
            AddButton("Non Stores", routeD.nonStoresD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.pjhD.count != 0)
            AddButton("PJH", routeD.pjhD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.storePanelD.count != 0)
            AddButton("Store Panels", routeD.storePanelD.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeD.storesD.count != 0)
            AddButton("Stores", routeD.storesD.count);
        else
            progressBar.incrementProgressBy(1);
    }

    private void AddButton(String name, int count)
    {
        final String n = name;
        Button btnRoute = new Button(this);
        btnRoute.setLayoutParams(new ViewGroup.LayoutParams(200, 100));
        btnRoute.setText(name);
        btnRoute.setId(R.id.ProgButt);
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList(n);
                }
            }
        });
        lay.addView(btnRoute);
        pickListCount ++;
        new GetPickStatus().execute(name, Integer.toString(count));
    }

    private void CreatePickList(String name)
    {
        Button temp = new Button(this);
        for (int index = 0; index < lay.getChildCount(); ++index) {
            View nextChild = lay.getChildAt(index);
            int id = nextChild.getId();
            if (id == R.id.ProgButt) {
                Button tempB = (Button) nextChild;
                if (tempB.getText().equals(name)) {
                    temp = tempB;
                }
            }
        }
        int color = 0;
        try{
            color = ((ColorDrawable)temp.getBackground()).getColor();
        }
        catch (Exception e) {}
        boolean progress = false;
        if (color == Color.GREEN || color == Color.YELLOW)
            progress = true;
        Intent intent = new Intent(this, PickListD.class);
        intent.putExtra("ROUTE", routeD);
        intent.putExtra("ORDER", name);
        intent.putExtra("USER", userID);
        intent.putExtra("PROGRESS", progress);
        startActivityForResult(intent, RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            lay.removeAllViews();
            DrawAnalysis();
        }
    }

    private String GetProgress(String name, int count)
    {
        int comp = 0;
        int prog = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WITH (READUNCOMMITTED) WHERE Pick_List = '"+name+"' AND Route = '"+routeD.name+"' AND Area = 'PK-DOORS'");
            while(reset.next())
            {
                String complete;
                try {complete = reset.getString("Complete");}catch (Exception e){complete = "-";}
                try {if(complete.isEmpty()) complete = "-";} catch (Exception e) {complete = "-";}
                if(complete.equals("Complete"))
                    comp++;
                else
                    prog++;
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
        if(comp != count && (prog != 0 || comp != 0))
            return "Progress";
        else if(comp == count)
            return "Picked";
        else
            return "None";
    }

    private class GetPickStatus extends AsyncTask<String, Integer, String> {
        protected  void onPreExecute() {

        }

        protected String doInBackground(String... args) {

            int count = Integer.parseInt(args[1]);
            String pick = GetProgress(args[0], count);
            return pick + "," + args[0];
        }

        protected void onPostExecute(String result) {

            String[] res = result.split(",");
            if(!res[0].equals("None")) {
                for (int index = 0; index < lay.getChildCount(); ++index) {
                    View nextChild = lay.getChildAt(index);
                    int id = nextChild.getId();
                    if (id == R.id.ProgButt) {
                        Button tempB = (Button) nextChild;
                        if (tempB.getText().equals(res[1])) {
                            if (res[0].equals("Picked")) {
                                tempB.setBackgroundColor(Color.GREEN);
                                pickListComplete++;
                            }
                            else if (res[0].equals("Progress"))
                                tempB.setBackgroundColor(Color.YELLOW);
                        }
                    }
                }
            }
            progressBar.incrementProgressBy(1);
            if(progressBar.getMax() == progressBar.getProgress())
                progressBar.setVisibility(View.GONE);
            if(pickListCount == pickListComplete && !routeComplete)
                CompleteRoute();

        }
    }

    private void CompleteRoute()
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String format = s.format(new Date());
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO [_Paul A0 Stores Timeline] ([User], [Process], [Status], [Date/Time]) VALUES('"+userID+"', '"+routeD.name+"', 'PK-DOORS', '"+format+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            routeComplete = true;
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
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
