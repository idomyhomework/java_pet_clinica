import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        StringBuilder result = new StringBuilder();
        for (Pet p : searchPetList(dni)) {
            result.append("Nombre: ").append(p.getNombre()).append("\n")
                    .append("Especie: ").append(p.getEspecie()).append("\n")
                    .append("Propietario: ").append(p.getPropietario()).append("\n")
                    .append("Edad: ").append(p.getEdad()).append("\n\n");
        }
        if (result.isEmpty()) {
            result.append("No se encontraron mascotas con el DNI: ").append(dni);
        }
        return result.toString();
    }

    public List<Pet> searchPetList(String dni) {
        String query = "SELECT * FROM mascotas WHERE dni = ?";
        List<Pet> pets = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pets.add(new Pet(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especie"),
                        rs.getString("propietario"),
                        rs.getString("dni"),
                        rs.getInt("edad")));
            }
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar mascotas: " + e.getMessage());
        }
        return pets;
    }

    public void updatePet(Pet p) {
        String query = "UPDATE mascotas SET nombre=?, especie=?, propietario=?, dni=?, edad=? WHERE id=?";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getEspecie());
            pstmt.setString(3, p.getPropietario());
            pstmt.setString(4, p.getDni());
            pstmt.setInt(5, p.getEdad());
            pstmt.setInt(6, p.getId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar la mascota: " + e.getMessage());
        }
    }

    public void deletePet(int id) {
        String query = "DELETE FROM mascotas WHERE id=?";
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar la mascota: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return this.isConected;
    }
}
