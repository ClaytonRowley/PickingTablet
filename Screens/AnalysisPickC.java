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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.richmondcabinets.claytonrowley.pickingtablet.Carcase.RouteC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalysisPickC extends AppCompatActivity {

    RouteC routeC;
    String userID;

    boolean routeComplete = false;

    int pickListCount = 0;
    int pickListComplete = 0;

    Button btnLineEnd;
    Button btnLineTop;
    Button btnLineShe;
    Button btnLineOth;
    Button btnNonEnd;
    Button btnNonTop;
    Button btnNonShe;
    Button btnNonOth;
    Button btnFlatEnd;
    Button btnFlatTop;
    Button btnFlatShe;
    Button btnFlatOth;
    Button btnFlatBacks;
    Button btnFlatRails;
    Button btnFlatDrawBox;
    Button btnExprEnd;
    Button btnExprTop;
    Button btnExprShe;
    Button btnExprOth;
    Button btnExprBacks;
    Button btnExprRails;
    Button btnExprDrawBox;
    Button btnJBacks;
    Button btnRails;
    Button btnDrawBox;
    Button btnCarrier;

    ProgressBar progressBar;

    LinearLayout layLine;
    LinearLayout layNon;
    LinearLayout layFlat;
    LinearLayout layExpress;
    LinearLayout layAll;

    boolean line = false;
    boolean non = false;
    boolean flat = false;
    boolean express = false;

    static final int RESULT = 1;
    static final int BTB = 2;
    static final int STKADJ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_pick_c);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        routeC = (RouteC)getIntent().getSerializableExtra("ROUTE");
        userID = getIntent().getStringExtra("USER");
        getSupportActionBar().setTitle(routeC.name);

        progressBar = (ProgressBar)findViewById(R.id.pB);

        btnLineEnd = (Button)findViewById(R.id.btnLineEnd);
        btnLineTop = (Button)findViewById(R.id.btnLineTop);
        btnLineShe = (Button)findViewById(R.id.btnLineShe);
        btnLineOth = (Button)findViewById(R.id.btnLineOth);
        btnNonEnd = (Button)findViewById(R.id.btnNonEnd);
        btnNonTop = (Button)findViewById(R.id.btnNonTop);
        btnNonShe = (Button)findViewById(R.id.btnNonShe);
        btnNonOth = (Button)findViewById(R.id.btnNonOth);
        btnFlatEnd = (Button)findViewById(R.id.btnFlatEnd);
        btnFlatTop = (Button)findViewById(R.id.btnFlatTop);
        btnFlatShe = (Button)findViewById(R.id.btnFlatShe);
        btnFlatOth = (Button)findViewById(R.id.btnFlatOth);
        btnFlatBacks = (Button)findViewById(R.id.btnFlatBacks);
        btnFlatRails = (Button)findViewById(R.id.btnFlatRails);
        btnFlatDrawBox = (Button)findViewById(R.id.btnFlatDraw);
        btnExprEnd = (Button)findViewById(R.id.btnExprEnd);
        btnExprTop = (Button)findViewById(R.id.btnExprTop);
        btnExprShe = (Button)findViewById(R.id.btnExprShe);
        btnExprOth = (Button)findViewById(R.id.btnExprOth);
        btnExprBacks = (Button)findViewById(R.id.btnExprBacks);
        btnExprRails = (Button)findViewById(R.id.btnExprRails);
        btnExprDrawBox = (Button)findViewById(R.id.btnExprDraw);
        btnJBacks = (Button)findViewById(R.id.btnJBacks);
        btnRails = (Button)findViewById(R.id.btnRails);
        btnDrawBox = (Button)findViewById(R.id.btnDrawBox);
        btnCarrier = (Button)findViewById(R.id.btnCarrier);

        btnLineEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Line Ends");
                }
            }
        });
        btnLineTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Line Top");
                }
            }
        });
        btnLineShe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Line Shelves");
                }
            }
        });
        btnLineOth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Line Other");
                }
            }
        });
        btnNonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Non Ends");
                }
            }
        });
        btnNonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Non Top");
                }
            }
        });
        btnNonShe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Non Shelves");
                }
            }
        });
        btnNonOth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Non Other");
                }
            }
        });
        btnJBacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Backs");
                }
            }
        });
        btnDrawBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Draw Box");
                }
            }
        });
        btnRails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Rails");
                }
            }
        });
        btnFlatEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Ends");
                }
            }
        });
        btnFlatTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Top");
                }
            }
        });
        btnFlatShe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Shelves");
                }
            }
        });
        btnFlatOth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Other");
                }
            }
        });
        btnFlatBacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Back");
                }
            }
        });
        btnFlatDrawBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Draw");
                }
            }
        });
        btnFlatRails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Flat Rail");
                }
            }
        });
        btnExprEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Ends");
                }
            }
        });
        btnExprTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Top");
                }
            }
        });
        btnExprShe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Shelves");
                }
            }
        });
        btnExprOth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Other");
                }
            }
        });
        btnExprBacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Back");
                }
            }
        });
        btnExprDrawBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Draw");
                }
            }
        });
        btnExprRails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Expr Rail");
                }
            }
        });
        btnCarrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getVisibility() == View.GONE) {
                    CreatePickList("Carrier");
                }
            }
        });
        layLine = (LinearLayout)findViewById(R.id.layLine);
        layNon = (LinearLayout)findViewById(R.id.layNon);
        layFlat = (LinearLayout)findViewById(R.id.layFlat);
        layExpress = (LinearLayout)findViewById(R.id.layExpress);
        layAll = (LinearLayout)findViewById(R.id.layAll);

        routeComplete = getIntent().getBooleanExtra("COMPLETE", false);

        progressBar.setMax(26);

        DrawAnalysis();
    }

    private void DrawAnalysis()
    {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        pickListCount = 0;
        pickListComplete = 0;
        line = false;
        non = false;

        if(routeC.lineEndsC.count == 0) {
            btnLineEnd.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Line Ends", Integer.toString(routeC.lineEndsC.count));
            pickListCount++;
            line = true;
        }
        if(routeC.lineTopBottC.count == 0) {
            btnLineTop.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Line Top", Integer.toString(routeC.lineTopBottC.count));
            pickListCount++;
            line = true;
        }
        if(routeC.lineShelvesC.count == 0) {
            btnLineShe.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Line Shelves", Integer.toString(routeC.lineShelvesC.count));
            pickListCount++;
            line = true;
        }
        if(routeC.lineOtherC.count == 0) {
            btnLineOth.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Line Other", Integer.toString(routeC.lineOtherC.count));
            pickListCount++;
            line = true;
        }
        if(routeC.nonEndsC.count == 0) {
            btnNonEnd.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Non Ends", Integer.toString(routeC.nonEndsC.count));
            pickListCount++;
            non = true;
        }
        if(routeC.nonTopBottC.count == 0) {
            btnNonTop.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Non Top", Integer.toString(routeC.nonTopBottC.count));
            pickListCount++;
            non = true;
        }
        if(routeC.nonShelvesC.count == 0) {
            btnNonShe.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Non Shelves", Integer.toString(routeC.nonShelvesC.count));
            pickListCount++;
            non = true;
        }
        if(routeC.nonOtherC.count == 0) {
            btnNonOth.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Non Other", Integer.toString(routeC.nonOtherC.count));
            pickListCount++;
            non = true;
        }
        if(routeC.jBacksC.count == 0) {
            btnJBacks.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Backs", "1");
            pickListCount++;
        }
        if(routeC.railC.count == 0) {
            btnRails.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Rails", "1");
            pickListCount++;
        }
        if(routeC.drawBoxC.count == 0) {
            btnDrawBox.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Draw Box", "1");
            pickListCount++;
        }
        if(routeC.flatTopBottC.count == 0) {
            btnFlatTop.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Top", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatEndsC.count == 0) {
            btnFlatEnd.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Ends", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatShelvesC.count == 0) {
            btnFlatShe.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Shelves", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatOtherC.count == 0) {
            btnFlatOth.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Other", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatBacksC.count == 0) {
            btnFlatBacks.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Back", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatDrawBoxC.count == 0) {
            btnFlatDrawBox.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Draw", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.flatRailsC.count == 0) {
            btnFlatRails.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Flat Rail", "1");
            pickListCount++;
            flat = true;
        }
        if(routeC.exprTopBottC.count == 0) {
            btnExprTop.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Top", Integer.toString(routeC.exprTopBottC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprEndsC.count == 0) {
            btnExprEnd.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Ends", Integer.toString(routeC.exprEndsC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprShelvesC.count == 0) {
            btnExprShe.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Shelves", Integer.toString(routeC.exprShelvesC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprOtherC.count == 0) {
            btnExprOth.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Other", Integer.toString(routeC.exprOtherC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprBacksC.count == 0) {
            btnExprBacks.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Back", Integer.toString(routeC.exprBacksC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprDrawBoxC.count == 0) {
            btnExprDrawBox.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Draw", Integer.toString(routeC.exprDrawBoxC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.exprRailsC.count == 0) {
            btnExprRails.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Expr Rail", Integer.toString(routeC.exprRailsC.count));
            pickListCount++;
            express = true;
        }
        if(routeC.carrierC.count == 0) {
            btnCarrier.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(1);
        } else {
            new GetPickStatus().execute("Carrier", Integer.toString(routeC.carrierC.count));
            pickListCount++;
        }

        if(!line)
            layLine.setVisibility(View.GONE);
        if(!non)
            layNon.setVisibility(View.GONE);
        if(!line && !non)
            layAll.setVisibility(View.GONE);
        if(!flat)
            layFlat.setVisibility(View.GONE);
        if(!express)
            layExpress.setVisibility(View.GONE);
    }

    private void CreatePickList(String name)
    {
        int color = 0;
        try {
            switch (name) {
                case "Line Ends":
                    color = ((ColorDrawable) btnLineEnd.getBackground()).getColor();
                    break;
                case "Line Top":
                    color = ((ColorDrawable) btnLineTop.getBackground()).getColor();
                    break;
                case "Line Shelves":
                    color = ((ColorDrawable) btnLineShe.getBackground()).getColor();
                    break;
                case "Line Other":
                    color = ((ColorDrawable) btnLineOth.getBackground()).getColor();
                    break;
                case "Non Ends":
                    color = ((ColorDrawable) btnNonEnd.getBackground()).getColor();
                    break;
                case "Non Top":
                    color = ((ColorDrawable) btnNonTop.getBackground()).getColor();
                    break;
                case "Non Shelves":
                    color = ((ColorDrawable) btnNonShe.getBackground()).getColor();
                    break;
                case "Non Other":
                    color = ((ColorDrawable) btnNonOth.getBackground()).getColor();
                    break;
                case "Backs":
                    color = ((ColorDrawable) btnJBacks.getBackground()).getColor();
                    break;
                case "Rails":
                    color = ((ColorDrawable) btnRails.getBackground()).getColor();
                    break;
                case "Draw Box":
                    color = ((ColorDrawable) btnDrawBox.getBackground()).getColor();
                    break;
                case "Flat Top":
                    color = ((ColorDrawable) btnFlatTop.getBackground()).getColor();
                    break;
                case "Flat Ends":
                    color = ((ColorDrawable) btnFlatEnd.getBackground()).getColor();
                    break;
                case "Flat Shelves":
                    color = ((ColorDrawable) btnFlatShe.getBackground()).getColor();
                    break;
                case "Flat Other":
                    color = ((ColorDrawable) btnFlatOth.getBackground()).getColor();
                    break;
                case "Flat Back":
                    color = ((ColorDrawable) btnFlatBacks.getBackground()).getColor();
                    break;
                case "Flat Rail":
                    color = ((ColorDrawable) btnFlatRails.getBackground()).getColor();
                    break;
                case "Flat Draw":
                    color = ((ColorDrawable) btnFlatDrawBox.getBackground()).getColor();
                    break;
                case "Expr Top":
                    color = ((ColorDrawable) btnExprTop.getBackground()).getColor();
                    break;
                case "Expr Ends":
                    color = ((ColorDrawable) btnExprEnd.getBackground()).getColor();
                    break;
                case "Expr Shelves":
                    color = ((ColorDrawable) btnExprShe.getBackground()).getColor();
                    break;
                case "Expr Other":
                    color = ((ColorDrawable) btnExprOth.getBackground()).getColor();
                    break;
                case "Expr Back":
                    color = ((ColorDrawable) btnExprBacks.getBackground()).getColor();
                    break;
                case "Expr Rail":
                    color = ((ColorDrawable) btnExprRails.getBackground()).getColor();
                    break;
                case "Expr Draw":
                    color = ((ColorDrawable) btnExprDrawBox.getBackground()).getColor();
                    break;
                case "Carrier":
                    color = ((ColorDrawable) btnCarrier.getBackground()).getColor();
                    break;
            }
        }
        catch (Exception e) {}
        boolean progress = false;
        if(color == Color.GREEN || color == Color.YELLOW)
            progress = true;
        Intent intent = new Intent(this, PickListC.class);
        intent.putExtra("ROUTE", routeC);
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
            ResultSet reset = stmt.executeQuery("SELECT * FROM Clayton_Order_Timeline WITH (READUNCOMMITTED) WHERE Pick_List = '"+name+"' AND Route = '"+routeC.name+"' AND Area = 'PK-CARCS'");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT)
        {
            DrawAnalysis();
        }
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
            Button btn = btnDrawBox;
            if(!res[0].equals("None")) {
                switch (res[1])
                {
                    case "Line Ends":
                        btn = btnLineEnd;
                        break;
                    case "Line Top":
                        btn = btnLineTop;
                        break;
                    case "Line Shelves":
                        btn = btnLineShe;
                        break;
                    case "Line Other":
                        btn = btnLineOth;
                        break;
                    case "Non Ends":
                        btn = btnNonEnd;
                        break;
                    case "Non Top":
                        btn = btnNonTop;
                        break;
                    case "Non Shelves":
                        btn = btnNonShe;
                        break;
                    case "Non Other":
                        btn = btnNonOth;
                        break;
                    case "Backs":
                        btn = btnJBacks;
                        break;
                    case "Rails":
                        btn = btnRails;
                        break;
                    case "Draw Box":
                        btn = btnDrawBox;
                        break;
                    case "Flat Top":
                        btn = btnFlatTop;
                        break;
                    case "Flat Ends":
                        btn = btnFlatEnd;
                        break;
                    case "Flat Shelves":
                        btn = btnFlatShe;
                        break;
                    case "Flat Other":
                        btn = btnFlatOth;
                        break;
                    case "Flat Back":
                        btn = btnFlatBacks;
                        break;
                    case "Flat Rail":
                        btn = btnFlatRails;
                        break;
                    case "Flat Draw":
                        btn = btnFlatDrawBox;
                        break;
                    case "Expr Top":
                        btn = btnExprTop;
                        break;
                    case "Expr Ends":
                        btn = btnExprEnd;
                        break;
                    case "Expr Shelves":
                        btn = btnExprShe;
                        break;
                    case "Expr Other":
                        btn = btnExprOth;
                        break;
                    case "Expr Back":
                        btn = btnExprBacks;
                        break;
                    case "Expr Rail":
                        btn = btnExprRails;
                        break;
                    case "Expr Draw":
                        btn = btnExprDrawBox;
                        break;
                    case "Carrier":
                        btn = btnCarrier;
                }
                    if (res[0].equals("Picked")) {
                        btn.setBackgroundColor(Color.GREEN);
                        pickListComplete++;
                    }
                    else if (res[0].equals("Progress"))
                        btn.setBackgroundColor(Color.YELLOW);

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
            String sql = "INSERT INTO [_Paul A0 Stores Timeline] ([User], [Process], [Status], [Date/Time]) VALUES('"+userID+"', '"+routeC.name+"', 'PK-CARCS', '"+format+"')";
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
