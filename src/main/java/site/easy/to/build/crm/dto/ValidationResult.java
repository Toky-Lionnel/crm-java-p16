package site.easy.to.build.crm.dto;
import java.util.*;

public class ValidationResult<T> {

    private List<T> validItems = new ArrayList<>();
    private List<T> invalidItems = new ArrayList<>();
    private List<ImportError> errors = new ArrayList<>();
    private int totalItems;
    private int validItemCount;
    private int invalidItemCount;
    private String nomTable;
    private String fileName;

    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getValidItemCount() {
        return validItemCount;
    }

    public void setValidItemCount(int validItemCount) {
        this.validItemCount = validItemCount;
    }

    public int getInvalidItemCount() {
        return invalidItemCount;
    }

    public void setInvalidItemCount(int invalidItemCount) {
        this.invalidItemCount = invalidItemCount;
    }

    public String getNomTable() {
        return nomTable;
    }

    public void setNomTable(String nomTable) {
        this.nomTable = nomTable;
    }

    public ValidationResult() {
    }

    public ValidationResult(List<T> validItems, List<T> invalidItems, List<ImportError> errors, int totalItems, int validItemCount, int invalidItemCount, String nomTable) {
        this.validItems = validItems;
        this.invalidItems = invalidItems;
        this.errors = errors;
        this.totalItems = totalItems;
        this.validItemCount = validItemCount;
        this.invalidItemCount = invalidItemCount;
        this.nomTable = nomTable;
    }


    public List<T> getValidItems() {
        return validItems;
    }
    public void setValidItems(List<T> validItems) {
        this.validItems = validItems;
    }
    public List<T> getInvalidItems() {
        return invalidItems;
    }
    public void setInvalidItems(List<T> invalidItems) {
        this.invalidItems = invalidItems;
    }
    public List<ImportError> getErrors() {
        return errors;
    }
    public void setErrors(List<ImportError> errors) {
        this.errors = errors;
    }

    // getters / setters
}