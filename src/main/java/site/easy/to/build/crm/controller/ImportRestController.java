package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.imports.ImportService;


@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class ImportRestController {

    private final ImportService importService;

    public ImportRestController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping 
    public ResponseEntity<String> importData(@RequestBody String json) throws Exception {
        return ResponseEntity.ok(this.importService.processImport(json).toString());
    }


    
}
