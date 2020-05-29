package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	private Function<Tarea, Double> calculoPagoPorTareaContratado = (tarea) -> {
		if((tarea.getDiasDemorados()*4) < tarea.getDuracionEstimada())
			return (tarea.getDuracionEstimada()*(this.costoHora*1.3));
		else if(((tarea.getDiasDemorados()*4)-8) > (tarea.getDuracionEstimada()))
			return (tarea.getDuracionEstimada()*(this.costoHora*0.75));
	 
		else return (tarea.getDuracionEstimada()* this.costoHora);
	};	
	private Function<Tarea, Double> calculoPagoPorTareaEfectivo = (tarea) -> {
		if((tarea.getDiasDemorados()*4) < tarea.getDuracionEstimada())
			return (tarea.getDuracionEstimada()*(this.costoHora*1.2));
		else return (tarea.getDuracionEstimada()* this.costoHora);
	};	 
		
	private Predicate<Tarea> puedeAsignarTareaContratado = (tarea) -> (this.tareasAsignadas.size()<5 
			&& tarea.getEmpleadoAsignado()==null 
			&& !this.tareasAsignadas.contains(tarea));
	private Predicate<Tarea> puedeAsignarTareaEfectivo = (tarea) -> ((this.getHoras() + tarea.getDuracionEstimada())<= 15
			&& !this.tareasAsignadas.contains(tarea)
			&& tarea.getEmpleadoAsignado()==null );
		
	/**
	 * @param cuil
	 * @param nombre
	 * @param tipo
	 * @param costoHora
	 */
	public Empleado(Integer cuil, String nombre, Double costoHora) {
		this.cuil = cuil;
		this.nombre = nombre;
		this.costoHora = costoHora;
	}


	/**
	 * 
	 */
	public Empleado() {
		super();
	}

	public Integer getHoras() {
		int horas = 0;
		for (Tarea tarea : tareasAsignadas) {
			horas += (tarea.getDuracionEstimada());
		}
		return horas;
	}


	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}
	
	
	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}


	public Double salario() {
		// cargar todas las tareas no facturadas
		// calcular el costo
		// marcarlas como facturadas.
		double salario=0.0;
		switch (tipo) {
			case CONTRATADO:
				for(Tarea tarea : tareasAsignadas) {
					if(tarea.getFacturada()==false && tarea.getFechaFin() != null) {
						salario += calculoPagoPorTareaContratado.apply(tarea);
						tarea.setFacturada(true);
					}
				}
			case EFECTIVO:
				for(Tarea tarea : tareasAsignadas) {
					if(tarea.getFacturada()==false && tarea.getFechaFin() !=null) {
						salario += calculoPagoPorTareaEfectivo.apply(tarea);
						tarea.setFacturada(true);
					}
				}
		}
		return salario;
	}

	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		double costo = 0.0;
		switch (tipo) {
			case CONTRATADO:
				if(t.getFechaFin() != null) costo = calculoPagoPorTareaContratado.apply(t);
				else costo = t.getDuracionEstimada() * this.costoHora;
			case EFECTIVO:
				if(t.getFechaFin() != null) costo = calculoPagoPorTareaEfectivo.apply(t);
				else costo = t.getDuracionEstimada() * this.costoHora;}
	return costo;
	}
		
	public Boolean asignarTarea(Tarea t) throws TareaAsignadaException {
		boolean condicion = false;
		if(this.tareasAsignadas == null) {
			this.tareasAsignadas = new ArrayList<Tarea>();
		}
		switch (tipo) {
			case CONTRATADO:
				condicion = (this.puedeAsignarTareaContratado.test(t));
				break;
			case EFECTIVO:
				condicion = (this.puedeAsignarTareaEfectivo.test(t));
				break;
		}
		if(condicion==true) {
			this.tareasAsignadas.add(t);
			t.asignarEmpleado(this);
		}
		else if(t.getEmpleadoAsignado()!= null) throw new TareaAsignadaException();
	return condicion;
	}
	
	public void comenzar(Integer idTarea) throws TareaNoAsignadaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
		boolean encontro = false;
		for(Tarea tarea : tareasAsignadas) {
			if(tarea.getId().equals(idTarea)) { 
				tarea.setFechaInicio(LocalDateTime.now());
				encontro=true;
			}
		}
		if (encontro == false) throw new TareaNoAsignadaException();
	}
	
	public void finalizar(Integer idTarea) throws TareaNoAsignadaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean encontro = false;
		for(Tarea tarea : tareasAsignadas) {
			if(tarea.getId().equals(idTarea)) { 
				tarea.setFechaFin(LocalDateTime.now());
				tarea.setDiasDemorados();
				encontro=true;
			}
		}
		if (encontro == false) throw new TareaNoAsignadaException();
	}

	public void comenzar(Integer idTarea,String fecha) throws TareaNoAsignadaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean encontro = false;
		for(Tarea tarea : tareasAsignadas) {
			if(tarea.getId().equals(idTarea)) { 
				tarea.setFechaInicio(LocalDateTime.parse(fecha, formatter));
				encontro=true;
			}
		}
		if (encontro == false) throw new TareaNoAsignadaException();
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaNoAsignadaException {
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
		boolean encontro = false;
		for(Tarea tarea : tareasAsignadas) {
			if(tarea.getId().equals(idTarea)) { 
				tarea.setFechaFin(LocalDateTime.parse(fecha, formatter));
				tarea.setDiasDemorados();
				encontro=true;
			}
		}
		if (encontro == false) throw new TareaNoAsignadaException();
	}
	
	public List<Tarea> getTareaNoFacturadas () {
		return this.tareasAsignadas.stream()
						.filter(t -> t.getFechaFin() !=null)
						.filter(t -> t.getFacturada()==false)
						.collect(Collectors.toList());
	}


	@Override
	public int hashCode() {
		return Objects.hash(cuil);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false; 
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Empleado other = (Empleado) obj;
		return Objects.equals(cuil, other.cuil);
	}
	
}
