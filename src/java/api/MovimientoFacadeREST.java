package api;

import DAOs.ArticuloDAO;
import DAOs.ConceptoDAO;
import DAOs.MovimientoDAO;
import DAOs.UsuarioDAO;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Concepto;
import HBMs.Gimnasio;
import HBMs.Movimiento;
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
@Path("movimientos")
public class MovimientoFacadeREST{
    
    public MovimientoFacadeREST() {
        
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Gimnasio entity) {
        
    }
    
    @GET
    @Path("listar/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerMovimientos(@PathParam("key") String key) {
        try {
            List <Movimiento> movimientos = MovimientoDAO.getInstance().obtenerMovimientosREST(key);
            if (movimientos == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(movimientos); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
     @GET
    @Path("obtener/{id}/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerMovimiento(@PathParam("id") String id,@PathParam("key") String key) {
        try {
            Movimiento movimiento = MovimientoDAO.getInstance().obtenerMovimientoREST(id,key);
            if (movimiento == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(movimiento); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
        //--------------------SINTAXIS DE ENTRADA-----------------------
//        {
//          "key": "fa3ff1f2fe04d563eedabd8c4f7433313bfc3d370ade29dd31d7ed13126591ed",
//          "movimiento": {
//            "concepto": {
//              "id": 1
//            },
//            "tipo": "Ingreso",
//            "descripcion": "pago a los profes",
//            "importe": 100
//          }
//        }
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearEntidad(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        String claseJSON = "movimiento";
        Movimiento entidad = new Movimiento();
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
            //obtener los objetos relacionados de la base de datos
            Concepto conceptoMapeado = ConceptoDAO.getInstance().findById(entidad.getConcepto().getId());
            entidad.setConcepto(conceptoMapeado);
            validar(entidad);
            //setear la bitacora (Tambien evita que se setee manualmente en la request)
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(usuario);
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            entidad.setBitacora(bitacora);
            //ajustar parametros que no deberian venir en la request para evitar que se guarden (Error de seguridad)
            MovimientoDAO.getInstance().crearMovimientoREST(entidad);
            //obtener la entidad basica para retornar en el json
            Concepto conceptoJSON = ConceptoDAO.getInstance().obtenerConceptoREST(String.valueOf(entidad.getConcepto().getId()), key);
            entidad.setConcepto(conceptoJSON);
            //guardar en base
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
    
    private void validar(Movimiento movimiento) throws Exception{
        //validacion de datos nulos
        if (movimiento.getDescripcion()== null) throw new Exception("El campo Descripcion no puede ser vacio.");
        if (movimiento.getTipo()== null) throw new Exception("El campo Tipo no puede ser vacio.");
        if (movimiento.getConcepto() == null) throw new Exception("El campo Concepto no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(movimiento.getDescripcion())) throw new Exception("El campo Descripcion no puede ser vacio.");
        if ("".equals(movimiento.getTipo())) throw new Exception("El campo Tipo no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (movimiento.getImporte() == 0) throw new Exception("El campo Precio no puede ser vacio.");
        if (movimiento.getConcepto().getId() == 0) throw new Exception("El campo Concepto no puede ser vacio.");
        
        //validacion del campo tipo
        if (!"Ingreso".equals(movimiento.getTipo()) && !"Egreso".equals(movimiento.getTipo())) throw new Exception("El campo Tipo debe ser ('Ingreso' - 'Egreso').");
        
        //validacion del campo id
        if (movimiento.getId() != 0) throw new Exception("El campo Id debe ser vacio.");
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
