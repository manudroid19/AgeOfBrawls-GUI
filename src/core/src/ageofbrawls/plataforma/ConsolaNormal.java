/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Santiago
 */
public class ConsolaNormal implements Consola {

    private Scanner sca;
    private JLabel salida;
    private Queue<String> cola;
    private long tiempo;
    private Timer t;

    public ConsolaNormal() {
        this.cola = new LinkedList<>();
        sca = new Scanner(System.in);
        t = new Timer(50, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() > tiempo + 3000) {
                    if (cola.isEmpty() && salida != null) {
                        salida.setText(null);
                    } else if( salida != null){
                        salida.setText(cola.poll());
                        tiempo = System.currentTimeMillis();
                    }
                }
            }
        });
        t.setRepeats(true);
        t.start();
    }

    @Override
    public void imprimir(String mensaje) {
        if (salida == null) {
            System.out.println(mensaje);
        } else {
            if (tiempo == 0 || salida.getText() == null) {
                tiempo = System.currentTimeMillis();
            }
            if (salida.getText() == null) {
                salida.setText(mensaje);
            } else {
                cola.add(mensaje);
            }

        }
    }

    @Override
    public void imprimir() {
        System.out.println();
    }

    @Override
    public String leer(String descripcion) {
        System.out.println(descripcion);
        return sca.nextLine();
    }

    @Override
    public void imprimirEnLinea(String mensaje) {
        System.out.print(mensaje);
    }

    @Override
    public String leerEnLinea(String descripcion) {
        System.out.print(descripcion);
        return sca.nextLine();
    }

    @Override
    public String leer() {
        return sca.nextLine();
    }

    @Override
    public void setSalida(JLabel j) {
        salida = j;
    }

}
