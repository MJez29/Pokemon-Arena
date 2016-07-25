/**
 * @(#)GraphicsFun.java
 *
 *
 * @author 
 * @version 1.00 2016/1/15
 */

import javax.swing.*;

public class GraphicsFun extends JFrame
{

    public GraphicsFun() 
    {
    	super("Hello, it's me");
    	setSize(1024, 768);
    	setVisible(true);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args)
    {
    	GraphicsFun frame = new GraphicsFun();
    }
}