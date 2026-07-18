package control;

import modelo.RegistroPendiente;
import modelo.Usuario;
import dao.DAOUsuario;
import util.EnviadorCorreo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Pantalla de confirmación del código de 6 dígitos. El registro real
 * en la tabla "usuarios" ocurre aquí (acción VerificarCodigo), nunca
 * antes: si el usuario nunca pone el código, no queda nada guardado.
 *
 * El código dura lo que diga DURACION_CODIGO_MS (más abajo). Si se acaba
 * el tiempo, la página solo avisa y bloquea el formulario: NO se
 * reenvía un código nuevo de forma automática, el usuario tiene que
 * dar clic en "Reenviar código".
 */
public class SVerificar extends HttpServlet {

    public static final long DURACION_CODIGO_MS = 50_000;

    private String generarCodigo()
    {
        int numero = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(numero);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");

        if ("VerificarCodigo".equals(accion))
        {
            procesarCodigo(request, response);
            return;
        }
        else if ("Reenviar".equals(accion))
        {
            procesarReenvio(request, response);
            return;
        }

        // GET normal: mostrar la pantalla del código
        HttpSession session = request.getSession(false);
        RegistroPendiente pendiente = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;

        if (pendiente == null)
        {
            response.sendRedirect("SLogin?accion=registro");
            return;
        }

        long transcurrido = System.currentTimeMillis() - pendiente.getGeneradoEn();
        int duracionTotalSeg = (int) (DURACION_CODIGO_MS / 1000);
        int restanteSeg = (int) Math.max(0, Math.min(duracionTotalSeg, (DURACION_CODIGO_MS - transcurrido) / 1000));
        boolean codigoExpirado = (transcurrido > DURACION_CODIGO_MS);

        request.setAttribute("correo", pendiente.getCorreo());
        request.setAttribute("restanteSeg", restanteSeg);
        request.setAttribute("codigoExpirado", codigoExpirado);

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
        rd.forward(request, response);
    }

    private void procesarCodigo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String correo = request.getParameter("tfCorreo");
        String codigo = request.getParameter("tfCodigo");

        HttpSession session = request.getSession(false);
        RegistroPendiente pendiente = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;

        if (pendiente == null)
        {
            response.sendRedirect("SLogin?accion=registro");
            return;
        }

        long transcurrido = System.currentTimeMillis() - pendiente.getGeneradoEn();
        if (transcurrido > DURACION_CODIGO_MS)
        {
            // Ya expiró: solo se muestra el aviso, NO se reenvía solo.
            request.setAttribute("correo", pendiente.getCorreo());
            request.setAttribute("restanteSeg", 0);
            request.setAttribute("codigoExpirado", true);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
            rd.forward(request, response);
            return;
        }

        if (correo != null && correo.equalsIgnoreCase(pendiente.getCorreo())
                && codigo != null && codigo.equals(pendiente.getCodigo()))
        {
            DAOUsuario dao = new DAOUsuario();

            if (dao.existeCorreo(pendiente.getCorreo()))
            {
                session.removeAttribute("registroPendiente");
                request.setAttribute("error", "Ese correo ya fue registrado. Inicia sesión o usa otro correo.");
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg_usuario.jsp");
                rd.forward(request, response);
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNombreCompleto(pendiente.getNombreCompleto());
            usuario.setMatricula(pendiente.getMatricula());
            usuario.setCorreo(pendiente.getCorreo());
            usuario.setPassword(pendiente.getPassword());
            dao.registrar(usuario);

            session.removeAttribute("registroPendiente");
            response.sendRedirect("SLogin?registrado=1");
        }
        else
        {
            request.setAttribute("correo", pendiente.getCorreo());
            long restante = Math.max(0, (DURACION_CODIGO_MS - transcurrido) / 1000);
            request.setAttribute("restanteSeg", (int) restante);
            request.setAttribute("codigoExpirado", false);
            request.setAttribute("error", "El código no es válido. Revísalo o pide uno nuevo.");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
            rd.forward(request, response);
        }
    }

    private void procesarReenvio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String correo = request.getParameter("tfCorreo");

        HttpSession session = request.getSession(false);
        RegistroPendiente pendiente = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;

        if (pendiente != null && pendiente.getCorreo().equalsIgnoreCase(correo))
        {
            String codigo = generarCodigo();
            pendiente.setCodigo(codigo);
            pendiente.setGeneradoEn(System.currentTimeMillis());
            session.setAttribute("registroPendiente", pendiente);
            session.setMaxInactiveInterval(600);

            EnviadorCorreo.enviarCodigoVerificacion(pendiente.getCorreo(), pendiente.getNombreCompleto(), codigo);

            request.setAttribute("correo", pendiente.getCorreo());
            request.setAttribute("restanteSeg", (int) (DURACION_CODIGO_MS / 1000));
            request.setAttribute("codigoExpirado", false);
            request.setAttribute("reenviado", true);
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
            rd.forward(request, response);
        }
        else
        {
            response.sendRedirect("SLogin?accion=registro&expirado=1");
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
        return "Servlet de verificación de código por correo";
    }
}