public class Pet {
    private String nombre;
    private String especie;
    private String propietario;
    private String dni;
    private int edad;

    private int id;

    public Pet(int id, String nombre, String especie, String propietario, String dni, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.propietario = propietario;
        this.dni = dni;
        this.edad = edad;

    }

    public Pet(String nombre, String especie, String propietario, String dni, int edad) {
        this.nombre = nombre;
        this.especie = especie;
        this.propietario = propietario;
        this.dni = dni;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getDni() {
        return dni;
    }

    public int getEdad() {
        return edad;
    }

    public int getId() {
        return id;
    }

}
