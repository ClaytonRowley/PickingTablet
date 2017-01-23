package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.Screens.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StockLocator extends AppCompatActivity {

    LinearLayout layBin;
    LinearLayout layDesc;
    LinearLayout layProd;
    LinearLayout layError;

    EditText ediProd;

    TextView txtDesc;
    TextView txtError;

    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_locator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layBin = (LinearLayout)findViewById(R.id.layBin);
        layProd = (LinearLayout)findViewById(R.id.layProd);
        layError = (LinearLayout)findViewById(R.id.layError);
        layDesc = (LinearLayout)findViewById(R.id.layDesc);

        ediProd = (EditText)findViewById(R.id.ediProd);

        txtError = (TextView)findViewById(R.id.txtError);
        txtDesc = (TextView)findViewById(R.id.txtDesc);

        btnScan = (Button)findViewById(R.id.btnScan);

        ediProd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(CheckProduct(ediProd.getText().toString().toUpperCase())) {
                        List<String> bins = GetBins(ediProd.getText().toString().toUpperCase());
                        DrawProducts(bins);
                        return true;
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: Product does not exist!");
                    }
                }
                return false;
            }
        });
        ediProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (layBin.getVisibility() == View.VISIBLE) {
                    layBin.removeAllViews();
                    layBin.setVisibility(View.INVISIBLE);
                    layDesc.setVisibility(View.INVISIBLE);
                }
                txtError.setText("");
                layError.setVisibility(View.INVISIBLE);
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
    }

    private boolean CheckProduct(String product) {
        boolean exists = false;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM scheme.stockm WITH(READUNCOMMITTED) WHERE product = '" + product + "'");
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

    private void DrawProducts(List<String> bins)
    {
        layBin.setVisibility(View.VISIBLE);
        for(String b : bins)
        {
            TextView txtP = new TextView(this);
            txtP.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtP.setText(b);
            txtP.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layBin.addView(txtP);
        }
    }

    private List<String> GetBins(String product)
    {
        List<String> bins = new ArrayList<>();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.v("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT scheme.stquem.bin_number, SUM(scheme.stquem.quantity) AS quantity, scheme.stockm.long_description FROM scheme.stquem INNER JOIN scheme.stockm ON scheme.stquem.warehouse = scheme.stockm.warehouse AND scheme.stquem.product = scheme.stockm.product WHERE scheme.stquem.product = '" + product + "' AND quantity != 0 GROUP BY scheme.stquem.bin_number, scheme.stockm.long_description");
            while (reset.next()) {
                layDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(reset.getString("long_description"));
                String bin = reset.getString("bin_number");
                if(!bin.equals("PI01Z") && !bin.equals("SWANS")) {
                    int quan = reset.getInt("quantity");
                    bins.add(bin + ", " + Integer.toString(quan));
                }
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Failed To Connect!", Toast.LENGTH_LONG);
            toast.show();
            Log.w("Error", e.getMessage());
        }
        return bins;
    }

}
