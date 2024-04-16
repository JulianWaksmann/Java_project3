package TP1_2048;

public class ResultadoMovimiento {
	boolean hayMovimiento;
	int puntaje;
	
	
	public ResultadoMovimiento(boolean hayMovimiento, int puntaje) {
		this.hayMovimiento = hayMovimiento;
		this.puntaje = puntaje;
	}
	
	public boolean getHayMovimiento() {
		return hayMovimiento;
	}


	public int getPuntaje() {
		return puntaje;
	}
}
