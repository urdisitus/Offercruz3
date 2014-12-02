/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offercruzbl;

import bo.com.offercruz.bl.contratos.IImagenBO;
import bo.com.offercruz.bl.impl.ImagenBO;
import bo.com.offercruz.bl.impl.UsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.dal.contrato.ICategoriaDAO;
import bo.com.offercruz.dal.imp.control.FactoriaDAOManager;
import bo.com.offercruz.entidades.Categoria;
import bo.com.offercruz.entidades.Imagen;
import bo.com.offercruz.entidades.Usuario;
import java.util.Date;

/**
 *
 * @author Ernesto
 */
public class OfferCruzBL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Imagen im = ImagenBO.construirImagen("C:\\Users\\Ernesto\\Desktop\\RAFAGA-NORMALIZATE\\499fc_ancelotti_casillas.jpg");
//        IImagenBO bo = FactoriaObjetosNegocio.getInstance().getIImagenBO();
//        bo.insertar(im);
//        Imagen imm = bo.getImagenRedimencionada(1, 100);
//        System.out.println(imm.getImagenFisica());
        FactoriaDAOManager.getDAOManager().iniciarTransaccion();
        try {
            ICategoriaDAO catDao = FactoriaDAOManager.getDAOManager().getCategoriaDAO();
            catDao.persistir(new Categoria("Ernesto", 1, 1, new Date(), new Date()));
            FactoriaDAOManager.getDAOManager().confirmarTransaccion();
        } catch (Exception e) {
            FactoriaDAOManager.getDAOManager().cancelarTransaccion();
        }

        String encrip = FactoriaObjetosNegocio.getInstance().getIUsuarioBO().encriptar("admin");
        System.out.println(encrip);
    }

}
