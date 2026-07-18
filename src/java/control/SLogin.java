package control;

import modelo.Usuario;
import modelo.RegistroPendiente;
import dao.DAOUsuario;
import util.EnviadorCorreo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SLogin extends HttpServlet {

    private DAOUsuario dao;

    private String generarCodigo()
    {
        int numero = (int) (Math.random() * 900000) + 100000; // 6 dígitos
        return String.valueOf(numero);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");

        if ("Registrar".equals(accion))
        {
            procesarRegistro(request, response);
            return;
        }
        else if ("Login".equals(accion))
        {
            procesarLogin(request, response);
            return;
        }
        else if ("Logout".equals(accion))
        {
            HttpSession session = request.getSession(false);
            if (session != null)
            {
                session.invalidate();
            }
            response.sendRedirect("SLogin");
            return;
        }
        else if ("registro".equals(accion))
        {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
            return;
        }
        else
        {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login_usuario.jsp");
            rd.forward(request, response);
            return;
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String nombreCompleto = request.getParameter("tfNombreCompleto");
        String matricula = request.getParameter("tfMatricula");
        String correo = request.getParameter("tfCorreo");
        String password = request.getParameter("tfPassword");
        String password2 = request.getParameter("tfPassword2");

        dao = new DAOUsuario();

        if (nombreCompleto == null || nombreCompleto.isBlank()
                || matricula == null || matricula.isBlank()
                || correo == null || correo.isBlank()
                || password == null || password.isBlank())
        {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.setAttribute("tfNombreCompleto", nombreCompleto);
            request.setAttribute("tfMatricula", matricula);
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        if (!password.equals(password2))
        {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.setAttribute("tfNombreCompleto", nombreCompleto);
            request.setAttribute("tfMatricula", matricula);
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        if (dao.existeCorreo(correo))
        {
            request.setAttribute("error", "Ese correo ya está registrado.");
            request.setAttribute("tfNombreCompleto", nombreCompleto);
            request.setAttribute("tfMatricula", matricula);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        if (dao.existeMatricula(matricula))
        {
            request.setAttribute("error", "Esa matrícula ya está registrada.");
            request.setAttribute("tfNombreCompleto", nombreCompleto);
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        String codigo = generarCodigo();
        RegistroPendiente pendiente = new RegistroPendiente(nombreCompleto, matricula, correo, password, codigo);
        HttpSession session = request.getSession(true);
        session.setAttribute("registroPendiente", pendiente);
        session.setMaxInactiveInterval(600);

        boolean correoEnviado = EnviadorCorreo.enviarCodigoVerificacion(correo, nombreCompleto, codigo);

        if (correoEnviado)
        {
            response.sendRedirect("SVerificar");
        }
        else
        {
            session.removeAttribute("registroPendiente");
            request.setAttribute("error", "No se pudo enviar el código de verificación. Intenta registrarte de nuevo.");
            request.setAttribute("tfNombreCompleto", nombreCompleto);
            request.setAttribute("tfMatricula", matricula);
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
            rd.forward(request, response);
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String correo = request.getParameter("tfCorreo");
        String password = request.getParameter("tfPassword");

        dao = new DAOUsuario();
        Usuario usuario = dao.validar(correo, password);

        if (usuario == null)
        {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        if (!usuario.isValidar())
        {
            request.setAttribute("error", "Todavía no has confirmado tu correo. Regístrate de nuevo para recibir un código.");
            request.setAttribute("tfCorreo", correo);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login_usuario.jsp");
            rd.forward(request, response);
            return;
        }

        if (usuario.esAdministrador())
        {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            response.sendRedirect("SAdmin");
            return;
        }

        request.setAttribute("error", "El inicio de sesión de alumnos todavía no está disponible. Por ahora solo el administrador puede iniciar sesión.");
        request.setAttribute("tfCorreo", correo);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login_usuario.jsp");
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
        return "Servlet de login: trae login_usuario.jsp y reg_usuario.jsp, procesa registro/login/logout";
    }
}