package userInterface;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import network.FileUpload;
import userInterface.shapes.Img;
import userInterface.shapes.StringShape;

public class ShapeChooser extends JPanel implements ActionListener{

	private ButtonGroup buttonGroup = new ButtonGroup();
    private JComboBox<String> fontComboBox;
   ApplicationContextVariables applicationContextVariables;
	public ShapeChooser(ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		setLayout(new GridLayout(5, 2));
		JToggleButton drawButton=new JToggleButton("DRAW");add(drawButton);drawButton.addActionListener(this);buttonGroup.add(drawButton);		
		drawButton.setSelected(true);
		JToggleButton lineButton=new JToggleButton("LINE");add(lineButton);lineButton.addActionListener(this);		buttonGroup.add(lineButton);		
		JToggleButton rectButton=new JToggleButton("RECTANGLE");add(rectButton);rectButton.addActionListener(this);		buttonGroup.add(rectButton);		
		JToggleButton frectButton=new JToggleButton("FILLED RECT");add(frectButton);frectButton.addActionListener(this);		buttonGroup.add(frectButton);		
		JToggleButton ovalButton=new JToggleButton("OVAL");add(ovalButton);ovalButton.addActionListener(this);		buttonGroup.add(ovalButton);		
		JToggleButton fovalButton=new JToggleButton("FILLED OVAL");add(fovalButton);fovalButton.addActionListener(this);		buttonGroup.add(fovalButton);		
		JButton imageButton=new JButton("IMAGE");add(imageButton);imageButton.addActionListener(this);		
		JToggleButton selectButton=new JToggleButton("MOVE");add(selectButton);selectButton.addActionListener(this);		buttonGroup.add(selectButton);		
		JButton undoButton=new JButton("UNDO");add(undoButton);undoButton.addActionListener(this);		
		String[] petStrings = { "Font:12", "Font:14", "Font:16", "Font:18", "Font:20", "Font:22", "Font:24", "Font:28", "Font:30", "Font:32", "Font:34" };
		 fontComboBox=new JComboBox<String>(petStrings);fontComboBox.setSelectedIndex(2);add(fontComboBox);fontComboBox.addActionListener(this);		
	}
	
	public void actionPerformed(ActionEvent e) {
		applicationContextVariables.getMainFrame().getDrawingArea().resetString();
		if (e.getSource() instanceof JButton ) {
			JButton button = (JButton) e.getSource();
			if(button.getText().equals("UNDO")){
				if (JOptionPane.showConfirmDialog(null, "Are you sure , there is no REDO button !!", "Undo", 
					    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
					    == JOptionPane.YES_OPTION){
				                              applicationContextVariables.getMainFrame().getDrawingArea().undo();
				}
			}else if(button.getText().equals("IMAGE")){
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile() != null) {
						File selectedFile=chooser.getSelectedFile();
	//					System.out.println(selectedFile.getName());
						String [] selectedFileSplitted=selectedFile.getName().split("\\.");
		//				System.out.println("selectedFileSplitted.length="+selectedFileSplitted.length);
						String extension="";
						if(selectedFileSplitted.length>0)
						 extension=selectedFileSplitted[selectedFileSplitted.length-1];
						BufferedImage bufferedImage=null;
						try {
							bufferedImage = ImageIO.read(selectedFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						int width=applicationContextVariables.getCurrentPage().getBufferedImage().getWidth();
						int height=applicationContextVariables.getCurrentPage().getBufferedImage().getHeight();
						Img img=new Img();
						img.image=(Image)bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						img.extension=extension;
						img.setInitialPoint1(0, 0);
						img.setInitialPoint2(width,height);
						img.setWidth(width);
						img.setHeight(height);

						        FileUpload fileUpload=new FileUpload();
								fileUpload.send(selectedFile.getAbsolutePath(), img.id,extension, applicationContextVariables.getServerIp(),applicationContextVariables.getUserEmail(),applicationContextVariables.getSessionId());
								
								applicationContextVariables.getCurrentPage().addShape(img,false); 
								applicationContextVariables.getMainFrame().getDrawingArea().repaint();
						



					}
				}
			}

				
		}
		if (e.getSource() instanceof JToggleButton ) {
			applicationContextVariables.setCurrentMod(((JToggleButton)e.getSource()).getText());
		}
		if(e.getSource() instanceof JComboBox){
			String selectedFont=(String)fontComboBox.getSelectedItem();
			selectedFont=selectedFont.replace("Font:","");
			applicationContextVariables.setFontSize(Integer.parseInt(selectedFont));
		}
		
		

		
		
	}
	
	public JComboBox<String> getFontComboBox() {
		return fontComboBox;
	}

	public void setFontComboBox(JComboBox<String> fontComboBox) {
		this.fontComboBox = fontComboBox;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}
}
