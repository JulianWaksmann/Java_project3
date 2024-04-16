package TP1_2048;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

public class Odometer extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numberOfDigits;
    private int[] currentValues;
    private int[] targetValues;
    private Timer timer;
    private int animationSpeed;
    private int[] digitOffsets;
    private Font font;

    public Odometer(int numberOfDigits, int animationSpeed) {
        this.numberOfDigits = numberOfDigits;
        this.animationSpeed = animationSpeed;
        this.currentValues = new int[numberOfDigits];
        this.targetValues = new int[numberOfDigits];
        this.digitOffsets = new int[numberOfDigits];
        this.font = new Font("Arial", Font.BOLD, 10);
        
        setLayout(new GridLayout(1, numberOfDigits, 1, 0));
        for (int i = 0; i < numberOfDigits; i++) {
            digitOffsets[i] = 0;
            DigitPanel digitPanel = new DigitPanel();
            add(digitPanel);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new OdometerTask(), 0, this.animationSpeed);
    }

    public void setValue(int value) {
        //String strValue = String.valueOf(value);
        String strValue = String.format("%0" + numberOfDigits + "d", value);
        int length = strValue.length();

        for (int i = 0; i < numberOfDigits; i++) {
            if (numberOfDigits - i <= length) {
                targetValues[i] = Character.getNumericValue(strValue.charAt(length - numberOfDigits + i));
            } else {
                targetValues[i] = 0;
            }
        }
    }

    private class OdometerTask extends TimerTask {
        @Override
        public void run() {
            for (int i = 0; i < numberOfDigits; i++) {
                if (currentValues[i] != targetValues[i]) {
                    if (currentValues[i] < 9) {
                        currentValues[i]++;
                    } else {
                        currentValues[i] = 0;
                    }
                }
            }
            repaint();
        }
    }

    private class DigitPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Color backgroundColor;
		Color foregroundColor;

        public DigitPanel() {
        	this.foregroundColor = Color.WHITE;
        	this.backgroundColor = Color.BLACK;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Dimension dimension = this.getSize();
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int digit = getParent().getComponentZOrder(this);
            int value = currentValues[digit];

            // Clear background
            g2d.setColor(this.backgroundColor);
            g2d.fillRect(0, 0, dimension.width, dimension.height);

            // Draw current value
            g2d.setColor(this.foregroundColor);

            // Calcular el tamaño del fuente basado en el ancho y alto del carácter '0'
            Font digitFont = CalcularFuenteOptimo(g2d);
            g2d.setFont(digitFont);
            
            FontMetrics fontMetrics = g2d.getFontMetrics(digitFont);
            Rectangle2D stringBounds = fontMetrics.getStringBounds(String.valueOf(value), g2d);

            // Calcular la posición para centrar el texto
            int x = (int) ((dimension.width - stringBounds.getWidth()) / 2);
            int y = (int) ((dimension.height - stringBounds.getHeight()) / 2) + fontMetrics.getAscent(); // Ajuste para centrar verticalmente

            g2d.drawString(String.valueOf(value), x, y);
            g2d.dispose();
        }
        
        private Font CalcularFuenteOptimo(Graphics2D g2d) {
            int fontSize = 1;
            Font newFont = font;
            while (true) {
                newFont = font.deriveFont(Font.BOLD, fontSize);
                g2d.setFont(newFont);
                FontMetrics fontMetrics = g2d.getFontMetrics();
                Rectangle2D stringBounds = fontMetrics.getStringBounds("0", g2d);
                if (stringBounds.getWidth() >= getWidth() || stringBounds.getHeight() >= getHeight()) {
                    break;
                }
                fontSize++;
            }
            return newFont;
        }
    }
}
