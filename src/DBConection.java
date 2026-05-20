import java.sql.*;

public class DBConection {
    private static final String URL = "jdbc:mysql://localhost:3306/clinicaVet";
    private static final String USER = "root";
    private static final String PASSWORD = "abcd1234";

    Statement stmt = null;
    Connection con = null;
    private boolean isConected = false;

    public void connect() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();
            isConected = true;
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void createPet(Pet p) {
        String query = "INSERT INTO mascotas (nombre, especie, propietario, dni, edad) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getEspecie());
            pstmt.setString(3, p.getPropietario());
            pstmt.setString(4, p.getDni());
            pstmt.setInt(5, p.getEdad());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al registrar la mascota: " + e.getMessage());
        }
    }

    public String searchPet(String dni) {
        String query = "SELECT * FROM mascotas WHERE dni = ?";
        StringBuilder result = new StringBuilder();
        int index = 0;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                index++;
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String propietario = rs.getString("propietario");
                int edad = rs.getInt("edad");

                result.append("Mascota ").append(index).append("\n")
                        .append("Nombre: ").append(nombre).append("\n")
                        .append("Especie: ").append(especie).append("\n")
                        .append("Propietario: ").append(propietario).append("\n")
                        .append("Edad: ").append(edad).append("\n\n");
            }

            if (index == 0) {
                result.append("No se encontraron mascotas con el DNI: ").append(dni);
            }

            pstmt.close();

        } catch (SQLException e) {
            System.out.println("Error al buscar la mascota: " + e.getMessage());
        }

        return result.toString();
    }

    public boolean isConnected() {
        return isConected;
    }
}
