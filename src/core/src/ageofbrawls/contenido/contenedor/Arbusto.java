/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.contenedor;

import ageofbrawls.contenido.Recursos.Comida;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;

/**
 *
 * @author Santiago
 */
public final class Arbusto extends Contenedor {

    public Arbusto(Recurso recurso) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
    }

    public Arbusto(Recurso recurso, String nombre) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
        super.setNombre(nombre);
    }

    @Override
    public void describirContenedorRecurso() {
        super.describirContenedorRecurso();
        System.out.println("Cantidad de comida: " + this.getRecurso().getCantidad());
    }

    @Override
    public String getDescribir() {
        return super.getDescribir() + "arbusto\r\n"
                + "Cantidad de comida: " + this.getRecurso().getCantidad();
    }

    @Override
    protected void checkTipoRecurso() throws ExcepcionCorrespondenciaRecursos {
        if (!(super.getRecurso() instanceof Comida)) {
            throw new ExcepcionCorrespondenciaRecursos("No corresponde el contenedor " + toString() + " con el recurso insertado");
        }
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public String toString() {
        return "arbusto";
    }

}
