package Fachadas;

import DAOs.ClienteDAO;
import HBMs.Bitacora;
import HBMs.Cliente;
import Utils.FileUpload;
import Utils.ManejadorSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.primefaces.model.UploadedFile;

public class ClienteFachada {
    
    
    private static ClienteFachada instance = null;
    
    public static ClienteFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new ClienteFachada();
        }
        return instance;
    }
    
    private ClienteFachada()  throws Exception{
        
    }
    
    public void validar(Cliente cliente, String fnac, UploadedFile partImagenPerfil) throws Exception{
        
        //validacion de datos nulos
        if (cliente.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        if (cliente.getCedula() == null) throw new Exception("El campo Cedula no puede ser vacio.");
        if (fnac == null) throw new Exception("El campo Fecha de nacimiento no puede ser vacio.");
        if (cliente.getCorreo()== null) throw new Exception("El campo Correo no puede ser vacio.");
        if (cliente.getCelular()== null) throw new Exception("El campo Celular no puede ser vacio.");
        if (cliente.getMutualista()== null) throw new Exception("El campo Mutualista no puede ser vacio.");
        if (cliente.getSexo()== null) throw new Exception("El campo Sexo no puede ser vacio.");
        if (cliente.getId()==0 && partImagenPerfil == null ) throw new Exception("El campo Foto no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(cliente.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        if ("".equals(cliente.getCedula())) throw new Exception("El campo Cedula no puede ser vacio.");
        if ("".equals(fnac)) throw new Exception("El campo Fecha de nacimiento no puede ser vacio.");
        if ("".equals(cliente.getCorreo())) throw new Exception("El campo Correo no puede ser vacio.");
        if ("".equals(cliente.getCelular())) throw new Exception("El campo Celular no puede ser vacio.");
        if ("".equals(cliente.getMutualista())) throw new Exception("El campo Mutualista no puede ser vacio.");
        if ("".equals(cliente.getSexo())) throw new Exception("El campo Sexo no puede ser vacio.");
        
        //validacion del formato de fecha correcto
        try{
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = format.parse(fnac);
        }catch(ParseException e){
            throw new Exception("El formato de la fecha es incorrecto");
        }
    }
    
    
    public boolean guardar(Cliente cliente, String fnac, UploadedFile partImagenPerfil, FileUpload foto) throws Exception{
        validar( cliente,  fnac,  partImagenPerfil);
        boolean esModificacion = true;
        if (cliente.getId()>0) ClienteDAO.getInstance().update(cliente);
        if (cliente.getId()==0){
            esModificacion = false;
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date = format.parse(fnac);
            cliente.setFnac(date);
            foto.setNombreArchivo(cliente.getCedula());
            cliente.setImagen(foto.guardarImagenPerfil());
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
            bitacora.setFecha(new Date());
            bitacora.setEstado("Activo");
            cliente.setBitacora(bitacora);
            ClienteDAO.getInstance().guardar(cliente);
        }
        return esModificacion;
    }
    
    public Converter obtenerConversorCliente() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                Object id = (Object)value;
                try{
                    int index = (id instanceof String)? Integer.valueOf((String)id) : (int) id;
                    Cliente elemento = (Cliente) ClienteDAO.getInstance().findById(index);
                    return elemento;
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                try{
                    if (value != null){
                        Cliente elemento = (Cliente) value;
                        Object o = ClienteDAO.getInstance().getIdentifier(elemento);
                        String result = String.valueOf(o);
                        return result;
                    }
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
        };
    }
    
}
