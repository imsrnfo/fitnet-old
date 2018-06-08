
package Controllers.gestionar;

import DAOs.ClienteDAO;
import DAOs.GimnasioDAO;
import Fachadas.ClienteFachada;
import HBMs.Cliente;
import Utils.FileUpload;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import Utils.ConversorGenerico;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;


@Named(value = "gestionarClienteController")
@ViewScoped
public class GestionarClienteController implements Serializable{
    
    Cliente cliente;
    
    UploadedFile partImagenPerfil;
    
    String fnac;
    
    FileUpload foto;
    
    public String getFnac() {
        return fnac;
    }
    
    public void setFnac(String fnac) {
        this.fnac = fnac;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    GestionarClienteController(){}
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("GESTIONAR_CLIENTES")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_cliente")!=null){
                if (!StringUtils.isNumeric(String.valueOf(params.get("id_cliente")))) cancelar();
                int id_cliente = Integer.valueOf(params.get("id_cliente"));
                Cliente cli = ClienteDAO.getInstance().buscarPorId(id_cliente);
                if (cli == null) cancelar();
                if (GimnasioDAO.getInstance().obtenerGimnasioPorEntidad(cli).getId() == ManejadorSession.getInstance().obtenerGimnasioLogeado().getId()){
                    cliente = cli;
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    fnac = df.format(cliente.getFnac());
                }
            }else{
                cliente = new Cliente();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public void guardar() throws ParseException{
        try{
            if (ClienteFachada.getInstance().guardar( cliente,  fnac,  partImagenPerfil, foto))
                ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_CLIENTES);
            else
                ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_COBRO+"/"+cliente.getId());
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GESTIONAR_CLIENTES);
    }
    
    

    public UploadedFile getPartImagenPerfil() {
        return partImagenPerfil;
    }

    public void setPartImagenPerfil(UploadedFile partImagenPerfil) throws IOException {
        this.partImagenPerfil = partImagenPerfil;
        foto = new FileUpload().cargarImagenEnBuffer(partImagenPerfil);
    }
    
    public void cargarSubirImagen(){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("subirImagen();");
    }
    
    public void cargarImagen() throws IOException{
        foto = new FileUpload().cargarImagenEnBuffer(partImagenPerfil);
    }
    
}
