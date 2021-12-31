package com.formacionbdi.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.formacionbdi.microservicios.commons.services.CommonService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

//CONTROLADOR COMPLETO CON EL CRUD

@CrossOrigin()
public class CommonController<E, S extends CommonService<E>> {

	//Inyectamos el service - tipo genérico de la interfaz
	@Autowired //permite inyectar dependencias con otras dentro de Spring
	protected S service; //Protected para reutilizar el servicio en el controlador
	
	//Mapear ruta URL - Request es del tipo GET
	//Si no colocamos ruta, esta seria a la raíz del proyecto
	@GetMapping
	public ResponseEntity<?> listar() {//? para dindicar que se puede guardar cualquier tipo
		return ResponseEntity.ok().body(service.findAll());
	}
	
	
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable) {//? para dindicar que se puede guardar cualquier tipo
		return ResponseEntity.ok().body(service.findAll(pageable));
	}
	
	
	@GetMapping("/{id}") //Ruta del ID
	public ResponseEntity<?> ver(@PathVariable Long id){
		Optional<E> o = service.findById(id);
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(o.get());
	}
	
	@PostMapping
	public ResponseEntity<?> crear(@Validated @RequestBody E entity, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		E entityDb = service.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
	}
	
		
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteByID(id);
		return ResponseEntity.noContent().build();
	}
	
	protected ResponseEntity<?> validar(BindingResult result){
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err ->{
			errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(errores);
	}
	
}
