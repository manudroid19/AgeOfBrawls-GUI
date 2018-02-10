/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.z.excepciones.Recursos.EscasezRecursos;

/**
 *
 * @author Santiago
 */
public final class EscasezRecursosCreacion extends ExcepcionEscasezRecursos {

    private int necesitas;

    public EscasezRecursosCreacion(String mensaje) {
        super(mensaje);
    }

    public EscasezRecursosCreacion(String mensaje, int necesitas) {
        super(mensaje);
        this.necesitas = necesitas;
    }
    public int getNecesitas(){
        return necesitas;
    }

}
