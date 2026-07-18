package control;

import modelo.Usuario;
import modelo.Alumno;
import dao.DAOUsuario;
import dao.DAOAlumno;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Panel del administrador (profesor). Aquí es donde se le da acceso
 * (status = Activo) a los alumnos que ya confirmaron su correo.
 * Solo pueden entrar cuentas con rol = "Administrador".
 */
public class SAdmin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null || !usuario.esAdministrador())
        {
            response.sendRedirect("SLogin");
            return;
        }

        String accion = request.getParameter("accion");

        if ("CambiarStatus".equals(accion))
        {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nuevoStatus = request.getParameter("nuevoStatus");

            DAOUsuario daoUsuario = new DAOUsuario();
            daoUsuario.actualizarStatus(idUsuario, nuevoStatus);

            // Al activar a alguien, además de darle acceso al login,
            // se le da de alta en la tabla "alumnos" (si no existía
            // ya) para que aparezca en el CRUD de calificaciones.
            // Al desactivar NO se borra nada, para no perder
            // calificaciones que ya se le hayan capturado.
            if ("Activo".equalsIgnoreCase(nuevoStatus))
            {
                Usuario cuenta = daoUsuario.buscarPorId(idUsuario);
                if (cuenta != null)
                {
                    DAOAlumno daoAlumno = new DAOAlumno();
                    Alumno alumnoExistente = daoAlumno.buscarPorCorreo(cuenta.getCorreo());

                    if (alumnoExistente == null)
                    {
                        // No existía todavía: se crea desde cero.
                        daoAlumno.insertar(construirAlumno(cuenta.getNombreCompleto(), cuenta.getMatricula(), cuenta.getCorreo()));
                    }
                    else if (esVacia(alumnoExistente.getMatricula()) && !esVacia(cuenta.getMatricula()))
                    {
                        // Ya existía (por ejemplo, de una activación
                        // anterior a que se agregara la matrícula al
                        // sistema) pero le falta la matrícula: se
                        // rellena sin tocar lo demás.
                        alumnoExistente.setMatricula(cuenta.getMatricula());
                        daoAlumno.actualizar(alumnoExistente);
                    }
                }
            }

            response.sendRedirect("SAdmin");
            return;
        }

        List<Usuario> alumnos = new DAOUsuario().listarAlumnos();
        request.setAttribute("alumnos", alumnos);
        request.setAttribute("usuario", usuario);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/admin.jsp");
        rd.forward(request, response);
    }

    private boolean esVacia(String texto)
    {
        return texto == null || texto.isBlank();
    }

    /**
     * "usuarios" solo guarda un campo de nombre completo, pero
     * "alumnos" necesita nombre, paterno y materno por separado.
     * Se hace una separación automática: la última palabra se toma
     * como materno, la penúltima como paterno, y el resto como
     * nombre(s). Si el nombre completo no tiene ese formato exacto,
     * el profesor puede corregirlo después desde SAlumnos.
     */
    private Alumno construirAlumno(String nombreCompleto, String matricula, String correo)
    {
        Alumno a = new Alumno();
        a.setMatricula(matricula);
        a.setCorreo(correo);

        String[] partes = nombreCompleto.trim().split("\\s+");

        if (partes.length >= 3)
        {
            a.setMaterno(partes[partes.length - 1]);
            a.setPaterno(partes[partes.length - 2]);

            StringBuilder nombre = new StringBuilder();
            for (int i = 0; i < partes.length - 2; i++)
            {
                if (i > 0)
                {
                    nombre.append(" ");
                }
                nombre.append(partes[i]);
            }
            a.setNombre(nombre.toString());
        }
        else if (partes.length == 2)
        {
            a.setNombre(partes[0]);
            a.setPaterno(partes[1]);
            a.setMaterno("");
        }
        else
        {
            a.setNombre(nombreCompleto);
            a.setPaterno("");
            a.setMaterno("");
        }

        return a;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet del panel de administrador";
    }
}