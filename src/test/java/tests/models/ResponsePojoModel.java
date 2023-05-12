package tests.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePojoModel {
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
