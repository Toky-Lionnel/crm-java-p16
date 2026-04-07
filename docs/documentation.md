# Documentation Spring Boot

1. Données : 
    tsy azo atao misy NULL raha tsy izany tsy mandeha ilay Thymeleaf 
```java
    {{customer.info}} // raha Null ilay info dia manao erreur ilay Thymeleaf
```

2. Jerena tsara ny userRoles : atao admin par défaut ny compte ampiasaina 


3. Service : interface ray de classe ray hanaovana implementation

4. Exemple si on veut du JSON 
```java 
@GetMapping(value = "/manager/show-all/json", produces = "application/json")
public ResponseEntity<List<Budget>> getAllContractsJson() {
    List<Budget> budgets = budgetService.findAll();
    return ResponseEntity.ok(budgets);
}
```

5. Gestion ana roles anaty SecurityConfig mila jerena tsara

6. Mandalo SecurityConfig aloha ny requete vao mandalo Controller , raha ohatra ka bloqué any am SecurityConfig dia misy problème ana CORS foana 

7. Données de test mila atao mifanaraka @ le enum sns ao 