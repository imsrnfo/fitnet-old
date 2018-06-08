package api;

import DAOs.GimnasioDAO;
import DAOs.UsuarioDAO;
import HBMs.Permiso;
import HBMs.PermisoUsuario;
import HBMs.Usuario;
import Utils.FileUpload;
import Utils.HibernateProxyTypeAdapter;
import Utils.Seguridad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Path("usuarios")
public class UsuarioFacadeREST{
    
    public UsuarioFacadeREST() {
        
    }
    
    @GET
    @Path("listar/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerUsuarios(@PathParam("key") String key) {
        try {
            List<Usuario> usuarios = UsuarioDAO.getInstance().obtenerUsuariosREST(key);
            if (usuarios == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron usuarioos en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(usuarios); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
     @GET
    @Path("obtenerPermisos/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerUsuarioConPermisos(@PathParam("key") String key) {
        try {
            Usuario user = UsuarioDAO.getInstance().obtenerUsuarioConPermisosREST(key);
            if (user == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(user); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
    @GET
    @Path("obtener/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerUsuario(@PathParam("key") String key) {
        try {
            Usuario user = UsuarioDAO.getInstance().obtenerUsuarioREST(key);
            if (user == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(user); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
//--------------------SINTAXIS DE ENTRADA-----------------------
//    {
//        "key": "fa3ff1f2fe04d563eedabd8c4f7433313bfc3d370ade29dd31d7ed13126591ed",
//        "usuario": {
//          "username": "ignacio",
//          "password": "vamospormas",
//          "nombre": "Ignacio Silva",
//          "imagen": "data:image/png;base64, …"
//        }
//    }
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearEntidad(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
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
            String key = rootObject.getString("key");
            if (key == null || "".equals(key)) return Response.serverError().entity("Error al obtener la clave del usuario.").build();
            Usuario usuario = UsuarioDAO.getInstance().obtenerUsuarioREST(key);
            if (usuario == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            JSONObject usuarioJSON = (JSONObject) rootObject.get("usuario");
            String jsonUsuarioString = usuarioJSON.toString();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            Usuario user = gson.fromJson(jsonUsuarioString, Usuario.class);
            
            //setea los permisos al usuario
            Set<PermisoUsuario> permisosUsuario = new HashSet(0);
            for (Object object : user.getPermisoUsuarios()){
                if (object instanceof LinkedTreeMap){
                    LinkedTreeMap ltm = (LinkedTreeMap) object;
                    if (ltm.get("permiso")!=null && ltm.get("permiso") instanceof LinkedTreeMap){
                         LinkedTreeMap permisoLTM = (LinkedTreeMap) ltm.get("permiso");
                         int idPermiso = (permisoLTM!=null && permisoLTM.get("id")!=null) ? ((Double) permisoLTM.get("id")).intValue() : 0;
                         PermisoUsuario pu = new PermisoUsuario();
                         Permiso p = UsuarioDAO.getInstance().obtenerPermisoPorId(idPermiso);
                         pu.setPermiso(p);
                        pu.setUsuario(user);
                        permisosUsuario.add(pu);
                    } 
                }
            }
            if (permisosUsuario.size()>0){
                user.setPermisoUsuarios(permisosUsuario);
            }else{
                user.setPermisoUsuarios(new HashSet(0));
            }
            validar(user);
            user.setGimnasio(GimnasioDAO.getInstance().obtenerGimnasioRest(key));
            user.setHashkey(new Seguridad().getPasswordSeguro(user.getUsername(), user.getPassword()));
            
            if (user.getImagen()!=null){
                try{
                    //setea la imagen al usuario
                    String delims = "[,]";
                    String[] parts = user.getImagen().split(delims);
                    String imageString = parts[1];
                    byte[] imageByteArray = Base64.getDecoder().decode(imageString);
                    FileUpload foto = new FileUpload().cargarImagenEnBuffer(imageByteArray);
                    foto.setNombreArchivo(user.getUsername());
                    user.setImagen(foto.guardarImagenPerfil());
                }catch(Exception e){
                    throw new Exception("Error al cargar la imagen del usuario");
                }
            }else{
                user.setImagen("/img/profilePicture/default.jpg");
            }
            
            UsuarioDAO.getInstance().crearUsuarioConPermisosREST(user);
            user = UsuarioDAO.getInstance().obtenerUsuarioConPermisosPorIdREST(user.getId());
            String json = gson.toJson(user);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JSONException ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
    }
    
    private void validar(Usuario usuario) throws Exception{
        if (usuario.getId()!=0) throw new Exception("El campo Id debe ser vacio.");
        
        if (usuario.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        if (usuario.getUsername()== null) throw new Exception("El campo Nick no puede ser vacio.");
        if (usuario.getPassword()== null) throw new Exception("El campo Contraseña no puede ser vacio.");
        
        if ("".equals(usuario.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        if ("".equals(usuario.getUsername())) throw new Exception("El campo Nick no puede ser vacio.");
        if ("".equals(usuario.getPassword())) throw new Exception("El campo Contraseña no puede ser vacio.");
        if ("".equals(usuario.getImagen())) throw new Exception("El campo Imagen no puede ser vacio.");
    }
}
