package Controllers.root;

import DAOs.GimnasioDAO;
import Fachadas.GimnasioFachada;
import HBMs.Gimnasio;
import HBMs.Usuario;
import Utils.FileUpload;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import Utils.ConversorGenerico;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

@Named(value = "rootGimnasioController")
@ViewScoped
public class RootGimnasioController implements Serializable{
    
    private UploadedFile partLogo;
    
    private UploadedFile partFoto;
    
    Gimnasio gimnasio;
    
    Usuario usuario;
    
    FileUpload logo;
    
    FileUpload foto;
    
    public UploadedFile getPartLogo() {
        return partLogo;
    }
    
    public void setPartLogo(UploadedFile partLogo) throws IOException {
        this.partLogo = partLogo;
        if (partLogo!=null) logo = new FileUpload().cargarImagenEnBuffer(partLogo);
    }
    
    public UploadedFile getPartFoto() {
        return partFoto;
    }
    
    public void setPartFoto(UploadedFile partFoto) throws IOException {
        this.partFoto = partFoto;
        if (partFoto!=null) foto = new FileUpload().cargarImagenEnBuffer(partFoto);
    }
    
    public void cargarSubirImagenLogo(){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("subirImagenLogo();");
    }
    
    public void cargarSubirImagenFoto(){
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("subirImagenFoto();");
    }
    
    public void cargarImagenLogo() throws IOException{
        foto = new FileUpload().cargarImagenEnBuffer(partLogo);
    }
    
    public void cargarImagenFoto() throws IOException{
        foto = new FileUpload().cargarImagenEnBuffer(partFoto);
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Gimnasio getGimnasio() {
        return gimnasio;
    }
    
    public void setGimnasio(Gimnasio gimnasio) {
        this.gimnasio = gimnasio;
    }
    
    RootGimnasioController(){}
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("GIMNASIO_GIMNASIOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_gimnasio")!=null){
                int id_gimnasio = Integer.valueOf(params.get("id_gimnasio"));
                gimnasio = GimnasioDAO.getInstance().findById(id_gimnasio);
            }else{
                gimnasio = new Gimnasio();
                usuario = new Usuario();
            }
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
    }
    
    public void guardar(){
        try{
            GimnasioFachada.getInstance().guardar(gimnasio, logo, foto, usuario);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GIMNASIO_GIMNASIOS);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("alert('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GIMNASIO_GIMNASIOS);
    }
}