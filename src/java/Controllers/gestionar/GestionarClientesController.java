
package Controllers.gestionar;

import DAOs.ClienteDAO;
import HBMs.Cliente;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "gestionarClientesController")
@ViewScoped
public class GestionarClientesController implements Serializable{
    
    public List<Cliente> obtenerClientes(){
        try {
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            return ClienteDAO.getInstance().obtenerClientes();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_CLIENTE);
    }
    
    public void detalle(int id_cliente) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_CLIENTE+"/"+id_cliente+"/detalle");
    }
    
    public void modificar(int id_cliente) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_CLIENTE+"/"+id_cliente+"/modificar");
    }
    
    public void borrar(int id_cliente){
        try {
            Cliente cli = ClienteDAO.getInstance().buscarPorIdConBitacora(id_cliente);
            ClienteDAO.getInstance().borrar(cli);
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
    
    public void cobrar(int id_cliente){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_COBRO+"/"+id_cliente);
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("GESTIONAR_CLIENTES")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}
