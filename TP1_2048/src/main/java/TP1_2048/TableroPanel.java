package TP1_2048;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TableroPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private int tamanioTablero;
    private int margen = 4; 
    private FichaPanel[][] fichas;
    private JLayeredPane panelSuperpuesto; 

    public TableroPanel() {
    	panelSuperpuesto = new JLayeredPane();
    	panelSuperpuesto.setLayout(null); // Desactivar el layout manager predeterminado
        add(panelSuperpuesto);
    	
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        int size = Math.min(dimension.width, dimension.height);
        return new Dimension(size, size);
    }
    

    private void crearFichas() {
        fichas = new FichaPanel[tamanioTablero][tamanioTablero];
        for (int i = 0; i < tamanioTablero; i++) {
            for (int j = 0; j < tamanioTablero; j++) {
                fichas[i][j] = new FichaPanel();
                this.add(fichas[i][j]);
            }
        }
		SwingUtilities.invokeLater(() -> {
			this.repaint();
		});
    }

    private void resize() {
    	if (this.tamanioTablero > 0) {
	        int anchoContenedor = getWidth();
	        int altoContenedor = getHeight();
	        int anchoFicha = (anchoContenedor - (tamanioTablero - 1) * margen) / tamanioTablero - margen / 2;
	        int altoFicha = (altoContenedor - (tamanioTablero - 1) * margen) / tamanioTablero - margen / 2;
	        
	        //int size = Math.min(anchoContenedor, altoContenedor);
	        //setSize(size, size);
	        
	        for (int i = 0; i < tamanioTablero; i++) {
	            for (int j = 0; j < tamanioTablero; j++) {
	                Dimension panelDimension = new Dimension(anchoFicha, altoFicha);
	                fichas[i][j].setPreferredSize(panelDimension);
	                fichas[i][j].setMinimumSize(panelDimension);
	                fichas[i][j].setMaximumSize(panelDimension);
	            }
	        }
    	}

        revalidate();
        repaint();
    }

    public void setTamanioTablero(int tamanioTablero) {
    	this.tamanioTablero = tamanioTablero;
        this.removeAll(); // Elimina los paneles anteriores
        this.setLayout(new GridLayout(tamanioTablero, tamanioTablero, margen, margen));
        crearFichas();
        revalidate();
        repaint();
    }
    
    public void RealizarPasoMovimiento(PasoMovimiento movimiento) {
    	setValorFicha(movimiento);
    }
    
    public void setValorFicha(PasoMovimiento movimiento) {
    	fichas[movimiento.getDestino().getY()][movimiento.getDestino().getX()].setValor(movimiento.getOrigen().getValor());
    }
}
