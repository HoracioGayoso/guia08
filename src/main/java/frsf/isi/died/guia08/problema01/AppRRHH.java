package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaAsignadaException;
import frsf.isi.died.guia08.problema01.modelo.TareaNoAsignadaException;

public class AppRRHH {

	private List<Empleado> empleados;
	
	
	
	/**
	 * 
	 */
	public AppRRHH() {
		super();
	}

	
	public List<Empleado> getEmpleados() {
		return empleados;
	}


	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}


	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		// crear un empleado
		// agregarlo a la lista
		if(empleados == null) empleados = new ArrayList<Empleado>();
		Empleado E = new Empleado (cuil, nombre, costoHora);
		E.setTipo(Tipo.CONTRATADO);
		empleados.add(E);
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		// crear un emplo
		// agregarlo a la lista
		if(empleados == null) empleados = new ArrayList<Empleado>();
		Empleado E = new Empleado (cuil, nombre, costoHora);
		E.setTipo(Tipo.EFECTIVO);
		empleados.add(E);
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws TareaAsignadaException {
		// crear un empleado
		// con el método buscarEmpleado() de esta clase
		// agregarlo a la lista	
		Empleado E = new Empleado();
		Optional<Empleado> O = this.buscarEmpleado(e -> (e.getCuil().equals(cuil)));
		if(O.isPresent()) {
			E = O.get();
		Tarea t = new Tarea(idTarea, descripcion, duracionEstimada);
		E.asignarTarea(t);
		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		try {
			Empleado E = new Empleado();
			Optional<Empleado> O = this.buscarEmpleado(e -> (e.getCuil().equals(cuil)));
			if(O.isPresent()) {
				E = O.get();
			E.comenzar(idTarea);
			}
		} catch (TareaNoAsignadaException e1) {
			e1.getMessage();
		}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		// crear un empleado
		// agregarlo a la lista
		try {
			Empleado E = new Empleado();
			Optional<Empleado> O = this.buscarEmpleado(e -> (e.getCuil().equals(cuil)));
			if(O.isPresent()) {
				E = O.get();
			E.finalizar(idTarea);
			}
		} catch (TareaNoAsignadaException e1) {
			e1.getMessage();
		}
	}


	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		if(empleados==null) empleados = new ArrayList<Empleado>();
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea= null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					Empleado E = new Empleado();
					E.setCuil(Integer.valueOf(fila[0]));
					E.setNombre(fila[1]);
					E.setCostoHora(Double.valueOf(fila[2]));
					E.setTipo(Tipo.CONTRATADO);
					this.empleados.add(E);
				}
			}
		}

	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
		if(empleados==null) empleados = new ArrayList<Empleado>();
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea= null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					Empleado E = new Empleado();
					E.setCuil(Integer.valueOf(fila[0]));
					E.setNombre(fila[1]);
					E.setCostoHora(Double.valueOf(fila[2]));
					E.setTipo(Tipo.EFECTIVO);
					this.empleados.add(E);
				}
			}
		}

	}

	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException, TareaAsignadaException {
		// leer datos del archivo
		// cada fila del archivo tendrá:
		// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea= null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					Tarea T = new Tarea();
					T.setId(Integer.valueOf(fila[0]));
					T.setDescripcion(fila[1]);
					T.setDuracionEstimada(Integer.valueOf(fila[2]));
					Empleado E;
					Optional<Empleado> O = this.buscarEmpleado(e -> (e.getCuil().equals(Integer.valueOf(fila[3]))));
					if(O.isPresent()) {
						E = O.get();
					E.asignarTarea(T);
					}
				}
			}
		} catch (TareaAsignadaException E1) {
			E1.getMessage();
		}
	}
	
	private void guardarTareasTerminadasCSV() throws IOException {
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
			try(Writer fileWriter= new FileWriter("src\\tareas.csv",true)) {
				try(BufferedWriter out = new BufferedWriter(fileWriter)){
					List<Tarea> listaTareas = this.getTodasTareasNoFacturadas();
					for(Tarea tarea : listaTareas) 
						out.write(tarea.asCsv()+ System.getProperty("line.separator"));
				}
			}
		}

	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
	
	public List<Tarea> getTodasTareasNoFacturadas() {
		return this.empleados.stream()
							.map(e -> e.getTareaNoFacturadas())
							.flatMap(List::stream)
							.collect(Collectors.toList());
	}
	

}
