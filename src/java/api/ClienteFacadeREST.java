package api;

import DAOs.ClienteDAO;
import DAOs.UsuarioDAO;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Usuario;
import Utils.FileUpload;
import Utils.HibernateProxyTypeAdapter;
import Utils.Seguridad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@Stateless
@Path("clientes")
public class ClienteFacadeREST{
    
    public ClienteFacadeREST() {}
    
    @GET
    @Path("listar/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerClientes(@PathParam("key") String key) {
        try {
            List<Cliente> clientes = ClienteDAO.getInstance().obtenerClientesREST(key);
            if (clientes == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron clientes en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(clientes); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
    @GET
    @Path("obtener/{id}/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerCliente(@PathParam("id") String id,@PathParam("key") String key) {
        try {
            Cliente cliente = ClienteDAO.getInstance().obtenerClienteREST(id, key);
            if (cliente == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron clientes en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(cliente); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
    //--------------------SINTAXIS DE ENTRADA-----------------------
//        {
//          "key": "fa3ff1f2fe04d563eedabd8c4f7433313bfc3d370ade29dd31d7ed13126591ed",
//          "cliente": {
//            "nombre": "Cliente1",
//            "cedula": "12345678",
//            "fnac": "09/01/1993",  -------> la fecha se le pasa en el siguiente formato dd/MM/yyyy para que gson la pueda parsear
//            "imagen": "data:image/png;base64, …",
//            "correo": "correo@correo.com.uy",
//            "celular": "123456789",
//            "mutualista": "AMSJ",
//            "sexo": "Masculino"
//          }
//        }
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearEntidad(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        String claseJSON = "cliente";
        Cliente entidad = new Cliente();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                crunchifyBuilder.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        System.out.println("Data Received: " + crunchifyBuilder.toString());
        String jsonString = crunchifyBuilder.toString();
        try {
            JSONObject rootObject = new JSONObject(jsonString);
            //obtener la key
            String key = rootObject.getString("key");
            if (key == null || "".equals(key)) return Response.serverError().entity("Error al obtener la clave del usuario.").build();
            //obtener usuario responsable
            Usuario usuario = UsuarioDAO.getInstance().obtenerUsuarioREST(key);
            if (usuario == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //obtener la entidad en JSON
            JSONObject entidadJSON = (JSONObject) rootObject.get(claseJSON);
            String jsonUsuarioString = entidadJSON.toString();
            GsonBuilder b = new GsonBuilder();
            b.setDateFormat("dd/MM/yyyy").registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            //obtener la entidad a partir del json
            entidad = gson.fromJson(jsonUsuarioString, entidad.getClass());
            validar(entidad);
            //setear la bitacora (Tambien evita que se setee manualmente en la request)
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(usuario);
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            entidad.setBitacora(bitacora);
            //ajustar parametros que no deberian venir en la request para evitar que se guarden (Error de seguridad)
            entidad.setMarcas(null);
            entidad.setCobros(null);
            
            if (entidad.getImagen()!=null){
                try{
            //setea la imagen al usuario
            String delims = "[,]";
            String[] parts = entidad.getImagen().split(delims);
            String imageString = parts[1];
            byte[] imageByteArray = Base64.getDecoder().decode(imageString);
            FileUpload foto = new FileUpload().cargarImagenEnBuffer(imageByteArray);
            foto.setNombreArchivo(entidad.getCedula());
            entidad.setImagen(foto.guardarImagenPerfil());
             }catch(Exception e){
                    throw new Exception("Error al cargar la imagen del usuario");
                }
            }else{
                entidad.setImagen("/img/profilePicture/default.jpg");
            }
            
            //guardar en base
            ClienteDAO.getInstance().crearClienteREST(entidad);
            ajustarBitacoraRest(bitacora);
            //se retorna un json con la entidad (tambien se retorna la bitacora con el usuario y su hash key, pero no importa porque es el mismo que se envio en la request)
            String json = gson.toJson(entidad);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JSONException ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            //return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.serverError().entity("Error al crear "+ "Cliente" +".").build();
    }
    
    private void validar(Cliente cliente) throws Exception{
        //validacion de datos nulos
        if (cliente.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        if (cliente.getCedula()== null) throw new Exception("El campo Cedula no puede ser vacio.");
        //if (cliente.getImagen()== null) throw new Exception("El campo Imagen no puede ser vacio."); //ahora puede ser nulo porque le pone la imagen por defecto
        if (cliente.getCorreo()== null) throw new Exception("El campo Correo no puede ser vacio.");
        if (cliente.getCelular()== null) throw new Exception("El campo Celular no puede ser vacio.");
        if (cliente.getMutualista()== null) throw new Exception("El campo Mutualista no puede ser vacio.");
        if (cliente.getSexo()== null) throw new Exception("El campo Sexo no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(cliente.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        if ("".equals(cliente.getCedula())) throw new Exception("El campo Cedula no puede ser vacio.");
        if ("".equals(cliente.getImagen())) throw new Exception("El campo Imagen no puede ser vacio.");
        if ("".equals(cliente.getCorreo())) throw new Exception("El campo Correo no puede ser vacio.");
        if ("".equals(cliente.getCelular())) throw new Exception("El campo Celular no puede ser vacio.");
        if ("".equals(cliente.getMutualista())) throw new Exception("El campo Mutualista no puede ser vacio.");
        if ("".equals(cliente.getSexo())) throw new Exception("El campo Sexo no puede ser vacio.");
        
        //validacion del campo id
        if (cliente.getId() != 0) throw new Exception("El campo Id debe ser vacio.");
        
        //validacion del campo cedula
        if (! new Seguridad().cedulaValida(cliente.getCedula())) throw new Exception("La cedula ingresada no es valida.");
        
        //validacion del campo sexo
        if (!"Masculino".equals(cliente.getSexo()) && !"Femenino".equals(cliente.getSexo()) ) throw new Exception("El campo Sexo debe ser ('Masculino' - 'Femenino').");
        
    }
    
    private void ajustarBitacoraRest(Bitacora bitacora){
        bitacora.setActividads(null);
        bitacora.setArticulos(null);
        bitacora.setClientes(null);
        bitacora.setCobros(null);
        bitacora.setConceptos(null);
        bitacora.setMarcas(null);
        bitacora.setMovimientos(null);
    }
}
