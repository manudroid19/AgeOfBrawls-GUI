/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.z.excepciones;

/**
 *
 * @author mprad
 */
public abstract class ExcepcionJuego extends Exception {

    private final String mensaje;

    public ExcepcionJuego(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
