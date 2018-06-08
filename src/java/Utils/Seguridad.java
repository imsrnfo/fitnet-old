package Utils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
public class Seguridad implements Serializable{
   
    //   constructores    
    public Seguridad(){}

    /**
     * Devuelve la combinacion passord + salt
     * Se utiliza para crear un hash del password con una cadena aleatoria (salt) agregada para aumentar seguridad.
     * Esta cadena se debe utilizar para recuperar el password.
     * @param Password
     * @return Array[0]: salt | Array[1]: passwordSeguro
     */
    public String[] getPasswordSeguro(String Password){
        String[] pass = new String[2];
        try{
            pass[0] = getSalt();
            pass[1] = getSecurePassword(Password, pass[0]);
        }catch(NoSuchProviderException | NoSuchAlgorithmException ex){}
        return pass;
    }
    
    /**
     * Devuelve el password correspondiente al salt.
     * @param Password
     * @param Salt caracteres generados de forma aleatoria para agregar al string de password.
     * @return 
     */
    public String getPasswordSeguro(String Password, String Salt){
        return getSecurePassword(Password, Salt);
    }
    
    /**
     * Utiliza MessageDigest SHA-256 para generar passwords.
     * Utiliza los bytes de sal y password y los convierte a hexadecimal.
     * @param passwordToHash
     * @param salt
     * @return 
     */
    private static String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     

    /**
     * Genera un array de caracteres de forma aleatoria.
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException 
     */
    private static String getSalt() throws NoSuchAlgorithmException, NoSuchProviderException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
    
    public boolean cedulaValida(String CedulaAComprobar){
      int cedula = 0;
      int digitoVerificador = 0;
       
      try{
        if (CedulaAComprobar.length()<8) {
           return false;
        }else{
            digitoVerificador = Integer.parseInt(CedulaAComprobar.substring(7));
            int sum = 0;
            int[] multiplicador= {2, 9, 8, 7, 6, 3, 4};
            for (int i = 0; i < 7; i++) {
                int actual = Integer.parseInt(CedulaAComprobar.substring(i,i+1));
                sum += Integer.parseInt(CedulaAComprobar.substring(i,i+1))*multiplicador[i];
            }
            int comprobacion = (10 - (sum % 10) ) % 10;
            if (comprobacion != digitoVerificador) {
                return false;
            }           
        }
      }catch(NumberFormatException ex){
          return false;
      }      
      return true;
   }
    
}