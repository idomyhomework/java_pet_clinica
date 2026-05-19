import java.sql.*;

public class DBConecttion {
    private static final String URL = "jdbc:mysql://localhost:3306/vetClinica";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    Statement stmt = null;
    Connection con = null;

    public void connect() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = con.createStatement();
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void createPet(Pet p) {
        String query = "INSERT INTO pets (nombre, especie, propietario, dni, edad) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getEspecie());
            pstmt.setString(3, p.getPropietario());
            pstmt.setString(4, p.getDni());
            pstmt.setInt(5, p.getEdad());
            pstmt.executeUpdate();
            System.out.println("Mascota registrada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar la mascota: " + e.getMessage());
        }
    }
}
