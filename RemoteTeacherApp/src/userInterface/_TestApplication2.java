/*
 * Created on Dec 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package userInterface;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JFileChooser;
import network.NetworkConnector;

/**
 * @author mpekmezci
 */
public class _TestApplication2 {
	public static void main(String[] args)  {
		args=new String[]{"localhost","012725c56ac63c5d37e44ba5d9011d98","burcu.pekmezci@gmail.com","elif.pekmezci@gmail.com"};
		MainApplication.main(args);
	}
}
