import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Window extends JFrame {
    private JTextField nombre = new JTextField(20);
    private JTextField especie = new JTextField(20);
    private JTextField propietario = new JTextField(20);
    private JTextField dni = new JTextField(20);
    private JTextField edad = new JTextField(20);
    DBConection db = new DBConection();

    String[] labels = { "Nombre: *", "Especie: *", "Propietario: *", "DNI: *", "Edad: *" };
    JTextField[] fields = { nombre, especie, propietario, dni, edad };

    public Window() {
        db.connect();
        if (!db.isConnected()) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.", "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
        setTitle("Registro de Mascotas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(350, 350));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // los campos de entrada
        for (int i = 0; i < labels.length; i++) {
            // Columna 0: etiqueta
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            panel.add(new JLabel(labels[i]), gbc);

            // Columna 1: campo de texto
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(fields[i], gbc);
        }
        // --- Fila final: Botones --- (Acciones con las mascotas)
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Acción"), gbc);
        gbc.gridx = 1;
        JButton createBtn = new JButton("Añadir mascota");
        panel.add(createBtn, gbc);
        gbc.gridx = 2;
        JButton importBtn = new JButton("Importar .txt");

        panel.add(importBtn, gbc);
        add(panel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        // --- Fila de acciones con el DNI --- (Buscar, exportar, limpiar)
        // --- Fila 1: Label + TextField ---
        JPanel searchFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("DNI del dueño:");
        JTextField searchDni = new JTextField(20);
        searchFieldPanel.add(searchLabel);
        searchFieldPanel.add(searchDni);

        // --- Fila 2: Botones ---
        JPanel searchBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Buscar mascota");
        JButton exportBtn = new JButton("Exportar .txt");
        JButton clearBtn = new JButton("Limpiar");

        searchBtnPanel.add(searchBtn);
        searchBtnPanel.add(exportBtn);
        searchBtnPanel.add(clearBtn);
        exportBtn.addActionListener(e -> exportarMascota(searchDni.getText()));
        importBtn.addActionListener(e -> importarMascota());

        // --- Panel que agrupa las dos filas ---
        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        searchPanel.add(searchFieldPanel);
        searchPanel.add(searchBtnPanel);

        // --- Área de resultados ---
        JTextArea searchResult = new JTextArea(10, 30);
        searchResult.setEditable(false);

        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(searchResult), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.CENTER); // CENTER en vez de SOUTH para que se expanda
        add(bottomPanel, BorderLayout.SOUTH);
        panel.add(new JLabel(""), gbc);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // --- Listeners ---
        clearBtn.addActionListener(e -> borrarTexto(searchResult));
        createBtn.addActionListener(e -> createPet());
        searchBtn.addActionListener(e -> {
            String dni = searchDni.getText();
            String result = searchPet(dni);
            searchResult.setText(result);
        });
    }

    private void createPet() {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String nombre = this.nombre.getText();
        String especie = this.especie.getText();
        String propietario = this.propietario.getText();
        String dni = this.dni.getText();
        try {
            int edad = Integer.parseInt(this.edad.getText());
            if (edad < 0) {
                JOptionPane.showMessageDialog(this, "La edad no puede ser negativa.", "Error de formato",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Pet pet = new Pet(nombre, especie, propietario, dni, edad);
            db.createPet(pet);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para la edad.", "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Mascota registrada exitosamente.", "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        for (int i = 0; i < fields.length; i++) {
            fields[i].setText("");
        }
    }

    private String searchPet(String dni) {
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un DNI.", "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        String search = "";
        search = db.searchPet(dni);

        return search;
    }

    private void exportarMascota(String dni) {
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un DNI.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PrintWriter pw = null;
        try {
            String datos = db.searchPet(dni);
            if (datos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron mascotas con ese DNI.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Abrir explorador para elegir dónde guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("export_mascotas.txt")); // nombre por defecto
            int opcion = fileChooser.showSaveDialog(this);

            if (opcion == JFileChooser.APPROVE_OPTION) {
                pw = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                pw.println(datos);
                System.out.println("Exportado correctamente a: " + fileChooser.getSelectedFile().getPath());
                JOptionPane.showMessageDialog(this, "Exportado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            System.out.println("Error de escritura: " + e.getMessage());
        } finally {
            if (pw != null)
                pw.close();
        }
    }

    private void importarMascota() {
        BufferedReader br = null;
        try {
            // Abrir explorador para elegir el fichero
            JFileChooser fileChooser = new JFileChooser();
            int opcion = fileChooser.showOpenDialog(this);

            if (opcion == JFileChooser.APPROVE_OPTION) {
                br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                nombre.setText(br.readLine());
                especie.setText(br.readLine());
                propietario.setText(br.readLine());
                dni.setText(br.readLine());
                edad.setText(br.readLine());
                System.out.println("Fichero cargado correctamente.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: El fichero no existe.");
        } catch (IOException e) {
            System.out.println("Error de lectura: " + e.getMessage());
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el fichero: " + e.getMessage());
            }
        }
    }

    private void borrarTexto(JTextArea field) {
        field.setText("");
    }
}