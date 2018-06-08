
package Controllers.reportes;

import DAOs.MovimientoDAO;
import HBMs.Movimiento;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "reportesMovimientosController")
@ViewScoped
public class ReportesMovimientosController  implements Serializable{
    
    public List<Movimiento> obtenerMovimientos(){
        try{
            return MovimientoDAO.getInstance().obtenerMovimientos();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("REPORTE_MOVIMIENTO")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}