package site.easy.to.build.crm.dto;
import java.util.*;

public class ValidationResult<T> {

    private List<T> validItems = new ArrayList<>();
    private List<T> invalidItems = new ArrayList<>();
    private List<ImportError> errors = new ArrayList<>();

    
    public ValidationResult() {
    }

    public ValidationResult(List<T> validItems, List<T> invalidItems, List<ImportError> errors) {
        this.validItems = validItems;
        this.invalidItems = invalidItems;
        this.errors = errors;
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