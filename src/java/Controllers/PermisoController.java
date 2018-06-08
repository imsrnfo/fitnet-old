
package Controllers;

import HBMs.Articulo;
import HBMs.Permiso;
import Utils.ManejadorSession;
import Utils.Permisos;
import Utils.ConversorGenerico;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "permisoController")
@ViewScoped
public class PermisoController extends ConversorGenerico<Articulo> implements Serializable{
    
    public boolean tienePermiso(String nombrePermiso){
        return ManejadorSession.getInstance().tienePermiso(nombrePermiso);
    }
 
    @PostConstruct
    public void init(){
        ANULACIONES_COBRO = tienePermiso("ANULACIONES_COBRO");
        ANULACIONES_MOVIMIENTOS = tienePermiso("ANULACIONES_MOVIMIENTOS");
        CAJA_MOVIMIENTOS = tienePermiso("CAJA_MOVIMIENTOS");
        CONFIGURACION_ACTIVIDADES = tienePermiso("CONFIGURACION_ACTIVIDADES");
        CONFIGURACION_ARTICULOS = tienePermiso("CONFIGURACION_ARTICULOS");
        CONFIGURACION_CONCEPTOS = tienePermiso("CONFIGURACION_CONCEPTOS");
        CONFIGURACION_USUARIOS = tienePermiso("CONFIGURACION_USUARIOS");
        ESTADISTICAS_GENERALES = tienePermiso("ESTADISTICAS_GENERALES");
        ESTADISTICAS_SOCIOS = tienePermiso("ESTADISTICAS_SOCIOS");
        GESTIONAR_CLIENTES = tienePermiso("GESTIONAR_CLIENTES");
        GESTIONAR_COBRO = tienePermiso("GESTIONAR_COBRO");
        GESTIONAR_MARCAS = tienePermiso("GESTIONAR_MARCAS");
        GIMNASIO_GIMNASIOS = tienePermiso("GIMNASIO_GIMNASIOS");
        REPORTES_COBRO = tienePermiso("REPORTES_COBRO");
        REPORTE_DEUDA = tienePermiso("REPORTE_DEUDA");
        REPORTE_MOVIMIENTO = tienePermiso("REPORTE_MOVIMIENTO");
    }
    
    boolean ANULACIONES_COBRO;
    boolean ANULACIONES_MOVIMIENTOS;
    boolean CAJA_MOVIMIENTOS;
    boolean CONFIGURACION_ACTIVIDADES;
    boolean CONFIGURACION_ARTICULOS;
    boolean CONFIGURACION_CONCEPTOS;
    boolean CONFIGURACION_USUARIOS;
    boolean ESTADISTICAS_GENERALES;
    boolean ESTADISTICAS_SOCIOS;
    boolean GESTIONAR_CLIENTES;
    boolean GESTIONAR_COBRO;
    boolean GESTIONAR_MARCAS;
    boolean GIMNASIO_GIMNASIOS;
    boolean REPORTES_COBRO;
    boolean REPORTE_DEUDA;
    boolean REPORTE_MOVIMIENTO;

    public boolean isANULACIONES_COBRO() {
        return ANULACIONES_COBRO;
    }

    public boolean isANULACIONES_MOVIMIENTOS() {
        return ANULACIONES_MOVIMIENTOS;
    }

    public boolean isCAJA_MOVIMIENTOS() {
        return CAJA_MOVIMIENTOS;
    }

    public boolean isCONFIGURACION_ACTIVIDADES() {
        return CONFIGURACION_ACTIVIDADES;
    }

    public boolean isCONFIGURACION_ARTICULOS() {
        return CONFIGURACION_ARTICULOS;
    }

    public boolean isCONFIGURACION_CONCEPTOS() {
        return CONFIGURACION_CONCEPTOS;
    }

    public boolean isCONFIGURACION_USUARIOS() {
        return CONFIGURACION_USUARIOS;
    }

    public boolean isESTADISTICAS_GENERALES() {
        return ESTADISTICAS_GENERALES;
    }

    public boolean isESTADISTICAS_SOCIOS() {
        return ESTADISTICAS_SOCIOS;
    }

    public boolean isGESTIONAR_CLIENTES() {
        return GESTIONAR_CLIENTES;
    }

    public boolean isGESTIONAR_COBRO() {
        return GESTIONAR_COBRO;
    }

    public boolean isGESTIONAR_MARCAS() {
        return GESTIONAR_MARCAS;
    }

    public boolean isGIMNASIO_GIMNASIOS() {
        return GIMNASIO_GIMNASIOS;
    }

    public boolean isREPORTES_COBRO() {
        return REPORTES_COBRO;
    }

    public boolean isREPORTE_DEUDA() {
        return REPORTE_DEUDA;
    }

    public boolean isREPORTE_MOVIMIENTO() {
        return REPORTE_MOVIMIENTO;
    }
    
    
    
}