package src;
/**
  * Ever wonder how many $$$ per beer that 12 pack is? Do you wonder if you are ever really 
  * saving by buying in larger quantities? The beerculator will make it easy for you to compute
  * the cost per 12 fl oz beer, and find out how much money you are REALLY saving!
  * 
  * The Beerculator calculates the dollars spent per beer. Made using Swing.
  * @author David Michel, /u/davejoshmike
  * @date May 6, 2016
  */

import java.awt.event.*;
import javax.swing.*;
import java.text.*;

public class Beerculator implements ActionListener, ItemListener
{
	private static String CurrentState = "MI";
	private JTextField displayField;
	private JTextField prevDisplayField;
	private JTextField prevprevDisplayField;
	private JTextField quantityField;
	private JTextField costField;
	private JTextField ouncesField;
	private JCheckBox taxCheckBox;
	private JCheckBox depositCheckBox;
	private JButton showMoreBtn;
	private JButton hideBtn;
	private JCheckBox ozCheckBox;
	private JCheckBox mLCheckBox;
	private boolean useTax = true;
	private boolean useDeposit = true;

	public  Beerculator()
	{
	JFrame frame = new JFrame("Beerculator");
	frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
	
	displayField = new JTextField(20);
	displayField.setEditable(false);
	frame.getContentPane().add(displayField);

	prevDisplayField = new JTextField(20);
	prevDisplayField.setEditable(false);
	frame.getContentPane().add(prevDisplayField);
	
	prevprevDisplayField = new JTextField(20);
	prevprevDisplayField.setEditable(false);
	frame.getContentPane().add(prevprevDisplayField);
	
	frame.getContentPane().add(new JLabel("Cost: $"));

	costField = new JTextField(20);
	costField.addActionListener(this);
	frame.getContentPane().add(costField);
	
	frame.getContentPane().add(new JLabel("Quantity: "));

	quantityField = new JTextField(20);
	quantityField.addActionListener(this);
	quantityField.setText("6");
	frame.getContentPane().add(quantityField);

	frame.getContentPane().add(new JLabel("Ounces per Beer: "));
	
	ouncesField = new JTextField(20);
	ouncesField.addActionListener(this);
	ouncesField.setText("12");
	frame.getContentPane().add(ouncesField);
	
	taxCheckBox = new JCheckBox("Tax");
	taxCheckBox.setSelected(true);
	frame.getContentPane().add(taxCheckBox);
	taxCheckBox.addItemListener(this);
	
	depositCheckBox = new JCheckBox("Deposit");
	depositCheckBox.setSelected(true);
	frame.getContentPane().add(depositCheckBox);
	depositCheckBox.addItemListener(this);

	optionsPanel = new JPanel();
	frame.getContentPane().add(panel, BorderLayout.CENTER);
	optionsPanel.setLayout = new BorderLayout(0, 0);

	showMoreBtn = new JButton("Show More");
	showMoreBtn.addActionListener(this);
	optionsPanel.add(showMoreBtn, BorderLayout.NORTH);

	hideBtn = new JButton("Hide");
    	hideBtn.addActionListener(this);
    	optionsPanel.add(btnHide, BorderLayout.SOUTH);



	//have an advanced options arrow?
	//chooseUnitsField = new JCheckBox("")
//	frame.getContentPane().add(new JLabel("mL (leave blank if unused): "));

	ozCheckBox = new JCheckBox("Use oz");
        ozCheckBox.setSelected(true);
	ozCheckBox.addActionListener(this);
	ozCheckBox.setVisible(false);
	ozCheckBox.setEnabled(false);
	//ozField.setVerticalAlignment(SwingConstants.TOP);
	optionsPanel.add(ozCheckBox, BorderLayout.CENTER);

       	mLCheckBox = new JCheckBox("Use mL");
        mLCheckBox.setSelected(false);
        mLCheckBox.addActionListener(this);
        mLCheckBox.setVisible(false);
        mLCheckBox.setEnabled(false);
        //mLField.setVerticalAlignment(SwingConstants.TOP);
        optionsPanel.add(mLCheckBox, BorderLayout.CENTER);

	frame.pack();
	frame.setVisible(true);
	displayField.setText("= $0.00 per 12 fl oz beer");

	}

	/**
	  * creates new thread and runs computation, changing the displayField when done
	  * example computation:
	  *	(((
	  	((5.50)*1.06)
	  	+(.10*4))
	  	/4)/16)*12)
	  * @param e, an ActionEvent (i.e. Enter Key Pressed)
	  */
	public void actionPerformed(ActionEvent e)
   	{
	//displayField.setText("Computing...");
	new Thread()
	{
	    public void run()
	    {
		try{
			DecimalFormat df = new DecimalFormat("0.00");
			double cost = Double.parseDouble(costField.getText());
			int quantity = Integer.parseInt(quantityField.getText());
			double ounces = Double.parseDouble(ouncesField.getText());
			double preCost = cost;
			double pricePerBeer;
			if(useTax){
				preCost = taxBeer(preCost, "MI"); //preCost = preCost*TAX;
			}
			if(useDeposit){
				preCost = preCost+(getDeposit()*quantity);
			}
			pricePerBeer = ((preCost/quantity)/ounces)*12; 
			prevprevDisplayField.setText("     "+prevDisplayField.getText());
			prevDisplayField.setText("     "+displayField.getText());
			displayField.setText("= $" + df.format(pricePerBeer) + " per 12 fl oz beer");
	    	} catch (Exception e){
	    		displayField.setText("Invalid parameter, please try again.");
	    	}
	    }
	}.start();
    }


	//TODO Unit Conversions (i.e. oz to mL)
	public double getConversion(String to, String from)
	{
		if(CurrentUnits == "oz"){
			
		}
	}


	public double taxBeer(double amt, String state)
	{
		double Tax;
		if(CurrentState.equals("MI"))
			Tax = 1.06;
		else if(CurrentState.equals("IL"))
			//$0.231 per gallon if alcohol content 0.5-7%,
			//$1.39 per gallon if > 7%,
			//$8.55 per gallon if > 20%
			//1 gallon = 128 oz
		//TODO add an abv/# of oz check? or just have a beer, wine or spirits checkbox
		//TODO add more states?
		//TODO add beer, wine, spirits checkbox
		return amt*Tax;
	}

	public double getDeposit()
	{
		double Deposit;
		if(CurrentState.equals("MI"))
			Deposit = .10;
		else if(CurrentState.equals("ME", "VT","CT","MA","NY","OR","IA"))
			Deposit = .05;
		else if(CurrentState.equals("CA")){
			//TODO finish Cali
			//if()
				Deposit = .05; //if bottles <= 24 fl oz
			//else if()
			//	Deposit = .10; //if bottles > 24 fl oz
		}
		return Deposit;
	}
	/**
	  * toggles true and false of our useTax, useDeposit variables
	  */
	public void itemStateChanged(ItemEvent e)
	{
		Object sender = e.getItemSelectable();
		if(sender == taxCheckBox){
			if(useTax)
			useTax = false;
			else
			useTax = true;
		} else if(sender == depositCheckBox){
			if(useDeposit)
			useDeposit = false;
			else
			useDeposit = true;
		}

		else if(sender == ozCheckBox){
			//TODO
			//CurrentUnits = oz
			//TODO Make CurrentUnits and CurrentState singletons?
		} else if(sender == mLCheckBox){
			//Todo
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		Object sender = e.getSource();
		if (sender.equals(showMoreBtn)){
			ozCheckBox.setVisible(true);
			mLCheckBox.setVisible(true);
		} else if (sender.equals(hideBtn)){
			ozCheckBox.setVisible(false);
			mLCheckBox.setVisible(false);
		}
	}

	public static void main(String args[])
	{
		Beerculator bc = new Beerculator();
	}
}
