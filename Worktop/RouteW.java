package com.richmondcabinets.claytonrowley.pickingtablet.Worktop;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 02/08/2016.
 */
public class RouteW implements Serializable{

    public String name;
    public WorktopW worktopW;
    public UpstandW upstandW;
    int drop;

    public RouteW()
    {
        name = "";
        worktopW = new WorktopW();
        upstandW = new UpstandW();
        drop = 0;
    }

    public void AddWorktop(Worktop w)
    {
        switch (w.analysisA)
        {
            case "WORKTOP":
                worktopW.AddOrder(w);
                break;
            case "UPSTAND":
                upstandW.AddOrder(w);
                break;
        }
    }

    public void RemoveWorktop(String worktop, String draw, String order)
    {
        switch (draw)
        {
            case "WORKTOP":
                worktopW.RemoveWorktop(worktop, order);
                break;
        }
    }
}
