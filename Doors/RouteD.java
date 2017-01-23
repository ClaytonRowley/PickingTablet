package com.richmondcabinets.claytonrowley.pickingtablet.Doors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clayton.Rowley on 08/07/2016.
 */
public class RouteD implements Serializable{

    public String name;
    public DoorD doorD;
    public ExpressQD expressQD;
    public FlatUnitsD flatUnitsD;
    public LineUnitsD lineUnitsD;
    public LShapeCBasesD lShapeCBasesD;
    public NonLineFeaturesD nonLineFeaturesD;
    public NonLineUnitsD nonLineUnitsD;
    public NonStoresD nonStoresD;
    public PJHD pjhD;
    public StorePanelD storePanelD;
    public StoresD storesD;
    public int drop;

    public RouteD()
    {
        name = "";
        doorD = new DoorD();
        expressQD = new ExpressQD();
        flatUnitsD = new FlatUnitsD();
        lineUnitsD = new LineUnitsD();
        lShapeCBasesD = new LShapeCBasesD();
        nonLineFeaturesD = new NonLineFeaturesD();
        nonLineUnitsD = new NonLineUnitsD();
        nonStoresD = new NonStoresD();
        pjhD = new PJHD();
        storePanelD = new StorePanelD();
        storesD = new StoresD();
        drop = 0;
    }

    public void AddDoor(Door d)
    {
        switch (d.drawing)
        {
            case "EXPRESS Q":
                expressQD.AddOrder(d);
                break;
            case "FLAT UNITS":
                flatUnitsD.AddOrder(d);
                break;
            case "LINE UNITS":
                lineUnitsD.AddOrder(d);
                break;
            case "LSHAPE C-BASES":
                lShapeCBasesD.AddOrder(d);
                break;
            case "NON LINE FEATURES":
                nonLineFeaturesD.AddOrder(d);
                break;
            case "NON LINE UNITS":
                nonLineUnitsD.AddOrder(d);
                break;
            case "NON STORES":
                nonStoresD.AddOrder(d);
                break;
            case "PJH":
                pjhD.AddOrder(d);
                break;
            case "STOREPANEL":
                storePanelD.AddOrder(d);
                break;
            case "STORES":
                storesD.AddOrder(d);
                break;
            default:
                doorD.AddOrder(d);
                break;
        }
    }

    public void RemoveDoor(String door, String draw, String order)
    {
        switch (draw)
        {
            case "EXPRESS Q":
                expressQD.RemoveDoor(door, order);
                break;
            case "FLAT UNITS":
                flatUnitsD.RemoveDoor(door, order);
                break;
            case "LINE UNITS":
                lineUnitsD.RemoveDoor(door, order);
                break;
            case "LSHAPE C-BASES":
                lShapeCBasesD.RemoveDoor(door, order);
                break;
            case "NON LINE FEATURES":
                nonLineFeaturesD.RemoveDoor(door, order);
                break;
            case "NON LINE UNITS":
                nonLineUnitsD.RemoveDoor(door, order);
                break;
            case "NON STORES":
                nonStoresD.RemoveDoor(door, order);
                break;
            case "PJH":
                pjhD.RemoveDoor(door, order);
                break;
            case "STOREPANEL":
                storePanelD.RemoveDoor(door, order);
                break;
            case "STORES":
                storesD.RemoveDoor(door, order);
                break;
            default:
                doorD.RemoveDoor(door, order);
                break;
        }
    }
}
