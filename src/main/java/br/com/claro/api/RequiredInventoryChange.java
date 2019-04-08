package br.com.claro.api;


/**
*
* @author Elison Correa
*/
public class RequiredInventoryChange {
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("    {").append(System.lineSeparator());
        builder.append("        quantity: ").append(quantity).append(System.lineSeparator());
        builder.append("    }");
        
        return builder.toString(); 
    }
}
