package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richmondcabinets.claytonrowley.pickingtablet.ProductListCD;
import com.richmondcabinets.claytonrowley.pickingtablet.Screens.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BinEnquiry extends AppCompatActivity {

    LinearLayout layBin;
    LinearLayout layProd;
    LinearLayout layError;

    EditText ediBin;

    TextView txtError;

    Button btnScan;

    ProductListCD pCD = new ProductListCD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_enquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layBin = (LinearLayout)findViewById(R.id.layBin);
        layProd = (LinearLayout)findViewById(R.id.layProd);
        layError = (LinearLayout)findViewById(R.id.layError);

        txtError = (TextView)findViewById(R.id.txtError);

        ediBin = (EditText)findViewById(R.id.editText);

        btnScan = (Button)findViewById(R.id.button);

        ediBin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(CheckBin(ediBin.getText().toString().toUpperCase())) {
                        pCD.PopulateProducts(ediBin.getText().toString().toUpperCase());
                        DrawProducts();
                        return true;
                    } else {
                        layError.setVisibility(View.VISIBLE);
                        txtError.setText("ERROR: BIN does not exist!");
                    }
                }
                return false;
            }
        });
        ediBin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(layProd.getVisibility() == View.VISIBLE) {
                    layProd.removeAllViews();
                    layProd.setVisibility(View.INVISIBLE);
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
                    ediBin.setText(ediBin.getText().subSequence(0, s.length() - 1));
                    ediBin.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
    }

    private void DrawProducts()
    {
        layProd.setVisibility(View.VISIBLE);
        for(String p : pCD.products)
        {
            TextView txtP = new TextView(this);
            txtP.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            txtP.setText(p);
            txtP.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layProd.addView(txtP);
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
