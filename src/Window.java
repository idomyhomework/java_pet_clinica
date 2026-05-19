import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private JTextField nombre = new JTextField(20);
    private JTextField especie = new JTextField(20);
    private JTextField propietario = new JTextField(20);
    private JTextField dni = new JTextField(20);
    private JTextField edad = new JTextField(20);

    public Window() {
        setTitle("Registro de Mascotas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(350, 350));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] labels = { "Nombre:", "Especie:", "Propietario:", "DNI:", "Edad:" };
        JTextField[] fields = { nombre, especie, propietario, dni, edad };

        for (int i = 0; i < labels.length; i++) {
            // Columna 0: etiqueta
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0; // <-- resetar a 0 per a l'etiqueta
            gbc.anchor = GridBagConstraints.EAST;
            panel.add(new JLabel(labels[i]), gbc);

            // Columna 1: campo de texto
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0; // <-- només per al camp
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(fields[i], gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Acción"), gbc);
        gbc.gridx = 1;
        panel.add(new JButton("Añadir mascota"), gbc);
        add(panel);
        panel.add(new JLabel(""), gbc);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}