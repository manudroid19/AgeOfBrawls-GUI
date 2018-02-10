/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.contenedor;

import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;

/**
 *
 * @author mprad
 */
public class Contenedor {

    private Recurso recurso;
    private String nombre;

    public Contenedor(Recurso recurso) throws ExcepcionCorrespondenciaRecursos {
        this.recurso = recurso;
        checkTipoRecurso();
    }

    public Contenedor() {
        recurso = null;
    }

    public Contenedor(Contenedor contenedor) throws ExcepcionArgumentosInternos {
        if (contenedor == null) {
            throw new ExcepcionArgumentosInternos("El contenedor no puede ser nulo");
        }
        nombre = contenedor.nombre;
        if (contenedor.recurso == null) {
            recurso = null;
        } else {
            recurso = contenedor.recurso.clonar();
        }
    }

    protected void checkTipoRecurso() throws ExcepcionCorrespondenciaRecursos {

    }

    public String getNombre() {
        if (nombre == null) {
            return "";
        }
        return nombre;
    }

    public Recurso getRecurso() {
        if (recurso != null) {
            return recurso;
        } else {
            return null;
        }
    }

    public void setNombre(String nombre) {
        if (nombre != null && !"".equals(nombre)) {
            this.nombre = nombre;
        }
    }

    public void setRecurso(Recurso recurso) throws ExcepcionCorrespondenciaRecursos {
        if (recurso != null) {
            this.recurso = recurso;
            checkTipoRecurso();
        }
    }

    public void describirContenedorRecurso() {
        System.out.println("Contenedor de recurso");
    }

    public String getDescribir() {
        return "Contenedor de recurso: ";
    }

    public Recurso procesar() throws ExcepcionArgumentosInternos {
        return recurso.clonar();
    }

    public boolean esTransitable() {
        return true;
    }

    @Override
    public String toString() {
        return "contenedorRecurso";
    }
}
