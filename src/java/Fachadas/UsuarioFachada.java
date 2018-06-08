package Fachadas;

import DAOs.UsuarioDAO;
import HBMs.Permiso;
import HBMs.PermisoUsuario;
import HBMs.Usuario;
import Utils.FileUpload;
import Utils.ManejadorSession;
import Utils.Seguridad;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UsuarioFachada {
    
    private static UsuarioFachada instance = null;
    
    public static UsuarioFachada getInstance() throws Exception {
        if(instance == null) {
            instance = new UsuarioFachada();
        }
        return instance;
    }
    
    private UsuarioFachada()  throws Exception{}
    
    public void validar(Usuario usuario) throws Exception{
        if (usuario.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        if (usuario.getUsername()== null) throw new Exception("El campo Nick no puede ser vacio.");
        if (usuario.getPassword()== null) throw new Exception("El campo Contraseña no puede ser vacio.");
        
        if ("".equals(usuario.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        if ("".equals(usuario.getUsername())) throw new Exception("El campo Nick no puede ser vacio.");
        if ("".equals(usuario.getPassword())) throw new Exception("El campo Contraseña no puede ser vacio.");
    }
    
    public void guardar(Usuario usuario, FileUpload foto, Map<Integer, Boolean> permisosSeleccionados, List<Permiso> todosPermisos) throws ParseException, Exception{
        validar(usuario);
        
        if (usuario.getId()>0){
            if (usuario.getPermisoUsuarios()==null) usuario.setPermisoUsuarios(new HashSet<>());
            usuario.getPermisoUsuarios().clear();
            List<Permiso> permisosUsuario = obtenerPermisosSeleccionados(permisosSeleccionados, todosPermisos);
            for (Permiso p : permisosUsuario){
                PermisoUsuario pu = new PermisoUsuario();
                pu.setPermiso(p);
                pu.setUsuario(usuario);
                usuario.getPermisoUsuarios().add(pu);
            }
            UsuarioDAO.getInstance().actualizarUsuarioConPermisos(usuario);
        }
        
        if (usuario.getId()==0){
            usuario.setGimnasio(ManejadorSession.getInstance().obtenerGimnasioLogeado());
            if (usuario.getPermisoUsuarios()==null) usuario.setPermisoUsuarios(new HashSet<>());
            List<Permiso> permisosUsuario = obtenerPermisosSeleccionados(permisosSeleccionados, todosPermisos);
            for (Permiso p : permisosUsuario){
                PermisoUsuario pu = new PermisoUsuario();
                pu.setPermiso(p);
                pu.setUsuario(usuario);
                usuario.getPermisoUsuarios().add(pu);
            }
            foto.setNombreArchivo(usuario.getUsername());
            usuario.setImagen(foto.guardarImagenPerfil());
            usuario.setHashkey(new Seguridad().getPasswordSeguro(usuario.getUsername(), usuario.getPassword()));
            UsuarioDAO.getInstance().crearUsuarioConPermisos(usuario);
        }
        
    }
    
    private  List<Permiso> obtenerPermisosSeleccionados(Map<Integer, Boolean> permisosSeleccionados, List<Permiso> todosPermisos){
        List<Permiso> result = new ArrayList<>();
        for (Map.Entry e : permisosSeleccionados.entrySet()) {
            boolean valor = (boolean)e.getValue();
            int Key = (int) e.getKey();
            if (valor) result.add(obtenerPermisoPorId(Key, todosPermisos));
        }
        return result;
    }
    
    private Permiso obtenerPermisoPorId(int idPermiso, List<Permiso> todosPermisos){
        for (Permiso iterPermiso : todosPermisos){
            if (iterPermiso.getId() == idPermiso) return iterPermiso;
        }
        return null;
    }
}
