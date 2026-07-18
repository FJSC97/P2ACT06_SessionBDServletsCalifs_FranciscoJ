package control;

import dao.DAOCalificacion;
import dao.DAOMateria;
import dao.DAOAlumno;
import modelo.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SCalifs extends HttpServlet {

    private DAOCalificacion dao;

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

        dao = new DAOCalificacion();

        String accion = request.getParameter("accion");

        if ("GenerarPlantilla".equals(accion))
        {
            int generados = dao.generarPlantilla();
            request.setAttribute("generados", generados);
        }
        else if ("Actualizar".equals(accion))
        {
            int idCalificacion = Integer.parseInt(request.getParameter("tfId"));
            String valorTexto = request.getParameter("tfCalificacion");
            Double valor = (valorTexto == null || valorTexto.isBlank()) ? null : Double.valueOf(valorTexto);
            dao.actualizarCalificacion(idCalificacion, valor);
        }

        Integer filtroAlumno = parseEnteroOpcional(request.getParameter("filtroAlumno"));
        Integer filtroMateria = parseEnteroOpcional(request.getParameter("filtroMateria"));
        Integer filtroParcial = parseEnteroOpcional(request.getParameter("filtroParcial"));

        request.setAttribute("lista", dao.listar(filtroAlumno, filtroMateria, filtroParcial));
        request.setAttribute("alumnos", new DAOAlumno().listar());
        request.setAttribute("materias", new DAOMateria().listar());
        request.setAttribute("filtroAlumno", filtroAlumno);
        request.setAttribute("filtroMateria", filtroMateria);
        request.setAttribute("filtroParcial", filtroParcial);
        request.setAttribute("parciales", DAOCalificacion.PARCIALES);
        request.setAttribute("usuario", usuario);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/Califs.jsp");
        rd.forward(request, response);
    }

    private Integer parseEnteroOpcional(String texto)
    {
        if (texto == null || texto.isBlank())
        {
            return null;
        }
        try
        {
            return Integer.valueOf(texto);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
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
        return "Servlet de calificaciones: genera plantilla, filtra por materia/parcial y captura valores";
    }
}