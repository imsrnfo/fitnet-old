
package Controllers.menu;

import DAOs.ClienteDAO;
import DAOs.CobroDAO;
import DAOs.MarcaDAO;
import HBMs.Actividad;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.Gimnasio;
import HBMs.Marca;
import HBMs.Usuario;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;


@Named(value = "menuLoginController")
@ViewScoped
public class MenuLoginController implements Serializable{
    
    private boolean forzarMarca = false;
    private List<Cliente> clientesLogueados;
    
    public List<Cliente> getClientesLogueados() {
        return clientesLogueados;
    }
    
    public void setClientesLogueados(List<Cliente> clientesLogueados) {
        this.clientesLogueados = clientesLogueados;
    }
    
    public String obtenerImagenGimnasio(){
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return (gym!=null)?gym.getImagen():"";
    }
    
    public String obtenerNombreGimnasio(){
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return (gym!=null)?gym.getNombre():"";
    }
    
    public String obtenerNombreUsuario(){
        Usuario usr = ManejadorSession.getInstance().obtenerUsuarioLogeado();
        return (usr!=null)?usr.getNombre():"";
    }
    
    public String obtenerImagenUsuario(){
        Usuario usr = ManejadorSession.getInstance().obtenerUsuarioLogeado();
        return (usr!=null)?usr.getImagen():"";
    }
    
    public void clienteLogin(String cedula){
        try{
            Cliente cli = ClienteDAO.getInstance().obtenerClientesPorCedula(cedula);
            
            if (cli == null) throw new Exception("No existe cliente asociado al numero de cedula.");
            
            boolean validarLoginPorPlazoActividad = false;
            
            List<Cobro> listaCobro = CobroDAO.getInstance().buscarInicializarActividadCobrosPorCliente(cli);
            Actividad actividadQueIngresa = null;
            for (Cobro item : listaCobro){
                if (item.getActividad()!=null){
                    if (item.getFdesde()!=null && item.getFecha()!=null){
                        Date hoy = new Date();
                        //la fecha actual debe ser igual o mayor a la fecha inicio e igual o menor a la fecha de finalizacion.
                        if (!hoy.before(item.getFdesde()) && !hoy.after(item.getFhasta())){
                            validarLoginPorPlazoActividad = true;
                            actividadQueIngresa = item.getActividad();
                        }
                        //verificar que no haya entrado mas veces de la que indica el atributo dias de la actividad
                        //PARA HACER ESTO SE REQUIERE QUE LA MARCA TENGA UNA ACTIVIDAD ASOCIADA, PERO EL TEMA ES A LA HORA DE ENTRAR A QUE ACTIVIDAD ENTRA
                    }
                }
            }
            RequestContext requestContext = RequestContext.getCurrentInstance();
            if (validarLoginPorPlazoActividad || forzarMarca){
                if (MarcaDAO.getInstance().obtenerMarcaClientePorDia(cli, new Date())!=null) throw new Exception("El cliente ya ingreso hoy.");
                Marca marca = new Marca();
                marca.setCliente(cli);
                marca.setFecha(new Date());
                // if (actividadQueIngresa!=null) marca.setActividad(actividadQueIngresa); //Si la entrada no fue forzada, se guarda la actividad que ingresa.
                Bitacora bitacora = new Bitacora();
                bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
                bitacora.setFecha(new Date());
                bitacora.setEstado("ACTIVO");
                marca.setBitacora(bitacora);
                MarcaDAO.getInstance().guardar(marca);
                
                try{
                    clientesLogueados = new ArrayList<>();
                    List<Marca> marcashoy = MarcaDAO.getInstance().obtenerAccesosHoy();
                    for (Marca item : marcashoy){
                        clientesLogueados.add(item.getCliente());
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                
                requestContext.execute("toastr.success('Login Exitoso')");
                
            }else{
                requestContext.execute("plazoVencido("+cedula+");");
                //throw new Exception("No existe actividad activa para el cliente.");
            }
        }catch(Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void permitirAccesso(String cedula, String password){
        Usuario usr = ManejadorSession.getInstance().obtenerUsuarioLogeado();
        if (usr.getPassword().equals(password)){
            forzarMarca = true;
            clienteLogin(cedula);
        }else{
            RequestContext.getCurrentInstance().execute("toastr.error('"+"Password de administrador invalido."+"');");
        }
    }
    
    @PostConstruct
    public void init(){
        if (ManejadorSession.getInstance().obtenerUsuarioLogeado()==null) ManejadorNavegacion.getInstance().redirigir("/");
        try{
            clientesLogueados = new ArrayList<>();
            List<Marca> marcashoy = MarcaDAO.getInstance().obtenerAccesosHoy();
            for (Marca marca : marcashoy){
                clientesLogueados.add(marca.getCliente());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
