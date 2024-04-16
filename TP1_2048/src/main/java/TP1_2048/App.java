package TP1_2048;

import java.awt.EventQueue;
//import java.awt.Image;
//
//import javax.swing.JFrame;
//import java.awt.Toolkit;
//import java.net.URL;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JButton;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;


public class App 
{
    public static void main( String[] args )
    {
        MotorJuego mj =  new MotorJuego();
       
        //ui.AgregarObservador(mj);
        
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIJuegoGrafico window = new UIJuegoGrafico();
					
					window.AgregarObservador(mj);
					mj.AgregarObservador(window);
					
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
