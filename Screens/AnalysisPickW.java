package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.richmondcabinets.claytonrowley.pickingtablet.Screens.R;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.OrderW;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.RouteW;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.Worktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalysisPickW extends AppCompatActivity {

    LinearLayout lay;
    RouteW routeW;
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
        setContentView(R.layout.activity_analysis_pick_w);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        routeW = (RouteW)getIntent().getSerializableExtra("ROUTE");
        userID = getIntent().getStringExtra("USER");
        getSupportActionBar().setTitle(routeW.name);

        lay = (LinearLayout)findViewById(R.id.lay);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        routeComplete = getIntent().getBooleanExtra("COMPLETE", false);

        progressBar.setMax(2);

        DrawAnalysis();
    }

    private void DrawAnalysis()
    {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        pickListCount = 0;
        pickListComplete = 0;

        if(routeW.worktopW.count != 0)
            AddButton("Worktops", routeW.worktopW.count);
        else
            progressBar.incrementProgressBy(1);
        if(routeW.upstandW.count != 0)
            AddButton("Upstands", routeW.upstandW.count);
        else
            progressBar.incrementProgressBy(1);

        CreateLowDown();
    }

    private List<OrderW> CreateBulk(List<OrderW> ord) {
        List<OrderW> temp = new ArrayList<>();
        OrderW bulk = new OrderW();
        bulk.name = "BULK";
        for (OrderW o : ord) {
            for (Worktop w : o.worktops) {
                Worktop tempW = new Worktop();
                tempW.product = w.product;
                tempW.desc = w.desc;
                tempW.total = w.total;
                bulk.AddWorktop(tempW);
            }
        }
        temp.add(bulk);
        return temp;
    }

    private void CreateLowDown()
    {
        List<OrderW> worktopList = CreateBulk(routeW.worktopW.orders);
        /*List<Worktop> worktopList = new ArrayList<>();
        if(routeW.worktopW.count != 0)
        {
            for(OrderW o:routeW.worktopW.orders)
            {
                for(Worktop w:o.worktops)
                {
                    Worktop tempW = FindWorktop(w.product, worktopList);
                    if(tempW.product.equals("NONE"))
                        worktopList.add(w);
                    else
                        tempW.total += w.total;
                }
            }
        }*/

        for(OrderW o:worktopList) {
            for (Worktop w : o.worktops) {
                final Worktop fW = w;
                TextView txtBlank = new TextView(this);
                txtBlank.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                txtBlank.setText("");
                txtBlank.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                lay.addView(txtBlank);

                LinearLayout l1 = new LinearLayout(this);
                l1.setOrientation(LinearLayout.HORIZONTAL);
                l1.setId(R.id.l1);
                l1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                TextView txtProd = new TextView(this);
                txtProd.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtProd.setText(w.product);
                txtProd.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
                l1.addView(txtProd);

                TextView txtDesc = new TextView(this);
                txtDesc.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtDesc.setText(w.desc);
                txtDesc.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                l1.addView(txtDesc);

                TextView txtQuant = new TextView(this);
                txtQuant.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtQuant.setText(Integer.toString(w.total));
                txtQuant.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
                txtQuant.setGravity(Gravity.CENTER);
                l1.addView(txtQuant);

                lay.addView(l1);
            }
        }
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
        Intent intent = new Intent(this, PickListW.class);
        intent.putExtra("ROUTE", routeW);
        intent.putExtra("ORDER", name);
        intent.putExtra("USER", userID);
        intent.putExtra("PROGRESS", progress);
        startActivityForResult(intent, RESULT);
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
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WITH (READUNCOMMITTED) WHERE Pick_List = '"+name+"' AND Route = '"+routeW.name+"' AND Area = 'PK-WKTPS'");
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
            String sql = "INSERT INTO [_Paul A0 Stores Timeline] ([User], [Process], [Status], [Date/Time]) VALUES('"+userID+"', '"+routeW.name+"', 'PK-WKTPS', '"+format+"')";
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            lay.removeAllViews();
            DrawAnalysis();
        }
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
