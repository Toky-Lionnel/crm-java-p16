package site.easy.to.build.crm.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class ImportRequest {
    private String table_name;
    private JsonNode data;



    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public JsonNode getData() {
        return data;
    }
    
    public void setData(JsonNode data) {
        this.data = data;
    }

    // getters / setters
}