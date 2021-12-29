import { Alumno } from "./alumno";
import { Pregunta } from "./pregunta";

export class Respuesta {
    id: number;
    texto: string;
    alumno: Alumno;
    pregunta: Pregunta;
}
