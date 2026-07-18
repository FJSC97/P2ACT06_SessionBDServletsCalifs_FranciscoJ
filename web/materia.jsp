<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Materia"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.util.List"%>
<%
    Materia edit = (Materia) request.getAttribute("edit");
    List<Materia> lista = (List<Materia>) request.getAttribute("lista");
    Usuario usuarioSesion = (Usuario) request.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Materias</title>
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
                <a href="SAlumnos">Alumnos</a>
                <a href="SMaterias" class="activo">Materias</a>
                <a href="SCalifs">Calificaciones</a>
            </nav>

            <div class="card">
                <h2><%= (edit != null) ? "Modificar materia" : "Registrar materia" %></h2>
                <form method="post" action="SMaterias" class="form-materia">
                    <input type="hidden" name="accion" value="<%= (edit != null) ? "Cambio" : "Alta" %>"/>
                    <% if (edit != null) { %>
                        <input type="hidden" name="tfId" value="<%= edit.getIdMateria() %>"/>
                    <% } %>
                    <input type="text" name="tfNombreMateria" value='<%= (edit != null) ? edit.getNombreMateria() : "" %>' placeholder="Nombre de la materia" required/>
                    <input type="submit" value="<%= (edit != null) ? "Modificar" : "Agregar" %>"/>
                </form>
            </div>

            <div class="card">
                <h2>Lista de materias (<%= (lista != null) ? lista.size() : 0 %>)</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Materia</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (lista != null) { for (Materia m : lista) { %>
                        <tr>
                            <td><%= m.getNombreMateria() %></td>
                            <td>
                                <form method="get" action="SMaterias" style="display:inline;">
                                    <input type="hidden" name="accion" value="Editar"/>
                                    <input type="hidden" name="tfId" value="<%= m.getIdMateria() %>"/>
                                    <button type="submit" class="btn-edit">Editar</button>
                                </form>
                                <form method="post" action="SMaterias" style="display:inline;">
                                    <input type="hidden" name="accion" value="Baja"/>
                                    <input type="hidden" name="tfId" value="<%= m.getIdMateria() %>"/>
                                    <button type="submit" class="btn-delete">Eliminar</button>
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
