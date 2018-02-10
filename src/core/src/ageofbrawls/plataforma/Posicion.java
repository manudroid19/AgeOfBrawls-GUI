/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 *
 * @author mprad
 */
public class Posicion {

    public static final int ESTE = 0;
    public static final int NORTE = 1;
    public static final int OESTE = 2;
    public static final int SUR = 3;
    public static final int NORESTE = 4;
    public static final int SURESTE = 5;
    private int x;
    private int y;

    public Posicion(int x, int y) {
        if (x > -1 && y > -1) {
            this.x = x;
            this.y = y;
        } else {
            this.x = -1;
            this.y = -1;
        }
    }

    public Posicion(String pos) {
        Pattern pat = Pattern.compile("\\((\\d+?,\\d+?)\\)", Pattern.DOTALL);
        if (!pat.matcher(pos).matches()) {
            this.x = -1;
            this.y = -1;
        } else {
            int y = Integer.parseInt(pos.substring(1, pos.indexOf(",")));
            int x = Integer.parseInt(pos.substring(pos.indexOf(",") + 1, pos.length() - 1));
            if (x > -1 && y > -1) {
                this.x = x;
                this.y = y;
            } else {
                this.x = -1;
                this.y = -1;
            }
        }
    }

    public Posicion(Posicion posicion) {
        this(posicion.getX(), posicion.getY());
    }

    public Posicion() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Posicion getAdy(int pos) throws ExcepcionDireccionNoValida {
        switch (pos) {
            case Posicion.ESTE:
                return new Posicion(x + 1, y);
            case Posicion.OESTE:
                return new Posicion(x - 1, y);
            case Posicion.NORTE:
                return new Posicion(x, y - 1);
            case Posicion.SUR:
                return new Posicion(x, y + 1);
            case Posicion.NORESTE:
                return new Posicion(x + 1, y - 1);
            case Posicion.SURESTE:
                return new Posicion(x + 1, y + 1);
            default:
                throw new ExcepcionDireccionNoValida("La dirección introducida no es válida");
                
        }
    }

    public Posicion getAdy(String direccion) throws ExcepcionDireccionNoValida {
        if (direccion == null) {
            throw new ExcepcionDireccionNoValida("La dirección introducida no es válida");
            
        }
        switch (direccion.toLowerCase()) {
            case "norte":
                return getAdy(Posicion.NORTE);
            case "sur":
                return getAdy(Posicion.SUR);
            case "este":
                return getAdy(Posicion.ESTE);
            case "oeste":
                return getAdy(Posicion.OESTE);
            default:
                throw new ExcepcionDireccionNoValida("La dirección introducida no es válida");
                
        }
    }
    
    public Posicion inversa() {
        return new Posicion(y,x);
    }
    
    public Posicion posicionAdyacenteLibre(Mapa mapa) throws ExcepcionNoExisteMapa {
        if (mapa == null) {
            throw new ExcepcionNoExisteMapa("El mapa no existe");
            
        }
        int i = x;
        int j = y;
        ArrayList<Posicion> candidatos = new ArrayList<>();
        for (int h = i - 1; h < i + 2; h++) {
            for (int k = j - 1; k < j + 2; k++) {
                Posicion pos = new Posicion(h, k);
                if (mapa.getCelda(pos) != null && mapa.getCelda(pos).esCeldaLibre(true) && mapa.perteneceAMapa(pos)) {
                    candidatos.add(pos);
                }
            }
        }
        if (candidatos.isEmpty()) {
            for (int h = i - 1; h < i + 2; h++) {
                for (int k = j - 1; k < j + 2; k++) {
                    Posicion pos = new Posicion(h, k);
                    if(mapa.getCelda(pos)==null){return this;}
                    if (mapa.getCelda(pos).esCeldaLibre(false) && mapa.perteneceAMapa(pos)) {
                        return pos;
                    }
                }
            }
        }
        Collections.shuffle(candidatos);
        return candidatos.get(0);
    }
    
    public String toStringMapa() {
        return  "(" + y + "," + x+")";
    }

    @Override
    public String toString() {
        return "posicion: " + "fila " + y + ", columna " + x;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.x;
        hash = 89 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posicion other = (Posicion) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
}
