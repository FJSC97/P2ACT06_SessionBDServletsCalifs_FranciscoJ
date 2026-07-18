package dao;

import modelo.Alumno;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAlumno
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean existeCorreo(String correo)
    {
        boolean existe = false;
        String sql = "SELECT idAlumno FROM alumnos WHERE correo = ?";
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

    public List<Alumno> listar()
    {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos ORDER BY paterno, materno, nombre";
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

    public Alumno buscarPorId(int idAlumno)
    {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumnos WHERE idAlumno = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            rs = ps.executeQuery();
            if (rs.next())
            {
                alumno = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return alumno;
    }

    public Alumno buscarPorCorreo(String correo)
    {
        Alumno alumno = null;
        String sql = "SELECT * FROM alumnos WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            if (rs.next())
            {
                alumno = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return alumno;
    }

    public boolean insertar(Alumno alumno)
    {
        boolean ok = false;
        String sql = "INSERT INTO alumnos (matricula, nombre, paterno, materno, correo) VALUES (?, ?, ?, ?, ?)";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, alumno.getMatricula());
            ps.setString(2, alumno.getNombre());
            ps.setString(3, alumno.getPaterno());
            ps.setString(4, alumno.getMaterno());
            ps.setString(5, alumno.getCorreo());
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean actualizar(Alumno alumno)
    {
        boolean ok = false;
        String sql = "UPDATE alumnos SET matricula = ?, nombre = ?, paterno = ?, materno = ?, correo = ? WHERE idAlumno = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, alumno.getMatricula());
            ps.setString(2, alumno.getNombre());
            ps.setString(3, alumno.getPaterno());
            ps.setString(4, alumno.getMaterno());
            ps.setString(5, alumno.getCorreo());
            ps.setInt(6, alumno.getIdAlumno());
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean eliminar(int idAlumno)
    {
        boolean ok = false;
        String sql = "DELETE FROM alumnos WHERE idAlumno = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    private Alumno mapear(ResultSet rs) throws SQLException
    {
        Alumno a = new Alumno();
        a.setIdAlumno(rs.getInt("idAlumno"));
        a.setMatricula(rs.getString("matricula"));
        a.setNombre(rs.getString("nombre"));
        a.setPaterno(rs.getString("paterno"));
        a.setMaterno(rs.getString("materno"));
        a.setCorreo(rs.getString("correo"));
        return a;
    }
}