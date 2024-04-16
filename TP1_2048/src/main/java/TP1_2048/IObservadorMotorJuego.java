package TP1_2048;

public interface IObservadorMotorJuego {
	void CrearTablero(int[][] tablero);
	void RealizarPasoMovimiento(PasoMovimiento movimiento);
	void ActualizarPuntos(int puntos);
	void Perdio();
	void Gano();
	void NuevoRecord(int muevoRecord);
}