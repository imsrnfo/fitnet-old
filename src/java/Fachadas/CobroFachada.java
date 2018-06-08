package Fachadas;

import DAOs.CobroDAO;
import HBMs.Actividad;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.CobroArticulo;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CobroFachada {
    
   
    private static CobroFachada instance = null;
    
    public static CobroFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new CobroFachada();
        }
        return instance;
    }
    
    private CobroFachada()  throws Exception{
      
    }
    
    public void validar(Cliente cliente, Cobro cobro, String fdesde, String fhasta, Actividad actividad) throws Exception{
        
        //validacion de datos nulos
        //if (actividad == null) throw new Exception("El campo Actividad no puede ser vacio.");
        if (cliente == null) throw new Exception("El campo Cliente no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (cobro.getImporteTotal()== 0) throw new Exception("El campo Importe Total no puede ser vacio.");
        
        //validacion de datos vacios
        if (actividad!=null){
            if ("".equals(fdesde)) throw new Exception("El campo Fecha Desde no puede ser vacio si Actividad no es vacio.");
            if ("".equals(fhasta)) throw new Exception("El campo Fecha Hasta no puede ser vacio si Actividad no es vacio.");
        }
        
        
        if (cobro.getImporteTotal() == 0) throw new Exception("El campo Importe Total no puede ser vacio.");
        //if (cobro.getDescuento()== 0) throw new Exception("El campo Descuento no puede ser vacio.");
        //if (cobro.getMotivoDescuento()== "") throw new Exception("El campo Motivo de Descuento no puede ser vacio.");
        //if (cobro.getEntrega()== 0) throw new Exception("El campo Entrega no puede ser vacio.");
        
        if (!"".equals(fdesde) && !"".equals(fhasta)){
            
            //validacion del formato de fecha correcto
            Date fechaDesde;
            Date fechaHasta;
            try{
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                fechaDesde = format.parse(fdesde);
                fechaHasta = format.parse(fhasta);
            }catch(ParseException e){
                throw new Exception("El formato de la fecha es incorrecto");
            }
            
            //fecha desde no puede ser menor que fecha hasta
            if (!fechaDesde.before(fechaHasta)){
                throw new Exception("El campo Fecha Desde debe ser menor estricto que el campo Fecha Hasta.");
            }
            
        }
    }
    
    public void guardar(Cliente cliente, Cobro cobro, String fdesde, String fhasta,List<CobroArticulo> listaCobroArticulo, Actividad actividad) throws Exception{
            if (!ManejadorSession.getInstance().tienePermiso("GESTIONAR_COBRO")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            if (cobro.getCobroArticulos()==null) cobro.setCobroArticulos(new HashSet<>());
            cobro.getCobroArticulos().addAll(listaCobroArticulo);
            cobro.setCliente(cliente);
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            cobro.setBitacora(bitacora);
            cobro.setFecha(new Date());
            int result = 0;
            for (CobroArticulo item : listaCobroArticulo){
                result+=item.getArticulo().getPrecio()*item.getCantidad();
            }
            if (actividad!=null) result += actividad.getPrecio();
            cobro.setImporteTotal(result);
            validar(cliente,cobro,fdesde,fhasta, actividad);
            int deuda = cobro.getImporteTotal() - cobro.getDescuento() - cobro.getEntrega();
            if (deuda < 0) deuda = 0;
            cobro.setDebe(deuda);
            if (actividad==null) {
                fdesde = "";
                fhasta = "";
            }else{
                cobro.setActividad(actividad);
            }
            if (!"".equals(fdesde) && !"".equals(fhasta)){
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date fechaDesde = format.parse(fdesde);
                Date fechaHasta = format.parse(fhasta);
                cobro.setFdesde(fechaDesde);
                cobro.setFhasta(fechaHasta);
            }
            System.out.println("guardando...");
            CobroDAO.getInstance().guardarCobroYCobroActividad(cobro);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.REPORTES_COBRO);
    }
    
   
}
