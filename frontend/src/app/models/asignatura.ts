export class Asignatura {
    id: number;
    nombre: string;
    padre: Asignatura;
    hijos: Asignatura[] = [];
}
