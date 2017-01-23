package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.richmondcabinets.claytonrowley.pickingtablet.ProductListCD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

public class StkAdj extends AppCompatActivity {

    LinearLayout layProd;
    LinearLayout layDesc;
    LinearLayout layOldQaun;
    LinearLayout layConfirm;
    LinearLayout layCom;
    LinearLayout layNewQuan;
    LinearLayout laySubmit;
    LinearLayout layError;

    EditText ediBin;
    AutoCompleteTextView ediProd;
    EditText ediCom;
    EditText ediNewQuan;

    Button btnBinScan;
    Button btnProdScan;
    Button btnProducts;
    Button btnConfirm;
    Button btnNewQuantity;
    Button btnComScan;
    Button btnComments;
    Button btnSubmit;

    TextView txtDesc;
    TextView txtOldQuan;
    TextView txtError;

    String userID;

    ProductListCD pCD = new ProductListCD();

    boolean once = false;

    int oldQuantity = 0;

    static final int BIN = 1;
    static final int PRODUCT = 2;
    static final int COMMENT = 3;

    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stk_adj);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context con = this;

        layProd = (LinearLayout)findViewById(R.id.layProd);
        layDesc = (LinearLayout)findViewById(R.id.layDesc);
        layOldQaun = (LinearLayout)findViewById(R.id.layOldQaun);
        layConfirm = (LinearLayout)findViewById(R.id.layConfirm);
        layCom = (LinearLayout)findViewById(R.id.layCom);
        layNewQuan = (LinearLayout)findViewById(R.id.layNewQuan);
        laySubmit = (LinearLayout)findViewById(R.id.laySubmit);
        layError = (LinearLayout)findViewById(R.id.layError);

        ediBin = (EditText)findViewById(R.id.ediBin);
        ediProd = (AutoCompleteTextView)findViewById(R.id.ediProd);
        ediCom = (EditText)findViewById(R.id.ediCom);
        ediNewQuan = (EditText)findViewById(R.id.ediNewQuan);

        btnBinScan = (Button)findViewById(R.id.btnBinScan);
        btnProdScan = (Button)findViewById(R.id.btnProdScan);
        btnProducts = (Button)findViewById(R.id.btnProducts);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnNewQuantity = (Button)findViewById(R.id.btnNewQuantity);
        btnComScan = (Button)findViewById(R.id.btnComScan);
        btnComments = (Button)findViewById(R.id.btnComments);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        txtDesc = (TextView)findViewById(R.id.txtDesc);
        txtOldQuan = (TextView)findViewById(R.id.txtOldQuan);
        txtError = (TextView)findViewById(R.id.txtError);

        userID = getIntent().getStringExtra("USER");

        ediBin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(CheckBin(ediBin.getText().toString().toUpperCase())) {
                        layProd.setVisibility(View.VISIBLE);
                        ediProd.requestFocus();
                        pCD.PopulateProducts(ediBin.getText().toString().toUpperCase());
                        String[] PRODUCTS = new String[pCD.products.size()];
                        for(int a = 0; a < pCD.products.size(); a++) {
                            String temp = pCD.products.get(a);
                            String[] tempS = temp.split(",");
                            PRODUCTS[a] = tempS[0];
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, PRODUCTS);
                        ediProd.setAdapter(adapter);
                        layError.setVisibility(View.INVISIBLE);
                        txtError.setText("");
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                    return true;
                }
                return false;
            }
        });
        ediBin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layProd.getVisibility() == View.VISIBLE) {
                    ediProd.setText("");
                    txtDesc.setText("");
                    layDesc.setVisibility(View.INVISIBLE);
                    layProd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediBin.setText(ediBin.getText().subSequence(0, s.length() - 1));
                    ediBin.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediProd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!ediProd.getText().toString().equals("")) {
                        boolean check = false;
                        for (String s : pCD.products) {
                            String[] t = s.split(",");
                            if (ediProd.getText().toString().toUpperCase().trim().equals(t[0].trim())) {
                                layDesc.setVisibility(View.VISIBLE);
                                txtDesc.setText(t[1].trim());
                                layOldQaun.setVisibility(View.VISIBLE);
                                txtOldQuan.setText("Quantity : " + t[2].trim());
                                oldQuantity = Integer.parseInt(t[2].trim());
                                layConfirm.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                                check = true;
                            }
                        }
                        if(!check) {
                            String desc = CheckProduct(ediProd.getText().toString().toUpperCase());
                            if (!desc.equals("NO")) {
                                layDesc.setVisibility(View.VISIBLE);
                                txtDesc.setText(desc);
                                layOldQaun.setVisibility(View.VISIBLE);
                                txtOldQuan.setText("Quantity : 0");
                                oldQuantity = 0;
                                layConfirm.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                            } else {
                                layError.setVisibility(View.VISIBLE);
                                txtError.setText("ERROR: Product not found!");
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        ediProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layOldQaun.getVisibility() == View.VISIBLE) {
                    txtOldQuan.setText("Quantity :");
                    layOldQaun.setVisibility(View.INVISIBLE);
                    laySubmit.setVisibility(View.INVISIBLE);
                    txtDesc.setText("");
                    layDesc.setVisibility(View.INVISIBLE);
                    ediCom.setText("");
                    layCom.setVisibility(View.INVISIBLE);
                    layConfirm.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediProd.setText(ediProd.getText().subSequence(0, s.length() - 1));
                    ediProd.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNewQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layCom.setVisibility(View.VISIBLE);
                ediCom.requestFocus();
            }
        });
        ediCom.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!ediCom.getText().toString().equals("")) {
                        layNewQuan.setVisibility(View.VISIBLE);
                        ediNewQuan.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
        ediCom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layNewQuan.getVisibility() == View.VISIBLE) {
                    ediNewQuan.setText("");
                    layNewQuan.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediCom.setText(ediCom.getText().subSequence(0, s.length() - 1));
                    ediCom.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediNewQuan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                if (result == EditorInfo.IME_ACTION_DONE) {
                    if (!ediNewQuan.getText().toString().equals("")) {
                        laySubmit.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
        btnBinScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnBinScanClick();
            }
        });
        btnProdScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnProductScanClick();
            }
        });
        btnComScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnCommentScanClick();
            }
        });
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence temp[] = new CharSequence[pCD.products.size()];

                for(int a = 0; a <pCD.products.size(); a++) {
                    temp[a] = pCD.products.get(a);
                }

                final CharSequence products[] = temp;

                final AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("Pick a product");
                builder.setItems(products, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        layOldQaun.setVisibility(View.INVISIBLE);
                        String[] te = products[which].toString().split(",");
                        layDesc.setVisibility(View.VISIBLE);
                        txtDesc.setText(te[1].trim());
                        ediProd.setText(te[0].trim());
                        layOldQaun.setVisibility(View.VISIBLE);
                        txtOldQuan.setText("Quantity : " + te[2].trim());
                        oldQuantity = Integer.parseInt(te[2].trim());
                        layConfirm.setVisibility(View.VISIBLE);
                        layError.setVisibility(View.INVISIBLE);
                        txtError.setText("");
                    }
                });
                builder.show();
            }
        });
        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence comms[] = new CharSequence[] {"Booking In Error", "Damaged", "Returns", "Incorrect Stock ID", "Stock Loss", "Machining Error", "Picking Error", "Poor Finish", "Dropped", "Water Damaged"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("Comment");
                builder.setItems(comms, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = comms[which].toString();
                        ediCom.setText(comment);
                        layNewQuan.setVisibility(View.VISIBLE);
                        ediNewQuan.requestFocus();
                    }
                });
                builder.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!once) {
                    once = true;
                    layError.setVisibility(View.INVISIBLE);
                    txtError.setText("");
                    if (StkAdj(ediProd.getText().toString().toUpperCase(), ediBin.getText().toString().toUpperCase(), ediCom.getText().toString(), Integer.parseInt(ediNewQuan.getText().toString()), oldQuantity)) {
                        finish();
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("Error: " + error);
                        once = false;
                    }
                }
            }
        });
    }

    private String CheckProduct(String product)
    {
        String exists = "NO";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM scheme.stockm WITH(READUNCOMMITTED) WHERE product = '" + product + "'");
            while (reset.next()) {
                exists = reset.getString("long_description").trim();
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return exists;
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
        if(warehouse.equals(""))
        {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
                Log.w("Connection", "Open");
                Statement stmt = conn.createStatement();
                ResultSet reset = stmt.executeQuery("SELECT warehouse FROM scheme.stockm WITH (READUNCOMMITTED) WHERE product = '"+product+"' AND warehouse IN('01','02','03')");
                while(reset.next())
                {
                    warehouse = reset.getString("warehouse").trim();
                }
                stmt.close();
                conn.close();
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            }
        }
        return warehouse;
    }

    private boolean StkAdj(String product, String bin, String comment, int total, int old) {
        String wh = GetWarehouse(product, bin);
        String com = ConvertComment(comment);
        int newQ = total - old;
        try {
            String SPsql = "EXEC [dbo].[Def_DefCap_EntireStkAdj] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            //PreparedStatement ps = conn.prepareStatement(SPsql);
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(30);
            ps.setString(1, userID.substring(0, 2) + "0001");
            ps.setString(2, userID);
            ps.setString(3, wh + product.trim());
            ps.setString(4, bin);
            ps.setString(5, com);
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

    private void BtnBinScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult( intent, BIN);
    }

    private void BtnProductScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, PRODUCT);
    }

    private void BtnCommentScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, COMMENT);
    }

    private String ConvertComment(String comment)
    {
        String com = comment;
        switch (com) {
            case "Water Damaged":
                return "water";
            case "Damaged":
                return "damaged";
            case "Dropped":
                return "dropped";
            case "Poor Finish":
                return "finish";
            case "Picking Error":
                return "pickerr";
            case "Machining Error":
                return "macherr";
            case "Stock Loss":
                return "stockloss";
            case "Incorrect Stock ID":
                return "stockid";
            case "Returns":
                return "return";
            case "Booking In Error":
                return "bookin";
        }
        return com;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case BIN:
                    ediBin.setText(intent.getStringExtra("SCAN_RESULT"));
                    if(CheckBin(ediBin.getText().toString().toUpperCase())) {
                        layProd.setVisibility(View.VISIBLE);
                        ediProd.requestFocus();
                        pCD.PopulateProducts(ediBin.getText().toString().toUpperCase());
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                    break;
                case PRODUCT:
                    for(String s : pCD.products)
                    {
                        String[] t = s.split(",");
                        if (ediProd.getText().toString().trim().equals(t[0].trim())) {
                            if (ediProd.getText().toString().toUpperCase().trim().equals(t[0].trim())) {
                                layDesc.setVisibility(View.VISIBLE);
                                txtDesc.setText(t[1].trim());
                                layOldQaun.setVisibility(View.VISIBLE);
                                txtOldQuan.setText("Quantity : " + t[2]);
                                layConfirm.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                            }
                        }
                    }
                    break;
                case COMMENT:
                    if(!ediCom.getText().toString().equals("")) {
                        laySubmit.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    private boolean CheckBin(String bin) {
        boolean exists = false;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM scheme.stkbinm WITH(READUNCOMMITTED) WHERE bin_code = '" + bin + "'");
            while (reset.next()) {
                exists = true;
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return exists;
    }
}
