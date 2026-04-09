package site.easy.to.build.crm.dto;
import java.util.List;

public class ImportFileName<T> {
    
    private String file_name;
    private String table_name;
    private List <T> data;


    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public ImportFileName() {
    }

    public ImportFileName(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public ImportFileName(String file_name, List<T> data, String table_name) {
        this.file_name = file_name;
        this.data = data;
        this.table_name = table_name;
    }
    
}
