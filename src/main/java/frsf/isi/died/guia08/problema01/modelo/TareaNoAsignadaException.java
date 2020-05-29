package frsf.isi.died.guia08.problema01.modelo;

public class TareaNoAsignadaException extends Exception {
	public TareaNoAsignadaException() {
		super("La tarea no esta asignada a este Empleado");
	}
}
