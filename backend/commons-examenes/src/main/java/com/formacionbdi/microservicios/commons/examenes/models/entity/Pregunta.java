package com.formacionbdi.microservicios.commons.examenes.models.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name ="preguntas")
public class Pregunta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	private String texto;
	
	@JsonIgnoreProperties(value = {"preguntas"}) //evitar relacion inversa
	//muchas preguntas - un examen
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examen_id") //llave foranea
	private Examen examen;
	

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof Pregunta)) {
			return false;
		}
		
		Pregunta a = (Pregunta) obj;
		
		//Eliminar un objeto de la relación
		return this.Id != null && this.Id.equals(a.getId());
	}
	
	
}
