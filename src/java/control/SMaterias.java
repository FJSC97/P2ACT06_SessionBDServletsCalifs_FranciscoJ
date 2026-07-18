package control;

import modelo.Materia;
import modelo.Usuario;
import dao.DAOMateria;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SMaterias extends HttpServlet {

    private DAOMateria dao;

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

        dao = new DAOMateria();

        String accion = request.getParameter("accion");
        Materia edit = null;

        if ("Alta".equals(accion))
        {
            Materia m = new Materia();
            m.setNombreMateria(request.getParameter("tfNombreMateria"));
            dao.insertar(m);
        }
        else if ("Cambio".equals(accion))
        {
            Materia m = new Materia();
            m.setIdMateria(Integer.parseInt(request.getParameter("tfId")));
            m.setNombreMateria(request.getParameter("tfNombreMateria"));
            dao.actualizar(m);
        }
        else if ("Baja".equals(accion))
        {
            int id = Integer.parseInt(request.getParameter("tfId"));
            dao.eliminar(id);
        }
        else if ("Editar".equals(accion))
        {
            int id = Integer.parseInt(request.getParameter("tfId"));
            edit = dao.buscarPorId(id);
        }

        request.setAttribute("lista", dao.listar());
        request.setAttribute("edit", edit);
        request.setAttribute("usuario", usuario);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/materia.jsp");
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
        return "Servlet CRUD de materias";
    }
}