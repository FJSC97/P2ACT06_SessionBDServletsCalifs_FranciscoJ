package dao;

import modelo.Usuario;
import conexion.ConexionMySQL;
import util.Seguridad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOUsuario
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean existeCorreo(String correo)
    {
        boolean existe = false;
        String sql = "SELECT idUsuario FROM usuarios WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            existe = rs.next();
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return existe;
    }

    /**
     * Se checa también que la matrícula no la haya usado alguien más,
     * igual que con el correo.
     */
    public boolean existeMatricula(String matricula)
    {
        boolean existe = false;
        String sql = "SELECT idUsuario FROM usuarios WHERE matricula = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, matricula);
            rs = ps.executeQuery();
            existe = rs.next();
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return existe;
    }

    /**
     * Inserta la cuenta ya con el correo validado (validar = 1),
     * pero con status = 'Inactivo': todavía le falta que el
     * administrador (profesor) le dé acceso. El rol por defecto
     * de cualquier registro hecho desde el formulario público es
     * "Alumno"; la cuenta de administrador se crea aparte, directo
     * en la base de datos (ver usuarios.sql).
     */
    public boolean registrar(Usuario usuario)
    {
        boolean ok = false;
        String sql = "INSERT INTO usuarios (nombreCompleto, matricula, correo, password, rol, validar, status) "
                   + "VALUES (?, ?, ?, ?, 'Alumno', 1, 'Inactivo')";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getMatricula());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, Seguridad.encriptar(usuario.getPassword()));
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public Usuario validar(String correo, String password)
    {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, Seguridad.encriptar(password));
            rs = ps.executeQuery();
            if (rs.next())
            {
                usuario = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return usuario;
    }

    /**
     * Lista todas las cuentas con rol "Alumno" para el panel del
     * administrador, para que pueda activarlas o desactivarlas.
     */
    public List<Usuario> listarAlumnos()
    {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = 'Alumno' ORDER BY nombreCompleto";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
            {
                lista.add(mapear(rs));
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return lista;
    }

    /**
     * El administrador usa esto para activar o desactivar el acceso
     * de un alumno.
     */
    public boolean actualizarStatus(int idUsuario, String status)
    {
        boolean ok = false;
        String sql = "UPDATE usuarios SET status = ? WHERE idUsuario = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    /**
     * Busca una cuenta por su id (se usa al activarla, para tomar su
     * nombre y correo y darlo de alta también en "alumnos").
     */
    public Usuario buscarPorId(int idUsuario)
    {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE idUsuario = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next())
            {
                usuario = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return usuario;
    }

    /**
     * Busca una cuenta por su correo (se usa para mantener
     * sincronizadas "usuarios" y "alumnos", que se relacionan
     * únicamente por el correo).
     */
    public Usuario buscarPorCorreo(String correo)
    {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            if (rs.next())
            {
                usuario = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return usuario;
    }

    /**
     * Cuando se edita un alumno desde SAlumnos, esto actualiza la
     * cuenta de "usuarios" que le corresponde (si existe), para que
     * el nombre, la matrícula y el correo no se queden desincronizados
     * entre las dos tablas. Se busca la cuenta por su correo ANTERIOR
     * (antes de la edición), por si justo el correo fue lo que cambió.
     */
    public boolean actualizarPorCorreo(String correoAnterior, String nombreCompleto, String matricula, String nuevoCorreo)
    {
        boolean ok = false;
        String sql = "UPDATE usuarios SET nombreCompleto = ?, matricula = ?, correo = ? WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCompleto);
            ps.setString(2, matricula);
            ps.setString(3, nuevoCorreo);
            ps.setString(4, correoAnterior);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    /**
     * Cuando se elimina un alumno desde SAlumnos, esto borra también
     * la cuenta de "usuarios" que le corresponde (si existe), para
     * que ese correo quede libre y la persona se pueda volver a
     * registrar si hace falta.
     */
    public boolean eliminarPorCorreo(String correo)
    {
        boolean ok = false;
        String sql = "DELETE FROM usuarios WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    private Usuario mapear(ResultSet rs) throws SQLException
    {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("idUsuario"));
        u.setNombreCompleto(rs.getString("nombreCompleto"));
        u.setMatricula(rs.getString("matricula"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setValidar(rs.getBoolean("validar"));
        u.setStatus(rs.getString("status"));
        return u;
    }
}