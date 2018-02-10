/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.contenedor;

import ageofbrawls.contenido.Recursos.Madera;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;

/**
 *
 * @author Santiago
 */
public final class Bosque extends Contenedor {

    public Bosque(Recurso recurso) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
    }

    public Bosque(Recurso recurso, String nombre) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
        super.setNombre(nombre);
    }

    @Override
    public void describirContenedorRecurso() {
        super.describirContenedorRecurso();
        System.out.println("Cantidad de madera: " + this.getRecurso().getCantidad());
    }

    @Override
    public String getDescribir() {
        return super.getDescribir() + "bosque\r\n"
                + "Cantidad de madera: " + this.getRecurso().getCantidad();
    }

    @Override
    protected void checkTipoRecurso() throws ExcepcionCorrespondenciaRecursos {
        if (!(super.getRecurso() instanceof Madera)) {
            throw new ExcepcionCorrespondenciaRecursos("No corresponde el contenedor " + toString() + " con el recurso insertado");
        }
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public String toString() {
        return "bosque";
    }

}
