package geogebra.cas.view;

import geogebra.main.Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CASOutputPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	//public static final int INDENT = 20; // pixel
	
	/**
	 * The text color of the output
	 */
	private static Color ERROR_COLOR = Color.red;
	private static Color OUTPUT_PREFIX_COLOR = Color.gray;
	private static String OUTPUT_PREFIX = "\u2192";

	private JLabel outputSign;
	private JLabel outputArea;
	private LaTeXPanel latexPanel; 

	public CASOutputPanel(Application app) {
		setBackground(Color.white);		
		setLayout(new BorderLayout(5,0));
		
		outputSign = new JLabel();	
		outputSign.setForeground(OUTPUT_PREFIX_COLOR);
		
		outputArea = new JLabel();	
		latexPanel = new LaTeXPanel(app);
		//will be overwritten later
		latexPanel.setForeground(Color.black);
		latexPanel.setBackground(Color.white);
		
		add(outputSign, BorderLayout.WEST);
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.setBackground(Color.white);
		centerPanel.add(outputArea);
		centerPanel.add(latexPanel);
		add(centerPanel, BorderLayout.CENTER);
	}
	
	public void setForeground(geogebra.common.awt.Color c){
		outputArea.setForeground(geogebra.awt.Color.getAwtColor(c));	
		latexPanel.setForeground(geogebra.awt.Color.getAwtColor(c));
	}
	
	public void setOutput(String output, String latexOutput, String cmd, boolean isError,geogebra.common.awt.Color c){
		boolean useLaTeXpanel = latexOutput != null && !isError;
		outputArea.setVisible(!useLaTeXpanel);
		latexPanel.setVisible(useLaTeXpanel);		
		setForeground(c);
		if (useLaTeXpanel) {
			latexPanel.setLaTeX(latexOutput);				
		} else {
			outputArea.setText(output);
			if (isError)
				outputArea.setForeground(ERROR_COLOR);	
		}	
		
		// update output sign
		if (cmd.length() == 0) {
			outputSign.setText(OUTPUT_PREFIX);
		}
		else if (cmd.equals("KeepInput")) {
			// show check character
			outputSign.setText("\u2713");
		} 
		else if (cmd.equals("Numeric")) {
			// show approx character
			outputSign.setText("\u2248");
		}
		else {
			outputSign.setText(cmd + ":");
		}
	}

	public String getOutput() {
		return outputArea.getText();
	}

	@Override
	final public void setFont(Font ft) {
		super.setFont(ft);
		
		if (ft == null) return;
		
		if (latexPanel != null)
			latexPanel.setFont(ft.deriveFont(ft.getSize() + 2f));
		
		if (outputArea != null)
			outputArea.setFont(ft);
		if (outputSign != null)
			outputSign.setFont(ft);
	}
}