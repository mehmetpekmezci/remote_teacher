package userInterface;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
public class LeftPanel extends JPanel {

	CameraPanel  cameraPanel1;
	CameraPanel  cameraPanel2;
	ColorChooser colorChooser;
	ShapeChooser shapeChooser;
	PageSelector pageSelector;	
	
	
    int HEIGHT_CAMERA_PANEL_1=200;
    int HEIGHT_CAMERA_PANEL_2=200;
	int HEIGHT_COLOR_CHOOSER=100;
	int HEIGHT_SHAPE_CHOOSER=100;
	int HEIGHT_PAGE_SELECTOR=120;
	ApplicationContextVariables applicationContextVariables;
	
	public LeftPanel(ApplicationContextVariables applicationContextVariables){
		setLayout(null);
		this.applicationContextVariables=applicationContextVariables;
		cameraPanel1=new CameraPanel();cameraPanel1.setBackground(Color.BLUE);cameraPanel1.setBounds(0, 0, applicationContextVariables.getLeftPanelWidth(), HEIGHT_CAMERA_PANEL_1);
		cameraPanel2=new CameraPanel();cameraPanel2.setBackground(Color.DARK_GRAY);cameraPanel2.setBounds(0, HEIGHT_CAMERA_PANEL_1, applicationContextVariables.getLeftPanelWidth(), HEIGHT_CAMERA_PANEL_2);
        colorChooser=new ColorChooser(applicationContextVariables);colorChooser.setBounds(0, HEIGHT_CAMERA_PANEL_1+HEIGHT_CAMERA_PANEL_2, applicationContextVariables.getLeftPanelWidth(), HEIGHT_COLOR_CHOOSER);
		shapeChooser=new ShapeChooser(applicationContextVariables);shapeChooser.setBounds(0, HEIGHT_CAMERA_PANEL_1+HEIGHT_CAMERA_PANEL_2+HEIGHT_COLOR_CHOOSER, applicationContextVariables.getLeftPanelWidth(), HEIGHT_SHAPE_CHOOSER);
		pageSelector=new PageSelector(applicationContextVariables);pageSelector.setBounds(0, HEIGHT_CAMERA_PANEL_1+HEIGHT_CAMERA_PANEL_2+HEIGHT_COLOR_CHOOSER+HEIGHT_SHAPE_CHOOSER, applicationContextVariables.getLeftPanelWidth(), HEIGHT_PAGE_SELECTOR);
		
		add(cameraPanel1);
		add(cameraPanel2);
		add(colorChooser);
		add(shapeChooser);
		add(pageSelector);
		setVisible(true);
	}




	public CameraPanel getCameraPanel1() {
		return cameraPanel1;
	}




	public void setCameraPanel1(CameraPanel cameraPanel1) {
		this.cameraPanel1 = cameraPanel1;
	}




	public CameraPanel getCameraPanel2() {
		return cameraPanel2;
	}




	public void setCameraPanel2(CameraPanel cameraPanel2) {
		this.cameraPanel2 = cameraPanel2;
	}




	public ColorChooser getColorChooser() {
		return colorChooser;
	}


	public void setColorChooser(ColorChooser colorChooser) {
		this.colorChooser = colorChooser;
	}


	public ShapeChooser getShapeChooser() {
		return shapeChooser;
	}


	public void setShapeChooser(ShapeChooser shapeChooser) {
		this.shapeChooser = shapeChooser;
	}


	public PageSelector getPageSelector() {
		return pageSelector;
	}


	public void setPageSelector(PageSelector pageSelector) {
		this.pageSelector = pageSelector;
	}


}
