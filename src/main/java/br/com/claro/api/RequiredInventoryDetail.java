package br.com.claro.api;

/**
*
* @author Elison Correa
*/
public class RequiredInventoryDetail {
    private String inventoryType;

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("    {").append(System.lineSeparator());
        builder.append("        inventoryType: ").append(inventoryType).append(System.lineSeparator());
        builder.append("    }");
        
        return builder.toString(); 
    }
    
    
}