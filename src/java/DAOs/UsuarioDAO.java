package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Cliente;
import HBMs.Gimnasio;
import HBMs.Permiso;
import HBMs.PermisoUsuario;
import HBMs.Usuario;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsuarioDAO extends DBGenericClass<Usuario>{
    
    private Session session;
    
    private static UsuarioDAO instance = null;
    
    public static UsuarioDAO getInstance() throws Exception {
        if(instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }
    
    private UsuarioDAO()  throws Exception{
        super(Usuario.class);
        session=super.getSession();
    }
    
    public Usuario obtenerUsuarioNombrePassword(Usuario usr) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("username", usr.getUsername()));
            criteria.add(Restrictions.eq("password", usr.getPassword()));
            Object result = criteria.uniqueResult();
            if (result != null) {
                return (Usuario) result;
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Usuario> listaUsuario = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select u from " + Usuario.class.getName() + " u " + "where u.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaUsuario = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaUsuario;
    }
    
    public List<Permiso> obtenerTodosLosPermisos() throws Exception{
        List <Permiso> listaPermiso = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select p from " + Permiso.class.getName() + " p where p.id <> 16");
            listaPermiso = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaPermiso;
    }
    
    public List<Permiso> obtenerPermisosDelUsuario(Usuario usr) throws Exception{
        List <Permiso> listaPermiso = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select pu.permiso from " + PermisoUsuario.class.getName() + " pu " + "where pu.usuario.id = :id_usuario");
            query.setParameter("id_usuario", usr.getId());
            listaPermiso = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaPermiso;
    }
    
    public Permiso obtenerPermisoPorId(int idPermiso) throws Exception{
        List <Permiso> listaPermiso = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select p from " + Permiso.class.getName() + " p " + "where p.id = :id_permiso");
            query.setParameter("id_permiso",idPermiso);
            listaPermiso = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaPermiso.get(0);
    }
    
    public void crearUsuarioConPermisos(Usuario usr) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
            //por el momento no se permitiran crear 2 clientes con la misma cedula independientemente del gimnasio al que pertenezcan
            Query query = session.createQuery("select u from " + Usuario.class.getName() + " u " + "where u.username = :username");
            query.setParameter("username", usr.getUsername());
            if (query.list().size() > 0) throw new Exception("Ya existe un Usuario registrado con el Username : " + usr.getUsername());
            
            trans = session.beginTransaction();
            
            //obtengo lista de CobroActividad para no perder los datos
            List<PermisoUsuario> listaPermisoUsuario = new ArrayList<>();
            for(Object object : usr.getPermisoUsuarios()){
                if (object instanceof PermisoUsuario){
                    PermisoUsuario permisoUsuario = (PermisoUsuario) object;
                    listaPermisoUsuario.add(permisoUsuario);
                }
            }
            
            //seteo la lista de CobroActividad del cobro a null para poder persistirlo
            usr.setPermisoUsuarios(null);
            
            //persistir Cobro vacio
            session.persist(usr);
            session.flush();
            
            //persistir lista de CobroActividad
            for (PermisoUsuario permisoUsuario : listaPermisoUsuario){
                session.merge(permisoUsuario);
                session.flush();
            }
            
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public void actualizarUsuarioConPermisos(Usuario usr) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            
            //limpio todos los permisos del usuario antes de setearle los nuevos
            Query q = session.createQuery("delete  " + PermisoUsuario.class.getName() + " pu  where pu.usuario.id = :id_usuario");
            q.setParameter("id_usuario", usr.getId());
            q.executeUpdate();
            session.flush();
            
            //obtengo lista de UsuariosPermisos para no perder los datos
            List<PermisoUsuario> listaPermisoUsuario = new ArrayList<>();
            for(Object object : usr.getPermisoUsuarios()){
                if (object instanceof PermisoUsuario){
                    PermisoUsuario permisoUsuario = (PermisoUsuario) object;
                    listaPermisoUsuario.add(permisoUsuario);
                }
            }
            
            //seteo la lista de USuariosPermisos del cobro a null para poder persistirlo
            usr.setPermisoUsuarios(null);
            
            //persistir Cobro vacio
            session.update(usr);
            session.flush();
            
            //persistir lista de UsuariosPermisos
            for (PermisoUsuario permisoUsuario : listaPermisoUsuario){
                session.merge(permisoUsuario);
                session.flush();
            }
            
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public Usuario obtenerUsuarioConPermisos (int idUsuario) throws Exception{
        List <Usuario> listaUsuario = new ArrayList<>();
         Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select u from " + Usuario.class.getName() + " u " + "where u.gimnasio.id = :id_gimnasio and u.id = :id_usuario");
            query.setParameter("id_usuario",idUsuario);
            query.setParameter("id_gimnasio", gym.getId());
            listaUsuario = query.list();
            for (Usuario usr : listaUsuario) {
                Hibernate.initialize(usr.getPermisoUsuarios());
                List<PermisoUsuario> listaPermisoUsuario = new ArrayList<PermisoUsuario>(usr.getPermisoUsuarios());
                for (PermisoUsuario permisoUsuario : listaPermisoUsuario) {
                    Hibernate.initialize(permisoUsuario.getPermiso());
                }
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaUsuario.get(0);
    }

    //-------------------------------------------------------RESTFUL-WS---------------------------------------------------
   
      public Usuario obtenerUsuarioREST(String key) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("hashkey", key));
            Object result = criteria.uniqueResult();
            if (result != null) {
                Usuario usr = (Usuario) result;
                usr.setGimnasio(null);
                usr.setPermisoUsuarios(null);
                usr.setBitacoras(null);
                return usr;
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return null;
    }
    
     public Usuario obtenerUsuarioConPermisosREST(String key) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("hashkey", key));
            Object result = criteria.uniqueResult();
            if (result != null) {
                Usuario usr = (Usuario) result;
                usr.setGimnasio(null);
                for (Object obj : usr.getPermisoUsuarios()){
                       PermisoUsuario pu = (PermisoUsuario) obj;
                       Hibernate.initialize(pu.getPermiso());
                       pu.setUsuario(null);
                       Permiso p = pu.getPermiso();
                       p.setPermisoUsuarios(null);
                }
                usr.setBitacoras(null);
                return usr;
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return null;
    }
     
      public List<Usuario> obtenerUsuariosREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List <Usuario> listaUsuario = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select u from " + Usuario.class.getName() + " u " + "where u.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaUsuario = query.list();
            if (listaUsuario!=null){
                for (Usuario usuario : listaUsuario){
                    usuario.setGimnasio(null);
                    usuario.setPermisoUsuarios(null);
                    usuario.setBitacoras(null);
                    usuario.setHashkey(null);
                }
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaUsuario;
    }

      public void crearUsuarioConPermisosREST(Usuario usr) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
              //por el momento no se permitiran crear 2 clientes con la misma cedula independientemente del gimnasio al que pertenezcan
            Query query = session.createQuery("select u from " + Usuario.class.getName() + " u " + "where u.username = :username");
            query.setParameter("username", usr.getUsername());
            if (query.list().size() > 0) throw new Exception("Ya existe un Usuario registrado con el Username : " + usr.getUsername());
            
            //obtengo lista de CobroActividad para no perder los datos
            List<PermisoUsuario> listaPermisoUsuario = new ArrayList<>();
            for(Object object : usr.getPermisoUsuarios()){
                if (object instanceof PermisoUsuario){
                    PermisoUsuario permisoUsuario = (PermisoUsuario) object;
                    listaPermisoUsuario.add(permisoUsuario);
                }
            }
            
            //seteo la lista de CobroActividad del cobro a null para poder persistirlo
            usr.setPermisoUsuarios(null);
            
            //persistir Cobro vacio
            session.persist(usr);
            session.flush();
            
            //persistir lista de CobroActividad
            for (PermisoUsuario permisoUsuario : listaPermisoUsuario){
                session.merge(permisoUsuario);
                session.flush();
            }
            
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
      
       public Usuario obtenerUsuarioConPermisosPorIdREST(int id) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Usuario.class);
            criteria.add(Restrictions.eq("id", id));
            Object result = criteria.uniqueResult();
            if (result != null) {
                Usuario usr = (Usuario) result;
                usr.setGimnasio(null);
                for (Object obj : usr.getPermisoUsuarios()){
                       PermisoUsuario pu = (PermisoUsuario) obj;
                       Hibernate.initialize(pu.getPermiso());
                       pu.setUsuario(null);
                       Permiso p = pu.getPermiso();
                       p.setPermisoUsuarios(null);
                }
                usr.setBitacoras(null);
                return usr;
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return null;
    }
}
