package org.alvarowau.democrud.controlador;

import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.alvarowau.democrud.Utilidades;
import org.alvarowau.democrud.modelo.Persona;
import org.alvarowau.democrud.modelo.PersonaDB;
import org.alvarowau.democrud.modelo.PersonaRequest;
import org.alvarowau.democrud.persistencia.ConnectionPoolManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PersonaController", urlPatterns = {"/PersonaController"})
public class PersonaController extends HttpServlet {

    private static final Logger log = Logger.getLogger(PersonaController.class.getName());
    private static Connection connection = null;
    PersonaDB personaDB;

    @Override
    public void init() throws ServletException {
        log.log(Level.INFO, "obteniendo conexión a la base de datos");
        try {
            connection = ConnectionPoolManager.getConnection();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error al conectar con la base de datos", ex);
            connection = null;
            return;
        }
        log.log(Level.INFO, "conexión establecida a la base de datos");
        personaDB = new PersonaDB(connection);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "N/A";

        }
        String resultado;
        switch (accion) {
            case "agregar":
                crearPersona(request, response);
                break;
            case "eliminar":
                eliminarPersona(request,response);
                break;
            case "editar":
                log.log(Level.INFO, "editando la persona");
                editarPersona(request, response);
                break;
            case "guardar":
                resultado = validarPersona(request);
                if (resultado.isEmpty()) {
                    log.log(Level.INFO, "se procede a crear la persona");
                    guardarPersona(request, response);
                    listarPersonas(request, response);
                }else{
                    mostrarErrores(request,response,resultado);
                }
                break;
            case "listar":
            default:
                listarPersonas(request, response);
                break;
        }

        log.log(Level.INFO, "se esta ejecutando el servlet con la accion: " + accion);

    }

    private void eliminarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Persona persona;
        String mensaje = "";  // Para mostrar mensajes en la JSP
        String estado = "";   // Puede ser "exito" o "error"

        if (Utilidades.isNumero(id) && Utilidades.isPositive(id)) {
            int idPersona = Integer.parseInt(id);
            try {
                persona = personaDB.findById(idPersona); // Buscamos a la persona por ID
                if (persona != null) { // Si la persona existe
                    try {
                        if (personaDB.eliminar(idPersona)) {  // Intentamos eliminarla
                            mensaje = "La persona con ID " + idPersona + " ha sido eliminada correctamente.";
                            estado = "exito";  // Operación exitosa
                        } else {
                            mensaje = "No se pudo eliminar la persona " + persona.getNombre() + ".";
                            estado = "error";  // Hubo un error al eliminar
                        }
                    } catch (SQLException e) {
                        log.log(Level.SEVERE, "Error al eliminar la persona", e);
                        mensaje = "Error al intentar eliminar la persona.";
                        estado = "error";
                    }
                } else {
                    mensaje = "No se ha encontrado una persona con ID " + idPersona + ".";
                    estado = "error";
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Error al eliminar la persona", e);
                mensaje = "Error en la búsqueda de la persona.";
                estado = "error";
            }
        } else {
            mensaje = "ID no válido.";
            estado = "error";
        }

        // Enviamos los atributos a la JSP
        request.setAttribute("respuesta", estado);  // Puede ser "exito" o "error"
        request.setAttribute("mensaje", mensaje);   // El mensaje específico
        listarPersonas(request, response);          // Volvemos a mostrar la lista actualizada
    }



    private void editarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String id = request.getParameter("id");
        if(Utilidades.isNumero(id) && Utilidades.isPositive(id)){
            Persona persona = recuperarPersonaPorId(Integer.parseInt(id));
            if(persona.getId() > 0){
                PersonaRequest personaRequest = new PersonaRequest(
                        persona.getId(), persona.getNombre(), persona.getApellidos(),
                        String.valueOf(persona.getFechaNac()),String.valueOf(persona.getExperiencia())
                );
                request.setAttribute("persona", personaRequest);
                request.setAttribute("errores", "");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/formulariopersona.jsp");
                dispatcher.forward(request, response);
            }else{
                log.log(Level.SEVERE, "No se puede editar la persona con id " + id);
                listarPersonas(request, response);
            }
        }

        //listarPersonas(request, response);
    }

    private boolean guardarPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mensaje = "";
        String estado = "";
        PersonaRequest personaRequest = recogerPersona(request);
        if (personaRequest.getId() < 0) {
            log.log(Level.SEVERE, "el guardado de la persona no se puede llevar a cabo");
            mensaje = "No se puede guardar la persona.";
            estado = "error";
        } else{
            Persona persona = covertirPersonaRequestAPersona(personaRequest);
            if (personaRequest.getId() == 0) {
                log.log(Level.INFO, "Se va a crear una persona nueva");
                try{
                    if(personaDB.insertar(persona)){
                        mensaje = "La persona con nombre " + persona.getNombre() + " ha sido agregada correctamente.";
                        estado = "exito";
                    }
                } catch (SQLException e) {
                    log.log(Level.SEVERE, "no se pudo insertar la persona",e);
                    mensaje = "No se puede guardar la persona.";
                    estado = "error";
                }

            } else if (personaRequest.getId() > 0) {
                log.log(Level.INFO, "Se va a actualizar una persona ");
                try{
                    if (personaDB.actualizar(persona)){
                        mensaje = "La persona con nombre " + persona.getNombre() + " ha sido actualizada correctamente.";
                        estado = "exito";
                    }else{
                        mensaje = "No se puede actualizar la persona.";
                        estado = "error";
                    }
                } catch (SQLException e) {
                    log.log(Level.SEVERE, "no se pudo actualizar la persona",e);
                    mensaje = "No se puede actualizar la persona.";
                    estado = "error";
                }
            }

        }
        request.setAttribute("respuesta", estado);  // Puede ser "exito" o "error"
        request.setAttribute("mensaje", mensaje);   // El mensaje específico
        return estado.equals("exito");
    }

    private Persona covertirPersonaRequestAPersona(PersonaRequest personaRequest) {
        int id = personaRequest.getId();
        String nombre = personaRequest.getNombre();
        String apellidos = personaRequest.getApellidos();
        if(Utilidades.isNumero(personaRequest.getExperiencia()) && Utilidades.isPositive(personaRequest.getExperiencia())){
                log.log(Level.INFO, "el experiencia es positivo");
                int experiencia = Integer.parseInt(personaRequest.getExperiencia());
                log.log(Level.INFO, "la fecha es: " + personaRequest.getFechaNacimiento());
                if(Utilidades.isFormatoFechaCorrecto(personaRequest.getFechaNacimiento())){
                    Date fechaNacimiento = Utilidades.fechaFormateada(personaRequest.getFechaNacimiento());
                    if(fechaNacimiento != null){
                        return new Persona(id,nombre,apellidos,fechaNacimiento,experiencia);
                    }
                }
            }
        log.log(Level.INFO, "algo salio mal");
        return new Persona();
    }

    private Persona recuperarPersonaPorId(int id) {
        try{
            return personaDB.findById(id);
        } catch (SQLException e) {
            log.log(Level.SEVERE,"no se pudo recuperar la persona",e);
            return new Persona();
        }
    }

    private void mostrarErrores(HttpServletRequest request, HttpServletResponse response, String errores)
            throws ServletException, IOException {
        PersonaRequest persona = recogerPersona(request);
        request.setAttribute("persona", persona);
        request.setAttribute("errores", errores);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/formulariopersona.jsp");
        dispatcher.forward(request, response);

    }

    private String validarPersona(HttpServletRequest request) {
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String fechaNac = request.getParameter("fechaNac");
        String experiencia = request.getParameter("experiencia");

        return personaDB.validar(nombre, apellidos, fechaNac, experiencia);

    }

    private void crearPersona(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PersonaRequest persona = new PersonaRequest();
        request.setAttribute("persona", persona);
        request.setAttribute("errores", "");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/formulariopersona.jsp");
        dispatcher.forward(request, response);
    }

    private void listarPersonas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Persona> personas;
        try {
            personas = personaDB.getPersonas(); // Obtenemos la lista de personas
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error al obtener las personas", e);
            personas = new ArrayList<>(); // Si hay un error, devolvemos una lista vacía
        }
        // Pasamos la lista de personas y el mensaje de respuesta a la JSP
        request.setAttribute("personas", personas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/personas.jsp");
        dispatcher.forward(request, response);
    }



    private PersonaRequest recogerPersona(HttpServletRequest request) {
        int idPersona;
        String id = request.getParameter("idPersona");
        if (id != null) {
            idPersona = Integer.parseInt(id);
        } else {
            idPersona = -1;
        }
        log.log(Level.INFO, "el id recuperado es " + id);
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String fechaNac = request.getParameter("fechaNac");
        String experiencia = request.getParameter("experiencia");
        return new PersonaRequest(idPersona, nombre, apellidos, fechaNac, experiencia);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
