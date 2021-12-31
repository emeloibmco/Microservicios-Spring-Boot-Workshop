package com.formacionbdi.microservicios.app.examenes.services;

import java.util.List;

import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.services.CommonService;

public interface ExamenService extends CommonService<Examen>{
	
	public List<Examen> findByNombre(String term);
	
}
