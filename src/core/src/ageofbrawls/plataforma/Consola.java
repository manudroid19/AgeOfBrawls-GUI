/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import javax.swing.JLabel;

/**
 *
 * @author Santiago
 */
public interface Consola {

    public void imprimir(String mensaje);

    public void imprimir();

    public void imprimirEnLinea(String mensaje);

    public String leer(String descripcion);

    public String leer();

    public String leerEnLinea(String descripcion);
    public void setSalida(JLabel j);

}
