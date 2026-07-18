package modelo;

public class Calificacion
{
    private int idCalificacion;
    private int idAlumno;
    private int idMateria;
    private int parcial;         // 1, 2 o 3
    private Double calificacion; // puede ser null mientras no se capture

    // Campos extra solo para mostrar en la tabla (vienen de un JOIN, no se guardan aquí)
    private String nombreAlumno;
    private String nombreMateria;

    public Calificacion()
    {
    }

    public int getIdCalificacion()
    {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion)
    {
        this.idCalificacion = idCalificacion;
    }

    public int getIdAlumno()
    {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno)
    {
        this.idAlumno = idAlumno;
    }

    public int getIdMateria()
    {
        return idMateria;
    }

    public void setIdMateria(int idMateria)
    {
        this.idMateria = idMateria;
    }

    public int getParcial()
    {
        return parcial;
    }

    public void setParcial(int parcial)
    {
        this.parcial = parcial;
    }

    public Double getCalificacion()
    {
        return calificacion;
    }

    public void setCalificacion(Double calificacion)
    {
        this.calificacion = calificacion;
    }

    public String getNombreAlumno()
    {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno)
    {
        this.nombreAlumno = nombreAlumno;
    }

    public String getNombreMateria()
    {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria)
    {
        this.nombreMateria = nombreMateria;
    }
}