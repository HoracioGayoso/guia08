package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {

	Empleado e1, e2;
	Tarea t1, t2, t3, t4, t5, t6, t7;

	
	@Before
	public void inicializar() {
		e1 = new Empleado(1542, "Horacio Gayoso", 15.00);
		e2 = new Empleado (2456, "Matias Gayoso", 10.00);
		t1 = new Tarea(1, "hola",8);
//		t2= new Tarea(2, "chau", 4);
//		t3= new Tarea(3, "limpiar", 4);
//		t4= new Tarea();
//		t5= new Tarea();
//		t6= new Tarea();
//		t7= new Tarea();
	}
	
	@Test
	public void asignarEmpleadoTest() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		assertTrue(t1.getEmpleadoAsignado().equals(e1));
	}
	@Test (expected=TareaAsignadaException.class)
	public void asignarEmpleadoTestFallaPorEstarAsignadaAOtroEmpleado() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e2.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		t1.asignarEmpleado(e2);
		assertTrue(t1.getEmpleadoAsignado().equals(e2));
	}
	@Test (expected=TareaAsignadaException.class)
	public void asignarEmpleadoTestFallaPorTareaTerminada() throws TareaAsignadaException, TareaNoAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		t1.setFechaFin(LocalDateTime.now());
		t1.asignarEmpleado(e1);
		assertTrue(t1.getEmpleadoAsignado().equals(e1));
	}
	@Test (expected=TareaAsignadaException.class)
	public void asignarEmpleadoTestFallaPorAmbasCondiciones() throws TareaAsignadaException, TareaNoAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		t1.setFechaFin(LocalDateTime.now());
		e2.setTipo(Tipo.EFECTIVO);
		t1.asignarEmpleado(e2);
		assertTrue(t1.getEmpleadoAsignado().equals(e2));
	}
}