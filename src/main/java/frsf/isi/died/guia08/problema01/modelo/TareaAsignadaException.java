package frsf.isi.died.guia08.problema01.modelo;

public class TareaAsignadaException extends Exception {
	public TareaAsignadaException() {
		super("La tarea ya esta asignada a otro Empleado o ya fue finalizada");
	}
}
