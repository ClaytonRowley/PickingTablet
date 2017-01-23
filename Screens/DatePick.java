package com.richmondcabinets.claytonrowley.pickingtablet.Screens;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatePick extends AppCompatActivity {

    Button btnDate1;
    Button btnDate2;
    Button btnDate3;
    Button btnDate4;
    Button btnDate5;
    Button btnLogI;

    String userID;

    LinearLayout layLog;
    LinearLayout layBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnDate1 = (Button) findViewById(R.id.btnDate1);
        btnDate2 = (Button) findViewById(R.id.btnDate2);
        btnDate3 = (Button) findViewById(R.id.btnDate3);
        btnDate4 = (Button) findViewById(R.id.btnDate4);
        btnDate5 = (Button) findViewById(R.id.btnDate5);
        btnLogI = (Button) findViewById(R.id.btnLog);

        layLog = (LinearLayout) findViewById(R.id.layLog);
        layBut = (LinearLayout) findViewById(R.id.layBut);

        btnDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateRoutes(btnDate1.getText().toString());
            }
        });

        btnDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateRoutes(btnDate2.getText().toString());
            }
        });

        btnDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateRoutes(btnDate3.getText().toString());
            }
        });

        btnDate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateRoutes(btnDate4.getText().toString());
            }
        });

        btnDate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopulateRoutes(btnDate5.getText().toString());
            }
        });

        btnLogI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });

        PopulateDates();
    }

    private void PopulateDates() {
        List<Date> dates = new ArrayList<>();
        Calendar temp = Calendar.getInstance();
        temp.setTime(new Date());
        int day = temp.get(Calendar.DAY_OF_WEEK);
        if (day != 1 && day != 7)
            dates.add(temp.getTime());
        do {
            temp.add(Calendar.DATE, 1);
            int day1 = temp.get(Calendar.DAY_OF_WEEK);
            if (day1 != 1 && day1 != 7)
                dates.add(temp.getTime());
        } while (dates.size() != 5);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        btnDate1.setText(sdf.format(dates.get(0)) + "\n" + GetDayOfWeek(dates.get(0)));
        btnDate2.setText(sdf.format(dates.get(1)) + "\n" + GetDayOfWeek(dates.get(1)));
        btnDate3.setText(sdf.format(dates.get(2)) + "\n" + GetDayOfWeek(dates.get(2)));
        btnDate4.setText(sdf.format(dates.get(3)) + "\n" + GetDayOfWeek(dates.get(3)));
        btnDate5.setText(sdf.format(dates.get(4)) + "\n" + GetDayOfWeek(dates.get(4)));
    }

    private String GetDayOfWeek(Date day)
    {
        Calendar temp = Calendar.getInstance();
        temp.setTime(day);
        int d = temp.get(Calendar.DAY_OF_WEEK);
        switch (d)
        {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
        }
        return "";
    }

    private void PopulateRoutes(String date)
    {
        Intent intent = new Intent(this, DepartmentPick.class);
        intent.putExtra("DATE", date.substring(0, 10));
        intent.putExtra("USER", userID);
        startActivity(intent);
    }

    private void LogIn()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_log_in);
        final EditText ediPass = (EditText)dialog.findViewById(R.id.ediPass);
        final EditText ediID = (EditText)dialog.findViewById(R.id.ediID);
        final TextView txtError = (TextView)dialog.findViewById(R.id.txtError);
        Button btnLog = (Button)dialog.findViewById(R.id.btnLog);
        Button btnExit = (Button)dialog.findViewById(R.id.btnExit);
        ediID.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    ediPass.requestFocus();
                    return true;
                }
                return false;
            }
        });
        ediID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediID.setText(ediID.getText().subSequence(0, s.length() - 1));
                    ediID.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        ediPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    txtError.setText("");
                    if (ediID.getText().length() != 0) {
                        if (ediPass.getText().length() != 0) {
                            if (CheckDetails(ediID.getText().toString(), ediPass.getText().toString())) {
                                txtError.setText("");
                                userID = ediID.getText().toString();
                                layLog.setVisibility(View.INVISIBLE);
                                layBut.setVisibility(View.VISIBLE);
                                UpdateVersion();
                                dialog.cancel();
                            } else {
                                txtError.setText("Incorrect login details");
                            }
                        } else {
                            txtError.setText("Enter your password!");
                        }
                    } else {
                        txtError.setText("Enter your ID!");
                    }
                    return true;
                }
                return false;
            }
        });
        ediPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    ediPass.setText(ediPass.getText().subSequence(0, s.length() - 1));
                    ediPass.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtError.setText("");
                if (ediID.getText().length() != 0) {
                    if (ediPass.getText().length() != 0) {
                        if (CheckDetails(ediID.getText().toString(), ediPass.getText().toString())) {
                            txtError.setText("");
                            userID = ediID.getText().toString();
                            layLog.setVisibility(View.INVISIBLE);
                            layBut.setVisibility(View.VISIBLE);
                            dialog.cancel();
                        } else {
                            txtError.setText("Incorrect login details");
                        }
                    } else {
                        txtError.setText("Enter your password!");
                    }
                } else {
                    txtError.setText("Enter your ID!");
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void UpdateVersion() {
        int cVersion = BuildConfig.VERSION_CODE;
        int lVersion = 0;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT * FROM Tablet_Version WHERE UserID ='MASTER'");
            while (reset.next()) {
                lVersion = Integer.parseInt(reset.getString("Version"));
            }
            stmt.executeUpdate("UPDATE Tablet_Version SET Version = '" + cVersion + "' WHERE UserID = '" + userID + "'");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
        if(lVersion != cVersion)
        {
            Toast t = Toast.makeText(getApplicationContext(), "Update Available", Toast.LENGTH_LONG);
            t.show();
        }
    }

    private boolean CheckDetails(String id, String pass)
    {
        String encrypted = DecryptPassword(pass);
        String password = "A";
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.7.4.6/live", "sa", "");
            Log.w("Connection", "Open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("SELECT user_id, user_pw FROM scheme.dcuserm WHERE user_id='"+id+"'");
            while (reset.next()) {
                password = reset.getString("user_pw");
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
        if(password.equals(encrypted))
            return true;
        else
            return false;
    }

    private String DecryptPassword(String pass)
    {
        String String1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz ";
        String str1 = "IAzO0YD6WQc_NgVSBwK4a273UpRHP9qXn1JfGoTZCFdh5LmexiEtswvurlbjMk8";
        int num1 = 63;
        int num2 = pass.length();
        String str2 = "";
        int num3 = 1;
        int num4 = num2;
        int Start1 = num3;
        while (Start1 <= num4)
        {
            String substring = pass.substring(Start1 - 1, Start1);
            int temp = (String1.indexOf(substring) + 2 + (Start1 - 1) * num2);
            int Start2 = temp % num1;
            if (Start2 <= 0)
                Start2 = 1;
            str2 += str1.substring(Start2 - 1, Start2);
            Start1 += 1;
        }
        int num5 = Start1;
        while (num5 <= 20)
        {
            int temp = 20 + (num5 - 1) * num2;
            int Start2 = temp % num1;
            if (Start2 <= 0)
                Start2 = 1;
            str2 += str1.substring(Start2 - 1, Start2);
            num5 += 1;
        }
        return str2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_pick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.LogOut) {
            layBut.setVisibility(View.INVISIBLE);
            layLog.setVisibility(View.VISIBLE);
            userID = "";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
