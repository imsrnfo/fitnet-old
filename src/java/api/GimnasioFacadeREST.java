package api;

import DAOs.GimnasioDAO;
import DAOs.UsuarioDAO;
import HBMs.Articulo;
import HBMs.Gimnasio;
import HBMs.Usuario;
import Utils.HibernateProxyTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

@Stateless
@Path("gimnasios")
public class GimnasioFacadeREST{
    
    public GimnasioFacadeREST() {
        
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Gimnasio entity) {
        
    }
    
    @GET
    @Path("obtener/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerGimansio(@PathParam("key") String key) {
        try {
            Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
            if (gym == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el gimnasio en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(gym); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
}
