package br.com.claro.api;

/**
*
* @author Elison Correa
*/
public class ActivityDetail {
    private String activityId;
    private String resourceId;
    private String data;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("    {").append(System.lineSeparator());
        builder.append("        activityId: ").append(activityId).append(",").append(System.lineSeparator());
        builder.append("        resourceId: ").append(resourceId).append(",").append(System.lineSeparator());
        builder.append("        date: ").append(data).append(System.lineSeparator());
        builder.append("    }");
        
        return builder.toString(); 
    }

    
    
    
}