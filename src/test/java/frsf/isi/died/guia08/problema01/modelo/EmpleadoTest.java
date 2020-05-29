package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.
	
	Empleado e1, e2;
	Tarea t1, t2, t3, t4, t5, t6, t7;

	
	@Before
	public void inicializar() {
		e1 = new Empleado(1542, "Horacio Gayoso", 15.00);
		e2 = new Empleado (2456, "Matias Gayoso", 10.00);
		t1 = new Tarea(1, "hola",8);
		t2= new Tarea(2, "chau", 4);
		t3= new Tarea(3, "limpiar", 4);
		t4= new Tarea();
		t5= new Tarea();
		t6= new Tarea();
		t7= new Tarea();
	}
	
	@Test
	public void testSalarioEmpleadoContratadoSinDemora() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		e1.finalizar(t1.getId(), "26-05-2020 20:00");
		Double sueldo = e1.salario();
		Double esperado = 156.0;
		assertEquals(esperado, sueldo);
		
	}
	@Test
	public void testSalarioEmpleadoContratadoConDemoras() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "23-05-2020 20:00");
		e1.finalizar(t1.getId(), "30-05-2020 20:00");
		Double sueldo = e1.salario();
		Double esperado = 90.0;
		assertEquals(esperado, sueldo);
		
	}
	@Test
	public void testSalarioEmpleadoContratadoConSueldoNormal() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		e1.finalizar(t1.getId(), "27-05-2020 20:00");
		Double sueldo = e1.salario();
		Double esperado = 120.0;
		assertEquals(esperado, sueldo);
		
	}
	@Test
	public void testSalarioEmpleadoEfectivoSinDemoras() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		e1.finalizar(t1.getId(), "26-05-2020 20:00");
		Double sueldo = e1.salario();
		Double esperado = 144.0;
		assertEquals(esperado, sueldo);
		
	}
	@Test
	public void testSalarioEmpleadoEfectivoConDemoras() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "23-05-2020 20:00");
		e1.finalizar(t1.getId(), "30-05-2020 20:00");
		Double sueldo = e1.salario();
		Double esperado = 120.0;
		assertEquals(esperado, sueldo);
		
	}
	@Test
	public void testCostoTarea() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		Double costo = e1.costoTarea(t1);
		Double esperado = 120.0;
		assertEquals(esperado, costo);
	}

	@Test
	public void testPuedeAsignarTareaEmpleadoContratado() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		List<Tarea> lista = e1.getTareasAsignadas();
		Tarea esperado = lista.get(0);
		assertEquals(esperado, t1);
	}
	@Test 
	public void testNoPuedeAsignarTareaPorMuchaCantidadEmpleadoContratado() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.asignarTarea(t4);
		e1.asignarTarea(t5);
		e1.asignarTarea(t6);
		assertFalse(e1.getTareasAsignadas().contains(t6));
	}

	@Test (expected=TareaAsignadaException.class)
	public void testNoPuedeAsignarPorqueYaEstaAsignadaEnOtroEmpleado() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e2.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e2.asignarTarea(t1);
		e2.asignarTarea(t2);
		e1.asignarTarea(t2);
	}
	@Test (expected=TareaAsignadaException.class)
	public void testNoPuedeAsignarTareaPorqueYaLaTieneAsignadaEmpleado() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e2.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e2.asignarTarea(t2);
		assertFalse(e1.asignarTarea(t1));
		assertFalse(e2.asignarTarea(t2));
	}
	
	@Test
	public void testNoPuedeAsignarTareaPorExcesoHorasEmpleadoEfectivo() throws TareaAsignadaException {
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		assertFalse(e1.getTareasAsignadas().contains(t3));
	}
	
	
	@Test
	public void testComenzarInteger() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId());
		boolean esperado = (t1.getFechaInicio() != null); 
		assertTrue(esperado);
	}
	@Test (expected=TareaNoAsignadaException.class)
	public void testNoPuedeComenzarPorNoEstarAsignadaInteger() throws TareaNoAsignadaException, TareaAsignadaException{
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.comenzar(t1.getId());
	}
	@Test
	public void testFinalizarInteger() throws TareaNoAsignadaException, TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId());
		e1.finalizar(t1.getId());
		boolean esperado = (t1.getFechaFin() != null); 
		assertTrue(esperado);
	}
	@Test (expected=TareaNoAsignadaException.class)
	public void testNoPuedeFinalizarPorNoEstarAsignadaInteger() throws TareaNoAsignadaException, TareaAsignadaException{
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.finalizar(t1.getId());
	}

	@Test
	public void testComenzarIntegerString() throws TareaAsignadaException, TareaNoAsignadaException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		boolean esperado = (t1.getFechaInicio().equals(LocalDateTime.parse("25-05-2020 20:00", formatter))); 
		assertTrue(esperado);
	}
	@Test (expected=TareaNoAsignadaException.class)
	public void testNoPuedeComenzarPorNoEstarAsignadaIntegerString() throws TareaNoAsignadaException, TareaAsignadaException{
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
	}

	@Test
	public void testFinalizarIntegerString() throws TareaAsignadaException, TareaNoAsignadaException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "25-05-2020 20:00");
		e1.finalizar(t1.getId(), "26-05-2020 20:00");
		boolean esperado = (t1.getFechaFin().equals(LocalDateTime.parse("26-05-2020 20:00", formatter))); 
		assertTrue(esperado);
	}
	@Test (expected=TareaNoAsignadaException.class)
	public void testNoPuedeFinalizarPorNoEstarAsignadaIntegerString() throws TareaNoAsignadaException, TareaAsignadaException{
		e1.setTipo(Tipo.EFECTIVO);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		e1.finalizar(t1.getId(), "26-05-2020 20:00");
	}
	@Test
	public void testTareasNoFacturadas() throws TareaAsignadaException {
		e1.setTipo(Tipo.CONTRATADO);
		e1.asignarTarea(t1);
		e1.asignarTarea(t2);
		e1.asignarTarea(t3);
		t1.setFacturada(true);
		List<Tarea> lista =e1.getTareaNoFacturadas();
		assertTrue(lista.contains(t2));
		assertTrue(lista.contains(t3));
		assertFalse(lista.contains(t1));
	}
}
