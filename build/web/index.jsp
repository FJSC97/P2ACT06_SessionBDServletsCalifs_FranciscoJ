<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema Escolar</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="home-wrap">

            <header class="home-header">
                <span class="home-header-punto"></span>
                <span class="home-header-marca">Sistema Escolar</span>
            </header>

            <section class="home-hero">
                <div class="home-hero-texto">
                    <span class="home-kicker">Plataforma para el grupo</span>
                    <h1>Gestiona tu grupo, todo en un solo lugar</h1>
                    <p>
                        Registra alumnos, organiza materias y captura calificaciones
                        por parcial.
                    </p>
                    <div class="botones-inicio">
                        <a href="SLogin" class="btn-inicio btn-inicio-primario">Iniciar sesión</a>
                        <a href="SLogin?accion=registro" class="btn-inicio btn-inicio-secundario">Registrarme</a>
                    </div>
                </div>

                <div class="home-hero-imagen">
                    <div class="home-hero-imagen-marco">
                        <img src="img/portada.png" alt="Sistema Escolar"/>
                    </div>
                </div>
            </section>

            <section class="home-features">
                <div class="home-feature-card">
                    <div class="home-feature-icono">🎓</div>
                    <h3>Alumnos</h3>
                    <p>Matrícula, correo y datos del grupo siempre al día.</p>
                </div>
                <div class="home-feature-card">
                    <div class="home-feature-icono">📚</div>
                    <h3>Materias</h3>
                    <p>Organiza las materias que se van a evaluar.</p>
                </div>
                <div class="home-feature-card">
                    <div class="home-feature-icono">📊</div>
                    <h3>Calificaciones</h3>
                    <p>Genera y captura las notas de cada parcial.</p>
                </div>
            </section>

            <p class="home-footer">Sistema Escolar</p>
        </div>
    </body>
</html>
