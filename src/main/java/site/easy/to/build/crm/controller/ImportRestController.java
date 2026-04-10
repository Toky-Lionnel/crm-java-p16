package site.easy.to.build.crm.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.export.ExportService;
import site.easy.to.build.crm.service.imports.ImportService;


@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class ImportRestController {

    private final ImportService importService;
    private final ExportService exportService;

    public ImportRestController(ImportService importService, ExportService exportService) {
        this.importService = importService;
        this.exportService = exportService;
    }

    @PostMapping 
    public ResponseEntity<String> importData(@RequestBody String json) throws Exception {
        return ResponseEntity.ok(this.importService.processImport(json).toString());
    }

    @GetMapping(value = "/export/{customerId}", produces = "text/csv")
    public ResponseEntity<String> exportDataAsCsv(@PathVariable int customerId) {
        String csv = this.exportService.exportDataToCsv(customerId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export-" + customerId + ".csv")
                .contentType(new MediaType("text", "csv"))
                .body(csv);
    }


    
}
