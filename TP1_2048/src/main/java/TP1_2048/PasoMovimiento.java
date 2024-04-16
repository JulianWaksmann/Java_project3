package TP1_2048;

public class PasoMovimiento {
	Celda origen;
	Celda destino;
	public PasoMovimiento(Celda origen, Celda destino) {
		this.origen = origen;
		this.destino = destino;
	}
	public Celda getOrigen() {
		return origen;
	}
	public Celda getDestino() {
		return destino;
	}
}
