import { Alumno } from "./alumno";
import { Examen } from "./examen";

export class Curso {
    id: number;
    nombre: string;
    createAt: string; 
    alumnos: Alumno[] = [];
    examenes: Examen[] = [];


}
