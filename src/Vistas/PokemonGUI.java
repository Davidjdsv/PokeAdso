package Vistas;

import Controladores.PokemonDao;
import Modelos.Pokemon;
import Persistencia.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PokemonGUI {
    private JPanel main;
    private JTable tablePokemon;
    private JTextField textFieldIdPokedex;
    private JTextField textFieldNombre;
    private JTextField textFieldPeso;
    private JTextField textFieldAltura;
    private JTextField textFieldDescripcion;
    private JTextField textFieldAtaque;
    private JTextField textFieldDefensa;
    private JTextField textFieldFoto;
    private JButton buttonAgregar;
    private JButton buttonEditar;
    private JButton buttonEliminar;
    private JComboBox comboBoxTipo;
    private JScrollPane scrollPanePokemon;
    int filas = 0;

    PokemonDao pokemonDao = new PokemonDao();

    public PokemonGUI(){
        obtenerDatos(); // Cargar datos al inicio

        // Acción del botón "Agregar"
        buttonAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String tipo = (String) comboBoxTipo.getSelectedItem();
                int peso = Integer.parseInt(textFieldPeso.getText());
                int altura = Integer.parseInt(textFieldAltura.getText());
                String descripcion = textFieldDescripcion.getText();
                int ataque = Integer.parseInt(textFieldAtaque.getText());
                int defensa = Integer.parseInt(textFieldDefensa.getText());
                String foto = textFieldFoto.getText();
                Pokemon pokemon = new Pokemon(0, nombre, tipo, peso, altura, descripcion, ataque, defensa, foto);
                pokemonDao.agregar(pokemon);
                obtenerDatos(); // Refrescar la tabla
                clear(); // Limpiar los campos
            }
        });

        // Acción del botón "Actualizar"
        buttonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String tipo = (String) comboBoxTipo.getSelectedItem();
                int peso = Integer.parseInt(textFieldPeso.getText());
                int altura = Integer.parseInt(textFieldAltura.getText());
                String descripcion = textFieldDescripcion.getText();
                int ataque = Integer.parseInt(textFieldAtaque.getText());
                int defensa = Integer.parseInt(textFieldDefensa.getText());
                String foto = textFieldFoto.getText();
                int id_pokemon = Integer.parseInt(textFieldIdPokedex.getText());

                Pokemon pokemon = new Pokemon(id_pokemon, nombre, tipo, peso, altura, descripcion, ataque, defensa, foto);
                pokemonDao.actualizar(pokemon);
                obtenerDatos(); // Refrescar la tabla
                clear(); // Limpiar los campos
            }
        });

        // Acción del botón "Eliminar"
        buttonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_pokemon = Integer.parseInt(textFieldIdPokedex.getText());
                pokemonDao.eliminar(id_pokemon);
                obtenerDatos(); // Refrescar la tabla
                clear(); // Limpiar los campos
            }
        });

        // Evento para seleccionar una fila en la tabla
        tablePokemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = tablePokemon.getSelectedRow();

                if (selectFila >= 0) {
                    textFieldIdPokedex.setText((String) tablePokemon.getValueAt(selectFila, 0));
                    textFieldNombre.setText((String) tablePokemon.getValueAt(selectFila, 1));
                    comboBoxTipo.setSelectedItem((String) tablePokemon.getValueAt(selectFila, 2));
                    textFieldPeso.setText(String.valueOf(tablePokemon.getValueAt(selectFila, 3)));
                    textFieldAltura.setText(String.valueOf(tablePokemon.getValueAt(selectFila, 4)));
                    textFieldDescripcion.setText((String) tablePokemon.getValueAt(selectFila, 5));
                    textFieldAtaque.setText(String.valueOf(tablePokemon.getValueAt(selectFila, 6)));
                    textFieldDefensa.setText(String.valueOf(tablePokemon.getValueAt(selectFila, 7)));
                    textFieldFoto.setText((String) tablePokemon.getValueAt(selectFila, 8));

                    filas = selectFila; // Guardar índice de la fila seleccionada
                }
            }
        });
    }

    // Método para limpiar los campos de entrada
    public void clear() {
        textFieldNombre.setText("");
        comboBoxTipo.setSelectedItem("");
        textFieldPeso.setText("");
        textFieldAltura.setText("");
        textFieldDescripcion.setText("");
        textFieldAtaque.setText("");
        textFieldDefensa.setText("");
        textFieldFoto.setText("");
    }

    // Instancia de la conexión a la base de datos
    ConexionDB conexionDB = new ConexionDB();

    // Método para obtener datos de la base de datos y mostrarlos en la tabla
    public void obtenerDatos() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("id pokedex");
        model.addColumn("nombre");
        model.addColumn("tipo");
        model.addColumn("peso");
        model.addColumn("altura");
        model.addColumn("descripcion");
        model.addColumn("ataque");
        model.addColumn("defensa");
        model.addColumn("foto");

        tablePokemon.setModel(model);
        String[] dato = new String[9];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM pokemon";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dato[4] = rs.getString(5);
                dato[5] = rs.getString(6);
                dato[6] = rs.getString(7);
                dato[7] = rs.getString(8);
                dato[8] = rs.getString(9);
                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pokedex");
        frame.setContentPane(new PokemonGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
        frame.setResizable(false);
    }
}
