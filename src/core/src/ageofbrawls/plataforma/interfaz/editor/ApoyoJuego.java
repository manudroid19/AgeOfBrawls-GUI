/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma.interfaz.editor;

import ageofbrawls.contenido.Personajes.Grupo;
import ageofbrawls.contenido.Personajes.Paisano;
import ageofbrawls.contenido.Personajes.Personaje;
import ageofbrawls.contenido.Personajes.Soldado;
import ageofbrawls.contenido.Personajes.Soldados.Caballero;
import ageofbrawls.contenido.edificio.Ciudadela;
import ageofbrawls.contenido.edificio.Cuartel;
import ageofbrawls.plataforma.Celda;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Comando;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.plataforma.interfaz.editor.paneles.EdificioPanel;
import ageofbrawls.plataforma.interfaz.editor.paneles.PersonajePanel;
import ageofbrawls.plataforma.interfaz.editor.paneles.RecursoPanel;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaEdificio;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaGrupo;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosConstruccion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEscasezRecursos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionNadaQueRecolectar;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteArchivo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteCivilizacion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteEdificio;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteGrupo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteSujeto;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author mprad
 */
public class ApoyoJuego extends javax.swing.JFrame {

    private Comando juego;
    private boolean buscando, moviendo;
    private long empezadoAMover;
    private int destinoX, destinoY;
    private Personaje movedor;
    private final static int ATACAR = 1;
    private final static int RECOLECTAR = 0;
    private Posicion posAnt, posAnt2;
    private final static int DEFENDER = 2;

    /**
     * Creates new form ApoyoJuego
     */
    public ApoyoJuego(Comando juego) {
        this.buscando = false;
        this.juego = juego;
        initComponents();
        for (Civilizacion civ : juego.getMapa().getCivilizaciones().values()) {
            civilizaciones.addItem(civ.getNombre());
        }
        civilizaciones.setSelectedItem(juego.getActiva());
        Juego.CONSOLA.setSalida(salida);
    }

    public void setBuscando(boolean bus) {
        this.buscando = bus;
    }

    public void seleccionado(int posX, int posY) {
        int fila = (-posY + juego.getMapa().getFilas()-1);
        int col = posX;
        posicionSel.setText(fila + " , " + col);
        Celda celda = juego.getMapa().getCelda(col, fila);
        CardLayout cards = (CardLayout) seleccionado.getLayout();

        if (isBuscando()) {
            destinoX = col;
            destinoY = fila;
            setMoviendo(true);
            setBuscando(false);
            setEmpezadoAMover(System.currentTimeMillis());
            PersonajePanel panel = new PersonajePanel(this.movedor, this);
            panel.setVisible(true);
            cambiarCard(cards,seleccionado, panel);
        } else {
            if (!celda.isOculto(getActiva())) {
                Personaje p = null;
                if (!celda.getPersonajes().isEmpty()) {
                    p = celda.getPersonajes().get(0);
                }
                if (!celda.getGrupos().isEmpty()) {
                    p = celda.getGrupos().get(0);
                }
                if (p != null && p.getCivilizacion() == getActiva()) {
                    PersonajePanel panel = new PersonajePanel(p, this);
                    panel.setVisible(true);
                    cambiarCard(cards,seleccionado, panel);
                    setBuscando(true);
                    setMovedor(p);

                } else if (celda.getContenedorRec().getRecurso() != null) {
                    RecursoPanel panel = new RecursoPanel(celda.getContenedorRec(), this);
                    panel.setVisible(true);
                    cambiarCard(cards,seleccionado, panel);
                } else if (celda.getEdificio() != null) {
                    EdificioPanel panel = new EdificioPanel(celda.getEdificio(), this);
                    panel.setVisible(true);
                    cambiarCard(cards,seleccionado, panel);
                } else {
                    cambiarCard(cards,seleccionado, nada);
                }
            } else {
                cambiarCard(cards,seleccionado, nada);
            }
        }
        seleccionado.updateUI();

    }

    private void cambiarCard(CardLayout cards,JPanel seleccionado, JPanel nada) {
        seleccionado.removeAll();
        seleccionado.add(nada, "1");
        cards.show(seleccionado, "1");
        this.validate();
    }

    public boolean moverStep() {
        if (movedor instanceof Caballero && posAnt != null && !posAnt.equals(posAnt2)) {
            setMoviendo(false);
            posAnt = null;
            return true;
        }
        if (this.movedor.getPosicion().getY() < this.destinoY) {
            try {
                this.movedor.mover("sur");
                renderPos();
                return true;
            } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida ex) {
                System.out.println(ex.getMensaje());
            }
        }
        if (this.movedor.getPosicion().getY() > this.destinoY) {
            try {
                this.movedor.mover("norte");
                renderPos();
                return true;
            } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida ex) {
                System.out.println(ex.getMensaje());
            }
        }
        if (this.movedor.getPosicion().getX() < this.destinoX) {
            try {
                this.movedor.mover("este");
                renderPos();
                return true;
            } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida ex) {
                System.out.println(ex.getMensaje());
                renderPos();
            }
        }
        if (this.movedor.getPosicion().getX() > this.destinoX) {
            try {
                this.movedor.mover("oeste");
                renderPos();
                return true;
            } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida ex) {
                System.out.println(ex.getMensaje());
            }
        }
        setMoviendo(false);
        return false;

    }

    public void renderPos() {
        posicionSel.setText(movedor.getPosicion().getY() + " , " + movedor.getPosicion().getX());
        posAnt2 = posAnt;
        posAnt = movedor.getPosicion();
    }

    public void setCanvas(Canvas canvas) {
        prev.add(canvas, BorderLayout.CENTER);
    }

    public boolean isBuscando() {
        return buscando;
    }

    public boolean isMoviendo() {
        return moviendo;
    }

    public void setMoviendo(boolean moviendo) {
        this.moviendo = moviendo;
    }

    public long getEmpezadoAMover() {
        return empezadoAMover;
    }

    public void setEmpezadoAMover(long empezadoAMover) {
        this.empezadoAMover = empezadoAMover;
    }

    public Personaje getMovedor() {
        return movedor;
    }

    public void setMovedor(Personaje movedor) {
        this.movedor = movedor;
    }

    public void showPopUp(int screenX, int screenY, int posX, int posY) {
        menuAccion.removeAll();
        int fila = (-posY + juego.getMapa().getFilas()-1);
        int col = posX;
        Celda c = juego.getMapa().getCelda(col, fila);
        if (c.getContenedorRec().getRecurso() != null) {
            showRecolectar(menuAccion, c);
        } else if (c.getEdificio() != null) {
            if (c.getEdificio().getCivilizacion().equals(getActiva())) {
                showDefender(menuAccion, c);
                if (c.getEdificio() instanceof Ciudadela || c.getEdificio() instanceof Cuartel) {
                    showCrear(menuAccion, c);
                }
                if (c.getEdificio() instanceof Ciudadela) {
                    showAlmacenar(menuAccion, c);
                }
            } else {
                showAtacar(menuAccion, c);
            }
        } else if (!c.getPersonajes().isEmpty()) {
            for (Personaje p : c.getPersonajes()) {
                JMenu pers = new JMenu(p.getNombre());
                menuAccion.add(pers);
                if (p.getCivilizacion().equals(getActiva())) {
                    if (p instanceof Paisano) {
                        showConstruir(pers, p);
                    }
                } else {
                    showAtacar(menuAccion, c);
                }
            }
            if (c.getPersonajes().size() > 1) {
                showAgrupar(menuAccion, c);
            }
        }
        if (!c.getGrupos().isEmpty()) {
            for (Grupo p : c.getGrupos()) {
                JMenu pers = new JMenu(p.getNombre());
                menuAccion.add(pers);
                if (p.getCivilizacion().equals(getActiva())) {
                    if (!p.getHaySoldado()) {
                        showConstruir(pers, (Personaje) p);
                    }
                    showDesagrupar(pers, p);
                } else {
                    showAtacar(menuAccion, c);
                }
            }

        }

        menuAccion.show(this, screenX + this.prev.getX(), screenY + this.prev.getY());
    }

    private void construirActionPerformed(ActionEvent evt, String quien, String tipo, String dir) {
        try {
            juego.construir(quien, tipo, dir);
        } catch (ExcepcionArgumentosInternos | ExcepcionNoExisteSujeto | ExcepcionAccionRestringidaPersonaje | ExcepcionDireccionNoValida | EscasezRecursosConstruccion | ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos ex) {
            Juego.CONSOLA.imprimir(ex.getMensaje());
        }
    }

    private void personajeItemActionPerformed(java.awt.event.ActionEvent evt, JMenuItem personaje, Celda celda, int modo) {
        Personaje pers = getActiva().getPersonajes().get(personaje.getText());
        if (pers == null) {
            pers = getActiva().getGrupos().get(personaje.getText());
        }
        if (modo == ATACAR) {
            try {
                juego.atacar(personaje.getText(), direccion(pers.getPosicion(), celda.getPosicion().inversa()));
            } catch (ExcepcionNadaQueRecolectar | ExcepcionNoExisteSujeto | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionDireccionNoValida | ExcepcionAccionRestringidaGrupo ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            }
        } else if (modo == DEFENDER) {
            try {
                juego.defender(personaje.getText(), direccion(pers.getPosicion(), celda.getPosicion().inversa()));
            } catch (ExcepcionNadaQueRecolectar | ExcepcionNoExisteSujeto | ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionEspacioInsuficiente ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            }
            return;
        }
        if (celda.getContenedorRec().getRecurso() != null) {
            try {
                juego.recolectar(personaje.getText(), direccion(pers.getPosicion(), celda.getPosicion().inversa()));
            } catch (ExcepcionNoExisteSujeto | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida | ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos | ExcepcionNadaQueRecolectar | ExcepcionAccionRestringidaPersonaje ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            }
        } else if (celda.getEdificio() != null && celda.getEdificio() instanceof Ciudadela) {
            try {
                juego.almacenar(personaje.getText(), direccion(pers.getPosicion(), celda.getPosicion().inversa()));
            } catch (ExcepcionNadaQueRecolectar ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            } catch (ExcepcionNoExisteSujeto | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosValoresIncorrectos | ExcepcionAccionRestringidaGrupo | ExcepcionEscasezRecursos ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            }

        }
    }

    private void crearActionPerformed(java.awt.event.ActionEvent evt, JMenuItem personaje, Celda celda) {
        try {
            juego.crear(celda.getEdificio().getNombre(), personaje.getText());
        } catch (ExcepcionNoExisteEdificio | ExcepcionArgumentosValoresIncorrectos | ExcepcionAccionRestringidaEdificio | ExcepcionEspacioInsuficiente | EscasezRecursosCreacion | ExcepcionNoExistePosicion | ExcepcionArgumentosInternos | ExcepcionNoExisteMapa ex) {
            Juego.CONSOLA.imprimir(ex.getMensaje());
        }

    }

    private String direccion(Posicion pos1, Posicion posicion) throws ExcepcionNadaQueRecolectar {
        if (pos1.getY() == posicion.getY()) {
            if (pos1.getX() == 1 + posicion.getX()) {
                return "oeste";
            } else if (pos1.getX() + 1 == posicion.getX()) {
                return "este";
            }
        } else if (pos1.getX() == posicion.getX()) {
            if (pos1.getY() == 1 + posicion.getY()) {
                return "norte";
            } else if (pos1.getY() + 1 == posicion.getY()) {
                return "sur";
            }
        }
        throw new ExcepcionNadaQueRecolectar("La acción debe realizarse en celdas adyacentes");
    }

    private void showRecolectar(JPopupMenu menuAccion, Celda c) {
        recolectar.setText("Recolectar");
        menuAccion.add(recolectar);
        recolectar.removeAll();
        ArrayList<Personaje> entes = new ArrayList<>(getActiva().getPersonajes().values());
        for (Grupo g : getActiva().getGrupos().values()) {
            entes.add((Personaje) g);
        }
        for (Personaje p : entes) {
            if ((p instanceof Paisano && p.getGrupo() == null) || p instanceof Grupo) {
                JMenuItem item = new JMenuItem(p.getNombre());
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        personajeItemActionPerformed(evt, item, c, RECOLECTAR);
                    }
                });
                recolectar.add(item);
            }
        }
    }

    private void showCrear(JPopupMenu menuAccion, Celda c) {
        crear.setText("Crear");
        menuAccion.add(crear);
        crear.removeAll();
        ArrayList<String> tipos = new ArrayList<>();
        if (c.getEdificio() instanceof Ciudadela) {
            tipos.add("paisano");
        } else {
            tipos.add("caballero");
            tipos.add("legionario");
            tipos.add("arquero");
        }
        for (String tipo : tipos) {
            JMenuItem item = new JMenuItem(tipo);
            item.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    crearActionPerformed(evt, item, c);
                }
            });
            crear.add(item);
        }
    }

    private Civilizacion getActiva() {
        return juego.getMapa().getCivilizaciones().get(juego.getActiva());
    }

    private void showAlmacenar(JPopupMenu menuAccion, Celda c) {
        almacenar.setText("Almacenar");
        menuAccion.add(almacenar);
        almacenar.removeAll();
        ArrayList<Personaje> entes = new ArrayList<>(getActiva().getPersonajes().values());
        for (Grupo g : getActiva().getGrupos().values()) {
            entes.add((Personaje) g);
        }
        for (Personaje p : entes) {
            JMenuItem item = new JMenuItem(p.getNombre());
            item.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    personajeItemActionPerformed(evt, item, c, RECOLECTAR);
                }
            });
            almacenar.add(item);
        }
    }

    private void showConstruir(JMenu pers, Personaje p) {
        JMenu construir = new JMenu("Construir");
        pers.add(construir);
        construir.removeAll();
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("Cuartel");
        tipos.add("Casa");
        tipos.add("Ciudadela");
        for (String tipo : tipos) {
            JMenu cons = new JMenu(tipo);
            ArrayList<String> dirs = new ArrayList<>();
            dirs.add("Norte");
            dirs.add("Sur");
            dirs.add("Este");
            dirs.add("Oeste");
            for (String dir : dirs) {
                JMenuItem item = new JMenuItem(dir);
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        construirActionPerformed(evt, p.getNombre(), tipo.toLowerCase(), dir);
                    }
                });
                cons.add(item);
            }
            construir.add(cons);
        }
    }

    private void showAgrupar(JPopupMenu menuAccion, Celda c) {
        JMenuItem agrupar = new JMenuItem("Agrupar");
        agrupar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    juego.agrupar(c.getPosicion().inversa().toStringMapa());
                } catch (ExcepcionNoExistePosicion | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje ex) {
                    Juego.CONSOLA.imprimir(ex.getMensaje());
                }
            }
        });
        menuAccion.add(agrupar);
    }

    private void showDesagrupar(JMenu pers, Grupo p) {
        JMenuItem desagrupar = new JMenuItem("Desagrupar");
        desagrupar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    juego.desagrupar(p.getNombre());
                } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionNoExisteGrupo ex) {
                    Juego.CONSOLA.imprimir(ex.getMensaje());
                }

            }
        });
        menuAccion.add(desagrupar);
    }

    private void showAtacar(JPopupMenu menuAccion, Celda c) {
        atacar.setText("Atacar");
        menuAccion.add(atacar);
        atacar.removeAll();
        ArrayList<Personaje> entes = new ArrayList<>();
        for (Personaje p : getActiva().getPersonajes().values()) {
            if (Soldado.class.isAssignableFrom(p.getClass())) {
                entes.add(p);
            }
        }
        for (Grupo g : getActiva().getGrupos().values()) {
            if (g.getHaySoldado()) {
                entes.add((Personaje) g);
            }
        }
        for (Personaje p : entes) {
            if ((Soldado.class.isAssignableFrom(p.getClass()) && p.getGrupo() == null) || p instanceof Grupo) {
                JMenuItem item = new JMenuItem(p.getNombre());
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        personajeItemActionPerformed(evt, item, c, ATACAR);
                    }
                });
                atacar.add(item);
            }
        }
    }

    private void showDefender(JPopupMenu menuAccion, Celda c) {
        defender.setText("Defender");
        menuAccion.add(defender);
        defender.removeAll();
        ArrayList<Personaje> entes = new ArrayList<>();
        for (Personaje p : getActiva().getPersonajes().values()) {
            entes.add(p);
        }
        for (Grupo g : getActiva().getGrupos().values()) {
            entes.add((Personaje) g);
        }
        for (Personaje p : entes) {
            if ((p.getGrupo() == null) || p instanceof Grupo) {
                JMenuItem item = new JMenuItem(p.getNombre());
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        personajeItemActionPerformed(evt, item, c, DEFENDER);
                    }
                });
                defender.add(item);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nada = new javax.swing.JPanel();
        menuAccion = new javax.swing.JPopupMenu();
        recolectar = new javax.swing.JMenu();
        almacenar = new javax.swing.JMenu();
        crear = new javax.swing.JMenu();
        atacar = new javax.swing.JMenu();
        defender = new javax.swing.JMenu();
        titSel = new javax.swing.JLabel();
        posicionSel = new javax.swing.JLabel();
        seleccionado = new javax.swing.JPanel();
        prev = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        civilizaciones = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        salida = new javax.swing.JLabel();
        guardarBoton = new javax.swing.JButton();
        cargarBoton = new javax.swing.JButton();

        javax.swing.GroupLayout nadaLayout = new javax.swing.GroupLayout(nada);
        nada.setLayout(nadaLayout);
        nadaLayout.setHorizontalGroup(
            nadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );
        nadaLayout.setVerticalGroup(
            nadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 402, Short.MAX_VALUE)
        );

        menuAccion.setFont(menuAccion.getFont().deriveFont(menuAccion.getFont().getSize()+6f));

        recolectar.setText("jMenu1");

        almacenar.setText("jMenu1");

        crear.setText("jMenu1");

        atacar.setText("jMenu1");

        defender.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Age of Brawls");
        setResizable(false);

        titSel.setFont(titSel.getFont().deriveFont(titSel.getFont().getSize()+4f));
        titSel.setText("Seleccionado:");

        posicionSel.setFont(posicionSel.getFont().deriveFont(posicionSel.getFont().getSize()+4f));
        posicionSel.setText("Nada");

        seleccionado.setLayout(new java.awt.CardLayout());

        prev.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Elemento seleccionado");

        civilizaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                civilizacionesActionPerformed(evt);
            }
        });

        jLabel3.setText("Civilizacion:");

        salida.setFont(salida.getFont().deriveFont(salida.getFont().getStyle() | java.awt.Font.BOLD, salida.getFont().getSize()+7));
        salida.setForeground(new java.awt.Color(255, 0, 51));
        salida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        guardarBoton.setText("Guardar");
        guardarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBotonActionPerformed(evt);
            }
        });

        cargarBoton.setText("Cargar");
        cargarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarBotonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(salida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titSel, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(posicionSel))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(seleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(guardarBoton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cargarBoton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(civilizaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(prev, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titSel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(posicionSel))
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(seleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(civilizaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardarBoton)
                            .addComponent(cargarBoton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(salida, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void civilizacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_civilizacionesActionPerformed
        try {
            juego.cambiar((String) civilizaciones.getSelectedItem());
        } catch (ExcepcionNoExisteCivilizacion ex) {
            //nunca pasa
        }
    }//GEN-LAST:event_civilizacionesActionPerformed

    private void guardarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBotonActionPerformed
        JFileChooser dir = new JFileChooser();
        dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dir.setDialogTitle("Seleccionar directorio");

        if (dir.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                juego.guardar(dir.getSelectedFile().getAbsolutePath());
            } catch (ExcepcionNoExisteArchivo ex) {
                Logger.getLogger(Disenador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_guardarBotonActionPerformed

    private void cargarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarBotonActionPerformed
        JFileChooser mapa = new JFileChooser();
        mapa.setDialogTitle("Selecciona el mapa");
        mapa.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
        mapa.setAcceptAllFileFilterUsed(false);
        if (mapa.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            JFileChooser personajes = new JFileChooser();
            personajes.setDialogTitle("Selecciona el archivo de personajes");
            personajes.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
            personajes.setAcceptAllFileFilterUsed(false);
            personajes.setCurrentDirectory(mapa.getCurrentDirectory());
            if (personajes.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                JFileChooser edificios = new JFileChooser();
                edificios.setDialogTitle("Selecciona el archivo de edificios");
                edificios.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                edificios.setAcceptAllFileFilterUsed(false);
                edificios.setCurrentDirectory(personajes.getCurrentDirectory());
                if (edificios.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    juego.cargarSeparado(mapa.getSelectedFile(), edificios.getSelectedFile(), personajes.getSelectedFile());
                }
            }
        }
    }//GEN-LAST:event_cargarBotonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu almacenar;
    private javax.swing.JMenu atacar;
    private javax.swing.JButton cargarBoton;
    private javax.swing.JComboBox<String> civilizaciones;
    private javax.swing.JMenu crear;
    private javax.swing.JMenu defender;
    private javax.swing.JButton guardarBoton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu menuAccion;
    private javax.swing.JPanel nada;
    private javax.swing.JLabel posicionSel;
    private javax.swing.JPanel prev;
    private javax.swing.JMenu recolectar;
    private javax.swing.JLabel salida;
    private javax.swing.JPanel seleccionado;
    private javax.swing.JLabel titSel;
    // End of variables declaration//GEN-END:variables

}
