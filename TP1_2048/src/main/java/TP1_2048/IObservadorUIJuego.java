package TP1_2048;

public interface IObservadorUIJuego {
	void NuevoJuego(int tamanioTablero, int probabilidadDelDos);
	void Mover(Direccion direccion);
}
