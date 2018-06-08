package Utils;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

public class ManejadorNavegacion {
    
    public static String INICIO = "/inicio";
    public static String ANULACIONES_COBRO = "/anulaciones/cobro";
    public static String ANULACIONES_MOVIMIENTOS = "/anulaciones/movimientos";
    public static String CAJA_MOVIMIENTOS = "/caja/movimientos";
    public static String CAJA_MOVIMIENTO = "/caja/movimiento";
    public static String CONFIGURACION_ACTIVIDADES = "/configuracion/actividades";
    public static String CONFIGURACION_ARTICULOS = "/configuracion/articulos";
    public static String CONFIGURACION_CONCEPTOS = "/configuracion/conceptos";
    public static String CONFIGURACION_USUARIOS = "/configuracion/usuarios";
    public static String CONFIGURACION_ACTIVIDAD = "/configuracion/actividad";
    public static String CONFIGURACION_ARTICULO = "/configuracion/articulo";
    public static String CONFIGURACION_CONCEPTO = "/configuracion/concepto";
    public static String CONFIGURACION_USUARIO = "/configuracion/usuario";
    public static String GESTIONAR_CLIENTES = "/gestionar/clientes";
    public static String GESTIONAR_MARCAS = "/gestionar/marcas";
    public static String GESTIONAR_CLIENTE = "/gestionar/cliente";
    public static String GESTIONAR_COBRO = "/gestionar/cobro";
    public static String GIMNASIO_GIMNASIOS = "/gimnasio/gimnasios";
    public static String GIMNASIO_GIMANSIO = "/gimnasio/gimnasio";
    public static String MENU_CLIENTELOGIN = "/menu/login";
    public static String REPORTES_COBRO = "/reportes/cobros";
    public static String REPORTE_DEUDA = "/reportes/deudas";
    public static String REPORTE_MOVIMIENTO = "/reportes/movimientos";
    public static String ESTADISTICAS_GENERALES = "/estadisticas/generales";
    public static String ESTADISTICAS_SOCIOS = "/estadisticas/socios";
    
    private static ManejadorNavegacion instance = null;
    
    public static ManejadorNavegacion getInstance() {
        if(instance == null) {
            instance = new ManejadorNavegacion();
        }
        return instance;
    }
    
    private ManejadorNavegacion() {
    }
    
    public void redirigir(String path){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+path);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorNavegacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String obtenerRequestParametro(String key){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
        return params.get(key);
    }
    
    public String obtenerDireccion(String recurso){
        String result = "";
        switch (recurso) {
            case "INICIO":
                result = INICIO;
                break;
            case "ANULACIONES_COBRO":
                result = ANULACIONES_COBRO;
                break;
            case "ANULACIONES_MOVIMIENTOS":
                result = ANULACIONES_MOVIMIENTOS;
                break;
            case "CAJA_MOVIMIENTOS":
                result = CAJA_MOVIMIENTOS;
                break;
            case "CAJA_MOVIMIENTO":
                result = CAJA_MOVIMIENTO;
                break;
            case "CONFIGURACION_ACTIVIDADES":
                result = CONFIGURACION_ACTIVIDADES;
                break;
            case "CONFIGURACION_ARTICULOS":
                result = CONFIGURACION_ARTICULOS;
                break;
            case "CONFIGURACION_CONCEPTOS":
                result = CONFIGURACION_CONCEPTOS;
                break;
            case "CONFIGURACION_USUARIOS":
                result = CONFIGURACION_USUARIOS;
                break;
            case "CONFIGURACION_ACTIVIDAD":
                result = CONFIGURACION_ACTIVIDAD;
                break;
            case "CONFIGURACION_ARTICULO":
                result = CONFIGURACION_ARTICULO;
                break;
            case "CONFIGURACION_CONCEPTO":
                result = CONFIGURACION_CONCEPTO;
                break;
            case "CONFIGURACION_USUARIO":
                result = CONFIGURACION_USUARIO;
                break;
            case "GESTIONAR_CLIENTES":
                result = GESTIONAR_CLIENTES;
                break;
            case "GESTIONAR_MARCAS":
                result = GESTIONAR_MARCAS;
                break;
            case "GESTIONAR_CLIENTE":
                result = GESTIONAR_CLIENTE;
                break;
            case "GESTIONAR_COBRO":
                result = GESTIONAR_COBRO;
                break;
            case "GIMNASIO_GIMNASIOS":
                result = GIMNASIO_GIMNASIOS;
                break;
            case "GIMNASIO_GIMANSIO":
                result = GIMNASIO_GIMANSIO;
                break;
            case "MENU_CLIENTELOGIN":
                result = MENU_CLIENTELOGIN;
                break;
            case "REPORTES_COBRO":
                result = REPORTES_COBRO;
                break;
            case "REPORTE_DEUDA":
                result = REPORTE_DEUDA;
                break;
                case "REPORTE_MOVIMIENTO":
                result = REPORTE_MOVIMIENTO;
                break;
            case "ESTADISTICAS_GENERALES":
                result = ESTADISTICAS_GENERALES;
                break;
            case "ESTADISTICAS_SOCIOS":
                result = ESTADISTICAS_SOCIOS;
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
    
}
