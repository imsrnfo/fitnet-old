
package Controllers.anulaciones;

import DAOs.MovimientoDAO;
import HBMs.Movimiento;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "anulacionesMovimientosController")
@ViewScoped
public class AnulacionesMovimientosController  implements Serializable{
    
    public List<Movimiento> obtenerMovimientos(){
        try{
        return MovimientoDAO.getInstance().obtenerMovimientos();
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
   
    public void anularMovimiento(String id_movimiento){
    }

    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("ANULACIONES_MOVIMIENTOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}