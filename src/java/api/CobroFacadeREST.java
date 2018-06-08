package api;

import DAOs.ArticuloDAO;
import DAOs.ClienteDAO;
import DAOs.CobroDAO;
import DAOs.UsuarioDAO;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.CobroArticulo;
import HBMs.Gimnasio;
import HBMs.Usuario;
import Utils.HibernateProxyTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
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
@Path("cobros")
public class CobroFacadeREST{
    
    public CobroFacadeREST() {
        
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Gimnasio entity) {
        
    }
    
    @GET
    @Path("listar/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerCobros(@PathParam("key") String key) {
        try {
            List<Cobro> cobros = CobroDAO.getInstance().obtenerCobrosREST(key);
            if (cobros == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(cobros); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
    @GET
    @Path("obtener/{id}/{key}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obtenerCobro(@PathParam("id") String id, @PathParam("key") String key) {
        try {
            Cobro cobro = CobroDAO.getInstance().obtenerCobroREST(id, key);
            if (cobro == null) return Response.status(Response.Status.NOT_FOUND).entity("No se encontro el usuario en la base de datos." ).build();
            //return Response.status(Response.Status.OK).entity(user).bu‌​ild();
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            String json = gson.toJson(cobro); //convert entity to json
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().entity("Error al obtener el usuario de la base de datos.").build();
        }
    }
    
    
        //--------------------SINTAXIS DE ENTRADA-----------------------
//        {
//          "key": "fa3ff1f2fe04d563eedabd8c4f7433313bfc3d370ade29dd31d7ed13126591ed",
//          "cobro": {
//            "cliente": {
//              "id": 1
//            },
//			"actividad": {
//              "id": 1
//            },
//            "fdesde": "02/05/2017",
//            "fhasta": "02/05/2017",
//            "entrega": 300,
//            "descuento": 100,
//            "motivoDescuento": "Motivo1",
//            "cobroArticulos": [
//              {
//                "articulo": {
//                  "id": 1
//                },
//                "cantidad": 1
//              }
//            ]
//          }
//        }
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearEntidad(InputStream incomingData) {
        StringBuilder crunchifyBuilder = new StringBuilder();
        String claseJSON = "cobro";
        Cobro entidad = new Cobro();
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
            //obtener los objetos relacionados de la base de datos
            Cliente clienteMapeado = ClienteDAO.getInstance().findById(entidad.getCliente().getId());
            entidad.setCliente(clienteMapeado);
            for (Object object : entidad.getCobroArticulos()){
                if (object instanceof CobroArticulo){
                    CobroArticulo caa = (CobroArticulo) object;
                    Articulo art = (caa.getArticulo()!=null) ? ArticuloDAO.getInstance().Obtener(caa.getArticulo().getId(), "REST", key) : null;
                    caa.setArticulo(art);
                }
            }
            entidad.setFecha(new Date());
            validar(entidad);
            //setear la bitacora (Tambien evita que se setee manualmente en la request)
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(usuario);
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            entidad.setBitacora(bitacora);
            //ajustar parametros que no deberian venir en la request para evitar que se guarden (Error de seguridad)

            //parsear cobro actividad articulo a mano
            for (Object object : entidad.getCobroArticulos()){
                if (object instanceof LinkedTreeMap){
                    CobroArticulo caa = new CobroArticulo();
                    
                    LinkedTreeMap ltm = (LinkedTreeMap) object;
                    
                   
                    
                    LinkedTreeMap articuloLTM =  (ltm.get("articulo") instanceof LinkedTreeMap) ? (LinkedTreeMap) ltm.get("articulo") : null;
                    int idArticulo = (articuloLTM!=null && articuloLTM.get("id")!=null) ? ((Double) articuloLTM.get("id")).intValue() : 0;
                    Articulo art = ArticuloDAO.getInstance().Obtener(idArticulo, "REST", key);
                    caa.setArticulo(art);
                    
                    int cantidad = (ltm!=null && ltm.get("cantidad")!=null) ? ((Double) ltm.get("cantidad")).intValue() : 0;
                    caa.setCantidad(cantidad);
                    
                    caa.setCobro(entidad);
                    
                    entidad.getCobroArticulos().add(caa);
                }
            }
            
            //calcula el Importe Total 
            int result = 0;
            for (Object item : entidad.getCobroArticulos()){
                if (item instanceof CobroArticulo){
                    CobroArticulo cobroActividadArticulo = (CobroArticulo) item;
                    
                    if (cobroActividadArticulo.getArticulo()!=null) result+=cobroActividadArticulo.getArticulo().getPrecio()*cobroActividadArticulo.getCantidad();
                }
            }
            entidad.setImporteTotal(result);
            
            //calcula la deuda del cliente
            entidad.setDebe(entidad.getImporteTotal() - entidad.getDescuento() - entidad.getEntrega());
            
            //entidad.setCobroActividadCobros(null);
            //guardar en base
            CobroDAO.getInstance().crearCobroREST(entidad);
            
            //obtener la entidad basica para retornar en el json
            Cliente clienteJSON = ClienteDAO.getInstance().obtenerClienteREST(String.valueOf(entidad.getCliente().getId()),key);
            entidad.setCliente(clienteMapeado);
            
            ajustarBitacoraRest(bitacora);
            
            //se retorna un json con la entidad (tambien se retorna la bitacora con el usuario y su hash key, pero no importa porque es el mismo que se envio en la request)
            String json = gson.toJson(CobroDAO.getInstance().obtenerCobroREST(String.valueOf(entidad.getId()), key));
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JSONException ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getMessage()).build();
        }
        return Response.serverError().entity("Error al obtener el "+ entidad.getClass() +" de la base de datos.").build();
    }
    
    private void validar(Cobro cobro) throws Exception{
        //validacion de datos nulos
        //if (cobro.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos vacios
        //if ("".equals(cobro.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos enteros nulos
        //if (cobro.getPrecio() == 0) throw new Exception("El campo Precio no puede ser vacio.");
        
        //validacion del campo debe
        if (cobro.getDebe()!= 0) throw new Exception("El campo debe debe ser vacio.");
        
        //validacion del campo importe total
        if (cobro.getImporteTotal()!= 0) throw new Exception("El campo importeTotal debe ser vacio.");
        
        //validacion del campo id
        if (cobro.getId() != 0) throw new Exception("El campo Id debe ser vacio.");
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



