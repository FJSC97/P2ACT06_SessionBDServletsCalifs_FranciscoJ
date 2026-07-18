<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String correo = (String) request.getAttribute("correo");
    Integer restanteSeg = (Integer) request.getAttribute("restanteSeg");
    Boolean codigoExpirado = (Boolean) request.getAttribute("codigoExpirado");
    Boolean reenviado = (Boolean) request.getAttribute("reenviado");
    String error = (String) request.getAttribute("error");

    int restante = (restanteSeg != null) ? restanteSeg : 0;
    boolean expirado = (codigoExpirado != null) && codigoExpirado;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirma tu cuenta</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="container auth-container">
            <div class="card auth-card">
                <h2>Confirma tu cuenta</h2>
                <p>Te enviamos un código de 6 dígitos a tu correo. Escríbelo aquí para activar tu cuenta.</p>

                <% if (reenviado != null && reenviado) { %>
                    <p class="msg-ok">Te reenviamos un nuevo código a tu correo.</p>
                <% } %>

                <% if (expirado) { %>
                    <p class="msg-error">Se acabó el tiempo para ese código. Da clic en "Reenviar código" para recibir uno nuevo.</p>
                <% } %>

                <% if (error != null) { %>
                    <p class="msg-error"><%= error %></p>
                <% } %>

                <p id="temporizador" class="temporizador<%= expirado ? " temporizador-agotado" : "" %>">
                    <% if (expirado) { %>
                        Tiempo agotado
                    <% } else { %>
                        Tiempo restante: <span id="segundos"><%= restante %></span> segundos
                    <% } %>
                </p>

                <form method="post" action="SVerificar" class="form-auth" id="formCodigo">
                    <input type="hidden" name="accion" value="VerificarCodigo"/>
                    <div>
                        <label for="tfCorreo">Correo</label>
                        <input type="email" name="tfCorreo" id="tfCorreo" value='<%= (correo != null) ? correo : "" %>' required/>
                    </div>
                    <div>
                        <label for="tfCodigo">Código de verificación</label>
                        <input type="text" name="tfCodigo" id="tfCodigo" maxlength="6" placeholder="123456" <%= expirado ? "disabled" : "" %> required/>
                    </div>
                    <input type="submit" id="btnConfirmar" value="Confirmar cuenta" <%= expirado ? "disabled" : "" %>/>
                </form>

                <form method="post" action="SVerificar" class="form-resend">
                    <input type="hidden" name="accion" value="Reenviar"/>
                    <input type="hidden" name="tfCorreo" value='<%= (correo != null) ? correo : "" %>'/>
                    <button type="submit" class="link-button">Reenviar código</button>
                </form>
            </div>
        </div>

        <script>
            (function () {
                var segundosRestantes = <%= restante %>;
                var yaExpirado = <%= expirado %>;

                if (yaExpirado || segundosRestantes <= 0) {
                    return;
                }

                var spanSegundos = document.getElementById("segundos");
                var temporizador = document.getElementById("temporizador");
                var btnConfirmar = document.getElementById("btnConfirmar");
                var campoCodigo = document.getElementById("tfCodigo");

                var intervalo = setInterval(function () {
                    segundosRestantes--;

                    if (segundosRestantes <= 0) {
                        clearInterval(intervalo);
                        // Solo se avisa y se bloquea. NO se reenvía solo.
                        temporizador.textContent = "Tiempo agotado";
                        temporizador.classList.add("temporizador-agotado");
                        btnConfirmar.disabled = true;
                        campoCodigo.disabled = true;

                        var aviso = document.createElement("p");
                        aviso.className = "msg-error";
                        aviso.textContent = "Se acabó el tiempo para ese código. Da clic en \"Reenviar código\" para recibir uno nuevo.";
                        temporizador.insertAdjacentElement("afterend", aviso);
                        return;
                    }

                    spanSegundos.textContent = segundosRestantes;
                    if (segundosRestantes <= 10) {
                        temporizador.classList.add("temporizador-critico");
                    }
                }, 1000);
            })();
        </script>
    </body>
</html>
