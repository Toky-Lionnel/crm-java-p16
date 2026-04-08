package site.easy.to.build.crm.dto;

public class ImportError {
    private String table;
    private Integer line;
    private String message;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImportError() {}

    public ImportError(String table, Integer line, String message) {
        this.table = table;
        this.line = line;
        this.message = message;
    }

    // getters
}