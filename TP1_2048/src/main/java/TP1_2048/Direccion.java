package TP1_2048;

public enum Direccion {
	ARRIBA(1),
	DERECHA(2),
	ABAJO(4),
	IZQUIERDA(8);
	
	private final int valor;

    Direccion(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
			

}