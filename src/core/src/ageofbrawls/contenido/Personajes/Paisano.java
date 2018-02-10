/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.Personajes;

import ageofbrawls.contenido.contenedor.Contenedor;
import ageofbrawls.contenido.Recursos.Comida;
import ageofbrawls.contenido.Recursos.Madera;
import ageofbrawls.contenido.Recursos.Piedra;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.contenido.contenedor.Arbusto;
import ageofbrawls.contenido.contenedor.Bosque;
import ageofbrawls.contenido.contenedor.Cantera;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Mapa;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosConstruccion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEscasezRecursos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionNadaQueRecolectar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mprad
 */
public final class Paisano extends Personaje {

    int capRec, cantRecMadera, cantRecPiedra, cantRecComida;

    public Paisano(Posicion posicion, String nombre, Civilizacion civilizacion) {
        super(posicion, nombre, civilizacion, 100, 50);
        capRec = 50;
    }

    public void setCapRec(int capRec) {
        this.capRec = capRec;
    }

    public void setCantRec(Class<? extends Recurso> clase, int valor) throws ExcepcionArgumentosValoresIncorrectos {
        if (Comida.class.equals(clase)) {
            setCantRecComida(valor);
        } else if (Madera.class.equals(clase)) {
            setCantRecMadera(valor);
        } else if (Piedra.class.equals(clase)) {
            setCantRecPiedra(valor);
        }
    }

    public void setCantRecMadera(int valor) throws ExcepcionArgumentosValoresIncorrectos {
        if (valor >= 0) {
            cantRecMadera = valor;
        } else {
            throw new ExcepcionArgumentosValoresIncorrectos("La cantidad no puede ser negativa");
        }
    }

    public void setCantRecPiedra(int valor) throws ExcepcionArgumentosValoresIncorrectos {
        if (valor >= 0) {
            cantRecPiedra = valor;
        } else {
            throw new ExcepcionArgumentosValoresIncorrectos("La cantidad no puede ser negativa");
        }
    }

    public void setCantRecComida(int valor) throws ExcepcionArgumentosValoresIncorrectos {
        if (valor >= 0) {
            cantRecComida = valor;
        } else {
            throw new ExcepcionArgumentosValoresIncorrectos("La cantidad no puede ser negativa");
        }
    }

    public int getCapRec() {
        return capRec;
    }

    public int getCantRecMadera() {
        return cantRecMadera;
    }

    public int getCantRecPiedra() {
        return cantRecPiedra;
    }

    public int getCantRecComida() {
        return cantRecComida;
    }

    public int getCantRecTotal() {
        return cantRecComida + cantRecMadera + cantRecPiedra;
    }

    public void describir() {
        super.describir();
        System.out.println("Capacidad de recolección: " + capRec);
        System.out.println("Cantidad de madera que transporta: " + cantRecMadera);
        System.out.println("Cantidad de comida que transporta: " + cantRecComida);
        System.out.println("Cantidad de piedra que transporta: " + cantRecPiedra);
        System.out.println("Cantidad de Recursos que lleva: " + (cantRecMadera + cantRecComida + cantRecPiedra));
    }
    public String getDescribir() {
        return super.getDescribir()+"\r\n"+
        "Capacidad de recolección: " + capRec+"\r\n"+
        "Cantidad de madera que transporta: " + cantRecMadera+"\r\n"+
        "Cantidad de comida que transporta: " + cantRecComida+"\r\n"+
        "Cantidad de piedra que transporta: " + cantRecPiedra+"\r\n"+
        "Cantidad de Recursos que lleva: " + (cantRecMadera + cantRecComida + cantRecPiedra);
    }

    @Override
    public void recolectar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionArgumentosValoresIncorrectos,ExcepcionNadaQueRecolectar, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaPersonaje{
        Mapa mapa = getCivilizacion().getMapa();
        if (mapa == null || direccion == null) {
            throw new ExcepcionDireccionNoValida("Error en recolectar.");
        }
        if (super.getGrupo() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede recolectar por si solo ya que pertenece a un grupo");
        }
        Posicion posicion = getPosicion();
        Civilizacion civilizacion = getCivilizacion();
        Posicion pos = posicion.getAdy(direccion);
        if(mapa.getCelda(pos)==null){
            throw new ExcepcionDireccionNoValida("No puedes recolectar de ahí");
        }
        Contenedor contenedor = mapa.getCelda(pos).getContenedorRec();
        if (pos.equals(posicion)) { //error con la posicion
            throw new ExcepcionArgumentosInternos("La posición no es válida");
        }
        if (civilizacion.getMapa().getCelda(posicion).getEdificio() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje está en un edificio y por tanto no puede recolectar");
            
        }
        if (this.getCantRecTotal() == this.capRec) {
            throw new ExcepcionAccionRestringidaPersonaje(this.getNombre() + " no puede recolectar más");
            
        }
        if (contenedor.getRecurso() == null) {
            throw new ExcepcionNadaQueRecolectar("Error: La celda destino no es un contenedor de recursos.");
        }
        int recolectando = Math.min(getCapRec() - this.getCantRecTotal(), contenedor.procesar().getCantidad());

        contenedor.getRecurso().setCantidad(contenedor.getRecurso().getCantidad() - recolectando);
        if (contenedor.getRecurso().getCantidad() ==0 || contenedor.getRecurso().getCantidad() - recolectando == 0) {
            mapa.getCelda(pos).hacerPradera();
        }
        if (mapa.getCelda(pos).getContenedorRec().getRecurso() == null) { //si se ha vuelto pradera, imprimo
            mapa.imprimir(civilizacion);
        }
        if (contenedor instanceof Bosque) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de madera");
            setCantRecMadera(getCantRecMadera() + recolectando);
        } else if (contenedor instanceof Arbusto) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de comida");
            setCantRecComida(getCantRecComida() + recolectando);
        } else if (contenedor instanceof Cantera) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de piedra");
            setCantRecPiedra(getCantRecPiedra() + recolectando);

        }
    }

    public void almacenar(String direccion) throws ExcepcionDireccionNoValida, ExcepcionArgumentosInternos,ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionArgumentosValoresIncorrectos {
        if (getGrupo() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede almacenar por si solo ya que pertenece a un grupo");
            
        }
        almacenarGenerico(direccion);
    }

    @Override
    protected void vaciarCantRecMadera() throws ExcepcionArgumentosValoresIncorrectos {
        setCantRecMadera(0);
    }

    @Override
    protected void vaciarCantRecComida() throws ExcepcionArgumentosValoresIncorrectos {
        setCantRecComida(0);
    }

    @Override
    protected void vaciarCantRecPiedra() throws ExcepcionArgumentosValoresIncorrectos {
        setCantRecPiedra(0);
    }

    @Override
    public void construir(String tipoC, String dir) throws ExcepcionArgumentosInternos, EscasezRecursosConstruccion, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, ExcepcionArgumentosValoresIncorrectos {
        if (tipoC == null || dir == null) {
            throw new ExcepcionDireccionNoValida("Error en consEdif.");
           
        }
        Grupo grupo = super.getGrupo();
        if (grupo != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede construir por si solo ya que pertenece a un grupo");
            
        }
        construirGenerico(tipoC, dir);
    }

    public void recuperarVida() throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosValoresIncorrectos {
        Civilizacion civilizacion = super.getCivilizacion();
        int puntosARecuperar = 50 - this.getSalud();
        if (puntosARecuperar == 0) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje tiene toda la vida");
            
        }
        int costeAlimento = (int) (puntosARecuperar * 0.8);
        if (civilizacion.getAlimentos() < costeAlimento) {
            int puntosRecuperados = (int) (civilizacion.getAlimentos() / 0.8);
            super.setSalud(puntosRecuperados, true);
            civilizacion.setAlimentos(0, false);
            return;
        }
        civilizacion.setAlimentos(-costeAlimento, true);
        this.recuperar();
        Juego.CONSOLA.imprimir("Coste de la recuperación de la vida: " + costeAlimento + " unidades de alimento de la ciudadela.");
    }

    public void reparar(Posicion pos) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionArgumentosValoresIncorrectos {
        if (getGrupo() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede reparar por si solo ya que pertenece a un grupo");
            
        }
        repararGenerico(pos);
    }

    @Override
    public String toString() {
        return "paisano";
    }

    @Override
    public void recuperar() throws ExcepcionArgumentosInternos {
        super.setSalud(50, false);
    }
}
