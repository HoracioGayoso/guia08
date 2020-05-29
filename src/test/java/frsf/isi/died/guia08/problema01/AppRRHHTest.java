package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaAsignadaException;

public class AppRRHHTest {

	AppRRHH app;
	Empleado e1, e2;
	Tarea t1, t2, t3, t4, t5, t6, t7;

	
	@Before
	public void inicializar() {
		app = new AppRRHH();
//		e1 = new Empleado(1542, "Horacio Gayoso", 15.00);
//		e2 = new Empleado (2456, "Matias Gayoso", 10.00);
//		t1 = new Tarea(1, "hola",8);
//		t2= new Tarea(2, "chau", 4);
//		t3= new Tarea(3, "limpiar", 4);
//		t4= new Tarea();
//		t5= new Tarea();
//		t6= new Tarea();
//		t7= new Tarea();
	}
	
	@Test
	public void testAgregarEmpleadoConrtatado() {
		app.agregarEmpleadoContratado(1542, "Horacio Gayoso", 15.00);
		List<Empleado> emp = app.getEmpleados();
		e1= new Empleado(1542, "Horacio Gayoso", 15.00);
		e1.setTipo(Tipo.CONTRATADO);
		assertEquals(emp.get(0), e1);
		assertEquals(emp.get(0).getTipo(), e1.getTipo());
	}
	@Test
	public void testAgregarEmpleadoEfectivo() {
		app.agregarEmpleadoEfectivo(1542, "Horacio Gayoso", 15.00);
		List<Empleado> emp = app.getEmpleados();
		e1= new Empleado(1542, "Horacio Gayoso", 15.00);
		e1.setTipo(Tipo.EFECTIVO);
		assertEquals(emp.get(0), e1);
		assertEquals(emp.get(0).getTipo(), e1.getTipo());
	}
	@Test
	public void testAsignarTarea() throws TareaAsignadaException {
		app.agregarEmpleadoContratado(1542, "Horacio Gayoso", 15.00);
		app.asignarTarea(1542, 1, "hola", 8);
		List<Empleado> emp = app.getEmpleados();
		t1= new Tarea(1, "hola", 8);
		e1=new Empleado(1542,"Horacio Gayoso", 15.00);
		assertTrue(emp.contains(e1));
		assertTrue(emp.get(0).getTareasAsignadas().contains(t1));	
	}
	@Test
	public void testEmpezarTarea() throws TareaAsignadaException {
		app.agregarEmpleadoContratado(1542, "Horacio Gayoso", 15.00);
		app.asignarTarea(1542, 1, "hola", 8);
		app.empezarTarea(1542, 1);
		t1= new Tarea(1, "hola", 8);
		e1=new Empleado(1542,"Horacio Gayoso", 15.00);
		List<Empleado> emp = app.getEmpleados();
		List<Tarea> tareas = emp.get(0).getTareasAsignadas();
		assertTrue(emp.contains(e1));
		assertTrue(!tareas.get(0).getFechaInicio().equals(null));
	}
	@Test
	public void testTerminarTarea() throws TareaAsignadaException {
		app.agregarEmpleadoContratado(1542, "Horacio Gayoso", 15.00);
		app.asignarTarea(1542, 1, "hola", 8);
		app.empezarTarea(1542, 1);
		app.terminarTarea(1542, 1);
		t1= new Tarea(1, "hola", 8);
		e1=new Empleado(1542,"Horacio Gayoso", 15.00);
		List<Empleado> emp = app.getEmpleados();
		List<Tarea> tareas = emp.get(0).getTareasAsignadas();
		assertTrue(emp.contains(e1));
		assertTrue(!tareas.get(0).getFechaFin().equals(null));
	}
	@Test
	public void testCargarEmpleadosContratadosCSV() throws FileNotFoundException, IOException {
		app.cargarEmpleadosContratadosCSV("src\\testAñadirEmpleadosContratados.csv");
		e1=new Empleado (1111, "Horacio Gayoso", 15.00);
		e2=new Empleado (2222, "Matias Gayoso", 10.00);
		assertTrue(app.getEmpleados().contains(e1));
		assertTrue(app.getEmpleados().contains(e2));
		assertTrue(app.getEmpleados().get(0).getTipo().equals(Tipo.CONTRATADO));
		assertTrue(app.getEmpleados().get(1).getTipo().equals(Tipo.CONTRATADO));
	}
	@Test
	public void testCargarEmpleadosEfectivosCSV() throws FileNotFoundException, IOException {
		app.cargarEmpleadosEfectivosCSV("src\\testAñadirEmpleadosEfectivos.csv");
		e1=new Empleado (3333, "Horacio Gayoso", 15.00);
		e2=new Empleado (4444, "Matias Gayoso", 10.00);
		assertTrue(app.getEmpleados().contains(e1));
		assertTrue(app.getEmpleados().contains(e2));
		assertTrue(app.getEmpleados().get(0).getTipo().equals(Tipo.EFECTIVO));
		assertTrue(app.getEmpleados().get(1).getTipo().equals(Tipo.EFECTIVO));
	}
	@Test
	public void testCargarTareasCSV() throws FileNotFoundException, IOException, TareaAsignadaException {
		app.cargarEmpleadosContratadosCSV("src\\testAñadirEmpleadosContratados.csv");
		app.cargarTareasCSV("src\\testAñadirTareas.csv");
		e1=new Empleado (1111, "Horacio Gayoso", 15.00);
		e2=new Empleado (2222, "Matias Gayoso", 10.00);
		t1=new Tarea(1,"limpiar",8);
		t2=new Tarea(2,"reparar",8);
		
		assertTrue(app.getEmpleados().contains(e1));
		assertTrue(app.getEmpleados().contains(e2));
		assertTrue(app.getEmpleados().get(0).getTareasAsignadas().contains(t1));
		assertTrue(app.getEmpleados().get(1).getTareasAsignadas().contains(t2));
	}
	@Test
	public void testFacturar() throws TareaAsignadaException, IOException {
		t1=new Tarea(1,"limpiar",8);
		t2=new Tarea(2,"reparar",8);
		e1=new Empleado (1111, "Horacio Gayoso", 15.00);
		e2=new Empleado (2222, "Matias Gayoso", 10.00);
		
		app.agregarEmpleadoContratado(1111, "Horacio Gayoso", 15.00);
		app.asignarTarea(1111, 1, "limpiar", 8);
		app.agregarEmpleadoContratado(2222, "Matias Gayoso", 10.00);
		app.asignarTarea(2222, 2,"reparar",8);
		app.empezarTarea(1111, 1);
		app.empezarTarea(2222, 2);
		app.terminarTarea(1111, 1);
		Double sueldo = app.facturar();
		Double esperado = 156.0;
		assertEquals(esperado, sueldo);
	}
}
