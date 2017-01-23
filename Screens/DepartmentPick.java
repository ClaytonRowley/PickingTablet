package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentPick extends AppCompatActivity {

    Button btnCarc;
    Button btnDoor;
    Button btnTrim;
    Button btnStor;
    Button btnPane;
    Button btnAppl;
    Button btnWork;

    boolean logged = false;

    ProgressBar progressBar;

    LinearLayout layBut;

    String userID;
    String date;

    int routes;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_pick);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = getIntent().getStringExtra("DATE");
        userID = getIntent().getStringExtra("USER");

        getSupportActionBar().setTitle(date);

        btnCarc = (Button)findViewById(R.id.btnCarc);
        btnDoor = (Button)findViewById(R.id.btnDoor);
        btnTrim = (Button)findViewById(R.id.btnTrim);
        btnStor = (Button)findViewById(R.id.btnStor);
        btnPane = (Button)findViewById(R.id.btnPane);
        btnAppl = (Button)findViewById(R.id.btnAppl);
        btnWork = (Button)findViewById(R.id.btnWork);

        routes = GetCount(date);

        layBut = (LinearLayout)findViewById(R.id.layBut);

        progressBar = (ProgressBar)findViewById(R.id.pB);

        new GetProgress().execute(btnCarc.getText().toString());
        new GetProgress().execute(btnDoor.getText().toString());
        new GetProgress().execute(btnTrim.getText().toString());
        new GetProgress().execute(btnStor.getText().toString());
        new GetProgress().execute(btnAppl.getText().toString());
        new GetProgress().execute(btnWork.getText().toString());
        new GetProgress().execute(btnPane.getText().toString());

        btnCarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnCarc.getText().toString());
                }
            }
        });

        btnDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnDoor.getText().toString());
                }
            }
        });

        btnTrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnTrim.getText().toString());
                }
            }
        });

        btnStor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnStor.getText().toString());
                }
            }
        });

        btnPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnPane.getText().toString());
                }
            }
        });

        btnAppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnAppl.getText().toString());
                }
            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    PopulateDates(btnWork.getText().toString());
                }
            }
        });

        progressBar.setMax(7);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
    }

    private int GetCount(String date)
    {
        int c = 0;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT COUNT(DISTINCT route_name) AS COUNT FROM scheme.optrnsdm WHERE product_type25 = '"+date+"'");
            while(reset.next()) {
                c = reset.getInt("COUNT");
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return c;
    }

    private String CheckPicked(String area, String table)
    {
        List<String> routes = GetRoute(table);
        if(routes.size() == 0)
            return "Complete";
        String res = Picked(area, routes);
        return res;
    }

    private String Picked(String area, List<String> routes)
    {
        String result = "";
        for(String r : routes)
        {
            result += "'" + r + "',";
        }
        result = result.substring(0, result.length() - 1);
        List<String> newRou = new ArrayList<>();
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Stores Timeline] WHERE UPPER(Status) LIKE '%" + area + "%' AND Process IN("+result+")");
            while(reset.next()) {
                String r = reset.getString("Process").trim();
                newRou.add(r);
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        boolean progress = false;
        boolean complete = true;
        for(String r : routes)
        {
            if(newRou.contains(r))
                progress = true;
            else
                complete = false;
        }
        if(complete)
            return "Complete";
        if(progress)
            return "P";
        return "NO";
    }

    private List<String> GetRoute(String table)
    {
        List<String> routes = new ArrayList<>();
        try{
            String SPsql = "EXEC [dbo].[CR_Pick_"+table+"] ?";
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
                    String route = reset.getString("Route");
                    if(!routes.contains(route))
                        routes.add(route);
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
        return routes;
    }

    private class GetProgress extends AsyncTask<String, String, String> {
        protected void onPreExecute() {

        }

        protected String doInBackground(String... args) {

            String p = "NO";

            switch (args[0]) {
                case "Carcases":
                    String resultC = CheckPicked("CARC", "Carc");
                    if(resultC.equals("NO"))
                        return resultC;
                    else
                        return resultC += ",Carcases";
                case "Doors":
                    String resultD = CheckPicked("DOOR", "Door");
                    if(resultD.equals("NO"))
                        return resultD;
                    else
                        return resultD += ",Doors";
                case "Trims":
                    String resultT = CheckPicked("TRIM", "Trim");
                    if(resultT.equals("NO"))
                        return resultT;
                    else
                        return resultT += ",Trims";
                case "Stores":
                    String resultS = CheckPicked("STORE", "Stor");
                    if(resultS.equals("NO"))
                        return resultS;
                    else
                        return resultS += ",Stores";
                case "Panels":
                    String resultP = CheckPicked("PANE", "Pane");
                    if(resultP.equals("NO"))
                        return resultP;
                    else
                        return resultP += ",Panels";
                case "Appliances":
                    String resultA = CheckPicked("APP", "Appl");
                    if(resultA.equals("NO"))
                        return resultA;
                    else
                        return resultA += ",Appliances";
                case "Worktops":
                    String resultW = CheckPicked("WKTP", "Work");
                    if(resultW.equals("NO"))
                        return resultW;
                    else
                        return resultW += ",Worktops";
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
                for(int index=0; index<layBut.getChildCount(); ++index) {
                    View nextChild = layBut.getChildAt(index);
                    Button b = (Button)nextChild;
                    if(b.getText().equals(temp[1]))
                    {
                        if(temp[0].equals("Complete")) {
                            b.setBackgroundColor(Color.GREEN);
                            return;
                        }
                        else if(temp[0].equals("P")) {
                            b.setBackgroundColor(Color.YELLOW);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void PopulateDates(String department)
    {
        Intent intent;
        switch (department) {
            case "Doors":
                intent = new Intent(this, RoutePickD.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Carcases":
                intent = new Intent(this, RoutePickC.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Worktops":
                intent = new Intent(this, RoutePickW.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Appliances":
                intent = new Intent(this, RoutePickA.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Panels":
                intent = new Intent(this, RoutePickP.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Trims":
                intent = new Intent(this, RoutePickT.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
            case "Stores":
                intent = new Intent(this, RoutePickS.class);
                intent.putExtra("USER", userID);
                intent.putExtra("DATE", date);
                startActivityForResult(intent, RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            routes = GetCount(date);
            new GetProgress().execute(btnCarc.getText().toString());
            new GetProgress().execute(btnDoor.getText().toString());
            new GetProgress().execute(btnTrim.getText().toString());
            new GetProgress().execute(btnStor.getText().toString());
            new GetProgress().execute(btnPane.getText().toString());
            new GetProgress().execute(btnAppl.getText().toString());
            new GetProgress().execute(btnWork.getText().toString());
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
