package control;

import modelo.Alumno;
import modelo.Usuario;
import dao.DAOAlumno;
import dao.DAOUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SAlumnos extends HttpServlet {

    private DAOAlumno dao;

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

        dao = new DAOAlumno();

        String accion = request.getParameter("accion");
        Alumno edit = null;

        if ("Cambio".equals(accion))
        {
            int id = Integer.parseInt(request.getParameter("tfId"));

            Alumno anterior = dao.buscarPorId(id);
            String correoAnterior = (anterior != null) ? anterior.getCorreo() : null;

            Alumno a = new Alumno();
            a.setIdAlumno(id);
            a.setMatricula(request.getParameter("tfMatricula"));
            a.setNombre(request.getParameter("tfNombre"));
            a.setPaterno(request.getParameter("tfPaterno"));
            a.setMaterno(request.getParameter("tfMaterno"));
            a.setCorreo(request.getParameter("tfCorreo"));
            dao.actualizar(a);

            if (correoAnterior != null)
            {
                new DAOUsuario().actualizarPorCorreo(correoAnterior, a.getNombreCompleto(), a.getMatricula(), a.getCorreo());
            }
        }
        else if ("Baja".equals(accion))
        {
            int id = Integer.parseInt(request.getParameter("tfId"));

            Alumno alumno = dao.buscarPorId(id);
            dao.eliminar(id);

            if (alumno != null && alumno.getCorreo() != null)
            {
                new DAOUsuario().eliminarPorCorreo(alumno.getCorreo());
            }
        }
        else if ("Editar".equals(accion))
        {
            int id = Integer.parseInt(request.getParameter("tfId"));
            edit = dao.buscarPorId(id);
        }

        request.setAttribute("lista", dao.listar());
        request.setAttribute("edit", edit);
        request.setAttribute("usuario", usuario);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/alumno.jsp");
        rd.forward(request, response);
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
        return "Servlet CRUD de alumnos";
    }
}