package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Actividad;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Gimnasio;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ActividadDAO extends DBGenericClass<Actividad>{
    
    private Session session;
    
    private static ActividadDAO instance = null;
    
    public static ActividadDAO getInstance() throws Exception{
        if(instance == null) {
            instance = new ActividadDAO();
        }
        return instance;
    }
    
    private ActividadDAO() throws Exception{
        super(Actividad.class);
        session=super.getSession();
    }
     
    public List<Actividad> obtenerActividades() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
         List <Actividad> listaActividad = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select a from " + Actividad.class.getName() + " a " + "where a.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaActividad = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaActividad;
    }

    public void guardar(Actividad actividad) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            Bitacora bitacora = actividad.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(actividad);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List<Actividad> obtenerActividadesREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Actividad> listaActividad = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select a from " + Actividad.class.getName() + " a " + "where a.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaActividad = query.list();
           if (listaActividad!=null){
               for(Actividad act : listaActividad){
                   act.setBitacora(null);
                   act.setCobros(null);
               }
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaActividad;
    }
    
    public Actividad obtenerActividadREST(String id, String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Actividad> listaActividad = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select a from " + Actividad.class.getName() + " a " + "where a.bitacora.usuario.gimnasio.id = :id_gimnasio and a.id = :id");	
           query.setParameter("id_gimnasio", gym.getId());
           query.setParameter("id", Integer.valueOf(id));
           listaActividad = query.list();
           if (listaActividad!=null){
               for(Actividad act : listaActividad){
                   act.setBitacora(null);
                   act.setCobros(null);
               }
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaActividad!=null && listaActividad.size()>0) ? listaActividad.get(0) : null;
    }
    
    public void crearActividadREST(Actividad actividad) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Bitacora bitacora = actividad.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(actividad);
            session.flush();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
   
}
