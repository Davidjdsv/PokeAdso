package Vistas;

import Controladores.PokemonDao;
import Modelos.Pokemon;
import Persistencia.ConexionDB;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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

    // TODO: Método para generar el PDF
    public void generarPDF() {
        try {
            String nombreArchivo = "Listado pokedex.pdf";
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            //PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();

            // Cargar imagen de fondo desde ruta absoluta
            String imagePath = "src/res/img/plantilla.jpg"; // imagen que subiste
            Image background = Image.getInstance(imagePath);
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            writer.getDirectContentUnder().addImage(background);

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Pokedex", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Pokemon:"));
            document.add(new Paragraph(" "));

            // Crear tabla con 3 columnas
            PdfPTable pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100);
            pdfTable.addCell("Nombre");
            pdfTable.addCell("Tipo");
            pdfTable.addCell("Peso");
            pdfTable.addCell("Altura");
            pdfTable.addCell("Descripcion");
            pdfTable.addCell("ATK");
            pdfTable.addCell("DEF");

            // Llenar la tabla con los datos del JTable
            for (int i = 0; i < tablePokemon.getRowCount(); i++) {
                pdfTable.addCell(tablePokemon.getValueAt(i, 1).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 2).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 3).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 4).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 5).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 6).toString());
                pdfTable.addCell(tablePokemon.getValueAt(i, 7).toString());

            }

            document.add(pdfTable);

            document.add(new Paragraph(" "));

            document.close();

            JOptionPane.showMessageDialog(null, "Listado generado exitosamente como '" + nombreArchivo + "'");

            // Abrir automáticamente el PDF
            if (Desktop.isDesktopSupported()) {
                File pdfFile = new File(nombreArchivo);
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(null, "Tu sistema no soporta abrir el archivo automáticamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar la factura: " + e.getMessage());
        }
    }

    // TODO: Método para enviar el PDF por correo
    public void enviarListado(String destinatario) {
        final String remitente = ""; // Cambia por tu correo
        final String contrasena = ""; // Contraseña de aplicación

        // Configuración de propiedades
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contrasena);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Listado de Pokedex");

            // Cuerpo del mensaje
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Adjuntamos el listado de acudientes registrados en el sistema!");

            // Adjuntar archivo
            MimeBodyPart attachmentPart = new MimeBodyPart();
            String archivo = "Listado pokedex.pdf";
            DataSource source = (DataSource) new FileDataSource(archivo);
            attachmentPart.setDataHandler(new DataHandler((javax.activation.DataSource) source));
            attachmentPart.setFileName(archivo);

            // Combinar texto + adjunto
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Enviar mensaje
            Transport.send(message);

            JOptionPane.showMessageDialog(null, "Listado enviado exitosamente a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al enviar el correo: " + e.getMessage());
        }
    }

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
