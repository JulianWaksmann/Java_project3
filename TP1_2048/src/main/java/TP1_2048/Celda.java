package TP1_2048;

public class Celda {
	
	private int x;
	private int y;
	private int valor;
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getValor() {
		return valor;
	}
	
	public int setValor(int valor) {
		return this.valor = valor;
	}

	public Celda() {
	}
	
	public Celda (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Celda (int x, int y, int valor) {
		this(x, y);
		this.valor = valor;
	}
	
}