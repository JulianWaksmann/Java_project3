package TP1_2048;

public interface IMotorJuego {
	
	void NuevoJuego(int tamanioTablero, int probabilidadDelDos);
	void Mover(Direccion direccion);
	void AgregarObservador(IObservadorMotorJuego observador);
	void RemoverObservador(IObservadorMotorJuego observador);
	

}
