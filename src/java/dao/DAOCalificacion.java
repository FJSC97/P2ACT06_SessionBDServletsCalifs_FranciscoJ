package dao;

import modelo.Calificacion;
import modelo.Alumno;
import modelo.Materia;
import conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOCalificacion
{
    public static final int[] PARCIALES = {1, 2, 3};

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Calificacion> listar(Integer idAlumno, Integer idMateria, Integer parcial)
    {
        List<Calificacion> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.idCalificacion, c.idAlumno, c.idMateria, c.parcial, c.calificacion, "
              + "       CONCAT(a.nombre, ' ', a.paterno, ' ', a.materno) AS nombreAlumno, "
              + "       m.nombreMateria "
              + "FROM calificaciones c "
              + "JOIN alumnos a ON a.idAlumno = c.idAlumno "
              + "JOIN materias m ON m.idMateria = c.idMateria "
              + "WHERE 1 = 1 ");

        if (idAlumno != null)
        {
            sql.append("AND c.idAlumno = ? ");
        }
        if (idMateria != null)
        {
            sql.append("AND c.idMateria = ? ");
        }
        if (parcial != null)
        {
            sql.append("AND c.parcial = ? ");
        }
        sql.append("ORDER BY a.paterno, a.materno, a.nombre, m.nombreMateria, c.parcial");

        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql.toString());
            int idx = 1;
            if (idAlumno != null)
            {
                ps.setInt(idx++, idAlumno);
            }
            if (idMateria != null)
            {
                ps.setInt(idx++, idMateria);
            }
            if (parcial != null)
            {
                ps.setInt(idx++, parcial);
            }
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

    public List<Calificacion> listarPorAlumnoYParcial(int idAlumno, int parcial)
    {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT c.idCalificacion, c.idAlumno, c.idMateria, c.parcial, c.calificacion, "
                   + "       CONCAT(a.nombre, ' ', a.paterno, ' ', a.materno) AS nombreAlumno, "
                   + "       m.nombreMateria "
                   + "FROM calificaciones c "
                   + "JOIN alumnos a ON a.idAlumno = c.idAlumno "
                   + "JOIN materias m ON m.idMateria = c.idMateria "
                   + "WHERE c.idAlumno = ? AND c.parcial = ? "
                   + "ORDER BY m.nombreMateria";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ps.setInt(2, parcial);
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

    private boolean existeCalificacion(int idAlumno, int idMateria, int parcial) throws SQLException
    {
        String sql = "SELECT idCalificacion FROM calificaciones WHERE idAlumno = ? AND idMateria = ? AND parcial = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, idAlumno);
        pst.setInt(2, idMateria);
        pst.setInt(3, parcial);
        ResultSet r = pst.executeQuery();
        boolean existe = r.next();
        r.close();
        pst.close();
        return existe;
    }

    private void insertarVacia(int idAlumno, int idMateria, int parcial) throws SQLException
    {
        String sql = "INSERT INTO calificaciones (idAlumno, idMateria, parcial, calificacion) VALUES (?, ?, ?, NULL)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, idAlumno);
        pst.setInt(2, idMateria);
        pst.setInt(3, parcial);
        pst.executeUpdate();
        pst.close();
    }

    public int generarPlantilla()
    {
        int generados = 0;
        List<Alumno> alumnos = new DAOAlumno().listar();
        List<Materia> materias = new DAOMateria().listar();

        try
        {
            con = ConexionMySQL.getConnection();

            for (Alumno alumno : alumnos)
            {
                for (Materia materia : materias)
                {
                    for (int parcial : PARCIALES)
                    {
                        if (!existeCalificacion(alumno.getIdAlumno(), materia.getIdMateria(), parcial))
                        {
                            insertarVacia(alumno.getIdAlumno(), materia.getIdMateria(), parcial);
                            generados++;
                        }
                    }
                }
            }

            con.close();
        }
        catch (SQLException e) {}

        return generados;
    }

    public boolean actualizarCalificacion(int idCalificacion, Double valor)
    {
        boolean ok = false;
        String sql = "UPDATE calificaciones SET calificacion = ? WHERE idCalificacion = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            if (valor == null)
            {
                ps.setNull(1, java.sql.Types.DECIMAL);
            }
            else
            {
                ps.setDouble(1, valor);
            }
            ps.setInt(2, idCalificacion);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    private Calificacion mapear(ResultSet rs) throws SQLException
    {
        Calificacion c = new Calificacion();
        c.setIdCalificacion(rs.getInt("idCalificacion"));
        c.setIdAlumno(rs.getInt("idAlumno"));
        c.setIdMateria(rs.getInt("idMateria"));
        c.setParcial(rs.getInt("parcial"));
        double valor = rs.getDouble("calificacion");
        c.setCalificacion(rs.wasNull() ? null : valor);
        c.setNombreAlumno(rs.getString("nombreAlumno"));
        c.setNombreMateria(rs.getString("nombreMateria"));
        return c;
    }
}