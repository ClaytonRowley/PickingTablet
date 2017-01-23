package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.ProductListCD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import com.google.zxing.integration.android.IntentIntegrator;

public class BinToBin extends AppCompatActivity {

    LinearLayout layProduct;
    LinearLayout layComment;
    LinearLayout layQuantity;
    LinearLayout layTo;
    LinearLayout laySubmit;
    LinearLayout layError;
    LinearLayout layDesc;

    EditText ediFrom;
    AutoCompleteTextView ediProduct;
    EditText ediComment;
    EditText ediQuantity;
    EditText ediTo;

    Button btnFromScan;
    Button btnProductScan;
    Button btnProducts;
    Button btnCommentScan;
    Button btnComments;
    Button btnToScan;
    Button btnSubmit;

    TextView txtQuantity;
    TextView txtError;
    TextView txtDesc;

    int quantity;
    String product;
    String userID;
    String key;
    String order;
    boolean cd;

    boolean once = false;

    String error;

    ProductListCD pCD = new ProductListCD();

    static final int FROMBIN = 1;
    static final int TOBIN = 2;
    static final int PRODUCT = 3;
    static final int COMMENT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_to_bin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context con = this;

        layProduct = (LinearLayout)findViewById(R.id.layProd);
        layComment = (LinearLayout)findViewById(R.id.layComm);
        layQuantity = (LinearLayout)findViewById(R.id.layQuan);
        layTo = (LinearLayout)findViewById(R.id.layTo);
        laySubmit = (LinearLayout)findViewById(R.id.laySubmit);
        layError = (LinearLayout)findViewById(R.id.layError);
        layDesc = (LinearLayout)findViewById(R.id.layDesc);

        ediFrom = (EditText)findViewById(R.id.ediFrom);
        ediProduct = (AutoCompleteTextView)findViewById(R.id.ediProd);
        ediComment = (EditText)findViewById(R.id.ediComm);
        ediQuantity = (EditText)findViewById(R.id.ediQuan);
        ediTo = (EditText)findViewById(R.id.ediTo);

        btnFromScan = (Button)findViewById(R.id.btnFromScan);
        btnProductScan = (Button)findViewById(R.id.btnProdScan);
        btnProducts = (Button)findViewById(R.id.btnProducts);
        btnCommentScan = (Button)findViewById(R.id.btnCommScan);
        btnComments = (Button)findViewById(R.id.btnComments);
        btnToScan = (Button)findViewById(R.id.btnToScan);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        txtQuantity = (TextView)findViewById(R.id.txtQuan);
        txtError = (TextView)findViewById(R.id.txtError);
        txtDesc = (TextView)findViewById(R.id.txtDesc);

        cd = getIntent().getBooleanExtra("CD", false);

        userID = getIntent().getStringExtra("USER");

        if(cd)
        {
            layComment.setVisibility(View.GONE);
            layTo.setVisibility(View.GONE);
            quantity = getIntent().getIntExtra("QUANTITY", 1);
            product = getIntent().getStringExtra("PRODUCT");
            key = getIntent().getStringExtra("KEY");
            order = getIntent().getStringExtra("ORDER");
            getSupportActionBar().setTitle(product);
        }

        ediFrom.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (CheckBin(ediFrom.getText().toString().toUpperCase())) {
                        layProduct.setVisibility(View.VISIBLE);
                        ediProduct.requestFocus();
                        pCD.PopulateProducts(ediFrom.getText().toString().toUpperCase());
                        String[] PRODUCTS = new String[pCD.products.size()];
                        for(int a = 0; a < pCD.products.size(); a++) {
                            String temp = pCD.products.get(a);
                            String[] tempS = temp.split(",");
                            PRODUCTS[a] = tempS[0];
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, PRODUCTS);
                        ediProduct.setAdapter(adapter);

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
        ediProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!ediProduct.getText().toString().equals("")) {
                        for (String s : pCD.products) {
                            String[] t = s.split(",");
                            if (ediProduct.getText().toString().toUpperCase().trim().equals(t[0].trim())) {
                                layDesc.setVisibility(View.VISIBLE);
                                txtDesc.setText(t[1].trim());
                                if(cd) {
                                    txtQuantity.setText(t[2].trim());
                                    ediProduct.setText(t[0]);
                                    ediQuantity.requestFocus();
                                    layQuantity.setVisibility(View.VISIBLE);
                                    layError.setVisibility(View.INVISIBLE);
                                    txtError.setText("");
                                }
                                else
                                {
                                    ediProduct.setText(t[0]);
                                    ediComment.requestFocus();
                                    layComment.setVisibility(View.VISIBLE);
                                    layError.setVisibility(View.INVISIBLE);
                                    txtError.setText("");
                                }
                                return true;
                            }
                        }
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: Product not found!");
                    }
                }
                return false;
            }
        });
        ediComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!ediComment.getText().toString().trim().equals("")) {
                        for (String s : pCD.products) {
                            String[] t = s.split(",");
                            if (ediProduct.getText().toString().trim().equals(t[0].trim())) {
                                    txtQuantity.setText(t[2].trim());
                                    ediQuantity.requestFocus();
                                    layQuantity.setVisibility(View.VISIBLE);
                                    layError.setVisibility(View.INVISIBLE);
                                    txtError.setText("");
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
        ediQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                if (result == EditorInfo.IME_ACTION_DONE) {
                    if (!ediQuantity.getText().toString().equals("")) {
                        int total = Integer.parseInt(txtQuantity.getText().toString());
                        int quant = Integer.parseInt(ediQuantity.getText().toString());
                        if (quant > total) {
                            txtError.setText("ERROR: Not enough product in the bin!");
                            layError.setVisibility(View.VISIBLE);
                        } else {
                            if (cd) {
                                laySubmit.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                            } else {
                                ediTo.setText("");
                                layTo.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                                ediTo.requestFocus();
                            }
                        }
                    }
                }
                return false;
            }
        });
        ediTo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (CheckBin(ediTo.getText().toString().toUpperCase())) {
                        if (!ediTo.getText().toString().equals("")) {
                            laySubmit.setVisibility(View.VISIBLE);
                            txtError.setText("");
                            layError.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                    return true;
                }
                return false;
            }
        });
        ediFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layProduct.getVisibility() == View.VISIBLE) {
                    ediProduct.setText("");
                    txtDesc.setText("");
                    layDesc.setVisibility(View.INVISIBLE);
                    layProduct.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediFrom.setText(ediFrom.getText().subSequence(0, s.length() - 1));
                    ediFrom.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(cd) {
                    if (layQuantity.getVisibility() == View.VISIBLE) {
                        ediQuantity.setText("");
                        layQuantity.setVisibility(View.INVISIBLE);
                        layDesc.setVisibility(View.INVISIBLE);
                        txtDesc.setText("");
                    }
                }
                else
                {
                    if (layComment.getVisibility() == View.VISIBLE) {
                        ediComment.setText("");
                        layComment.setVisibility(View.INVISIBLE);
                        layDesc.setVisibility(View.INVISIBLE);
                        txtDesc.setText("");
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediProduct.setText(ediProduct.getText().subSequence(0, s.length() - 1));
                    ediProduct.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layQuantity.getVisibility() == View.VISIBLE) {
                    ediQuantity.setText("");
                    layQuantity.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediComment.setText(ediComment.getText().subSequence(0, s.length() - 1));
                    ediComment.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(cd) {
                    if (laySubmit.getVisibility() == View.VISIBLE) {
                        laySubmit.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    if(layTo.getVisibility() == View.VISIBLE) {
                        ediTo.setText("");
                        layTo.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ediTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(laySubmit.getVisibility() == View.VISIBLE)
                    laySubmit.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediTo.setText(ediTo.getText().subSequence(0, s.length() - 1));
                    ediTo.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence temp[] = new CharSequence[pCD.products.size()];

                for (int a = 0; a < pCD.products.size(); a++) {
                    temp[a] = pCD.products.get(a);
                }

                final CharSequence products[] = temp;

                final AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("Pick a product");
                builder.setItems(products, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        layComment.setVisibility(View.INVISIBLE);
                        String[] te = products[which].toString().split(",");
                        layDesc.setVisibility(View.VISIBLE);
                        txtDesc.setText(te[1].trim());
                        if (cd) {
                            txtQuantity.setText(te[2].trim());
                            ediProduct.setText(te[0].trim());
                            ediQuantity.requestFocus();
                            layQuantity.setVisibility(View.VISIBLE);
                            layError.setVisibility(View.INVISIBLE);
                            txtError.setText("");
                        } else {
                            ediProduct.setText(te[0]);
                            ediComment.requestFocus();
                            layComment.setVisibility(View.VISIBLE);
                            layError.setVisibility(View.INVISIBLE);
                            txtError.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence comms[] = new CharSequence[] {"TOPUP", "PUTAWAY", "LINE"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("Comment");
                builder.setItems(comms, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = comms[which].toString();
                        ediComment.setText(comment);
                        for (String s : pCD.products) {
                            String[] t = s.split(",");
                            if (ediProduct.getText().toString().trim().equals(t[0].trim())) {
                                txtQuantity.setText(t[2].trim());
                                ediQuantity.requestFocus();
                                layQuantity.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                                break;
                            }
                        }
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
                    if (cd) {
                        layError.setVisibility(View.INVISIBLE);
                        txtError.setText("");
                        if (Pick(ediProduct.getText().toString().toUpperCase(), ediFrom.getText().toString().toUpperCase(), Integer.parseInt(ediQuantity.getText().toString()), userID, order, key)) {
                            Intent result = new Intent();
                            setResult(Activity.RESULT_OK, result);
                            finish();
                        } else {
                            layError.setVisibility(View.VISIBLE);
                            txtError.setText("Error: " + error);
                            once = false;
                        }
                    } else {
                        layError.setVisibility(View.INVISIBLE);
                        txtError.setText("");
                        if (BTB(ediProduct.getText().toString().toUpperCase(), ediFrom.getText().toString().toUpperCase(), Integer.parseInt(ediQuantity.getText().toString()), userID, ediComment.getText().toString(), ediTo.getText().toString().toUpperCase())) {
                            finish();
                        } else {
                            layError.setVisibility(View.VISIBLE);
                            txtError.setText("Error: " + error);
                            once = false;
                        }
                    }
                }
            }
        });

        btnFromScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFromScanClick();
            }
        });
        btnToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToScanClick();
            }
        });
        btnProductScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnProductScanClick();
            }
        });
        btnCommentScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCommentScanClick();
            }
        });
    }

    private void btnFromScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, FROMBIN);
    }

    private void btnToScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, TOBIN);
    }

    private void btnProductScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, PRODUCT);
    }

    private void btnCommentScanClick()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        Intent intent = integrator.createScanIntent();
        startActivityForResult(intent, COMMENT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case FROMBIN:
                    ediFrom.setText(intent.getStringExtra("SCAN_RESULT"));
                    if (CheckBin(ediFrom.getText().toString().toUpperCase())) {
                        layProduct.setVisibility(View.VISIBLE);
                        ediProduct.requestFocus();
                        pCD.PopulateProducts(ediFrom.getText().toString().toUpperCase());
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                    break;
                case TOBIN:
                    ediTo.setText(intent.getStringExtra("SCAN_RESULT"));
                    if (CheckBin(ediTo.getText().toString().toUpperCase())) {
                        laySubmit.setVisibility(View.VISIBLE);
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                    break;
                case PRODUCT:
                    ediProduct.setText(intent.getStringExtra("SCAN_RESULT"));
                    for (String s : pCD.products) {
                        String[] t = s.split(",");
                        if (ediProduct.getText().toString().trim().equals(t[0].trim())) {
                            if(cd) {
                                txtQuantity.setText(t[2].trim());
                                ediProduct.setText(t[0]);
                                ediQuantity.requestFocus();
                                layQuantity.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                                break;
                            } else {
                                ediProduct.setText(t[0]);
                                txtQuantity.setText(t[2].trim());
                                ediComment.requestFocus();
                                layComment.setVisibility(View.VISIBLE);
                                layError.setVisibility(View.INVISIBLE);
                                txtError.setText("");
                            }
                        }
                    }
                    layError.setVisibility(View.VISIBLE);
                    txtError.setText("ERROR: Product not found!");
                    break;
                case COMMENT:
                    ediComment.setText(intent.getStringExtra("SCAN_RESULT"));
                    for (String s : pCD.products) {
                        String[] t = s.split(",");
                        if (ediProduct.getText().toString().trim().equals(t[0].trim())) {
                            txtQuantity.setText(t[2].trim());
                            ediQuantity.requestFocus();
                            layQuantity.setVisibility(View.VISIBLE);
                            layError.setVisibility(View.INVISIBLE);
                            txtError.setText("");
                            break;
                        }
                    }
                    break;
            }
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

    private boolean Pick(String product, String bin, int quantity, String userID, String order, String key)
    {
        String wh = GetWarehouse(product, bin);
        try {
            String SPsql = "EXEC [dbo].[Def_DefCap_EntireBinToBin] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            //PreparedStatement ps = conn.prepareStatement(SPsql);
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(30);
            ps.setString(1, userID.substring(0, 2) + "0001");
            ps.setString(2, userID);
            ps.setString(3, wh + product.trim());
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

            if(res == 1)
                return true;
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

    private boolean BTB(String product, String bin, int quantity, String userID, String comment, String to)
    {
        String wh = GetWarehouse(product, bin);
        try {
            String SPsql = "EXEC [dbo].[Def_DefCap_EntireBinToBin] ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            //PreparedStatement ps = conn.prepareStatement(SPsql);
            CallableStatement ps = conn.prepareCall(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(30);
            ps.setString(1, userID.substring(0, 2) + "0001");
            ps.setString(2, userID);
            ps.setString(3, wh + product.trim());
            ps.setString(4, to);
            ps.setString(5, bin);
            ps.setString(6, comment);
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

            if(res == 1)
                return true;
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
