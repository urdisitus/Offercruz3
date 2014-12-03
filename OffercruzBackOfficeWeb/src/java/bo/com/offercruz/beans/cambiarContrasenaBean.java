/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.entidades.Usuario;
import static javassist.CtMethod.ConstParameter.string;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Dario
 */
@ManagedBean
@RequestScoped
public class cambiarContrasenaBean{
 @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    private String contraseñaActual;  
    private String nuevaContraseña;  
    private String confirmarContraseña; 
    
    public cambiarContrasenaBean() {   
        System.out.println("Entro al constructor");
    }
    
        public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public String getcontraseñaActual() {
        return contraseñaActual;
    }
 
    public void setcontraseñaActual(String password1) {
        this.contraseñaActual = password1;
    }
 
    public String getnuevaContraseña() {
        return nuevaContraseña;
    }
 
    public void setnuevaContraseña(String password2) {
        this.nuevaContraseña = password2;
    }
 
    public String getconfirmarContraseña() {
        return confirmarContraseña;
    }
 
    public void setPconfirmarContraseña(String password3) {
        this.confirmarContraseña = password3;
    }
    public void guardarPas(){
        System.out.println("Entro a guardar");
        IUsuarioBO usuarioBO = FactoriaObjetosNegocio.getInstance().getIUsuarioBO();
        String contraseña=usuarioBO.encriptar(contraseñaActual);
        Usuario User=loginBean.currentUser;
        if(contraseña.equals(User.getPassword())&&(nuevaContraseña.equals(confirmarContraseña))){
            System.out.println("se puede cambiar");
            String nuevaEncriptada=usuarioBO.encriptar(nuevaContraseña);
            User.setPassword(nuevaEncriptada);
            usuarioBO.actualizar(User);
        }
        
    }



}
