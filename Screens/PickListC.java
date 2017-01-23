package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.Bin;
import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.Carcase;
import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.ExprDrawBoxC;
import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.OrderC;
import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.RouteC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PickListC extends AppCompatActivity {

    String category;
    RouteC routeC;

    Button btnLeft;
    Button btnRight;

    ProgressBar progressBar;

    //LinearLayout lay;
    LinearLayout base;
    LinearLayout wall;
    LinearLayout tall;
    LinearLayout other;

    boolean progress;

    int pageNo;
    int pageNoMax;

    int prodCount;
    int prodPicked;

    List<String> edged;

    String key;
    String order;
    String userID;

    String ShortProduct;
    int ShortQuantity;
    String ShortAnalysisD;

    String picked;

    List<OrderC> orders;
    List<String> completeOrders;
    OrderC currentOrder;

    TextView orderNo;
    TextView routeNo;

    Carcase currentProduct;

    boolean bulk = false;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list_c);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        routeC = (RouteC) getIntent().getSerializableExtra("ROUTE");
        category = getIntent().getStringExtra("ORDER");
        userID = getIntent().getStringExtra("USER");
        progress = getIntent().getBooleanExtra("PROGRESS", false);
        getSupportActionBar().setTitle(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderNo = (TextView) findViewById(R.id.txtOrder);
        routeNo = (TextView) findViewById(R.id.txtRoute);

        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);

        //lay = (LinearLayout) findViewById(R.id.lay);
        base = (LinearLayout) findViewById(R.id.layBase);
        wall = (LinearLayout) findViewById(R.id.layWall);
        tall = (LinearLayout) findViewById(R.id.layTall);
        other = (LinearLayout) findViewById(R.id.layOther);
        progressBar = (ProgressBar) findViewById(R.id.pB);

        completeOrders = new ArrayList<>();

        prodCount = 0;
        prodPicked = 0;

        switch (category) {
            case "Line Ends":
                orders = routeC.lineEndsC.orders;
                key = routeC.lineEndsC.key;
                break;
            case "Line Top":
                orders = routeC.lineTopBottC.orders;
                key = routeC.lineTopBottC.key;
                break;
            case "Line Shelves":
                orders = routeC.lineShelvesC.orders;
                key = routeC.lineShelvesC.key;
                break;
            case "Line Other":
                orders = routeC.lineOtherC.orders;
                key = routeC.lineOtherC.key;
                break;
            case "Non Ends":
                orders = routeC.nonEndsC.orders;
                key = routeC.nonEndsC.key;
                break;
            case "Non Top":
                orders = routeC.nonTopBottC.orders;
                key = routeC.nonTopBottC.key;
                break;
            case "Non Shelves":
                orders = routeC.nonShelvesC.orders;
                key = routeC.nonShelvesC.key;
                break;
            case "Non Other":
                orders = routeC.nonOtherC.orders;
                key = routeC.nonOtherC.key;
                break;
            case "Backs":
                orders = CreateBulk(routeC.jBacksC.orders);
                bulk = true;
                key = routeC.jBacksC.key;
                break;
            case "Rails":
                orders = CreateBulk(routeC.railC.orders);
                bulk = true;
                key = routeC.railC.key;
                break;
            case "Draw Box":
                orders = CreateBulk(routeC.drawBoxC.orders);
                bulk = true;
                key = routeC.drawBoxC.key;
                break;
            case "Flat Top":
                orders = CreateBulk(routeC.flatTopBottC.orders);
                bulk = true;
                key = routeC.flatTopBottC.key;
                break;
            case "Flat Ends":
                orders = CreateBulk(routeC.flatEndsC.orders);
                bulk = true;
                key = routeC.flatEndsC.key;
                break;
            case "Flat Shelves":
                orders = CreateBulk(routeC.flatShelvesC.orders);
                bulk = true;
                key = routeC.flatShelvesC.key;
                break;
            case "Flat Other":
                orders = CreateBulk(routeC.flatOtherC.orders);
                bulk = true;
                key = routeC.flatOtherC.key;
                break;
            case "Flat Back":
                orders = CreateBulk(routeC.flatBacksC.orders);
                bulk = true;
                key = routeC.flatBacksC.key;
                break;
            case "Flat Rail":
                orders = CreateBulk(routeC.flatRailsC.orders);
                bulk = true;
                key = routeC.flatRailsC.key;
                break;
            case "Flat Draw":
                orders = CreateBulk(routeC.flatDrawBoxC.orders);
                bulk = true;
                key = routeC.flatDrawBoxC.key;
                break;
            case "Expr Top":
                orders = routeC.exprTopBottC.orders;
                key = routeC.exprTopBottC.key;
                break;
            case "Expr Ends":
                orders = routeC.exprEndsC.orders;
                key = routeC.exprEndsC.key;
                break;
            case "Expr Shelves":
                orders = routeC.exprShelvesC.orders;
                key = routeC.exprShelvesC.key;
                break;
            case "Expr Other":
                orders = routeC.exprOtherC.orders;
                key = routeC.exprOtherC.key;
                break;
            case "Expr Back":
                orders = routeC.exprBacksC.orders;
                key = routeC.exprBacksC.key;
                break;
            case "Expr Rail":
                orders = routeC.exprRailsC.orders;
                key = routeC.exprRailsC.key;
                break;
            case "Expr Draw":
                orders = routeC.exprDrawBoxC.orders;
                key = routeC.flatDrawBoxC.key;
                break;
            case "Carrier":
                orders = routeC.carrierC.orders;
                key = routeC.carrierC.key;
        }
        pageNoMax = orders.size() - 1;
        pageNo = 0;
        DrawPickList(pageNo);
    }

    private String CheckPickedTimeline(String ord)
    {
        String result = "NO";
        String route = "";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Log.v("Connection", "Open");
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WITH (READUNCOMMITTED) WHERE Order_No = '" + ord + "' AND Pick_List = '"+ category + "' AND Area = 'PK-CARCS'");
            while (reset.next()) {
                route = reset.getString("Route").trim();
                String complete;
                result = "P";
                try {complete = reset.getString("Complete");}catch (Exception e){complete = "";}
                try {if(complete.isEmpty()) complete = "";} catch (Exception e) {complete = "";}
                result = complete;
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }

        if(!route.equals(routeC.name) && !route.equals(""))
        {
            UpdateRoute();
        }
        return  result;
    }

    private void UpdateRoute()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Log.v("Connection", "Open");
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Statement stmt = conn.createStatement();
            String sql = "UPDATE Clayton_Order_Timeline SET Route = '"+routeC.name+"' WHERE Order_No = '" + order + "'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
    }

    private List<OrderC> CreateBulk(List<OrderC> ord) {
        List<OrderC> temp = new ArrayList<>();
        OrderC bulk = new OrderC();
        bulk.name = ord.get(0).name;
        for (OrderC o : ord) {
            for (Carcase c : o.carcases) {
                Carcase co = new Carcase();
                co.route = c.route;
                co.order = c.order;
                co.product = c.product;
                co.desc = c.desc;
                co.drawing = c.drawing;
                co.analysisA = c.analysisA;
                co.analysisD = c.analysisD;
                co.analysisE = c.analysisE;
                co.category = c.category;
                co.consumer = c.consumer;
                co.warehouse = c.warehouse;
                co.total = c.total;
                co.edged = c.edged;
                co.scanned = c.scanned;
                bulk.AddCarcase(co);
            }
        }
        temp.add(bulk);
        return temp;
    }

    public void BtnLeftClick(View v)
    {
        if(progressBar.getVisibility() == View.GONE) {
            pageNo--;
            DrawPickList(pageNo);
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void BtnRightClick(View v)
    {
        if(progressBar.getVisibility() == View.GONE) {
            pageNo++;
            DrawPickList(pageNo);
        }
    }

    private List<String> GetEdged(String order)
    {
        List<String> edged = new ArrayList<>();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Edging WHERE order_no = '" + order + "'");
            while (reset.next()) {
                String prod = reset.getString("Component").trim();
                String col = reset.getString("Edging Tape").trim();
                int qty = reset.getInt("Comp Qty");
                String e = prod + "," + col + ","  + qty;
                edged.add(e);
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return edged;
    }

    private String IsEdged(List<String> edged, Carcase c)
    {
        String result = "NO";
        String color;
        int q = 0;
        for (String e : edged)
        {
            String[] temp = e.split(",");
            if(temp[0].equals(c.product)) {
                color = temp[1];
                q += Integer.parseInt(temp[2]);
                result = color + "," + q;
            }
        }
        return result;
    }

    private void DrawPickList(int page) {
        currentOrder = orders.get(page);
        Carcase tempC = currentOrder.carcases.get(0);

        edged = GetEdged(currentOrder.name);
        if (edged.size() != 0)
        {
            getSupportActionBar().setTitle(category + "  ** CONTAINS EDGED PRODUCTS **");
        }

        routeNo.setText(tempC.route);
        if(bulk)
            orderNo.setVisibility(View.GONE);
        orderNo.setText(tempC.order);
        order = tempC.order;

        wall.setVisibility(View.VISIBLE);
        tall.setVisibility(View.VISIBLE);
        base.setVisibility(View.VISIBLE);
        other.setVisibility(View.VISIBLE);

        //lay.removeAllViews();
        wall.removeAllViews();
        tall.removeAllViews();
        base.removeAllViews();
        other.removeAllViews();

        TextView txtWall = new TextView(this);
        txtWall.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtWall.setText("Wall");
        txtWall.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
        txtWall.setGravity(Gravity.CENTER_HORIZONTAL);
        wall.addView(txtWall);

        TextView txtTall = new TextView(this);
        txtTall.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtTall.setText("Tall");
        txtTall.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
        txtTall.setGravity(Gravity.CENTER_HORIZONTAL);
        tall.addView(txtTall);

        TextView txtBase = new TextView(this);
        txtBase.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtBase.setText("Base");
        txtBase.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
        txtBase.setGravity(Gravity.CENTER_HORIZONTAL);
        base.addView(txtBase);

        TextView txtOther = new TextView(this);
        txtOther.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtOther.setText("Other");
        txtOther.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
        txtOther.setGravity(Gravity.CENTER_HORIZONTAL);
        other.addView(txtOther);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(currentOrder.carcases.size());

        prodPicked = 0;
        prodCount = 0;

        for(Carcase c:currentOrder.carcases)
        {
            c.edged = IsEdged(edged, c);
            final Carcase fC = c;
            TextView txtBlank = new TextView(this);
            txtBlank.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            txtBlank.setText("");
            txtBlank.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);

            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.VERTICAL);
            line.setId(R.id.line);
            line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout l1 = new LinearLayout(this);
            l1.setOrientation(LinearLayout.HORIZONTAL);
            l1.setId(R.id.l1);
            l1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            l2.setId(R.id.l2);
            l2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            LinearLayout l3 = new LinearLayout(this);
            l3.setOrientation(LinearLayout.HORIZONTAL);
            l3.setId(R.id.l3);
            l3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            final TextView txtProd = new TextView(this);
            txtProd.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtProd.setText(c.product);
            txtProd.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
            l1.addView(txtProd);

            TextView txtDesc = new TextView(this);
            txtDesc.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtDesc.setText(c.desc);
            txtDesc.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
            l1.addView(txtDesc);

            TextView txtQuant = new TextView(this);
            txtQuant.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtQuant.setText(Integer.toString(c.total));
            txtQuant.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
            txtQuant.setGravity(Gravity.CENTER);
            l1.addView(txtQuant);

            Button btnLocate = new Button(this);
            btnLocate.setText("Locate Stock");
            btnLocate.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
            btnLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(progressBar.getVisibility() == View.GONE) {
                        FindStock(fC.product);
                    }
                }
            });
            l1.addView(btnLocate);

            line.addView(l1);

            if(c.consumer.equals("CD")) {
                Button btnCD = new Button(this);
                btnCD.setText(c.consumer);
                btnCD.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                btnCD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(progressBar.getVisibility() == View.GONE) {
                            CutDown(fC.total, fC.product, fC.analysisD);
                        }
                    }
                });
                l2.addView(btnCD);
            }
            else {
                TextView txtCons = new TextView(this);
                txtCons.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtCons.setText(c.consumer);
                txtCons.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                l2.addView(txtCons);
            }

            final TextView txtPicked = new TextView(this);
            final TextView txtShortaged = new TextView(this);

            Button btnPick = new Button(this);
            btnPick.setText("Pick");
            btnPick.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
            btnPick.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(progressBar.getVisibility() == View.GONE) {
                        currentProduct = fC;
                        String pick = txtPicked.getText().toString();
                        String shor = txtShortaged.getText().toString();
                        pick = pick.substring(8, pick.length());
                        pick = pick.substring(0, pick.length() - 1);
                        shor = shor.substring(11, shor.length());
                        shor = shor.substring(0, shor.length() - 1);
                        int p = Integer.parseInt(pick);
                        int s = Integer.parseInt(shor);
                        int total = p + s;
                        if (total != fC.total) {
                            PickStock(fC.product, fC.total, total, fC.warehouse, fC.analysisD, fC.desc);
                        }
                    }
                }
            });
            l2.addView(btnPick);


            txtPicked.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtPicked.setText("Picked [ ]");
            txtPicked.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            txtPicked.setGravity(Gravity.CENTER);
            l2.addView(txtPicked);

            txtShortaged.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtShortaged.setText("Shortaged [ ]");
            txtShortaged.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            txtShortaged.setGravity(Gravity.CENTER);
            l2.addView(txtShortaged);

            new GetPickStatus().execute(c.product, c.order, Integer.toString(c.total), c.analysisD);

            line.addView(l2);

            if(!c.edged.equals("NO"))
            {
                String temp[] = c.edged.split(",");
                TextView txtE = new TextView(this);
                txtE.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtE.setText(temp[0]);
                txtE.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                txtE.setGravity(Gravity.CENTER);
                l3.addView(txtE);

                final TextView txtQ = new TextView(this);
                txtQ.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
                txtQ.setText(temp[1]);
                txtQ.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                txtQ.setGravity(Gravity.CENTER);
                l3.addView(txtQ);

                Button btnEdge = new Button(this);
                btnEdge.setText("Pick Edged");
                btnEdge.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
                btnEdge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (progressBar.getVisibility() == View.GONE) {
                            String pick = txtPicked.getText().toString();
                            String shor = txtShortaged.getText().toString();
                            pick = pick.substring(8, pick.length());
                            pick = pick.substring(0, pick.length() - 1);
                            shor = shor.substring(11, shor.length());
                            shor = shor.substring(0, shor.length() - 1);
                            int p = Integer.parseInt(pick);
                            int s = Integer.parseInt(shor);
                            int total = p + s;
                            if (total != fC.total) {
                                PickEdged(txtProd.getText().toString(), txtQ.getText().toString(), fC.analysisD);
                            }
                        }
                    }
                });

                l3.addView(btnEdge);

                line.addView(l3);
            }

            //lay.addView(line);
            switch(c.analysisD)
            {
                case "WALL":
                    wall.addView(line);
                    wall.addView(txtBlank);
                    break;
                case "TALL":
                    tall.addView(line);
                    tall.addView(txtBlank);
                    break;
                case "BASE":
                    base.addView(line);
                    base.addView(txtBlank);
                    break;
                default:
                    other.addView(line);
                    other.addView(txtBlank);
                    break;
            }

            prodCount++;
        }

        if(wall.getChildCount() == 1)
            wall.setVisibility(View.GONE);
        if(tall.getChildCount() == 1)
            tall.setVisibility(View.GONE);
        if(base.getChildCount() == 1)
            base.setVisibility(View.GONE);
        if(other.getChildCount() == 1)
            other.setVisibility(View.GONE);

        Toast toast = Toast.makeText(getApplicationContext(), "Getting pick status!", Toast.LENGTH_SHORT);
        toast.show();

        if(pageNo == 0)
            btnLeft.setEnabled(false);
        else
            btnLeft.setEnabled(true);

        if(pageNo == pageNoMax)
            btnRight.setEnabled(false);
        else
            btnRight.setEnabled(true);

        if(prodPicked == 0)
            progress = false;
    }

    private void PickEdged(String prod, String quant, final String analysisD)
    {
        int total = GetEdgedQuant(prod);
        int q = Integer.parseInt(quant);
        String ord = orderNo.getText().toString() + key;
        if(bulk)
            ord = routeC.name + key;

        final String p = prod;
        final String o = ord;
        final String qu = Integer.toString(q);
        final String t = quant;

        if(total == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog;
            builder.setMessage("No product is in the EDGED cell");
            builder.setTitle("Alert!");
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
        else if(total < q)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog;
            builder.setMessage("Not enough product is in the EDGED cell");
            builder.setTitle("Alert!");
            builder.setPositiveButton("Pick", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String SPsql = "EXEC [dbo].[Def_DefCap_EntireBinToBin] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                        Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
                        PreparedStatement ps = conn.prepareStatement(SPsql);
                        ps.setEscapeProcessing(true);
                        ps.setQueryTimeout(30);
                        ps.setString(1, userID.substring(0, 2) + "0001");
                        ps.setString(2, userID);
                        ps.setString(3, "03" + p);
                        ps.setString(4, "PI01Z");
                        ps.setString(5, "EDGED");
                        ps.setString(6, o);
                        ps.setString(7, " ");
                        ps.setString(8, " ");
                        ps.setString(9, " ");
                        ps.setString(10, " ");
                        ps.setString(11, qu);
                        ps.setString(12, " ");
                        ps.setString(13, " ");
                        ps.setString(14, " ");
                        ResultSet rs = ps.executeQuery();
                        AsyncTask test = new GetPickStatus().execute(p, orderNo.getText().toString(), t, analysisD);
                        if (!progress) {
                            progress = true;
                            MarkProgress();
                        }
                    } catch (Exception e) {
                        Log.w("Error", e.getMessage());
                    }
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
        else
        {
            try {
                String SPsql = "EXEC [dbo].[Def_DefCap_EntireBinToBin] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
                PreparedStatement ps = conn.prepareStatement(SPsql);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(30);
                ps.setString(1, userID.substring(0, 2) + "0001");
                ps.setString(2, userID);
                ps.setString(3, "03" + prod);
                ps.setString(4, "PI01Z");
                ps.setString(5, "EDGED");
                ps.setString(6, ord);
                ps.setString(7, " ");
                ps.setString(8, " ");
                ps.setString(9, " ");
                ps.setString(10, " ");
                ps.setString(11, quant);
                ps.setString(12, " ");
                ps.setString(13, " ");
                ps.setString(14, " ");
                ResultSet rs = ps.executeQuery();
                AsyncTask test = new GetPickStatus().execute(p, orderNo.getText().toString(), t, analysisD);
                if(!progress)
                {
                    progress = true;
                    MarkProgress();
                }
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
        }
    }

    private int GetEdgedQuant(String prod)
    {
        int tot = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT SUM(quantity) AS q FROM scheme.stquem WHERE bin_number = 'EDGED' AND prod_code = '"+prod+"' AND quantity <> 0");
            while (reset.next()) {
                int t = reset.getInt("q");
                tot += t;
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return tot;
    }

    private int CheckBulk(String product, String order, String pickList)
    {
        int tot = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM CR_Bulk_Pick_Checker WHERE Product = '"+product+"' AND Order_No = '" + order + "' AND Pick_List = '"+ pickList+"'");
            while(reset.next()) {
                int t = reset.getInt("Quantity");
                tot += t;
            }
        } catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return tot;
    }

    private int CheckPicked(String product, String order)
    {
        order = order + key;
        if(bulk)
            order = routeC.name + key;
        int tot = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM scheme.stkhstm WITH (READUNCOMMITTED) WHERE product = '"+product+"' AND to_bin_number = 'PI01Z' AND comments LIKE '"+order+"%'");
            while (reset.next()) {
                int t = reset.getInt("movement_quantity");
                tot += t;
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return tot;
    }

    private int CheckShortages(String product, String order)
    {
        int tot = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Shortages] WITH (READUNCOMMITTED) WHERE Product = '"+product+"' AND [Order No] = '"+order+"' AND [Pick List] = '"+category+"'");
            while (reset.next()) {
                int t = reset.getInt("Quantity");
                tot += t;
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return tot;
    }

    private int CheckBulkShortages(String product)
    {
        List<OrderC> tOrders = new ArrayList<>();

        switch (category)
        {
            case "Backs":
                tOrders = routeC.jBacksC.orders;
                break;
            case "Rails":
                tOrders = routeC.railC.orders;
                break;
            case "Draw Box":
                tOrders = routeC.drawBoxC.orders;
                break;
            case "Flat Top":
                tOrders = routeC.flatTopBottC.orders;
                break;
            case "Flat Ends":
                tOrders = routeC.flatEndsC.orders;
                break;
            case "Flat Shelves":
                tOrders = routeC.flatShelvesC.orders;
                break;
            case "Flat Other":
                tOrders = routeC.flatOtherC.orders;
                break;
            case "Flat Back":
                tOrders = routeC.flatBacksC.orders;
                break;
            case "Flat Rail":
                tOrders = routeC.flatRailsC.orders;
                break;
            case "Flat Draw":
                tOrders = routeC.flatDrawBoxC.orders;
                break;
        }

        String ord = "(";

        for(OrderC o : tOrders)
        {
            ord += "'" + o.name + "',";
        }

        ord = ord.substring(0, ord.length() - 1);
        ord += ")";

        int tot = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Shortages] WITH (READUNCOMMITTED) WHERE Product = '"+product+"' AND [Order No] IN "+ord+" AND [Pick List] = '"+category+"'");
            while (reset.next()) {
                int t = reset.getInt("Quantity");
                tot += t;
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return tot;
    }

    private class GetPickStatus extends AsyncTask<String, Integer, String> {
        protected  void onPreExecute() {

        }

        protected String doInBackground(String... args) {
            int shortaged = 0;
            int total = Integer.parseInt(args[2]);
            boolean scanned = false;
            for(Carcase c:currentOrder.carcases)
            {
                if(c.product.equals(args[0]))
                    scanned = c.scanned;
            }
            int picked = 0;
            if(scanned)
                picked = CheckPicked(args[0], args[1]);
            else
                picked = CheckBulk(args[0], args[1], category);

            if(picked < total) {
                if(!bulk)
                    shortaged = CheckShortages(args[0], args[1]);
                else
                    shortaged = CheckBulkShortages(args[0]);
            }
            return args[0] + "," + picked + "," + shortaged + "," + total + "," + args[3];

        }

        protected void onPostExecute(String result) {

            String[] res = result.split(",");
            List<View> lines = new ArrayList<>();
            LinearLayout lay;
            switch (res[4])
            {
                case "WALL":
                    lay = wall;
                    break;
                case "TALL":
                    lay = tall;
                    break;
                case "BASE":
                    lay = base;
                    break;
                default:
                    lay = other;
                    break;
            }
            for (int index = 0; index < lay.getChildCount(); ++index) {
                View nextChild = lay.getChildAt(index);
                int id = nextChild.getId();
                if (id == R.id.line) {
                    lines.add(nextChild);
                }
            }

            for (View v : lines) {
                View vL1 = ((LinearLayout) v).getChildAt(0);
                View vTP = ((LinearLayout) vL1).getChildAt(0);
                TextView txtP = (TextView) vTP;
                if (txtP.getText().equals(res[0])) {
                    int p = Integer.parseInt(res[1]);
                    int s = Integer.parseInt(res[2]);
                    int t = Integer.parseInt(res[3]);
                    if ((p + s) >= t) {
                        v.setBackgroundColor(Color.GREEN);
                        prodPicked++;
                    }
                    View vL2 = ((LinearLayout) v).getChildAt(1);
                    View vP = ((LinearLayout) vL2).getChildAt(2);
                    View vS = ((LinearLayout) vL2).getChildAt(3);
                    TextView tP = (TextView) vP;
                    tP.setText("Picked [" + res[1] + "]");
                    TextView tS = (TextView) vS;
                    tS.setText("Shortaged [" + res[2] + "]");
                    IncrementBar();
                }
            }
            if (prodPicked == 0)
                progress = false;
            else
                progress = true;

            if (prodCount == prodPicked) {
                currentOrder.picked = true;
                MarkComplete();
                if(bulk) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }else {
                    if (!completeOrders.contains(currentOrder.name))
                        completeOrders.add(currentOrder.name);

                    if (completeOrders.size() == orders.size())
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }

            String pick = CheckPickedTimeline(orderNo.getText().toString());
            if(pick.equals("NO") && prodPicked >= 1)
                MarkProgress();
            if((prodCount == prodPicked) && !pick.equals("Complete"))
                MarkComplete();
        }
    }

    private void CutDown(final int quantity, final String prod, final String analysisD)
    {
        ShortProduct = prod;
        ShortQuantity = quantity;
        ShortAnalysisD = analysisD;
        Intent intent = new Intent(this, BinToBin.class);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("USER", userID);
        intent.putExtra("PRODUCT", prod);
        intent.putExtra("KEY", key);
        intent.putExtra("ORDER", order);
        intent.putExtra("CD", true);
        startActivityForResult(intent, RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            if(resultCode == RESULT_OK)
            {
                CreateShortage(ShortProduct, ShortQuantity, ShortQuantity, "CUT DOWN", ShortAnalysisD);
            }
        }
    }

    private void PickStock(final String product, final int pickListTotal, final int pickedTotal, final String wh, final String analysisD, final String desc)
    {
        List<Bin> bins = LocateStock(product);
        List<String> sB = new ArrayList<>();
        int totalProductAvailable = 0;
        boolean etherow = false;
        sB.add("");
        for(Bin b:bins)
        {
            if(!b.name.equals("PI01Z") && !b.name.equals("SWANS") && !b.name.substring(0, 2).equals("WA") && !b.name.equals("CP99A") && !b.name.equals("") && !b.name.equals("EDGED")) {
                sB.add(b.name);
                totalProductAvailable += b.quantity;
            }
            if(b.name.substring(0, 2).equals("WA") || b.name.equals("ETH01"))
            {
                etherow = true;
            }
        }
        final List<Bin> binsF = bins;
        if(sB.size() != 1)
        {
            PickDialog(sB, binsF, pickListTotal, pickedTotal, totalProductAvailable, product, wh, analysisD, desc);
        }
        else if(sB.size() == 1 && etherow)
        {
            int needed = pickListTotal - pickedTotal;
            ShortageDialog(product, needed, pickListTotal, analysisD, "ETH");
        }
        else
        {
            int needed = pickListTotal - pickedTotal;
            ShortageDialog(product, needed, pickListTotal, analysisD, "NO");
        }
    }

    private void PickDialog(List<String> sB, final List<Bin> bins, final int pickListTotal, final int pickedTotal, final int totalProductAvailable, final String product, final String wh, final String analysisD, final String desc)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_pick);
        final Spinner spiBin = (Spinner)dialog.findViewById(R.id.spiFromBin);
        final ArrayAdapter<String> dat = new ArrayAdapter<String>(this, R.layout.spinner_layout, sB);
        final TextView txtQuant = (TextView)dialog.findViewById(R.id.textView);
        final EditText ediQuant = (EditText)dialog.findViewById(R.id.txtQuantity);
        final LinearLayout layError = (LinearLayout)dialog.findViewById(R.id.layError);
        final TextView txtError = (TextView)dialog.findViewById(R.id.txtError);
        Button btnPick = (Button)dialog.findViewById(R.id.btnPick);

        dat.setDropDownViewResource(R.layout.spinner_layout);

        final int needed = pickListTotal - pickedTotal;

        spiBin.setAdapter(dat);
        spiBin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = spiBin.getItemAtPosition(position).toString();
                for (Bin b : bins) {
                    if (b.name.equals(choice))
                        txtQuant.setText(Integer.toString(b.quantity));
                }
                if (choice.equals(""))
                    txtQuant.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ediQuant.getText().toString().length() != 0 && !txtQuant.getText().toString().equals("")) {
                    int pickingTotal = Integer.parseInt(ediQuant.getText().toString());
                    int available = Integer.parseInt(txtQuant.getText().toString());
                    if (pickingTotal == 0) {
                        ShortageDialog(product, needed, pickListTotal, analysisD, "Create a shortage?");
                        dialog.cancel();
                    } else if (pickingTotal > available) {
                        PickTooMuch();
                    } else if (pickingTotal > needed) {
                        PickTooMuch();
                    } else if (pickingTotal < needed && pickingTotal < available) {
                        NotPickEnough();
                    } else if (pickingTotal < needed && pickingTotal == available) {
                        if (totalProductAvailable != available) {
                            //PICK NO SHORTAGE
                            if (currentProduct.scanned) {
                                if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh, analysisD)) {
                                    dialog.cancel();
                                } else {
                                    layError.setVisibility(View.VISIBLE);
                                    txtError.setText("ERROR: " + error);
                                }
                            } else {
                                PickBulk(pickingTotal);
                            }
                        } else {
                            //PICK & SHORTAGE
                            if (currentProduct.scanned) {
                                if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh, analysisD)) {
                                    final int n = needed - pickingTotal;
                                    ShortageDialog(product, n, pickListTotal, analysisD, "NO");
                                    dialog.cancel();
                                } else {
                                    layError.setVisibility(View.VISIBLE);
                                    txtError.setText("ERROR: " + error);
                                }
                            } else {
                                PickBulk(pickingTotal);
                            }
                        }
                    } else if (pickingTotal == needed) {
                        if (currentProduct.scanned) {
                            if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh, analysisD)) {
                                dialog.cancel();
                            } else {
                                layError.setVisibility(View.VISIBLE);
                                txtError.setText("ERROR: " + error);
                            }
                        } else {
                            PickBulk(pickingTotal);
                        }
                    }
                }
            }
        });
        dialog.setTitle(product + "  " + desc + "  " + needed);
        dialog.show();
    }

    private void PickBulk(int quantity)
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO CR_Bulk_Pick_Checker VALUES('"+currentProduct.order+"','"+currentProduct.product+"','"+quantity+"','"+userID+"','"+category+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            AsyncTask test = new GetPickStatus().execute(currentProduct.product, orderNo.getText().toString(), Integer.toString(currentProduct.total), currentProduct.analysisD);
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    private void ShortageDialog(final String product, final int shortageQuantity, final int pickListTotal, final String analysisD, final String comm)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(comm.equals("NO")) {
            builder.setMessage("No items in stock, create a shortage?");
            builder.setTitle("No Stock");
        }
        else if(comm.equals("ETH")) {
            builder.setMessage("Stock located at Etherow, create a shortage?");
            builder.setTitle("Etherow");
        }
        else{
            builder.setMessage(comm);
            builder.setTitle("Manual Shortage");
        }
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!comm.equals("ETH")) {
                    ShortageComment(product, shortageQuantity, pickListTotal, analysisD);
                } else {
                    CreateShortage(product, shortageQuantity, pickListTotal, "ETHEROW", analysisD);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShortageComment(final String stock, final int shortageQuantity, final int pickListTotal, final String analysisD)
    {
        final CharSequence comms[] = new CharSequence[] {"BOUGHT IN", "MACHINE SHOP", "CUT DOWN", "EDGED", "MTO ISSUED", "PALLET CD", "CARRIER"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comment");
        builder.setItems(comms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comment = comms[which].toString();
                if(!bulk)
                    CreateShortage(stock, shortageQuantity, pickListTotal, comment, analysisD);
                else
                    CreateBulkShortage(stock, shortageQuantity, pickListTotal, comment, analysisD);
            }
        });
        builder.show();
    }

    private void CreateShortage(String product, int shortageQuantity, int pickListTotal, String comment, String analysisD)
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String format = s.format(new Date());
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO [_Paul A0 Shortages] ([User], [Order No], [Product], [Quantity], [Date], [Comments], [Pick List]) VALUES('"+userID+"', '"+orderNo.getText()+"', '"+product+"', '"+shortageQuantity+"', '"+format+"', '"+comment+"', '"+category+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            AsyncTask test = new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(pickListTotal), analysisD);
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }


    private void CreateBulkShortage(String product, int shortageQuantity, int pickListTotal, String comment, String analysisD)
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String format = s.format(new Date());

        List<String> shortage = new ArrayList<>();
        int currentShortQuant = 0;
        //Get list of shortages
        List<OrderC> tOrders = new ArrayList<>();

        switch (category)
        {
            case "Backs":
                tOrders = routeC.jBacksC.orders;
                break;
            case "Rails":
                tOrders = routeC.railC.orders;
                break;
            case "Draw Box":
                tOrders = routeC.drawBoxC.orders;
                break;
            case "Flat Top":
                tOrders = routeC.flatTopBottC.orders;
                break;
            case "Flat Ends":
                tOrders = routeC.flatEndsC.orders;
                break;
            case "Flat Shelves":
                tOrders = routeC.flatShelvesC.orders;
                break;
            case "Flat Other":
                tOrders = routeC.flatOtherC.orders;
                break;
            case "Flat Back":
                tOrders = routeC.flatBacksC.orders;
                break;
            case "Flat Rail":
                tOrders = routeC.flatRailsC.orders;
                break;
            case "Flat Draw":
                tOrders = routeC.flatDrawBoxC.orders;
                break;
        }

        for(OrderC o : tOrders)
        {
            for(Carcase c : o.carcases)
            {
                if(c.product.equals(product))
                {
                    if(c.total <= shortageQuantity && currentShortQuant != shortageQuantity)
                    {
                        currentShortQuant += c.total;
                        String sh = c.order + "," + Integer.toString(c.total);
                        shortage.add(sh);
                    }
                    else if(c.total > shortageQuantity && currentShortQuant != shortageQuantity)
                    {
                        int rem = shortageQuantity - currentShortQuant;
                        currentShortQuant += rem;
                        String sh = c.order + "," + Integer.toString(rem);
                        shortage.add(sh);
                    }
                }
            }
        }

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            for(String sh : shortage) {
                String[] temp = sh.split(",");
                String sql = "INSERT INTO [_Paul A0 Shortages] ([User], [Order No], [Product], [Quantity], [Date], [Comments], [Pick List]) VALUES('" + userID + "', '" + temp[0] + "', '" + product + "', '" + Integer.parseInt(temp[1]) + "', '" + format + "', '" + comment + "', '" + category + "')";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            conn.close();
            AsyncTask test = new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(pickListTotal), analysisD);
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }

    }

    private void PickTooMuch()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        builder.setMessage("You are picking too much, check your quantities!");
        builder.setTitle("Stop!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void NotPickEnough()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        builder.setMessage("You are not picking enough and the product is available");
        builder.setTitle("Stop!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private boolean Pick(String product, String bin, int pickingTotal, int pickListTotal, String wh, String analysisD)
    {
        String ord = order + key;
        if(bulk)
            ord = routeC.name + key;
        try {
            String SPsql = "EXEC [dbo].[Def_DefCap_EntireBinToBin] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(30);
            ps.setString(1, userID.substring(0, 2) + "0001");
            ps.setString(2, userID);
            ps.setString(3, wh + product);
            ps.setString(4, "PI01Z");
            ps.setString(5, bin);
            ps.setString(6, ord);
            ps.setString(7, " ");
            ps.setString(8, " ");
            ps.setString(9, " ");
            ps.setString(10, " ");
            ps.setString(11, Integer.toString(pickingTotal));
            ps.registerOutParameter(12, Types.VARCHAR);
            ps.registerOutParameter(13, Types.INTEGER);
            ps.setString(14, " ");
            boolean hadResults = ps.execute();

            while(hadResults)
            {
                ResultSet rs = ps.getResultSet();

                hadResults = ps.getMoreResults();
            }

            int res = ps.getInt(13);
            String status = ps.getString(12);

            if(res == 1) {
                AsyncTask test = new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(pickListTotal), analysisD);
                if(!progress)
                {
                    progress = true;
                    MarkProgress();
                }
                return true;
            }
            else
            {
                error = status;
                return false;
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
            return false;
        }
    }

    private void FindStock(String stock)
    {
        List<Bin> bins = LocateStock(stock);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_stock_location);
        LinearLayout l = (LinearLayout)dialog.findViewById(R.id.dLay);
        dialog.setTitle(stock);

        for(Bin b:bins)
        {
            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);

            TextView txt = new TextView(this);
            txt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            txt.setText(b.name);
            txt.setGravity(Gravity.CENTER);
            txt.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            line.addView(txt);

            TextView txt1 = new TextView(this);
            txt1.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            txt1.setText(Integer.toString(b.quantity));
            txt1.setGravity(Gravity.CENTER);
            txt1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            line.addView(txt1);

            l.addView(line);
        }
        if(bins.size() == 0) {
            TextView txt = new TextView(this);
            txt.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            txt.setText("No Stock");
            txt.setGravity(Gravity.CENTER);
            txt.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            l.addView(txt);
        }
        dialog.show();
    }

    private List<Bin> LocateStock(String stock)
    {
        List<Bin> bins = new ArrayList<>();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT bin_number, SUM(quantity) AS Total FROM scheme.stquem WITH (READUNCOMMITTED) WHERE product = '" + stock + "' AND quantity > 0 GROUP BY scheme.stquem.bin_number");
            while (reset.next()) {
                String n = reset.getString("bin_number").trim();
                if(!n.equals("PI01Z") && !n.equals("SWANS") && !n.equals("")) {
                    int q = reset.getInt("Total");
                    Bin b = new Bin(n, q);
                    bins.add(b);
                }
            }
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return bins;
    }

    private void IncrementBar()
    {
        int pro = progressBar.getProgress() + 1;
        progressBar.setProgress(pro);

        if(progressBar.getProgress() == progressBar.getMax())
            progressBar.setVisibility(View.GONE);
    }

    private void MarkComplete()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "UPDATE Clayton_Order_Timeline SET Complete = 'Complete' WHERE Order_No ='" + order+"' AND Pick_List ='" + category + "' AND Area = 'PK-CARCS'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    private void MarkProgress()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO Clayton_Order_Timeline (Order_No, Pick_List, Area, Route, UserID) VALUES('"+order+"', '"+category+"', 'PK-CARCS', '"+routeC.name+"', '"+userID+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
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
