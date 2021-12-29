import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Alumno } from 'src/app/models/alumno';
import { AlumnoService } from 'src/app/services/alumno.service';

@Component({
  selector: 'app-alumnos-form',
  templateUrl: './alumnos-form.component.html',
  styleUrls: ['./alumnos-form.component.css']
})
export class AlumnosFormComponent implements OnInit {
  titulo= 'Crear alumnos';

  alumno: Alumno= new Alumno();
  error: any;
  constructor(private service: AlumnoService,
     private router: Router,
     private route: ActivatedRoute ) { }



  ngOnInit(){
    this.route.paramMap.subscribe(params => {
      const id: number = +params.get('id');
      if(id){
        this.service.ver(id).subscribe(alumno => 
          this.alumno= alumno)
      }
    })
  }

  public crear():void {
    console.log(this.alumno);
    this.service.crear(this.alumno).subscribe(alumno =>{
      console.log(alumno);
      alert(`Alumno ${alumno.nombre} creado con éxito`);
      this.router.navigate(["/alumnos"]);
  }, err => {
    if(err.status === 400){
      this.error = err.error;
      console.log(this.error);
    }
  })
}

public editar(): void {
  console.log(this.alumno);
  this.service.editar(this.alumno).subscribe(alumno =>{
    console.log(alumno);
    alert(`Alumno ${alumno.nombre} actualizado con éxito`);
    this.router.navigate(["/alumnos"]);
}, err => {
  if(err.status === 400){
    this.error = err.error;
    console.log(this.error);
  }
})
}

}
