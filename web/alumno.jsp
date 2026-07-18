<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Alumno"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.util.List"%>
<%
    Alumno edit = (Alumno) request.getAttribute("edit");
    List<Alumno> lista = (List<Alumno>) request.getAttribute("lista");
    Usuario usuarioSesion = (Usuario) request.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alumnos</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="container">
            <div class="userbar">
                <span>Administrador: <strong><%= usuarioSesion.getNombreCompleto() %></strong></span>
                <form method="post" action="SLogin">
                    <input type="hidden" name="accion" value="Logout"/>
                    <input type="submit" class="btn-logout" value="Cerrar sesión"/>
                </form>
            </div>

            <nav class="topnav">
                <a href="SAdmin">Panel admin</a>
                <a href="SAlumnos" class="activo">Alumnos</a>
                <a href="SMaterias">Materias</a>
                <a href="SCalifs">Calificaciones</a>
            </nav>

            <div class="card">
                <p>
                    Ya no se pueden dar de alta alumnos manualmente aquí. Un alumno solo
                    aparece cuando se registra por su cuenta y el administrador lo activa
                    (panel de admin). Aquí solo puedes corregir o eliminar lo que ya existe.
                </p>
            </div>

            <% if (edit != null) { %>
            <div class="card">
                <h2>Modificar alumno</h2>
                <form method="post" action="SAlumnos" class="form-grid-editar">
                    <input type="hidden" name="accion" value="Cambio"/>
                    <input type="hidden" name="tfId" value="<%= edit.getIdAlumno() %>"/>
                    <div><input type="text" name="tfMatricula" value='<%= (edit.getMatricula() != null) ? edit.getMatricula() : "" %>' placeholder="Matrícula" required/></div>
                    <div><input type="text" name="tfNombre" value='<%= edit.getNombre() %>' placeholder="Nombre" required/></div>
                    <div><input type="text" name="tfPaterno" value='<%= edit.getPaterno() %>' placeholder="Apellido paterno" required/></div>
                    <div><input type="text" name="tfMaterno" value='<%= edit.getMaterno() %>' placeholder="Apellido materno" required/></div>
                    <div><input type="text" name="tfCorreo" value='<%= edit.getCorreo() %>' placeholder="Correo" required/></div>
                    <input type="submit" value="Modificar"/>
                </form>
            </div>
            <% } %>

            <div class="card">
                <h2>Lista de alumnos (<%= (lista != null) ? lista.size() : 0 %>)</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Matrícula</th>
                            <th>Nombre</th>
                            <th>Paterno</th>
                            <th>Materno</th>
                            <th>Correo</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (lista != null) { for (Alumno a : lista) { %>
                        <tr>
                            <td><%= a.getMatricula() %></td>
                            <td><%= a.getNombre() %></td>
                            <td><%= a.getPaterno() %></td>
                            <td><%= a.getMaterno() %></td>
                            <td><%= a.getCorreo() %></td>
                            <td>
                                <form method="get" action="SAlumnos" style="display:inline;">
                                    <input type="hidden" name="accion" value="Editar"/>
                                    <input type="hidden" name="tfId" value="<%= a.getIdAlumno() %>"/>
                                    <button type="submit" class="btn-edit">Editar</button>
                                </form>
                                <form method="post" action="SAlumnos" style="display:inline;">
                                    <input type="hidden" name="accion" value="Baja"/>
                                    <input type="hidden" name="tfId" value="<%= a.getIdAlumno() %>"/>
                                    <button type="submit" class="btn-delete">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                        <% } } %>
                    </tbody>
                </table>
                <% if (lista == null || lista.isEmpty()) { %>
                    <p>Todavía no hay alumnos. Espera a que se registren y actívalos desde el panel de admin.</p>
                <% } %>
            </div>
        </div>
    </body>
</html>
