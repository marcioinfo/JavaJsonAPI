package br.com.claro.api;


/**
 *
 * @author Elison Correa
 */
public class Event {
    
    private String eventType;
    private String time;
    private String user;
    private String subscriptionId;
    private ActivityDetail activityDetails; 
    private RequiredInventoryDetail requiredInventoryDetail;
    private RequiredInventoryChange requiredInventoryChange;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ActivityDetail getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(ActivityDetail ActivityDetails) {
        this.activityDetails = ActivityDetails;
    }

    public RequiredInventoryDetail getRequiredInventoryDetail() {
        return requiredInventoryDetail;
    }

    public void setRequiredInventoryDetail(RequiredInventoryDetail requiredInventoryDetail) {
        this.requiredInventoryDetail = requiredInventoryDetail;
    }

    public RequiredInventoryChange getRequiredInventoryChange() {
        return requiredInventoryChange;
    }

    public void setRequiredInventoryChange(RequiredInventoryChange requiredInventoryChange) {
        this.requiredInventoryChange = requiredInventoryChange;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("{").append(System.lineSeparator());
        builder.append("    eventType: ").append(this.eventType).append(",").append(System.lineSeparator());
        builder.append("    time: ").append(this.time).append(",").append(System.lineSeparator());
        builder.append("    user: ").append(this.user).append(",").append(System.lineSeparator());
        builder.append("    subscriptionId: ").append(this.subscriptionId).append(",").append(System.lineSeparator());
        
        builder.append("    activityDetails: ");
        if (this.activityDetails != null) {
            builder.append(this.activityDetails.toString()).append(",");
        } else {
            builder.append("null,");
        }
        builder.append(System.lineSeparator());
        
        builder.append("    requiredInventoryDetails: ");
        
        if (this.requiredInventoryDetail != null) {
            builder.append(this.requiredInventoryDetail.toString()).append(",");
        } else {
            builder.append("null,");
        }
        
        builder.append(System.lineSeparator());
        
        builder.append("    requiredInventoryChanges: ");
        
        if (this.requiredInventoryChange != null) {
            builder.append(this.requiredInventoryChange.toString());
        } else {
            builder.append("null");
        }
        builder.append(System.lineSeparator());
        builder.append("}").append(System.lineSeparator());
        return builder.toString();
    }
    
    
    
}