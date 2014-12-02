/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.utils;

import bo.com.offercruz.entidades.Usuario;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Olvinho
 */
public class MailHelper {

    private static String plantillaGeneral;
    private static String credenciales;

    static {
        try {
            String absoluteWebPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            plantillaGeneral = readFile(absoluteWebPath + "/html/contenedor.html", Charset.defaultCharset());
            credenciales = readFile(absoluteWebPath + "/html/credenciales.html", Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static BodyPart getBodyPartEnvuelto(String texto) throws MessagingException {
        BodyPart cuerpo = new MimeBodyPart();
        cuerpo.setContent(plantillaGeneral.replace("{texto}", texto), "text/html");
        return cuerpo;
    }

    private static BodyPart getBodyPartCredenciales(String usuario, String contrasena) throws MessagingException {
        return getBodyPartEnvuelto(credenciales.replace("{usuario}", usuario).replace("{pass}", contrasena));
    }

    public static void enviarCredenciales(Usuario usuario) {
        final String username = "offercruz@gmail.com";
        final String dominio = "gmail.com";
        final String password = "offercruz123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", 587);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username + "@" + dominio));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(usuario.getCorreoElectronico()));
            message.setSubject("Credencial OfferCruz");
            Multipart multiPartes = new MimeMultipart();
            multiPartes.addBodyPart(getBodyPartCredenciales(usuario.getLogin(), usuario.getPassword()));
            message.setContent(multiPartes);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {

        }
    }
}
