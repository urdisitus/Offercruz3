/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;

import bo.com.offercruz.bl.contratos.IImagenBO;
import bo.com.offercruz.dal.contrato.IImagenDAO;
import bo.com.offercruz.entidades.Imagen;
import com.sun.prism.Image;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ernesto
 */
public class ImagenBO extends ObjetoNegocioGenerico<Imagen, Integer, IImagenDAO> implements IImagenBO {

    @Override
    IImagenDAO getObjetoDAO() {
        return getDaoManager().getImagenDAO();
    }

    @Override
    protected void preInsertar(Imagen entidad) {
        entidad.setEstado(1);
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
    }

    @Override
    protected void preActualizar(Imagen entidad) {
        entidad.setFechaModificacion(new Date());
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH) {
        int IMG_HEIGHT = IMG_WIDTH * originalImage.getHeight() / originalImage.getWidth();
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int IMG_WIDTH) {
        int IMG_HEIGHT = IMG_WIDTH * originalImage.getHeight() / originalImage.getWidth();
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    public static String imageToString(BufferedImage bImage, String path) {
        String imageString = null;

        String formatName = path.substring(path.lastIndexOf('.') + 1, path.length());

        //image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, formatName, baos);
            baos.flush();
            byte[] imageAsRawBytes = baos.toByteArray();
            baos.close();

            //bytes to string
            imageString = Base64.getEncoder().encodeToString(imageAsRawBytes);
        } catch (IOException ex) {
            Logger.getLogger(ImagenBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imageString;
    }

    public static BufferedImage stringToImage(String imageString) {
        //string to ByteArrayInputStream
        BufferedImage bImage = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(imageString));
        try {
            bImage = ImageIO.read(bais);
        } catch (IOException ex) {
            Logger.getLogger(ImagenBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bImage;
    }

    @Override
    public Imagen getImagenRedimencionada(int id, int width) {
        Imagen im = ejecutarEnTransaccion(() -> getObjetoDAO().recuperarPorId(id));
        BufferedImage bi = stringToImage(im.getImagenFisica());
        BufferedImage biN = resizeImage(bi, width);
        im.setImagenFisica(imageToString(biN, im.getNombre()));
        return im;
    }

    public static Imagen construirImagen(String direccion) {
        Imagen im = new Imagen();
        im.setNombre(direccion);
        try {
            BufferedImage bi = ImageIO.read(new File(direccion));
            im.setAlto(bi.getHeight());
            im.setAncho(bi.getWidth());
            im.setImagenFisica(imageToString(bi, direccion));
            return im;
        } catch (IOException ex) {
            Logger.getLogger(ImagenBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
