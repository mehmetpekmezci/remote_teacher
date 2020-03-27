package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Enumeration;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooser extends JColorChooser implements ChangeListener {

	ApplicationContextVariables applicationContextVariables;
	public ColorChooser(ApplicationContextVariables applicationContextVariables){
		this.applicationContextVariables=applicationContextVariables;
		//this.setLayout(new BorderLayout());
       UIManager.put("ColorChooser.swatchesSwatchSize", new Dimension(9,10));
        AbstractColorChooserPanel[] panels=getChooserPanels();
        for(int i=0;i<panels.length;i++){
        	if(!panels[i].getDisplayName().equals("Swatches"))removeChooserPanel(panels[i]);
        }

        JPanel p = (JPanel) getChooserPanels()[0].getComponent(0);
        p.remove(2);
        p.remove(1);
        ((BorderLayout)this.getLayout()).setHgap(0);;
        ((BorderLayout)this.getLayout()).setVgap(0);



        setPreviewPanel(new JPanel());
       getSelectionModel().addChangeListener(this);
       
	}
	public void stateChanged(ChangeEvent e) {
		applicationContextVariables.setCurrentColor(getSelectionModel().getSelectedColor());
		Enumeration enumeration=applicationContextVariables.getMainFrame().getLeftPanel().getShapeChooser().getButtonGroup().getElements();
		
		while(enumeration.hasMoreElements()){
			JToggleButton jToggleButton=(JToggleButton)enumeration.nextElement();
			if(!jToggleButton.getText().equals("MOVE")){
				jToggleButton.setForeground(getSelectionModel().getSelectedColor());
				jToggleButton.repaint();				
			}
		}
		applicationContextVariables.getMainFrame().getLeftPanel().getShapeChooser().getFontComboBox().setForeground(getSelectionModel().getSelectedColor());
		applicationContextVariables.getMainFrame().getLeftPanel().getShapeChooser().getFontComboBox().repaint();
	}
}
