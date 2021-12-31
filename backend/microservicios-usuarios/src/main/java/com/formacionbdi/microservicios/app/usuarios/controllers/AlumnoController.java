package com.formacionbdi.microservicios.app.usuarios.controllers;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.formacionbdi.microservicios.app.usuarios.services.AlumnoService;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.controllers.CommonController;

//CONTROLADOR COMPLETO CON EL CRUD

@RestController //Marcamos la clase como un controlador de tipo rest
@CrossOrigin()
public class AlumnoController extends CommonController<Alumno, AlumnoService>{
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id){
		
		Optional<Alumno> o = service.findById(id);
		
		if(o.isEmpty() || o.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}
		
		Resource imagen = new ByteArrayResource(o.get().getFoto());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(imagen);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		//1. Buscamos por el id
		Optional<Alumno> o = service.findById(id);
		
		//2. Validamos que existe
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//3. Si existe los obtenemos y cambiamos los datos que vienen en el request
		Alumno alumnoDb = o.get(); //Obtenemos el alumno
		alumnoDb.setNombre(alumno.getNombre()); //Set para modificar y get para obtener el nombre del alumno
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
		
		//Fecha no se edita, por lo tanto no utilizamos método set en ese caso
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
	}
	
	//Filtrar por nombre o apellido
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(service.findByNombreOrApellido(term));
	}

	
	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
		if(!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		
		return super.crear(alumno, result);
	}
	
	
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Alumno alumno, BindingResult result, @PathVariable Long id, 
			@RequestParam MultipartFile archivo) throws IOException{
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		//1. Buscamos por el id
		Optional<Alumno> o = service.findById(id);
		
		//2. Validamos que existe
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		//3. Si existe los obtenemos y cambiamos los datos que vienen en el request
		Alumno alumnoDb = o.get(); //Obtenemos el alumno
		alumnoDb.setNombre(alumno.getNombre()); //Set para modificar y get para obtener el nombre del alumno
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
		
		//Fecha no se edita, por lo tanto no utilizamos método set en ese caso
		
		if(!archivo.isEmpty()) {
			alumnoDb.setFoto(archivo.getBytes());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
	}
	
	
}
