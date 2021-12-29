package com.formacionbdi.microservicios.commons.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//Indicamos que el tipo de las Entity es E --> Genérico
//Necesitamos no deénder de nada sino que se genérico
public interface CommonService<E> {
	//CRUD Básico
	
	public Iterable<E> findAll();
	
	public Page<E> findAll(Pageable pageable);
	
	public Optional<E> findById(Long id);
	
	public E save(E entity);
	
	public void deleteByID(Long id);	
	
	
	
}
