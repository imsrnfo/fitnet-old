
package Controllers.reportes;

import DAOs.CobroDAO;
import HBMs.Cliente;
import HBMs.Cobro;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "reportesDeudasController")
@ViewScoped
public class ReportesDeudasController implements Serializable{
    
    public List<Cobro> obtenerDeudasPorCliente(){
        try{
            List<Cobro> result = new ArrayList<>();
            List<Object[]> deudasClientes = CobroDAO.getInstance().obtenerDeudasDeClientes(ManejadorSession.getInstance().obtenerGimnasioLogeado());
            if (deudasClientes!=null){
                for (Object[] item : deudasClientes){
                    Cobro cobro = new Cobro();
                    Cliente cli = (Cliente) item[0];
                    Long deudaLong = (Long) item[1];
                    cobro.setCliente(cli);
                    cobro.setDebe(deudaLong.intValue());
                    result.add(cobro);
                }
            }
            return result;
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("REPORTE_DEUDA")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}
