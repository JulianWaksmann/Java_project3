package TP1_2048;

import java.awt.Color;
//import java.awt.EventQueue;
//import java.awt.Image;

//import javax.swing.*;
import java.awt.Toolkit;
//import java.net.URL;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.border.LineBorder;

import javax.swing.JFrame;
import javax.swing.JPanel;

//import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
//import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

//Interfaz funcional para representar el método a ejecutar en los observadores
@FunctionalInterface
interface AccionObservadorUI {
void ejecutar(IObservadorUIJuego observador);
}

public class UIJuegoGrafico implements IUIJuego, IObservadorMotorJuego{
	
	private static final int PROCENTAJE_INICIAL = 75;
	private static final int DISTANCIA_A_BORDE = 10;
	private List<IObservadorUIJuego> observadores;
	private JFrame frame;
	//private JTextArea txtConsola;
	private Odometer odoPuntajeActual;
	private Odometer odoMejorPuntaje;
	private TableroPanel pnlTablero;
	private JPanel pnlPuntajes;
	
	JButton btnArriba;
	JButton btnIzquierda;
	JButton btnDerecha;
	JButton btnAbajo;
	JSpinner spnTamanio;
	JSpinner spnPorcentajeDos;
	
	private int[][] tablero;
	
	private boolean juegoFinalizado;
	private JPanel pnlNuevoJuego;
	private JLabel lblTamanio;
	
	public JFrame getFrame() {
        return frame;
    }

	public UIJuegoGrafico() {
		initialize();
	}

	private void initialize() {
		this.juegoFinalizado = true;
		
		SoundPlayer.addClip("/TP1_2048/silent.wav", "silencio");
		SoundPlayer.addClip("/TP1_2048/ficha_mover.wav", "mover");
		SoundPlayer.addClip("/TP1_2048/juego_nuevo.wav", "nuevo");
		SoundPlayer.addClip("/TP1_2048/juego_perdio.wav", "perdio");
		SoundPlayer.addClip("/TP1_2048/juego_gano.wav", "gano");
		SoundPlayer.addClip("/TP1_2048/juego_pregunta.wav", "pregunta");
		
		observadores = new ArrayList<IObservadorUIJuego>();
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(UIJuegoGrafico.class.getResource("/TP1_2048/2048.gif")));
		frame.setTitle("2048");
		
		// Crear y agregar un panel de fondo personalizado al JFrame
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        try {
			BufferedImage backgroundImage = ImageIO.read(UIJuegoGrafico.class.getResource("/TP1_2048/2048.32x32.gif"));
			backgroundPanel.setBackgroundImage(backgroundImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        frame.setContentPane(backgroundPanel);
		
		
		// Obtener dimensiones del escritorio
        Dimension tamanioEscritorio = Toolkit.getDefaultToolkit().getScreenSize();
        int ancho = (int) (tamanioEscritorio.getWidth() * PROCENTAJE_INICIAL / 100.0);
        int alto = (int) (tamanioEscritorio.getHeight() * PROCENTAJE_INICIAL / 100.0);
		
        // Establecer dimensiones del JFrame
        //frame.setSize(640, 480);
        frame.setSize(ancho, alto);
        frame.setMinimumSize(new Dimension(640, 480));
        
        // Centrar el JFrame en la pantalla
        frame.setLocationRelativeTo(null);
        
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
        pnlTablero = new TableroPanel();
        pnlTablero.setBackground(new Color(250, 240, 230));
        pnlTablero.setBorder(new LineBorder(new Color(0, 0, 0)));
        pnlTablero.setBounds(204,135, 213, 253);
		frame.getContentPane().add(pnlTablero);
		
		JPanel pnlDireccion = new JPanel();
		pnlDireccion.setBackground(new Color(250, 240, 230));
		pnlDireccion.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlDireccion.setBounds(199, 11, 113, 113);
		frame.getContentPane().add(pnlDireccion);
		pnlDireccion.setLayout(null);
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/TP1_2048/arrow_up.gif"));
		
		btnArriba = new JButton(icon);
		btnArriba.setBounds(38, 2, 36, 36);
		pnlDireccion.add(btnArriba);
		
		icon = new ImageIcon(getClass().getResource("/TP1_2048/arrow_left.gif"));
		btnIzquierda = new JButton(icon);
		btnIzquierda.setBounds(2, 38, 36, 36);
		pnlDireccion.add(btnIzquierda);
		
		icon = new ImageIcon(getClass().getResource("/TP1_2048/arrow_right.gif"));
		btnDerecha = new JButton(icon);
		btnDerecha.setBounds(75, 38, 36, 36);
		pnlDireccion.add(btnDerecha);
		
		icon = new ImageIcon(getClass().getResource("/TP1_2048/arrow_down.gif"));
		btnAbajo = new JButton(icon);
		btnAbajo.setBounds(38, 75, 36, 36);
		pnlDireccion.add(btnAbajo);
		
		pnlPuntajes = new JPanel();
		pnlPuntajes.setBackground(new Color(250, 240, 230));
		pnlPuntajes.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlPuntajes.setBounds(350, 11, 264, 113);
		frame.getContentPane().add(pnlPuntajes);
		pnlPuntajes.setLayout(null);
		
	    odoMejorPuntaje = new Odometer(6, 25 );
	    odoMejorPuntaje.setBounds(130, 11, 123, 31);
	    pnlPuntajes.add(odoMejorPuntaje);
	    odoMejorPuntaje.setValue(0);
	    
	    JLabel lblMejorPuntaje = new JLabel("Mejor Puntaje");
	    lblMejorPuntaje.setHorizontalAlignment(SwingConstants.TRAILING);
	    lblMejorPuntaje.setFont(new Font("Arial", Font.BOLD, 15));
	    lblMejorPuntaje.setBounds(10, 11, 110, 31);
	    pnlPuntajes.add(lblMejorPuntaje);
	    
	    JLabel lblPuntajeActual = new JLabel("Puntaje Actual");
	    lblPuntajeActual.setHorizontalAlignment(SwingConstants.TRAILING);
	    lblPuntajeActual.setFont(new Font("Arial", Font.BOLD, 15));
	    lblPuntajeActual.setBounds(10, 71, 110, 31);
	    pnlPuntajes.add(lblPuntajeActual);
	    
	    odoPuntajeActual = new Odometer(6, 25);
	    odoPuntajeActual.setValue(0);
	    odoPuntajeActual.setBounds(130, 71, 123, 31);
	    pnlPuntajes.add(odoPuntajeActual);
	    
	    pnlNuevoJuego = new JPanel();
	    pnlNuevoJuego.setBounds(10, 11, 179, 113);
	    pnlNuevoJuego.setBackground(new Color(250, 240, 230));
	    pnlNuevoJuego.setBorder(new LineBorder(new Color(0, 0, 0)));
	    frame.getContentPane().add(pnlNuevoJuego);
	    pnlNuevoJuego.setLayout(null);
	    
	    JButton btnNuevoJuego = new JButton("Nuevo Juego");
	    btnNuevoJuego.setBounds(10, 10, 159, 23);
	    pnlNuevoJuego.add(btnNuevoJuego);
	    
	    lblTamanio = new JLabel("Tamaño de la grilla");
	    lblTamanio.setBounds(10, 44, 109, 23);
	    pnlNuevoJuego.add(lblTamanio);
	    
	    spnTamanio = new JSpinner();
	    spnTamanio.setModel(new SpinnerNumberModel(4, 4, 8, 1));
	    spnTamanio.setBounds(129, 44, 40, 23);
	    pnlNuevoJuego.add(spnTamanio);
	    
	    JLabel lblPorcentajeDos = new JLabel("Probabilidad del 2");
	    lblPorcentajeDos.setBounds(10, 80, 109, 23);
	    pnlNuevoJuego.add(lblPorcentajeDos);
	    
	    spnPorcentajeDos = new JSpinner();
	    spnPorcentajeDos.setModel(new SpinnerNumberModel(90, 10, 90, 5));
	    spnPorcentajeDos.setBounds(129, 80, 40, 23);
	    pnlNuevoJuego.add(spnPorcentajeDos);
	    btnNuevoJuego.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		btnNevoJuegoActionPerformed(e);
	    	}
	    });
			    
		btnAbajo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoPerformed(e, Direccion.ABAJO);
			}
		});
		btnDerecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoPerformed(e, Direccion.DERECHA);
			}
		});
		btnIzquierda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoPerformed(e, Direccion.IZQUIERDA);
			}
		});
		btnArriba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movimientoPerformed(e, Direccion.ARRIBA);
			}
		});
		
		/*
		txtConsola = new JTextArea();
		txtConsola.setFont(new Font("Lucida Console", Font.PLAIN, 13));
	    JScrollPane scrollPane = new JScrollPane(txtConsola, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBounds(25, 186, 304, 274);
	    frame.getContentPane().add(scrollPane);
	    */
		
        /*
		frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // Obtener el tamaño del JFrame
                int size = Math.min(frame.getWidth(), frame.getHeight());
                // Establecer el tamaño del tablero
                pnlTablero.setSize(new Dimension(size, size));
                // Redibujar el tablero
                pnlTablero.revalidate();
                pnlTablero.repaint();
            }
        });
        */
		
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	resizePuntaje();
            	resizeTablero();
            }
        });
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	if (!juegoFinalizado) {
	            	SoundPlayer.playClip("pregunta");
	            	int option = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas salir del juego?", "2048", JOptionPane.YES_NO_OPTION);
		            if (option == JOptionPane.YES_OPTION) {
		                frame.dispose();
		            }
            	}
            	else
            	{
	                frame.dispose();
            	}
            }
        });
        

	    
	    // Creamos un KeyEventDispatcher global
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
            	boolean procesado = procesarTecla(e); 
            	
            	if (!procesado) {
            		Component componenteOrigen = e.getComponent(); //componente original que generó el evento
            		//reenvia el evento al componente con foco
                    Component componenteEnFoco = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (componenteEnFoco != null && componenteEnFoco != componenteOrigen && componenteEnFoco instanceof Component) {
                        // Enviamos el evento de teclado al componente original
                    	componenteEnFoco.dispatchEvent(e);
                    }
            		
            	}
            	
                
                // retorna false para permitir que el evento siga su curso normal
            	// Retorna true para indicar que el evento fue procesado
                return procesado;
            }
        });
        this.establercerEstadoBotones(false);
        //hace play de un wav silencioso para evitar el bug de la primera reprduccion
        SoundPlayer.playClip("silencio");
	}
	private void resizePuntaje() {
        // Obtener el tamaño del JFrame
        Dimension frameSize = frame.getContentPane().getSize();
        Dimension puntajesSize = pnlPuntajes.getSize();
        
        int x = frameSize.width - puntajesSize.width - DISTANCIA_A_BORDE;
        Point p = pnlPuntajes.getLocation();
        p.x = x;
        pnlPuntajes.setLocation(p);
	}
	
	private void resizeTablero() {
        // Obtener el tamaño del JFrame
        Dimension frameSize = frame.getContentPane().getSize();
        Point tableroLocation = pnlTablero.getLocation();
        
        int altoTablero = frameSize.height - tableroLocation.y - 10;
        int anchoTablero = frameSize.width - 20;
        //int nuevoTamanio = Math.min(altoTablero, anchoTablero);
        //int nuevoPosicionX = (frameSize.width - nuevoTamanio) / 2; 
        
        //pnlTablero.setBounds(nuevoPosicionX, tableroLocation.y, nuevoTamanio, nuevoTamanio);
        pnlTablero.setBounds(10, tableroLocation.y, anchoTablero, altoTablero);
	}
	
	

	@Override
	public void AgregarObservador(IObservadorUIJuego observador) {
		observadores.add(observador);		
	}

	@Override
	public void RemoverObservador(IObservadorUIJuego observador) {
		observadores.remove(observador);		
	}
	
	
	/*********************************************
	 * Metodos genericos de llamada a observador *
	 *********************************************/
	// Ejecutar una acción en todos los observadores utilizando reflection
    public void ejecutarEnObservadores(String nombreMetodo, Object... args) {
        for (IObservadorUIJuego observador : observadores) {
            try {
                Method metodo = IObservadorMotorJuego.class.getMethod(nombreMetodo, obtenerTiposArgumentos(args));
                metodo.invoke(observador, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Obtener los tipos de los argumentos
    private Class<?>[] obtenerTiposArgumentos(Object... args) {
        Class<?>[] tipos = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            tipos[i] = args[i].getClass();
        }
        return tipos;
    }

    // Ejecutar una acción en todos los observadores
    public void ejecutarEnObservadores(AccionObservadorUI accion) {
        for (IObservadorUIJuego observador : observadores) {
            accion.ejecutar(observador);
        }
    }
    
    private void NuevoJuego(int tamanioTablero, int probabilidadDelDos) {
    	
    	if (!this.juegoFinalizado) {
    		SoundPlayer.playClip("pregunta");
    		if (JOptionPane.showConfirmDialog(frame, "El juego no finalizó.\r\nEmpiezas uno nuevo?", "2048", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    			this.juegoFinalizado = true;
    		}
    	}
    	if (this.juegoFinalizado) {
    		SoundPlayer.playClip("nuevo");
			ejecutarEnObservadores(observador -> observador.NuevoJuego(tamanioTablero, probabilidadDelDos));
			this.juegoFinalizado = false;
			this.establercerEstadoBotones(!juegoFinalizado);
    	}
	}
	
	private void Mover(Direccion direccion) {
		SoundPlayer.playClip("mover");
		ejecutarEnObservadores(observador -> observador.Mover(direccion));
	}

	@Override
	public void CrearTablero(int[][] tablero) {
		
		this.pnlTablero.setTamanioTablero(tablero.length);
		
		this.tablero= new int[tablero.length][tablero.length];
		for(int y = 0; y < tablero.length; y++) {
			for (int x =0; x < tablero.length; x ++) {
				this.tablero[y][x] = tablero[y][x];
			}
		}
		imprimirTablero(this.tablero);
	}

	@Override
	public void RealizarPasoMovimiento(PasoMovimiento movimiento) {
		this.tablero[movimiento.getDestino().getY()][movimiento.getDestino().getX()] = movimiento.getOrigen().getValor();
		this.pnlTablero.RealizarPasoMovimiento(movimiento);
		//this.pnlTablero.setValorFicha(movimiento);
		imprimirTablero(this.tablero);
	}

	@Override
	public void ActualizarPuntos(int puntos) {
		odoPuntajeActual.setValue(puntos);
	}

	@Override
	public void Perdio() {
		SoundPlayer.playClip("perdio");
		this.juegoFinalizado = true;
		this.establercerEstadoBotones(!this.juegoFinalizado);
		JOptionPane.showMessageDialog(frame, "Perdiste...");
		
		//txtConsola.append("Jugador: PERDIO \r\n\r\n");
		debugPrint("Jugador: PERDIO \r\n\r\n");
	}

	@Override
	public void Gano() {
		SoundPlayer.playClip("gano");
		this.juegoFinalizado = true;
		this.establercerEstadoBotones(!this.juegoFinalizado);
		JOptionPane.showMessageDialog(frame, "Ganaste!!!");
		//txtConsola.append("Jugador: GANO \r\n\r\n");
		debugPrint("Jugador: GANO \r\n\r\n");
	}
	
	@Override
	public void NuevoRecord(int nuevoRecord) {
		this.odoMejorPuntaje.setValue(nuevoRecord);
	}
	
	private void establercerEstadoBotones(boolean enabled) {
		this.btnArriba.setEnabled(enabled);
		this.btnIzquierda.setEnabled(enabled);
		this.btnDerecha.setEnabled(enabled);
		this.btnAbajo.setEnabled(enabled);
	}
	

	
	
	/************************************************************/
	private void btnNevoJuegoActionPerformed(ActionEvent e) {
		int tamanio = (int)this.spnTamanio.getValue();
		int porcentajeDos = (int)this.spnPorcentajeDos.getValue();
		NuevoJuego(tamanio, porcentajeDos);
		//NuevoJuego(4, 90);
	}
	
	private void  movimientoPerformed(ActionEvent e, Direccion direccion) {
		Mover(direccion);
	}
	
	private boolean procesarTecla(KeyEvent e) {
		boolean procesado = false;
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			int codigoTecla = e.getKeyCode();
			switch(codigoTecla) {
			case KeyEvent.VK_UP:
				Mover(Direccion.ARRIBA);
				procesado = true;
				break;
			case KeyEvent.VK_RIGHT:
				Mover(Direccion.DERECHA);
				procesado = true;
				break;
			case KeyEvent.VK_DOWN:
				Mover(Direccion.ABAJO);
				procesado = true;
				break;
			case KeyEvent.VK_LEFT:
				Mover(Direccion.IZQUIERDA);
				procesado = true;
				break;
			}
		}
		
		return procesado;
	}
	
	private void debugPrint(String str) {
		System.out.print(str);
	}
		
	private void imprimirTablero(int[][] tablero) {
		
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tablero.length; i++) {
            sb.append("+----");
        }
        sb.append("+\r\n");
        String separador = sb.toString();
		
		for(int y = 0; y < tablero.length; y++) {
			//txtConsola.append(separador);
			debugPrint(separador);
			for (int x =0; x < tablero.length; x ++) {
				//txtConsola.append("|" + String.format("%4d", tablero[y][x]));
				debugPrint("|" + String.format("%4d", tablero[y][x]));
			}
			//txtConsola.append("|\r\n");
			debugPrint("|\r\n");
		}
		//txtConsola.append(separador + "\r\n\r\n");
		debugPrint(separador + "\r\n\r\n");
	}
	
	private class BackgroundPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private BufferedImage backgroundImage = null;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Dibujar la imagen de fondo repetidamente en todo el área del JPanel
                int width = backgroundImage.getWidth();
                int height = backgroundImage.getHeight();
                for (int x = 0; x < getWidth(); x += width) {
                    for (int y = 0; y < getHeight(); y += height) {
                        g.drawImage(backgroundImage, x, y, null);
                    }
                }
            }
        }
        
        public void setBackgroundImage(BufferedImage backgroundImage) {
        	this.backgroundImage = backgroundImage;
        }
    }
}