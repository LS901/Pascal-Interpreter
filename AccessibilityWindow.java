package view.windows;

import managers.AccessibilityManager;
import managers.WindowManager;
import utils.jsyntax.JEditTextArea;
import utils.jsyntax.TextAreaDefaults;
import utils.jsyntax.TextAreaPainter;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 * Accessibility options for users
 */
public class AccessibilityWindow {

  private JPanel panelOptions;
  private JComboBox jcbOutputFontSize;
  private JComboBox jcbCodeFontSize;
  private JComboBox jcbFontType;
  private JDialog dialog;
  private JCheckBox colourBox = new JCheckBox();
  private JCheckBox visualBox = new JCheckBox();
  private JCheckBox textSpeechBox = new JCheckBox();
  private JCheckBox fontTypeBox = new JCheckBox();
  private boolean colourChecked = false;
  private boolean visualChecked = false;
  private boolean textChecked = false;
  private int selectedOutputFont = 10;
  private int selectedCodeFont = 10;
  private static String selectedFontType;
  private JEditTextArea newChanges;

  private WindowManager wm = WindowManager.getInstance();
  
  /**
   * Creates a new AccessibilityWindow object.
   */
  public AccessibilityWindow() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    
    // Main Panel
    JPanel panelInterpreter = new JPanel(new GridLayout(0,1));
    
    // Colour Blind Options
    JPanel colourBlind = new JPanel();    
    JLabel colourBlindness = new JLabel("High Contrast Mode");
    Font heading = new Font("Sans-serif", Font.BOLD, 14);
    colourBlindness.setFont(heading);
    colourBlind.add(colourBlindness);
    
    if(AccessibilityManager.getHighContrastEnabled()) {
    	colourBox.setSelected(true);
    }else {
    	colourBox.setSelected(false);
    }
    
    colourBlind.add(colourBox);
    panelInterpreter.add(colourBlind);
    
    panelInterpreter.add(new JSeparator(SwingConstants.HORIZONTAL));
    
    // Visually Impaired Options
    JPanel visualImpair = new JPanel();
    JLabel visuallyImpaired = new JLabel("Visually Impaired");
    visuallyImpaired.setFont(heading);
    visualImpair.add(visuallyImpaired);
    visualImpair.add(visualBox);
    panelInterpreter.add(visualImpair);
    
    // Panel for setting font sizes & font types

    jcbOutputFontSize = new JComboBox();	
    jcbCodeFontSize = new JComboBox(); 	
    jcbFontType = new JComboBox();
    
    jcbOutputFontSize.setEnabled(false);
    jcbCodeFontSize.setEnabled(false);
    jcbFontType.setEnabled(false);
    	
    // Populate the font size combo boxes 
    for (int i = 10; i < 50; i++) {
      jcbOutputFontSize.addItem(String.valueOf(i));
      jcbCodeFontSize.addItem(String.valueOf(i));
    }
    
    // Populate font types 
    String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	    for ( int i = 0; i < fonts.length; i++ )
	    {
	      jcbFontType.addItem(fonts[i]);
	    }
    
	    
    JPanel editorFontSize = new JPanel();
    editorFontSize.add(new JLabel("Editor font size: "));
    editorFontSize.add(jcbCodeFontSize);
    
    JPanel interpreterFontSize = new JPanel();
    interpreterFontSize.add(new JLabel("Interpreter font size: "));
    interpreterFontSize.add(jcbOutputFontSize);
    
    JPanel fontType = new JPanel();
    fontType.add(new JLabel("Font Type: "));
    fontType.add(jcbFontType);
    fontTypeBox.setEnabled(false);
    fontType.add(fontTypeBox);
    
    panelInterpreter.add(editorFontSize);
    panelInterpreter.add(interpreterFontSize);
    panelInterpreter.add(fontType);
    
    //Enables or Disables combo boxes based on whether visually impaired is checked.
    visualBox.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(visualBox.isSelected()) {
				jcbCodeFontSize.setEnabled(true);
				jcbOutputFontSize.setEnabled(true);
				fontTypeBox.setEnabled(true);
			}else{
				jcbCodeFontSize.setEnabled(false);
				jcbOutputFontSize.setEnabled(false);
				fontTypeBox.setEnabled(false);
				fontTypeBox.setSelected(false);
				jcbFontType.setEnabled(false);
			}
			
		}
    });
    
    // Enables Font Type box when checkbox is selected.
    fontTypeBox.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(visualBox.isSelected() && fontTypeBox.isSelected()) {
				jcbFontType.setEnabled(true);
				
			}else{
				jcbFontType.setEnabled(false);	
			}
			
		}
    });

    panelInterpreter.add(new JSeparator(SwingConstants.HORIZONTAL));
    
    //Text-to-Speech Options
    JPanel textToSpeech = new JPanel();
    JLabel textSpeech = new JLabel("Text-To-Speech\n");
    JLabel textSpeechInstr = new JLabel("Highlight and press copy button");
    textSpeech.setFont(heading);
    textToSpeech.add(textSpeech);
  
    textToSpeech.add(textSpeechBox);
    panelInterpreter.add(textToSpeech);
    
    JPanel ttsInstr = new JPanel();
    ttsInstr.add( textSpeechInstr);
    panelInterpreter.add(ttsInstr);

    //Buttons for applying options or cancelling
    
    //Apply button returns true/false depending on if checkboxes are checked.
    JButton buttonApply = new JButton("Apply");
	    buttonApply.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if(colourBox.isSelected()) {
	    			AccessibilityManager.setHighContrastEnabled(true);
	    			wm.getEditorWindow().repaintArea(false);
	    			wm.getConsoleWindow().repaintArea(false);
	    		}else{
	    			AccessibilityManager.setHighContrastEnabled(false);
	    			wm.getEditorWindow().repaintArea(true);
	    			wm.getConsoleWindow().repaintArea(true);
	    		}
	    		
	    		//If visually impaired checkbox is checked, obtain the values currently selected in the combo boxes.
	    		if(visualBox.isSelected()) {
	    			AccessibilityManager.setMagnifyEnabled(true);
	    			wm.getMainMenu().fontMagnification(false);
	    			
	    				String outputText = (String)jcbOutputFontSize.getSelectedItem();
	    				AccessibilityManager.setOutputFont(Integer.parseInt(outputText));
	    				
	    				String codeText = (String)jcbCodeFontSize.getSelectedItem();
	    				AccessibilityManager.setCodeFont(Integer.parseInt(codeText));
	    				
	    				AccessibilityManager.setFontType((String) jcbFontType.getSelectedItem());
	    				
	    			// if Font type checkbox is also selected, change the size and the font type.
	    			if(fontTypeBox.isSelected()) {
	    				wm.getConsoleWindow().setFontSize(AccessibilityManager.getOutputFont(),false);
		    			wm.getEditorWindow().setFontSize(AccessibilityManager.getCodeFont(),false);
	    			}else {
	    				wm.getConsoleWindow().setFontSize(AccessibilityManager.getOutputFont());
		    			wm.getEditorWindow().setFontSize(AccessibilityManager.getCodeFont());
	    			}

	    		}else{
	    			AccessibilityManager.setMagnifyEnabled(false);
	    			wm.getMainMenu().fontMagnification(true);
	    			wm.getEditorWindow().setFontSize(12,true);
	    			wm.getConsoleWindow().setFontSize(12, true);
	    		}
	    		
	    		if(textSpeechBox.isSelected()) {
	    			AccessibilityManager.setTTSEnabled(true);
	    		}else{
	    			AccessibilityManager.setTTSEnabled(false);
	    		}
	    		
	    		wm.repaintAll();
	    		close();
	    	}
	    
	    });
	    
    JButton buttonCancel = new JButton("Cancel");
    buttonCancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          close();
        }
      });
    
    //Add the buttons to a Panel
    JPanel panelButtons = new JPanel();
    panelButtons.add(buttonApply);
    panelButtons.add(buttonCancel);
    
    //Concatenate all items
    panelOptions = new JPanel(new BorderLayout());
    panelOptions.add(panelInterpreter, BorderLayout.CENTER);
    panelOptions.add(panelButtons,BorderLayout.PAGE_END);
  }

  /**
   * Displays the options window
   */
  public void show() {

    dialog = new JDialog(wm.getMainScreenFrame(), "Accessibility Options");
    dialog.setModal(true);
    dialog.getContentPane().add(panelOptions);     
    dialog.setMinimumSize(new Dimension(400,350));
    dialog.setSize(400, 350);
    dialog.setLocationRelativeTo(wm.getMainScreenFrame());
    dialog.setVisible(true);
  }

  /**
   * Closes the options window
   */
  public void close() {
    if (dialog != null)
      dialog.dispose();
  }
  
  //Accessor methods
  public boolean getColourChecked() {
	  return colourChecked;
  }
  
  public boolean getVisualChecked() {
	  return visualChecked;
  }
  
  public boolean getTextChecked() {
	  return textChecked;
  }
  
  public int getSelectedOutputFont() {
	  return selectedOutputFont;
  }
  
  public int getSelectedCodeFont() {
	  return selectedCodeFont;
  }
  
  public static String getSelectedFontType() {
	  return selectedFontType;
  }
  
};

  