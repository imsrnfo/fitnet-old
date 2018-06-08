package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Concepto;
import HBMs.Gimnasio;
import HBMs.Marca;
import HBMs.Movimiento;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class MovimientoDAO extends DBGenericClass<Movimiento>{
    
    private Session session;
    
    private static MovimientoDAO instance = null;
    
    public static MovimientoDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new MovimientoDAO();
        }
        return instance;
    }
    
    private MovimientoDAO()  throws Exception{
        super(Movimiento.class);
        session=super.getSession();
    }
    
    public List<Movimiento> obtenerMovimientos() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
         List <Movimiento> listaMovimiento = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select m from " + Movimiento.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaMovimiento = query.list();
           for (Movimiento movimiento : listaMovimiento){
               Hibernate.initialize(movimiento.getConcepto());
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaMovimiento;
    }

    public void guardar(Movimiento movimiento) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            Bitacora bitacora = movimiento.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(movimiento);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List<Movimiento> obtenerMovimientosREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Movimiento> listaMovimiento = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select m from " + Movimiento.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio");	
           query.setParameter("id_gimnasio", gym.getId());
           listaMovimiento = query.list();
           for (Movimiento movimiento : listaMovimiento){
               movimiento.setBitacora(null);
               //movimiento.setConcepto(null); //se le envia el concepto asociado al movimiento
               Hibernate.initialize(movimiento.getConcepto());
               Concepto concepto = movimiento.getConcepto();
               concepto.setMovimientos(null);
               concepto.setBitacora(null);
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaMovimiento;
    }
    
    public Movimiento obtenerMovimientoREST(String id, String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
         List <Movimiento> listaMovimiento = new ArrayList<>();
         try{
           session = HibernateUtil.getSessionFactory().openSession();
           Query query = session.createQuery("select m from " + Movimiento.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and m.id = :id");	
           query.setParameter("id_gimnasio", gym.getId());
           query.setParameter("id", Integer.valueOf(id));
           listaMovimiento = query.list();
           for (Movimiento movimiento : listaMovimiento){
               movimiento.setBitacora(null);
               movimiento.setConcepto(null);
           }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaMovimiento!=null && listaMovimiento.size()>0) ? listaMovimiento.get(0) : null;
    }
    
     public void crearMovimientoREST(Movimiento entidad) throws Exception{
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
