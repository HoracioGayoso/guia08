package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	private long diasDemorados;



	public Tarea() {
		super();
	}
	

	public Tarea(Integer id, String descripcion, Integer duracionEstimada) {
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.setFacturada(false);
	}
	
	public void asignarEmpleado(Empleado e) throws TareaAsignadaException {
		// si la tarea ya tiene un empleado asignado
		// y tiene fecha de finalizado debe lanzar una excepcion
		if(this.empleadoAsignado != null || this.fechaFin != null) throw new TareaAsignadaException();
		else this.empleadoAsignado = e;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}

	public long getDiasDemorados() {
		return diasDemorados;
	}


	public void setDiasDemorados() {
		this.diasDemorados = (Duration.between(fechaInicio, fechaFin).toDays()) - (this.duracionEstimada/24);
	}


	public String asCsv() {
		return this.id+ ";"+ this.descripcion+";"+ this.duracionEstimada+";"+ this.getEmpleadoAsignado().getCuil()+ ";"+ this.getEmpleadoAsignado().getNombre();
		}


	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		Tarea other = (Tarea) obj;
		return Objects.equals(id, other.id);
	}

}
