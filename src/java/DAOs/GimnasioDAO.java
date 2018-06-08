package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Actividad;
import HBMs.Articulo;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.Gimnasio;
import HBMs.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class GimnasioDAO extends DBGenericClass<Gimnasio>{
    
    private Session session;
    
    private static GimnasioDAO instance = null;
    
    public static GimnasioDAO getInstance() throws Exception {
        if(instance == null) {
            instance = new GimnasioDAO();
        }
        return instance;
    }
    
    private GimnasioDAO() throws Exception {
        super(Gimnasio.class);
        session=super.getSession();
    }
    
    public Gimnasio obtenerGimnasio(Usuario usr) throws Exception{
        Gimnasio gym = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select g from " + Gimnasio.class.getName() + " g, " + Usuario.class.getName() + " u " +
                    "where u.gimnasio.id = g.id and u.id=:id_usuario");
            query.setParameter("id_usuario", usr.getId());
            gym = query.list().size()>0 ? (Gimnasio) query.list().get(0) : null;
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return gym;
    }
    
    public Gimnasio obtenerGimnasioPorEntidad(Object entidad) throws Exception{
        Gimnasio gym = null;
        try{
            
            String className = "";
            int idEntidad = 0;
            
            if (entidad instanceof Actividad){
                className = Actividad.class.getName();
                idEntidad = ((Actividad) entidad).getId();
            }else  if (entidad instanceof Cliente){
                className = Cliente.class.getName();
                idEntidad = ((Cliente) entidad).getId();
            }else  if (entidad instanceof Cobro){
                className = Cobro.class.getName();
                idEntidad = ((Cobro) entidad).getId();
            }else  if (entidad instanceof Articulo){
                className = Articulo.class.getName();
                idEntidad = ((Articulo) entidad).getId();
            }
            
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select g from " + Gimnasio.class.getName() + " g, " + className + " e " +
                    "where e.bitacora.usuario.gimnasio.id = g.id and e.id=:id_entidad");
            query.setParameter("id_entidad", idEntidad);
            gym = query.list().size()>0 ? (Gimnasio) query.list().get(0) : null;
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return gym;
    }
    
    public Gimnasio obtenerGimnasioRest(String key) throws Exception{
        Gimnasio gym = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select g from " + Gimnasio.class.getName() + " g, " + Usuario.class.getName() + " u " +
                    "where u.gimnasio.id = g.id and u.hashkey=:key");
            query.setParameter("key", key);
            gym = query.list().size()>0 ? (Gimnasio) query.list().get(0) : null;
            if (gym!=null){
                gym.setUsuarios(null);
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return gym;
    }
    
}
