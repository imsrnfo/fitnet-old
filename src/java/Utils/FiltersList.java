package Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("filtersList")
@ViewScoped
public class FiltersList implements Serializable{
    
    private List<?> filteredItems;

    public List<?> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<?> filteredItems) {
        this.filteredItems = filteredItems;
    }
    
    
    //en caso de 2 tablas
    private List<?> filteredItems2;

    public List<?> getFilteredItems2() {
        return filteredItems2;
    }

    public void setFilteredItems2(List<?> filteredItems2) {
        this.filteredItems2 = filteredItems2;
    }
    
}
