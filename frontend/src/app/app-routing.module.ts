import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AlumnosFormComponent } from './components/alumnos/alumnos-form.component';
import { AlumnosComponent } from './components/alumnos/alumnos.component';
import { CursosComponent } from './components/cursos/cursos.component';
import { ExamenesComponent } from './components/examenes/examenes.component';

const routes: Routes = [
  {path:'alumnos', component:AlumnosComponent},
  {path:'alumnos/form', component:AlumnosFormComponent},
  {path:'alumnos/form/:id', component:AlumnosFormComponent},
  {path:'cursos', component:CursosComponent},
  {path:'examenes', component:ExamenesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
