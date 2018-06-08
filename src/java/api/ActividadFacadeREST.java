package api;

import DAOs.ActividadDAO;
import DAOs.UsuarioDAO;
import HBMs.Actividad;
import HBMs.Bitacora;
import HBMs.Usuario;
import Utils.HibernateProxyTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
@Path("actividades")
public class ActividadFacadeREST{
    
    public ActividadFacadeREST() {
        
    }
    
    @GET
    @Path("listar/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerActividades(@PathParam("key") String key) {
        try {
            List<Actividad> actividades = ActividadDAO.getInstance().obtenerActividadesREST(key);
            if (actividades == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontraron actividades en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(actividades); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener la actividad de la base de datos.").build();
        }
    }
    
    @GET
    @Path("obtener/{id}/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerActividad(@PathParam("id") String id,@PathParam("key") String key) {
        try {
            Actividad actividad = ActividadDAO.getInstance().obtenerActividadREST(id, key);
            if (actividad == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro la actividad en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(actividad); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener la actividad de la base de datos.").build();
        }
    }
    
    
    //--------------------SINTAXIS DE ENTRADA-----------------------
//    {
//      "key": "fa3ff1f2fe04d563eedabd8c4f7433313bfc3d370ade29dd31d7ed13126591ed",
//      "actividad": {
//        "nombre": "Musculacion",
//        "precio": 500,
//        "dias": 30,
//        "periodo": 30
//      }
//    }
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearEntidad(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        String claseJSON = "actividad";
        Actividad entidad = new Actividad();
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
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
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
            //entidad.setCobroActividadArticulos(null);
            //guardar en base
            ActividadDAO.getInstance().crearActividadREST(entidad);
            ajustarBitacoraRest(bitacora);
            //se retorna un json con la entidad (tambien se retorna la bitacora con el usuario y su hash key, pero no importa porque es el mismo que se envio en la request)
            String json = gson.toJson(entidad);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JSONException ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.serverError().entity("Error al obtener el "+ entidad.getClass() +" de la base de datos.").build();
    }
    
    private void validar(Actividad actividad) throws Exception{
        //validacion de datos nulos
        if (actividad.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(actividad.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (actividad.getPrecio() == 0) throw new Exception("El campo Precio no puede ser vacio.");
        if (actividad.getDias()== 0) throw new Exception("El campo Dias no puede ser vacio.");
        if (actividad.getPeriodo()== 0) throw new Exception("El campo Periodo no puede ser vacio.");
        
        //validacion del campo id
        if (actividad.getId() != 0) throw new Exception("El campo Id debe ser vacio.");
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
