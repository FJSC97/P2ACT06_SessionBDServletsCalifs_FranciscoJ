package dao;

import modelo.Materia;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOMateria
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Materia> listar()
    {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT * FROM materias ORDER BY nombreMateria";
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

    public Materia buscarPorId(int idMateria)
    {
        Materia materia = null;
        String sql = "SELECT * FROM materias WHERE idMateria = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMateria);
            rs = ps.executeQuery();
            if (rs.next())
            {
                materia = mapear(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return materia;
    }

    public boolean insertar(Materia materia)
    {
        boolean ok = false;
        String sql = "INSERT INTO materias (nombreMateria) VALUES (?)";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, materia.getNombreMateria());
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean actualizar(Materia materia)
    {
        boolean ok = false;
        String sql = "UPDATE materias SET nombreMateria = ? WHERE idMateria = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, materia.getNombreMateria());
            ps.setInt(2, materia.getIdMateria());
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean eliminar(int idMateria)
    {
        boolean ok = false;
        String sql = "DELETE FROM materias WHERE idMateria = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idMateria);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    private Materia mapear(ResultSet rs) throws SQLException
    {
        Materia m = new Materia();
        m.setIdMateria(rs.getInt("idMateria"));
        m.setNombreMateria(rs.getString("nombreMateria"));
        return m;
    }
}
