package com.richmondcabinets.claytonrowley.pickingtablet.Carcase;

import java.io.Serializable;

/**
 * Created by Clayton.Rowley on 26/07/2016.
 */
public class RouteC implements Serializable {

    public String name;
    public LineEndsC lineEndsC;
    public NonEndsC nonEndsC;
    public LineTopBottC lineTopBottC;
    public NonTopBottC nonTopBottC;
    public LineShelvesC lineShelvesC;
    public NonShelvesC nonShelvesC;
    public JBacksC jBacksC;
    public RailC railC;
    public DrawBoxC drawBoxC;
    public LineOtherC lineOtherC;
    public NonOtherC nonOtherC;
    public FlatEndsC flatEndsC;
    public FlatTopBottC flatTopBottC;
    public FlatShelvesC flatShelvesC;
    public FlatOtherC flatOtherC;
    public FlatRailsC flatRailsC;
    public FlatDrawBoxC flatDrawBoxC;
    public FlatBacksC flatBacksC;
    public ExprEndsC exprEndsC;
    public ExprTopBottC exprTopBottC;
    public ExprShelvesC exprShelvesC;
    public ExprOtherC exprOtherC;
    public ExprRailsC exprRailsC;
    public ExprDrawBoxC exprDrawBoxC;
    public ExprBacksC exprBacksC;
    public CarrierC carrierC;

    public RouteC()
    {
        name = "";
        lineEndsC = new LineEndsC();
        nonEndsC = new NonEndsC();
        lineTopBottC = new LineTopBottC();
        nonTopBottC = new NonTopBottC();
        lineShelvesC = new LineShelvesC();
        nonShelvesC = new NonShelvesC();
        jBacksC = new JBacksC();
        railC = new RailC();
        drawBoxC = new DrawBoxC();
        nonOtherC = new NonOtherC();
        lineOtherC = new LineOtherC();
        flatEndsC = new FlatEndsC();
        flatTopBottC = new FlatTopBottC();
        flatShelvesC = new FlatShelvesC();
        flatOtherC = new FlatOtherC();
        flatRailsC = new FlatRailsC();
        flatDrawBoxC = new FlatDrawBoxC();
        flatBacksC = new FlatBacksC();
        exprEndsC = new ExprEndsC();
        exprTopBottC = new ExprTopBottC();
        exprShelvesC = new ExprShelvesC();
        exprOtherC = new ExprOtherC();
        exprRailsC = new ExprRailsC();
        exprDrawBoxC = new ExprDrawBoxC();
        exprBacksC = new ExprBacksC();
        carrierC = new CarrierC();
    }

    public void AddCarcase(Carcase c)
    {
        switch (c.category)
        {
            case "LINE END":
                lineEndsC.AddOrder(c);
                break;
            case "NON END":
                nonEndsC.AddOrder(c);
                break;
            case "LINE TOP":
                lineTopBottC.AddOrder(c);
                break;
            case "NON TOP":
                nonTopBottC.AddOrder(c);
                break;
            case "LINE SHELVES":
                lineShelvesC.AddOrder(c);
                break;
            case "NON SHELVES":
                nonShelvesC.AddOrder(c);
                break;
            case "BACK":
                jBacksC.AddOrder(c);
                break;
            case "RAIL":
                railC.AddOrder(c);
                break;
            case "DRAW":
                drawBoxC.AddOrder(c);
                break;
            case "NON OTHER":
                nonOtherC.AddOrder(c);
                break;
            case "LINE OTHER":
                lineOtherC.AddOrder(c);
                break;
            case "FLAT TOP":
                flatTopBottC.AddOrder(c);
                break;
            case "FLAT END":
                flatEndsC.AddOrder(c);
                break;
            case "FLAT RAIL":
                flatRailsC.AddOrder(c);
                break;
            case "FLAT SHELVES":
                flatShelvesC.AddOrder(c);
                break;
            case "FLAT BACK":
                flatBacksC.AddOrder(c);
                break;
            case "FLAT OTHER":
                flatOtherC.AddOrder(c);
                break;
            case "FLAT DRAW":
                flatDrawBoxC.AddOrder(c);
                break;
            case "EXPR TOP":
                exprTopBottC.AddOrder(c);
                break;
            case "EXPR END":
                exprEndsC.AddOrder(c);
                break;
            case "EXPR RAIL":
                exprRailsC.AddOrder(c);
                break;
            case "EXPR SHELVES":
                exprShelvesC.AddOrder(c);
                break;
            case "EXPR BACK":
                exprBacksC.AddOrder(c);
                break;
            case "EXPR OTHER":
                exprOtherC.AddOrder(c);
                break;
            case "EXPR DRAW":
                exprDrawBoxC.AddOrder(c);
                break;
            case "CARRIER":
                carrierC.AddOrder(c);
                break;
        }
    }

    public void RemoveCarcase(String door, String draw, String order)
    {
        switch (draw)
        {
            case "LINE END":
                lineEndsC.RemoveCarcase(door, order);
                break;
            case "NON END":
                nonEndsC.RemoveCarcase(door, order);
                break;
            case "LINE TOP":
                lineTopBottC.RemoveCarcase(door, order);
                break;
            case "NON TOP":
                nonTopBottC.RemoveCarcase(door, order);
                break;
            case "LINE SHELVES":
                lineShelvesC.RemoveCarcase(door, order);
                break;
            case "NON SHELVES":
                nonShelvesC.RemoveCarcase(door, order);
                break;
            case "BACK":
                jBacksC.RemoveCarcase(door, order);
                break;
            case "RAIL":
                railC.RemoveCarcase(door, order);
                break;
            case "DRAW":
                drawBoxC.RemoveCarcase(door, order);
                break;
            case "NON OTHER":
                nonOtherC.RemoveCarcase(door, order);
                break;
            case "LINE OTHER":
                lineOtherC.RemoveCarcase(door, order);
                break;
            case "FLAT TOP":
                flatTopBottC.RemoveCarcase(door, order);
                break;
            case "FLAT END":
                flatEndsC.RemoveCarcase(door, order);
                break;
            case "FLAT RAIL":
                flatRailsC.RemoveCarcase(door, order);
                break;
            case "FLAT SHELVES":
                flatShelvesC.RemoveCarcase(door, order);
                break;
            case "FLAT BACK":
                flatBacksC.RemoveCarcase(door, order);
                break;
            case "FLAT OTHER":
                flatOtherC.RemoveCarcase(door, order);
                break;
            case "FLAT DRAW":
                flatDrawBoxC.RemoveCarcase(door, order);
                break;
            case "EXPR TOP":
                exprTopBottC.RemoveCarcase(door, order);
                break;
            case "EXPR END":
                exprEndsC.RemoveCarcase(door, order);
                break;
            case "EXPR RAIL":
                exprRailsC.RemoveCarcase(door, order);
                break;
            case "EXPR SHELVES":
                exprShelvesC.RemoveCarcase(door, order);
                break;
            case "EXPR BACK":
                exprBacksC.RemoveCarcase(door, order);
                break;
            case "EXPR OTHER":
                exprOtherC.RemoveCarcase(door, order);
                break;
            case "EXPR DRAW":
                exprDrawBoxC.RemoveCarcase(door, order);
                break;
            case "CARRIER":
                carrierC.RemoveCarcase(door, order);
                break;
        }
    }
}
