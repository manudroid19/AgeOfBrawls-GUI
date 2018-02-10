/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.z.excepciones.Recursos.EscasezRecursos;

/**
 *
 * @author mprad
 */
public final class ExcepcionEspacioInsuficiente extends ExcepcionEscasezRecursos {

    private int creados;

    public ExcepcionEspacioInsuficiente(String mensaje) {
        super(mensaje);
    }

    public ExcepcionEspacioInsuficiente(String mensaje, int creados) {
        super(mensaje);
        this.creados=creados;
    }
    public int getCreados(){
        return creados;
    }

}
