package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Concepto;
import HBMs.Gimnasio;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ConceptoDAO extends DBGenericClass<Concepto>{
    
    private Session session;
    
    private static ConceptoDAO instance = null;
    
    public static ConceptoDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new ConceptoDAO();
        }
        return instance;
    }
    
    private ConceptoDAO() throws Exception {
        super(Concepto.class);
        session=super.getSession();
    }
    
    public List<Concepto> obtenerConceptos() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
         List <Concepto> listaConcepto = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select c from " + Concepto.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaConcepto = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaConcepto;
    }

    public void guardar(Concepto concepto) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            Bitacora bitacora = concepto.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(concepto);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
     public List<Concepto> obtenerConceptosREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Concepto> listaConcepto = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select c from " + Concepto.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaConcepto = query.list();
           if (listaConcepto!=null){
               for(Concepto concepto : listaConcepto){
                   concepto.setBitacora(null);
                   concepto.setMovimientos(null);
               }
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaConcepto;
    }
     
      public Concepto obtenerConceptoREST(String id, String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Concepto> listaConcepto = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select c from " + Concepto.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.id = :id");	
           query.setParameter("id_gimnasio", gym.getId());
           query.setParameter("id", Integer.valueOf(id));
           listaConcepto = query.list();
           if (listaConcepto!=null){
               for(Concepto concepto : listaConcepto){
                   concepto.setBitacora(null);
                   concepto.setMovimientos(null);
               }
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaConcepto!=null && listaConcepto.size()>0) ? listaConcepto.get(0): null;
    }
      
       public void crearConceptoREST(Concepto entidad) throws Exception{
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
}
