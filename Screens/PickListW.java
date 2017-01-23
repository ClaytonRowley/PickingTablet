package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.Bin;
import com.richmondcabinets.claytonrowley.pickingtablet.Screens.R;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.OrderW;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.RouteW;
import com.richmondcabinets.claytonrowley.pickingtablet.Worktop.Worktop;

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

public class PickListW extends AppCompatActivity {

    String drawing;
    RouteW routeW;

    Button btnLeft;
    Button btnRight;

    ProgressBar progressBar;

    LinearLayout lay;

    boolean progress;

    int pageNo;
    int pageNoMax;

    int prodCount;
    int prodPicked;

    String key;
    String order;
    String userID;

    Worktop currentProduct;

    List<OrderW> orders;
    OrderW currentOrder;

    List<String> completeOrders;

    TextView orderNo;
    TextView routeNo;

    String ShortProduct;
    int ShortQuantity;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list_w);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        routeW = (RouteW)getIntent().getSerializableExtra("ROUTE");
        drawing = getIntent().getStringExtra("ORDER");
        userID = getIntent().getStringExtra("USER");
        progress = getIntent().getBooleanExtra("PROGRESS", false);
        getSupportActionBar().setTitle(drawing);

        orderNo = (TextView)findViewById(R.id.txtOrder);
        routeNo = (TextView)findViewById(R.id.txtRoute);

        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);

        lay = (LinearLayout)findViewById(R.id.lay);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        completeOrders = new ArrayList<>();

        prodCount = 0;
        prodPicked = 0;

        switch (drawing)
        {
            case "Worktops":
                orders = routeW.worktopW.orders;
                key = routeW.worktopW.key;
                break;
            case "Upstands":
                orders = routeW.upstandW.orders;
                key = routeW.upstandW.key;
                break;
        }
        orders = ReverseOrders(orders);
        pageNoMax = orders.size() - 1;
        pageNo = 0;
        DrawPickList(pageNo);
    }

    private List<OrderW> ReverseOrders(List<OrderW> orders)
    {
        List<OrderW> list = new ArrayList<>();
        for(int a = orders.size(); a != 0; --a)
        {
            list.add(orders.get(a-1));
        }
        return list;
    }

    public void BtnLeftClick(View v)
    {
        if(progressBar.getVisibility() == View.GONE) {
            pageNo--;
            DrawPickList(pageNo);
        }
    }

    public void BtnRightClick(View v)
    {
        if(progressBar.getVisibility() == View.GONE) {
            pageNo++;
            DrawPickList(pageNo);
        }
    }

    private void DrawPickList(int page)
    {
        currentOrder = orders.get(page);
        Worktop tempW = currentOrder.worktops.get(0);

        routeNo.setText(tempW.route);
        orderNo.setText(tempW.order);
        order = tempW.order;

        lay.removeAllViews();

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(currentOrder.worktops.size());

        prodPicked = 0;
        prodCount = 0;

        for(Worktop w:currentOrder.worktops)
        {
            final Worktop fW = w;
            TextView txtBlank = new TextView(this);
            txtBlank.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            txtBlank.setText("");
            txtBlank.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            lay.addView(txtBlank);

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

            Button btnLocate = new Button(this);
            btnLocate.setText("Locate Stock");
            btnLocate.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
            btnLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(progressBar.getVisibility() == View.GONE) {
                        FindStock(fW.product);
                    }
                }
            });
            l1.addView(btnLocate);

            line.addView(l1);

            Button btnCD = new Button(this);
            btnCD.setText("Cut Down");
            btnCD.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            btnCD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(progressBar.getVisibility() == View.GONE) {
                        CutDown(fW.total, fW.product);
                    }
                }
            });
            l2.addView(btnCD);

            final TextView txtPicked = new TextView(this);
            final TextView txtShortaged = new TextView(this);

            Button btnPick = new Button(this);
            btnPick.setText("Pick");
            btnPick.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
            btnPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (progressBar.getVisibility() == View.GONE) {
                        currentProduct = fW;
                        String pick = txtPicked.getText().toString();
                        String shor = txtShortaged.getText().toString();
                        pick = pick.substring(8, pick.length());
                        pick = pick.substring(0, pick.length() - 1);
                        shor = shor.substring(11, shor.length());
                        shor = shor.substring(0, shor.length() - 1);
                        int p = Integer.parseInt(pick);
                        int s = Integer.parseInt(shor);
                        int total = p + s;
                        if (total != fW.total) {
                            PickStock(fW.product, fW.total, total, fW.warehouse, fW.desc, fW.cutDown);
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

            new GetPickStatus().execute(w.product, w.order, Integer.toString(w.total));

            line.addView(l2);

            lay.addView(line);

            prodCount++;
        }

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

    private void PickStock(final String product, final int pickListTotal, final int pickedTotal, final String wh, final String desc, final String cutFrom)
    {
        List<Bin> bins = LocateStock(product);
        List<String> sB = new ArrayList<>();
        int totalProductAvailable = 0;
        sB.add("");
        for(Bin b:bins)
        {
            if(!b.name.equals("PI01Z") && !b.name.equals("SWANS") && !b.name.substring(0, 2).equals("WA") && !b.name.equals("CP99A") && !b.name.equals("")) {
                sB.add(b.name);
                totalProductAvailable += b.quantity;
            }
        }
        final List<Bin> binsF = bins;
        if(sB.size() != 1)
        {
            PickDialog(sB, binsF, pickListTotal, pickedTotal, totalProductAvailable, product, wh, desc);
        }
        else
        {
            bins = LocateStock(cutFrom);
            sB = new ArrayList<>();
            totalProductAvailable = 0;
            sB.add("");
            for(Bin b:bins)
            {
                if(!b.name.equals("PI01Z") && !b.name.equals("SWANS") && !b.name.substring(0, 2).equals("WA") && !b.name.equals("CP99A") && !b.name.equals("")) {
                    sB.add(b.name);
                    totalProductAvailable += b.quantity;
                }
            }
            if(sB.size() != 1)
            {
                //CUTDOWNS AVAILABLE
                final List<String> sBF = sB;
                final List<Bin> binsFF = bins;
                int needed = pickListTotal - pickedTotal;
                final int ratio = GetCutDownRatio(product, cutFrom);
                int cutFromRequired = needed / ratio;
                if((needed % ratio) != 0)
                    cutFromRequired ++;
                if(totalProductAvailable >= cutFromRequired)
                {
                    final int av = totalProductAvailable;
                    final int n = needed;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Create cut down using " + cutFrom + "?");
                    builder.setTitle("Cut Down");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PickDialog(sBF, binsFF, pickListTotal, pickedTotal, av, product, wh, desc, cutFrom, ratio);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ShortageDialog(product, n, pickListTotal, "NO");
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    ShortageDialog(product, needed, pickListTotal, "NO");
                }
            }
            else {
                int needed = pickListTotal - pickedTotal;
                ShortageDialog(product, needed, pickListTotal, "NO");
            }
        }
    }

    private int GetCutDownRatio(String product, String cutFrom)
    {
        String productLength = product.substring(product.length() - 3, product.length());
        String cutDownLength = cutFrom.substring(cutFrom.length() - 3, cutFrom.length());

        if(productLength.equals("200"))
        {
            if(cutDownLength.equals("300"))
                return 1;
            if(cutDownLength.equals("400") || cutDownLength.equals("410"))
                return 2;
        }
        if(productLength.equals("150"))
            return 2;

        return 1;
    }

    private void PickDialog(List<String> sB, final List<Bin> bins, final int pickListTotal, final int pickedTotal, final int totalProductAvailable, final String product, final String wh, final String desc, final String cutFrom, final int ratio)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_pick);
        final Spinner spiBin = (Spinner)dialog.findViewById(R.id.spiFromBin);
        ArrayAdapter<String> dat = new ArrayAdapter<>(this, R.layout.spinner_layout, sB);
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
                    if (b.name.equals(choice)) {
                        txtQuant.setText(Integer.toString(b.quantity));
                    }
                }
                if (choice.equals(""))
                    txtQuant.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ediQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    txtError.setText("");
                    layError.setVisibility(View.GONE);
                } else {
                    int total = Integer.parseInt(ediQuant.getText().toString());
                    if (total != 0 && total <= Integer.parseInt(txtQuant.getText().toString())) {
                        int createTotal = total * ratio;
                        txtError.setText("Use " + total + " " + cutFrom + " to create " + createTotal + " " + product);
                        layError.setVisibility(View.VISIBLE);
                    } else {
                        ediQuant.setText(txtQuant.getText());
                    }
                }
            }
        });

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ediQuant.getText().toString().length() != 0 && !txtQuant.getText().toString().equals("")) {
                    int pickingTotal = Integer.parseInt(ediQuant.getText().toString());
                    int available = Integer.parseInt(txtQuant.getText().toString());
                    if (pickingTotal == 0) {
                        //NOT PICKING ANYTHING
                    } else if (pickingTotal > available) {
                        PickTooMuch();
                    } else {
                        int useTotal = Integer.parseInt(ediQuant.getText().toString());
                        int createTotal = useTotal * ratio;
                        if (stkAdj(product, "WT53A", createTotal, -1)) {
                            if (stkAdj(cutFrom, spiBin.getSelectedItem().toString(), useTotal, totalProductAvailable)) {
                                int pickTotal = 0;
                                if (createTotal > needed)
                                    pickTotal = needed;
                                if (createTotal < needed)
                                    pickTotal = createTotal;
                                if (Pick(product, "WT35A", pickTotal, pickListTotal, wh)) {
                                    dialog.cancel();
                                } else {
                                    layError.setVisibility(View.VISIBLE);
                                    txtError.setText("ERROR: " + error);
                                }
                            } else {
                                layError.setVisibility(View.VISIBLE);
                                txtError.setText("ERROR: " + error);
                            }
                        } else {
                            layError.setVisibility(View.VISIBLE);
                            txtError.setText("ERROR: " + error);
                        }
                    }
                }
            }
        });
        dialog.setTitle(product + "  " + needed);
        dialog.show();
    }

    private void PickDialog(List<String> sB, final List<Bin> bins, final int pickListTotal, final int pickedTotal, final int totalProductAvailable, final String product, final String wh, final String desc)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_pick);
        final Spinner spiBin = (Spinner)dialog.findViewById(R.id.spiFromBin);
        ArrayAdapter<String> dat = new ArrayAdapter<>(this, R.layout.spinner_layout, sB);
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
                        ShortageDialog(product, needed, pickListTotal, "Create a shortage?");
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
                            if(currentProduct.scanned) {
                                if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh)) {
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
                            if(currentProduct.scanned) {
                                if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh)) {
                                    final int n = needed - pickingTotal;
                                    ShortageDialog(product, n, pickListTotal, "NO");
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
                        if(currentProduct.scanned) {
                            if (Pick(product, spiBin.getSelectedItem().toString(), pickingTotal, pickListTotal, wh)) {
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
            String sql = "INSERT INTO CR_Bulk_Pick_Checker VALUES('"+currentProduct.order+"','"+currentProduct.product+"','"+quantity+"','"+userID+"','"+drawing+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            AsyncTask test = new GetPickStatus().execute(currentProduct.product, orderNo.getText().toString(), Integer.toString(currentProduct.total));
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    private void ShortageDialog(final String product, final int shortageQuantity, final int pickListTotal, final String comm)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(comm.equals("NO")) {
            builder.setMessage("No items in stock, create a shortage?");
            builder.setTitle("No Stock");
        }
        else{
            builder.setMessage(comm);
            builder.setTitle("Manual Shortage");
        }
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShortageComment(product, shortageQuantity, pickListTotal);
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

    private void ShortageComment(final String stock, final int shortageQuantity, final int pickListTotal)
    {
        final CharSequence comms[] = new CharSequence[] {"BOUGHT IN", "MACHINE SHOP", "CUT DOWN", "EDGED", "MTO ISSUED", "PALLET CD", "CARRIER"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comment");
        builder.setItems(comms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comment = comms[which].toString();
                CreateShortage(stock, shortageQuantity, pickListTotal, comment);
            }
        });
        builder.show();
    }

    private void CreateShortage(String product, int shortageQuantity, int pickListTotal, String comment)
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String format = s.format(new Date());
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO [_Paul A0 Shortages] ([User], [Order No], [Product], [Quantity], [Date], [Comments], [Pick List]) VALUES('"+userID+"', '"+orderNo.getText()+"', '"+product+"', '"+shortageQuantity+"', '"+format+"', '"+comment+"', '"+drawing+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(pickListTotal));
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    private void CutDown(final int quantity, final String prod)
    {
        ShortProduct = prod;
        ShortQuantity = quantity;
        Intent intent = new Intent(this, BinToBin.class);
        intent.putExtra("QUANTITY", quantity);
        intent.putExtra("USER", userID);
        intent.putExtra("PRODUCT", prod);
        intent.putExtra("KEY", key);
        intent.putExtra("ORDER", order);
        intent.putExtra("CD", true);
        startActivityForResult(intent, RESULT);
    }

    private int CheckPicked(String product, String order)
    {
        order = order + key;
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
            ResultSet reset = stmt.executeQuery("SELECT * FROM [_Paul A0 Shortages] WITH (READUNCOMMITTED) WHERE Product = '"+product+"' AND [Order No] = '"+order+"' AND [Pick List] = '"+drawing+"'");
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
            for(Worktop w:currentOrder.worktops)
            {
                if(w.product.equals(args[0]))
                    scanned = w.scanned;
            }
            int picked = 0;
            if(scanned)
                picked = CheckPicked(args[0], args[1]);
            else
                picked = CheckBulk(args[0], args[1], drawing);
            if(picked < total)
                shortaged = CheckShortages(args[0], args[1]);

            return args[0] + "," + picked + "," + shortaged + "," + total;

        }

        protected void onPostExecute(String result) {

            String[] res = result.split(",");
            List<View> lines = new ArrayList<>();
            for(int index=0; index<lay.getChildCount(); ++index) {
                View nextChild = lay.getChildAt(index);
                int id = nextChild.getId();
                if(id == R.id.line)
                {
                    lines.add(nextChild);
                }
            }

            for(View v : lines)
            {
                View vL1 = ((LinearLayout) v).getChildAt(0);
                View vTP = ((LinearLayout) vL1).getChildAt(0);
                TextView txtP = (TextView) vTP;
                if(txtP.getText().equals(res[0]))
                {
                    int p = Integer.parseInt(res[1]);
                    int s = Integer.parseInt(res[2]);
                    int t = Integer.parseInt(res[3]);
                    if((p + s) >= t) {
                        v.setBackgroundColor(Color.GREEN);
                        prodPicked ++;
                    }
                    View vL2 = ((LinearLayout) v).getChildAt(1);
                    View vP = ((LinearLayout) vL2).getChildAt(2);
                    View vS = ((LinearLayout) vL2).getChildAt(3);
                    TextView tP = (TextView)vP;
                    tP.setText("Picked ["+res[1]+"]");
                    TextView tS = (TextView)vS;
                    tS.setText("Shortaged [" + res[2] + "]");
                    IncrementBar();
                }
            }
            if(prodPicked == 0)
                progress = false;
            else
                progress = true;
            if(prodCount == prodPicked) {
                currentOrder.picked = true;
                MarkComplete();
                if (!completeOrders.contains(currentOrder.name))
                    completeOrders.add(currentOrder.name);

                if (completeOrders.size() == orders.size())
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            String pick = CheckPickedTimeline(orderNo.getText().toString());
            if(pick.equals("NO") && prodPicked >= 1)
                MarkProgress();
            if((prodCount == prodPicked) && !pick.equals("Complete"))
                MarkComplete();
        }
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
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WITH (READUNCOMMITTED) WHERE Order_No = '" + ord + "' AND Pick_List = '"+ drawing + "' AND Area = 'PK-WKTPS'");
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

        if(!route.equals(routeW.name) && !route.equals(""))
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
            String sql = "UPDATE Clayton_Order_Timeline SET Route = '"+routeW.name+"' WHERE Order_No = '" + order + "'";
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

    @Override
    public void onBackPressed() {

    }

    private void MarkComplete()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "UPDATE Clayton_Order_Timeline SET Complete = 'Complete' WHERE Order_No ='" + order+"' AND Pick_List ='" + drawing + "' AND Area = 'PK-WKTPS'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
    }

    private void IncrementBar()
    {
        int pro = progressBar.getProgress() + 1;
        progressBar.setProgress(pro);

        if(progressBar.getProgress() == progressBar.getMax())
            progressBar.setVisibility(View.GONE);
    }

    private boolean stkAdj(String product, String bin, int total, int old)
    {
        String wh = "03";
        int newQ;
        if(old != -1)
            newQ = 0 - total;
        else
            newQ = total - old;
        try {
            String SPsql = "EXEC [dbo].[Def_DefCap_EntireStkAdj] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(30);
            ps.setString(1, userID.substring(0, 2) + "0001");
            ps.setString(2, userID);
            ps.setString(3, wh + product.trim());
            ps.setString(4, bin);
            ps.setString(5, "CUTDOWN");
            ps.setString(6, " ");
            ps.setString(7, " ");
            ps.setString(8, " ");
            ps.setString(9, " ");
            ps.setString(10, Integer.toString(newQ));
            ps.setString(11, " ");
            ps.registerOutParameter(12, Types.VARCHAR);
            ps.registerOutParameter(13, Types.INTEGER);
            ps.setString(14, " ");
            boolean hadResults = ps.execute();

            String result = "";
            while(hadResults)
            {
                ResultSet rs = ps.getResultSet();

                hadResults = ps.getMoreResults();
            }

            int res = ps.getInt(13);
            String status = ps.getString(12);

            if(res == 1)
                return true;
            else
            {
                error = status;
                return false;
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
            error = e.getMessage();
            return false;
        }
    }

    private String GetWarehouse(String product, String bin)
    {
        String warehouse = "";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT warehouse FROM scheme.stquem WITH (READUNCOMMITTED) WHERE product = '"+product+"' AND bin_number = '"+bin+"'");
            while(reset.next())
            {
                warehouse = reset.getString("warehouse").trim();
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
        return warehouse;
    }

    private boolean Pick(String product, String bin, int quantity, int required, String wh)
    {
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
            ps.setString(6, order + key);
            ps.setString(7, " ");
            ps.setString(8, " ");
            ps.setString(9, " ");
            ps.setString(10, " ");
            ps.setString(11, Integer.toString(quantity));
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
                AsyncTask test = new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(required));
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

    private void MarkProgress()
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO Clayton_Order_Timeline (Order_No, Pick_List, Area, Route, UserID) VALUES('"+order+"', '"+drawing+"', 'PK-WKTPS', '"+routeW.name+"', '"+userID+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            if(resultCode == RESULT_OK)
            {
                PhantomShortage(ShortProduct, ShortQuantity, "CD", ShortQuantity, "");
            }
        }
    }

    private void PhantomShortage(String product, int quantity, String cons, int required, String comment)
    {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String format = s.format(new Date());
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO [_Paul A0 Shortages] ([User], [Order No], [Product], [Quantity], [Date], [Comments], [Status], [Pick List]) VALUES('"+userID+"', '"+orderNo.getText()+"', '"+product+"', '"+quantity+"', '"+format+"', '"+comment+"', 'COMPLETE', '"+drawing+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            AsyncTask test = new GetPickStatus().execute(product, orderNo.getText().toString(), Integer.toString(required));
            if(!progress)
            {
                progress = true;
                MarkProgress();
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
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
