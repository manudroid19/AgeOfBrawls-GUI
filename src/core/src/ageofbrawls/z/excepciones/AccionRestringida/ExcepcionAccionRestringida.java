/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.z.excepciones.AccionRestringida;

import ageofbrawls.z.excepciones.ExcepcionJuego;

/**
 *
 * @author Santiago
 */
public abstract class ExcepcionAccionRestringida extends ExcepcionJuego {
    
    public ExcepcionAccionRestringida(String mensaje) {
        super(mensaje);
    }
    
}
