package org.alvarowau.democrud.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonaDB {

    private Connection connection;

    public PersonaDB(Connection connection) {
        this.connection = connection;
    }

    public List<Persona> getPersonas() throws SQLException {
        List<Persona> personas = new ArrayList<>();
        int id, experiencia;
        String nombre, apellidos;
        Date fechaNac;
        try {
            String query = "SELECT * FROM personas";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                personas.add(covertirResultsetEnPersona(rs));
            }
        } catch (SQLException e) {
            throw e;
        }
        return personas;
    }

    public boolean insertar(Persona persona) throws SQLException {
        String sqlInsertar = "INSERT INTO personas (nombre, apellidos, experiencia, fechaNac) VALUES (?, ?,?,?);";
        if (persona.getId() < 1)
            try {
                PreparedStatement ps = connection.prepareStatement(sqlInsertar);
                ps.setString(1, persona.getNombre());
                ps.setString(2, persona.getApellidos());
                ps.setInt(3, persona.getExperiencia());
                ps.setDate(4, convertirFecha(persona.getFechaNac()));
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    return false;
                } else {
                    return true;
                }

            } catch (SQLException e) {
                throw e;
            }
        else {
            return false;
        }
    }

    public Persona findById(int id) throws SQLException {
        String sql = "SELECT * FROM personas WHERE idpersona = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return covertirResultsetEnPersona(rs);
            }
            return null; // Si no se encuentra, retorna null
        } catch (SQLException e) {
            throw e;
        }
    }

    public String validar(String nombre, String apellidos, String fechaNac, String experiencia) {
        StringBuilder resultado = new StringBuilder();

        // Validación del nombre
        if (nombre.isEmpty()) {
            resultado.append("<p>El nombre está vacío.</p>");
        } else if (nombre.length() < 3) {
            resultado.append("<p>El nombre debe tener un mínimo de 3 caracteres.</p>");
        } else {

        }

        // Validación de apellidos
        if (apellidos.isEmpty()) {
            resultado.append("<p>Los apellidos están vacíos.</p>");
        } else if (apellidos.length() < 3) {
            resultado.append("<p>Los apellidos deben tener un mínimo de 3 caracteres.</p>");
        }

        // Validación de la fecha de nacimiento
        if (fechaNac.isEmpty()) {
            resultado.append("<p>La fecha de nacimiento está vacía.</p>");
        } else {
            String regex = "^\\d{4}-\\d{2}-\\d{2}$"; // Formato: yyyy-MM-dd
            if (!fechaNac.matches(regex)) {
                resultado.append("<p>La fecha de nacimiento no es válida. Debe estar en el formato yyyy-MM-dd.</p>");
            }
        }

        // Validación de la experiencia
        if (experiencia.isEmpty()) {
            resultado.append("<p>La experiencia está vacía.</p>");
        } else {
            if (!experiencia.matches("^[0-9]+$")) {
                resultado.append("<p>La experiencia debe ser un número.</p>");
            }
        }

        return resultado.toString();
    }

    public boolean actualizar(Persona persona) throws SQLException {
        // Consulta base para actualizar
        StringBuilder sql = new StringBuilder("UPDATE personas SET ");

        // Bandera para saber si algún campo ha cambiado
        boolean camposActualizados = false;

        try {
            // Obtenemos la persona actual de la base de datos para comparar
            Persona personaConId = findById(persona.getId());

            if (personaConId != null) {
                // Crear una lista de parámetros que se asignarán al PreparedStatement
                List<Object> parametros = new ArrayList<>();

                // Comparamos los campos y añadimos las partes correspondientes de la consulta
                if (!persona.getNombre().equals(personaConId.getNombre())) {
                    sql.append("nombre = ?, ");
                    parametros.add(persona.getNombre());
                    camposActualizados = true;
                }
                if (!persona.getApellidos().equals(personaConId.getApellidos())) {
                    sql.append("apellidos = ?, ");
                    parametros.add(persona.getApellidos());
                    camposActualizados = true;
                }
                if (persona.getExperiencia() != personaConId.getExperiencia()) {
                    sql.append("experiencia = ?, ");
                    parametros.add(persona.getExperiencia());
                    camposActualizados = true;
                }
                if (persona.getFechaNac().getTime() != personaConId.getFechaNac().getTime()) {
                    sql.append("fechaNac = ?, ");
                    parametros.add(new java.sql.Date(persona.getFechaNac().getTime()));
                    camposActualizados = true;
                }
                // Si no hay campos que actualizar, retornamos falso
                if (!camposActualizados) {
                    return false;
                }

                // Eliminamos la última coma y espacio
                sql.setLength(sql.length() - 2);

                // Añadimos la condición WHERE para identificar a la persona por su ID
                sql.append(" WHERE idPersona = ?");
                parametros.add(persona.getId());

                // Preparamos la sentencia
                try {
                    PreparedStatement ps = connection.prepareStatement(sql.toString());
                    // Asignamos los parámetros al PreparedStatement
                    for (int i = 0; i < parametros.size(); i++) {
                        ps.setObject(i + 1, parametros.get(i));
                    }

                    // Ejecutamos la actualización
                    int filasActualizadas = ps.executeUpdate();
                    return filasActualizadas > 0;
                }catch (SQLException e) {
                    throw e;
                }
            } else {
                System.out.println("No se encontró ninguna persona con el ID proporcionado.");
                return false;
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private Persona covertirResultsetEnPersona(ResultSet rs) throws SQLException {
        Persona persona = new Persona();
        persona.setId(rs.getInt("idpersona"));
        persona.setNombre(rs.getString("nombre"));
        persona.setApellidos(rs.getString("apellidos"));
        persona.setExperiencia(rs.getInt("experiencia"));
        persona.setFechaNac(rs.getDate("fechaNac"));
        return persona;
    }

    private java.sql.Date convertirFecha(Date fechaNac) {
        return new java.sql.Date(fechaNac.getTime());
    }

    public boolean eliminar(int idPersona) throws SQLException {
        String sql = "DELETE FROM personas WHERE idPersona = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            int filasEliminadas = ps.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

}
