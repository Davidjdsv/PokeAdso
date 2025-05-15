package Controladores;

import Modelos.Pokemon;
import Persistencia.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PokemonDao {
    private ConexionDB conexionDb = new ConexionDB();

    // Método para agregar un nuevo cliente a la base de datos
    public void agregar(Pokemon pokemon) {
        Connection con = conexionDb.getConnection();
        String query = "INSERT INTO pokemon (nombre, tipo, peso, altura, descripcion, ataque, defensa, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pokemon.getNombre());
            pst.setString(2, pokemon.getTipo());
            pst.setInt(3, pokemon.getPeso());
            pst.setInt(4, pokemon.getAltura());
            pst.setString(5, pokemon.getDescripcion());
            pst.setInt(6, pokemon.getAtaque());
            pst.setInt(7, pokemon.getDefensa());
            pst.setString(8, pokemon.getFoto());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pokémon agregado con éxito a la Pokedex!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al capturar el Pokémon (!)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un cliente de la base de datos por su ID
    public void eliminar(int id_pokedex) {
        Connection con = conexionDb.getConnection();
        String query = "DELETE FROM pokemon WHERE id_pokedex = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_pokedex);

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pokémon eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al liberar el Pokémon (!)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar los datos de un cliente existente en la base de datos
    public void actualizar(Pokemon pokemon) {
        Connection con = conexionDb.getConnection();
        String query = "UPDATE pokemon SET nombre = ?, tipo = ?, peso = ?, altura = ?, descripcion = ?, ataque = ?, defensa = ?, foto = ? WHERE id_pokedex = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pokemon.getNombre());
            pst.setString(2, pokemon.getTipo());
            pst.setInt(3, pokemon.getPeso());
            pst.setInt(4, pokemon.getAltura());
            pst.setString(5, pokemon.getDescripcion());
            pst.setInt(6, pokemon.getAtaque());
            pst.setInt(7, pokemon.getDefensa());
            pst.setString(8, pokemon.getFoto());
            pst.setInt(9, pokemon.getId_pokedex());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Pokémon actualizado con éxito!");
            } else {
                JOptionPane.showMessageDialog(null, "Error, no se pudo actualizar los datos del Pokémon (!)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
