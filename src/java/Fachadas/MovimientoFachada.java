package Fachadas;

import DAOs.MovimientoDAO;
import HBMs.Bitacora;
import HBMs.Concepto;
import HBMs.Movimiento;
import Utils.ManejadorSession;
import java.util.Date;

public class MovimientoFachada {

    private static MovimientoFachada instance = null;
    
    public static MovimientoFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new MovimientoFachada();
        }
        return instance;
    }
    
    private MovimientoFachada()  throws Exception{}
    
    public void validar(Movimiento movimiento, Concepto concepto) throws Exception{
        if (movimiento.getDescripcion()== null) throw new Exception("El campo Descripcion no puede ser vacio.");
        if (concepto == null) throw new Exception("El campo Concepto no puede ser vacio.");
        if (movimiento.getTipo()== null) throw new Exception("El campo Tipo no puede ser vacio.");
        
        if (movimiento.getImporte()== 0) throw new Exception("El campo Importe no puede ser vacio.");
        
        if ("".equals(movimiento.getDescripcion())) throw new Exception("El campo Descripcion no puede ser vacio.");
        if ("".equals(movimiento.getTipo())) throw new Exception("El campo Tipo no puede ser vacio.");
    }
    
    public void guardar(Movimiento movimiento, Concepto concepto) throws Exception{
        MovimientoFachada.getInstance().validar(movimiento,concepto);
        movimiento.setConcepto(concepto);
        Bitacora bitacora = new Bitacora();
        bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
        bitacora.setFecha(new Date());
        bitacora.setEstado("ACTIVO");
        movimiento.setBitacora(bitacora);
        MovimientoDAO.getInstance().guardar(movimiento);
    }
    
}
