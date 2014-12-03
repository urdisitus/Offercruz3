/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bo.com.offercruz.beans;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Marcelo
 */
@ManagedBean
public class GaleriaPortada {
    private List<String> images;
    
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        for (int i = 1; i <= 5; i++) {
            images.add("wallpaper" + i + ".jpg");
        }
    }

    public List<String> getImages() {
        return images;
    }
    /**
     * Creates a new instance of GaleriaPortada
     */
    public GaleriaPortada() {
    }
    
}
