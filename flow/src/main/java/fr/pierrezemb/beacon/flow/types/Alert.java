package fr.pierrezemb.beacon.flow.types;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class Alert {

    @JsonProperty("selector")
    private String selector;
    @JsonProperty("active")
    private Boolean active;

    public Alert() {
    }

    public Alert(String selector, Boolean active) {
        this.selector = selector;
        this.active = active;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "selector='" + selector + '\'' +
                ", active=" + active +
                '}';
    }
}
