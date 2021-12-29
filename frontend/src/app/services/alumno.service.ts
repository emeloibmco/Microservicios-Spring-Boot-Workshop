import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Alumno } from '../models/alumno';

@Injectable({
  providedIn: 'root'
})
export class AlumnoService {

  private baseEndpoint = 'http://servicio-usuarios-microservicios-springboot-mysql.openshift-demo-us-south-ea9753cca330b7f05a99ad5b2c8b5da1-0000.us-south.containers.appdomain.cloud';
  private cabeceras: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  constructor(private http: HttpClient) { }

  public listar(): Observable<Alumno[]> {
    return this.http.get<Alumno[]>(this.baseEndpoint);
  }

  public listarPaginas(page: string, size: string): Observable<any>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size);
    return this.http.get<any>(`${this.baseEndpoint}/pagina`, {params: params});
  }

  public ver(id: number): Observable<Alumno>{
    return this.http.get<Alumno>(`${this.baseEndpoint}/${id}`);
  }

  public crear(alumno:Alumno): Observable<Alumno>{
    return this.http.post<Alumno>(this.baseEndpoint, alumno, { headers: this.cabeceras});
  }

  public editar(alumno:Alumno): Observable<Alumno>{
    return this.http.put<Alumno>(`${this.baseEndpoint}/${alumno.id}`, { headers: this.cabeceras});
  }

  public eliminar(id: number): Observable<void>{
    return this.http.delete<void>(`${this.baseEndpoint}/${id}`);
  }

 
}
