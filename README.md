# rest-assured-homework

Rest-Assured + TestNG API tests.

## Stack

- Java 17
- Rest-Assured 5.5.0
- TestNG 7.10.2
- Maven Surefire (suite: `testng.xml`)

## Run

```bash
mvn test
```

## Tests

| Class | Covers |
|---|---|
| `BookStoreTest` | tasks 1-5 (books list, ISBN/author extraction, per-book validation via data provider, unauthorized DELETE) |
| `PetStoreTest` | tasks 6-9 (store order, form-based pet update, 404 body, login session id) |
| `OpenLibraryTest` | task 10 (Harry Potter search) |
