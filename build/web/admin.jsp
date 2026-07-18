<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.util.List"%>
<%
    Usuario usuarioSesion = (Usuario) request.getAttribute("usuario");
    List<Usuario> alumnos = (List<Usuario>) request.getAttribute("alumnos");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Panel del administrador</title>
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
                <a href="SAdmin" class="activo">Alumnos (acceso)</a>
                <a href="SAlumnos">Ver alumnos</a>
                <a href="SMaterias">Materias</a>
                <a href="SCalifs">Calificaciones</a>
            </nav>

            <div class="card">
                <h2>Alumnos registrados</h2>
                <p>Aquí se le da acceso a un alumno una vez que ya confirmó su correo. Solo puede iniciar sesión si su status está en "Activo". Al presionar "Activar", el alumno también se agrega automáticamente a la lista de "Alumnos" (para calificaciones), con su matrícula, nombre y correo. Ya no se pueden registrar alumnos manualmente: la única forma de que aparezca uno nuevo es que se registre y tú lo actives aquí.</p>

                <table>
                    <thead>
                        <tr>
                            <th>Matrícula</th>
                            <th>Nombre</th>
                            <th>Correo</th>
                            <th>Correo confirmado</th>
                            <th>Status</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (alumnos != null) { for (Usuario u : alumnos) { %>
                        <tr>
                            <td><%= u.getMatricula() %></td>
                            <td><%= u.getNombreCompleto() %></td>
                            <td><%= u.getCorreo() %></td>
                            <td><%= u.isValidar() ? "Sí" : "No" %></td>
                            <td><span class="estado estado-<%= u.getStatus().toLowerCase() %>"><%= u.getStatus() %></span></td>
                            <td>
                                <form method="post" action="SAdmin">
                                    <input type="hidden" name="accion" value="CambiarStatus"/>
                                    <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>"/>
                                    <% if ("Activo".equalsIgnoreCase(u.getStatus())) { %>
                                        <input type="hidden" name="nuevoStatus" value="Inactivo"/>
                                        <button type="submit" class="btn-delete">Desactivar</button>
                                    <% } else { %>
                                        <input type="hidden" name="nuevoStatus" value="Activo"/>
                                        <button type="submit" class="btn-edit">Activar</button>
                                    <% } %>
                                </form>
                            </td>
                        </tr>
                        <% } } %>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
