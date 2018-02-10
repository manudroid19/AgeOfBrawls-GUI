/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.z.excepciones.Movimiento;

import ageofbrawls.z.excepciones.ExcepcionJuego;

/**
 *
 * @author Santiago
 */
public abstract class ExcepcionMovimiento extends ExcepcionJuego {
    
    public ExcepcionMovimiento(String mensaje) {
        super(mensaje);
    }
    
}
