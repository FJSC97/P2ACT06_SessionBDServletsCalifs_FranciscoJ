<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Calificacion"%>
<%@page import="modelo.Materia"%>
<%@page import="modelo.Alumno"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.util.List"%>
<%
    List<Calificacion> lista = (List<Calificacion>) request.getAttribute("lista");
    List<Materia> materias = (List<Materia>) request.getAttribute("materias");
    List<Alumno> alumnosFiltro = (List<Alumno>) request.getAttribute("alumnos");
    Integer generados = (Integer) request.getAttribute("generados");
    Integer filtroAlumno = (Integer) request.getAttribute("filtroAlumno");
    Integer filtroMateria = (Integer) request.getAttribute("filtroMateria");
    Integer filtroParcial = (Integer) request.getAttribute("filtroParcial");
    int[] parciales = (int[]) request.getAttribute("parciales");
    Usuario usuarioSesion = (Usuario) request.getAttribute("usuario");

    String fAlumno = (filtroAlumno != null) ? String.valueOf(filtroAlumno) : "";
    String fMateria = (filtroMateria != null) ? String.valueOf(filtroMateria) : "";
    String fParcial = (filtroParcial != null) ? String.valueOf(filtroParcial) : "";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calificaciones</title>
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
                <a href="SMaterias">Materias</a>
                <a href="SCalifs" class="activo">Calificaciones</a>
            </nav>

            <div class="card">
                <h2>Generar plantilla de calificaciones</h2>
                <p>
                    Al presionar este botón, el sistema revisa todos los alumnos, todas las materias
                    y los 3 parciales, y crea automáticamente el registro de calificación que le
                    falte a cada combinación. Si agregas un alumno o una materia nueva, vuelve a
                    presionar este botón. Las que ya estaban capturadas nunca se borran ni se
                    sobrescriben.
                </p>

                <% if (generados != null) { %>
                    <p class="msg-ok">
                        <% if (generados > 0) { %>
                            Se generaron <%= generados %> registro(s) de calificación nuevo(s).
                        <% } else { %>
                            No había nada nuevo que generar: todo ya estaba generado.
                        <% } %>
                    </p>
                <% } %>

                <form method="post" action="SCalifs">
                    <input type="hidden" name="accion" value="GenerarPlantilla"/>
                    <input type="hidden" name="filtroAlumno" value="<%= fAlumno %>"/>
                    <input type="hidden" name="filtroMateria" value="<%= fMateria %>"/>
                    <input type="hidden" name="filtroParcial" value="<%= fParcial %>"/>
                    <button type="submit" class="btn-generar">Generar plantilla de calificaciones</button>
                </form>
            </div>

            <div class="card">
                <h2>Captura de calificaciones (<%= (lista != null) ? lista.size() : 0 %>)</h2>

                <form method="get" action="SCalifs" class="form-materia" style="margin-bottom:20px;">
                    <div style="flex:1; min-width:200px;">
                        <label for="filtroAlumno" style="display:block; font-size:13px; font-weight:600; margin-bottom:6px; color:var(--ink-700);">Alumno</label>
                        <select name="filtroAlumno" id="filtroAlumno" style="width:100%; padding:11px 12px; border:1px solid var(--line); border-radius:var(--radius); background:var(--paper-100); font-family:var(--font-body); font-size:14.5px;">
                            <option value="">Todos los alumnos</option>
                            <% if (alumnosFiltro != null) { for (Alumno al : alumnosFiltro) { %>
                                <option value="<%= al.getIdAlumno() %>" <%= (filtroAlumno != null && filtroAlumno == al.getIdAlumno()) ? "selected" : "" %>>
                                    <%= al.getNombreCompleto() %>
                                </option>
                            <% } } %>
                        </select>
                    </div>
                    <div style="flex:1; min-width:200px;">
                        <label for="filtroMateria" style="display:block; font-size:13px; font-weight:600; margin-bottom:6px; color:var(--ink-700);">Materia</label>
                        <select name="filtroMateria" id="filtroMateria" style="width:100%; padding:11px 12px; border:1px solid var(--line); border-radius:var(--radius); background:var(--paper-100); font-family:var(--font-body); font-size:14.5px;">
                            <option value="">Todas las materias</option>
                            <% if (materias != null) { for (Materia m : materias) { %>
                                <option value="<%= m.getIdMateria() %>" <%= (filtroMateria != null && filtroMateria == m.getIdMateria()) ? "selected" : "" %>>
                                    <%= m.getNombreMateria() %>
                                </option>
                            <% } } %>
                        </select>
                    </div>
                    <div>
                        <label for="filtroParcial" style="display:block; font-size:13px; font-weight:600; margin-bottom:6px; color:var(--ink-700);">Parcial</label>
                        <select name="filtroParcial" id="filtroParcial" style="padding:11px 12px; border:1px solid var(--line); border-radius:var(--radius); background:var(--paper-100); font-family:var(--font-body); font-size:14.5px;">
                            <option value="">Todos</option>
                            <% if (parciales != null) { for (int p : parciales) { %>
                                <option value="<%= p %>" <%= (filtroParcial != null && filtroParcial == p) ? "selected" : "" %>>
                                    Parcial <%= p %>
                                </option>
                            <% } } %>
                        </select>
                    </div>
                    <input type="submit" value="Filtrar"/>
                </form>

                <table>
                    <thead>
                        <tr>
                            <th>Alumno</th>
                            <th>Materia</th>
                            <th>Parcial</th>
                            <th>Calificación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (lista != null) { for (Calificacion c : lista) { %>
                        <tr>
                            <td><%= c.getNombreAlumno() %></td>
                            <td><%= c.getNombreMateria() %></td>
                            <td>Parcial <%= c.getParcial() %></td>
                            <td>
                                <form method="post" action="SCalifs" class="form-inline-calif">
                                    <input type="hidden" name="accion" value="Actualizar"/>
                                    <input type="hidden" name="tfId" value="<%= c.getIdCalificacion() %>"/>
                                    <input type="hidden" name="filtroAlumno" value="<%= fAlumno %>"/>
                                    <input type="hidden" name="filtroMateria" value="<%= fMateria %>"/>
                                    <input type="hidden" name="filtroParcial" value="<%= fParcial %>"/>
                                    <input type="number" step="0.1" min="0" max="10" name="tfCalificacion"
                                           value='<%= (c.getCalificacion() != null) ? String.valueOf(c.getCalificacion()) : "" %>' placeholder="--"/>
                                    <button type="submit" class="btn-edit">Guardar</button>
                                </form>
                            </td>
                        </tr>
                        <% } } %>
                    </tbody>
                </table>
                <% if (lista == null || lista.isEmpty()) { %>
                    <p>No hay calificaciones que mostrar con este filtro. Da de alta alumnos y materias, y presiona "Generar plantilla de calificaciones".</p>
                <% } %>
            </div>
        </div>

        <script>
            document.querySelectorAll('.form-inline-calif').forEach(function (form) {
                form.addEventListener('submit', function () {
                    sessionStorage.setItem('scrollCalifs', window.scrollY);
                });
            });

            (function () {
                var y = sessionStorage.getItem('scrollCalifs');
                if (y !== null) {
                    window.scrollTo(0, parseInt(y, 10));
                    sessionStorage.removeItem('scrollCalifs');
                }
            })();
        </script>
    </body>
</html>
