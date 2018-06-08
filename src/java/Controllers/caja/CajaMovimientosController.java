
package Controllers.caja;

import DAOs.MovimientoDAO;
import HBMs.Movimiento;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "cajaMovimientosController")
@ViewScoped
public class CajaMovimientosController  implements Serializable{
    
    
    public List<Movimiento> obtenerMovimientos(){
        try{
            return MovimientoDAO.getInstance().obtenerMovimientos();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CAJA_MOVIMIENTO);
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CAJA_MOVIMIENTOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}