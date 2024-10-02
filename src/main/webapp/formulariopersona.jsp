<%@ page import="org.alvarowau.democrud.modelo.Persona" %>
<%@ page import="org.alvarowau.democrud.modelo.PersonaRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear Persona</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .form-container {
            max-width: 600px;
            margin: 50px auto;
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

        .btn-primary:hover {
            background-color: #0056b3;
        }

        .btn-secondary:hover {
            background-color: #6c757d;
        }

        .form-label {
            font-weight: bold;
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

<%
    PersonaRequest persona = (PersonaRequest) request.getAttribute("persona");
    String errores = (String) request.getAttribute("errores");
%>

<!-- Contenedor de errores -->
<c:if test="${not empty errores}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Error!</strong> ${errores}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<!-- Barra de navegación -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">CRUD MVC</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="#">Crear Persona</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Contenedor del formulario -->
<div class="container form-container">
    <div class="card">
        <div class="card-header">
            <h2>${persona != null && persona.id > 0 ? 'Actualizar Persona' : 'Crear Persona'}</h2>
        </div>
        <div class="card-body p-4">
            <form action="PersonaController" method="POST">
                <input type="hidden" id="accion" name="accion" value="guardar">

                <!-- ID Persona (no editable) -->
                <div class="mb-3">
                    <label for="idPersona" class="form-label">ID Persona</label>
                    <input type="text" class="form-control" id="idPersonaDisable" name="idPersonaDisable"
                           value="${persona != null ? persona.id : ''}" disabled>
                    <input type="hidden" id="idPersona" name="idPersona" value="${persona != null ? persona.id : ''}">
                </div>

                <!-- Nombre -->
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ingresa el nombre"
                           value="${persona != null ? persona.nombre : ''}" required>
                </div>

                <!-- Apellidos -->
                <div class="mb-3">
                    <label for="apellidos" class="form-label">Apellidos <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="apellidos" name="apellidos"
                           placeholder="Ingresa los apellidos" value="${persona != null ? persona.apellidos : ''}"
                           required>
                </div>

                <!-- Fecha de Nacimiento -->
                <div class="mb-3">
                    <label for="fechaNac" class="form-label">Fecha de Nacimiento <span
                            class="text-danger">*</span></label>
                    <input type="date" class="form-control" id="fechaNac" name="fechaNac"
                           value="${persona != null ? persona.fechaNacimiento : ''}" required>
                </div>

                <!-- Experiencia -->
                <div class="mb-3">
                    <label for="experiencia" class="form-label">Experiencia (años) <span
                            class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="experiencia" name="experiencia" min="0"
                           placeholder="Ingresa los años de experiencia"
                           value="${persona != null ? persona.experiencia : ''}" required>
                </div>

                <!-- Botones -->
                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-primary btn-lg">${persona != null && persona.id > 0 ? 'Actualizar Persona' : 'Crear Persona'}</button>
                    <a href="PersonaController" class="btn btn-secondary btn-lg">Regresar</a>
                </div>
            </form>

        </div>
    </div>
</div>

<!-- Footer -->
<footer>
    <div class="container">
        <p>&copy; 2024 ÁlvaroWau | Todos los derechos reservados.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>
</html>
