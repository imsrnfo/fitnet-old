package DAOs;

import DataBase.HibernateUtil;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Gimnasio;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ArticuloDAO extends AbstractDAO<Articulo>{
    
    private Session session;
    
    private static ArticuloDAO instance = null;
    
    public static ArticuloDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new ArticuloDAO();
        }
        return instance;
    }
    
    private ArticuloDAO()  throws Exception{
        super(Articulo.class);
        session=super.getSession();
    }

    @Override
    public void Crear(Articulo entidad, String... params) throws Exception {
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Bitacora bitacora = entidad.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(entidad);
            session.flush();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }

    @Override
    public void Modificar(Articulo entidad, String... params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Articulo Obtener(int idEntidad, String... params) throws Exception {
        
        //Obtiene el gimnasio segun desde donde se llama
        Gimnasio gym;
        if (params.length>1 && "REST".equals(params[0])){
            if (params.length==1) throw new Exception("Debe ingresar el Token.");
            String key = params[1];
             gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        }else{
             gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        }
        
        List <Articulo> listaArticulo = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select a from " + Articulo.class.getName() + " a " + "where a.bitacora.usuario.gimnasio.id = :id_gimnasio and a.id = :id");
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("id", Integer.valueOf(idEntidad));
            listaArticulo = query.list();
            
            //para que caso que se llame desde el servicio
            if (params.length>1 && "REST".equals(params[0])){
                if (listaArticulo != null){
                    for (Articulo articulo : listaArticulo){
                        articulo.setBitacora(null);
                        articulo.setCobroArticulos(null);
                    }
                }
            }
            
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaArticulo!=null && listaArticulo.size()>0) ? listaArticulo.get(0) : null;
    }

    @Override
    public List<Articulo> Listar(String... params) throws Exception {
        
        //Obtiene el gimnasio segun desde donde se llama
        Gimnasio gym;
        if (params.length>=2 && "REST".equals(params[0])){
            if (params.length==1) throw new Exception("Debe ingresar el Token.");
            String key = params[1];
             gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        }else{
             gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        }
        
        List <Articulo> listaArticulo = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select a from " + Articulo.class.getName() + " a " + "where a.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaArticulo = query.list();
            
            //para que caso que se llame desde el servicio
            if (params.length>=2 && "REST".equals(params[0])){
                if (listaArticulo != null){
                    for (Articulo articulo : listaArticulo){
                        articulo.setBitacora(null);
                        articulo.setCobroArticulos(null);
                    }
                }
            }
            
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaArticulo;
    }
}
