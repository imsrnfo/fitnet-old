
package Controllers.gestionar;

import DAOs.ActividadDAO;
import DAOs.ClienteDAO;
import DAOs.CobroDAO;
import DAOs.GimnasioDAO;
import Fachadas.ActividadFachada;
import Fachadas.ArticuloFachada;
import Fachadas.ClienteFachada;
import Fachadas.CobroFachada;
import HBMs.Actividad;
import HBMs.Articulo;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.CobroArticulo;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;


@Named(value = "gestionarCobroController")
@ViewScoped
public class GestionarCobroController implements Serializable{
    
    Actividad actividad;
    
    Articulo articulo;
    
    Cliente cliente;
    
    CobroArticulo cobroArticulo;
    
    Cobro cobro = new Cobro();
    
    List<CobroArticulo> listaCobroArticulo = new ArrayList<>();
    
    String fdesde;
    
    String fhasta;

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
    
    public String getFdesde() {
        return fdesde;
    }
    
    public void setFdesde(String fdesde) {
        this.fdesde = fdesde;
    }
    
    public String getFhasta() {
        return fhasta;
    }
    
    public void setFhasta(String fhasta) {
        this.fhasta = fhasta;
    }
    
    public List<CobroArticulo> getListaCobroArticulo() {
        return listaCobroArticulo;
    }
    
    public void setListaCobroActividad(List<CobroArticulo> listaCobroArticulo) {
        this.listaCobroArticulo = listaCobroArticulo;
    }
    
    public Cobro getCobro() {
        return cobro;
    }
    
    public void setCobro(Cobro cobro) {
        this.cobro = cobro;
    }
    
    public CobroArticulo getCobroActividad() {
        if(cobroArticulo == null){
            cobroArticulo = new CobroArticulo();
        }
        return cobroArticulo;
    }
    
    public void setCobroActividad(CobroArticulo cobroArticulo) {
        this.cobroArticulo = cobroArticulo;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Actividad getActividad() {
        return actividad;
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
        String dt = "";
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            dt = df.format(new Date());
            this.fdesde = dt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, actividad.getDias());  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
            this.fhasta = dt;
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
    
    public List<Actividad> obtenerActividades(){
        try{
            return ActividadFachada.getInstance().obtenerActividades();
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public List<Articulo> obtenerArticulos(){
        try{
            return ArticuloFachada.getInstance().obtenerArticulos();
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void guardar(){
        try{
            CobroFachada.getInstance().guardar(cliente,cobro,fdesde,fhasta,listaCobroArticulo, actividad);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ACTIVIDADES);
    }
    
    
    
    public List<Cobro> obtenerCobrosPorCliente(){
        try{
            if (cliente!=null){
                return  CobroDAO.getInstance().buscarInicializarCobrosPorCliente(cliente);
            }else{
                return new ArrayList<>();
            }
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public List<Cobro> obtenerCobros(){
        try{
            if (cliente!=null){
                return  CobroDAO.getInstance().buscarInicializarCobros();
            }else{
                return new ArrayList<>();
            }
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public int obtenerImporteTotal(){
        int result = 0;
        for (CobroArticulo item : listaCobroArticulo){
            result+=item.getArticulo().getPrecio()*item.getCantidad();
        }
        if (actividad!=null) result += actividad.getPrecio();
        return result;
    }
    
    public int calcularImporteTotalConDescuento(){
        int result = 0;
        for (CobroArticulo item : listaCobroArticulo){
            result+=item.getArticulo().getPrecio()*item.getCantidad();
        }
        if (actividad!=null)result+=actividad.getPrecio();
        return result - cobro.getDescuento();
    }
    
    public void agregarCobroArticulo(){
        try{
            validarAgregarCobroArticulo();
            cobroArticulo.setArticulo(articulo);
            cobroArticulo.setCobro(cobro);
            listaCobroArticulo.add(cobroArticulo);
            cobroArticulo = new CobroArticulo();
        }catch(Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void validarAgregarCobroArticulo() throws Exception{
        //validacion de datos nulos
        //if (cliente == null) throw new Exception("El campo Cliente no puede ser vacio.");
        if (articulo == null) throw new Exception("El campo Articulo no puede ser vacio.");
        if (cobroArticulo.getCantidad()== null) throw new Exception("El campo Cantidad no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (cobroArticulo.getCantidad()== 0) throw new Exception("El campo Cantidad no puede ser vacio.");
    }
    
    public Converter obtenerConversorCliente() throws Exception {
        return ClienteFachada.getInstance().obtenerConversorCliente();
    }
    
    public Converter obtenerConversorActividad() throws Exception {
        return ActividadFachada.getInstance().obtenerConversorActividad();
    }
    
    public Converter obtenerConversorArticulo() throws Exception{
        return ArticuloFachada.getInstance().obtenerConversorArticulo();
    }
    
    @PostConstruct
    public void init(){
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_cliente")!=null){
                if (!StringUtils.isNumeric(String.valueOf(params.get("id_cliente")))) cancelar();
                int id_cliente = Integer.valueOf(params.get("id_cliente"));
                Cliente cli = ClienteDAO.getInstance().findById(id_cliente);
                if (cli == null) cancelar();
                if (GimnasioDAO.getInstance().obtenerGimnasioPorEntidad(cli).getId() == ManejadorSession.getInstance().obtenerGimnasioLogeado().getId()) cliente = cli;
            }else{
                cliente = new Cliente();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public String obtenerFechaFinalizacionActividad(Actividad actividad){
        String dt = "";
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            dt = df.format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, actividad.getDias());  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
            return dt;
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public List<Cliente> obtenerClientes(){
        try {
            return ClienteDAO.getInstance().obtenerClientes();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
}
