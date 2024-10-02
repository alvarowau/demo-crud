<%@ page import="org.alvarowau.democrud.modelo.Persona" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Listado de Personas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: #007bff;
            color: white;
            text-align: center;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-secondary:hover {
            background-color: #6c757d;
        }
        footer {
            background-color: #343a40;
            color: white;
            text-align: center;
            padding: 20px 0;
            position: fixed;
            width: 100%;
            bottom: 0;
        }
    </style>
</head>
<body>
<%-- Variables para capturar el mensaje y la respuesta --%>
<%
    String respuesta = (String) request.getAttribute("respuesta");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<!-- Mostrar mensajes de error o éxito -->
<c:if test="${not empty mensaje}">
    <div class="alert alert-dismissible fade show"
         role="alert"
         class="${respuesta eq 'exito' ? 'alert alert-success' : 'alert alert-danger'}">
        <strong>${respuesta eq 'exito' ? 'Éxito!' : 'Error!'}</strong> ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>


<!-- Barra de navegación -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">CRUD MVC</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="PersonaController?accion=listar">Aplicación</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container p-4">
    <div class="card">
        <div class="card-header">
            <h2>Listado de Personas</h2>
        </div>
        <div class="card-body p-4">
            <!-- Tabla de personas -->
            <table class="table table-bordered table-striped">
                <thead class="table-dark">
                <tr>
                    <th>ID Persona</th>
                    <th>Nombre</th>
                    <th>Apellidos</th>
                    <th>Experiencia</th>
                    <th>Fecha Nacimiento</th>
                    <th>Opciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="persona" items="${personas}">
                    <tr>
                        <td>${persona.id}</td>
                        <td>${persona.nombre}</td>
                        <td>${persona.apellidos}</td>
                        <td>${persona.experiencia}</td>
                        <td>${persona.fechaNac}</td>
                        <td>
                            <a href="PersonaController?accion=editar&id=${persona.id}" class="btn btn-outline-success btn-sm">Editar</a>
                            <a href="PersonaController?accion=eliminar&id=${persona.id}" class="btn btn-outline-danger btn-sm">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Formulario para agregar persona -->
            <form action="PersonaController" method="POST" class="text-center mt-4">
                <input type="hidden" name="accion" value="agregar">
                <button type="submit" class="btn btn-primary btn-lg">Agregar Persona</button>
            </form>

            <!-- Botón para volver al índice -->
            <div class="text-center mt-3">
                <a href="index.jsp" class="btn btn-secondary btn-lg">Volver al Índice</a>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer>
    <div class="container">
        <p>&copy; 2024 Álvaro Wau| Todos los derechos reservados.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
