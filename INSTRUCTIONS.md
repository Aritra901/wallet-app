# Digital Wallet Microservice

This exercise evaluates your ability to implement and extend a Spring Boot microservice for a payments system. You will receive a ZIP file containing the initial project skeleton, including Git history. The skeleton provides basic structure for user registration and wallet creation, with 100% test coverage for that feature.

### Getting Started

1. Unzip the provided project folder.
2. Open the project in IntelliJ IDEA (or your preferred IDE supporting Spring Boot and Gradle).
3. Verify setup:
    - Run `./gradlew test` to execute existing tests.
    - Start the full stack with `docker-compose up --build` (requires Docker).
    - Access the app at `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui.html`.
4. Initialize Git if needed: `git init` (though history is included) and ensure commits are tracked.

### Exercise Overview

Implement the following user stories using Test-Driven Development (TDD). Focus on deposit, withdrawal, transfer, and balance inquiry features, plus any necessary fixes observed during development.

#### User Stories

1. **User Registration**  
   As a new customer, I want to register an account, so that I can use the wallet service.  
   **Acceptance Criteria**:
    - POST /users with {username, email} succeeds with 201 Created.
    - Duplicate username or email returns 409 Conflict.
    - Invalid fields return 400 Bad Request.

2. **Wallet Creation**  
   As a registered user, I want to create a digital wallet, so that I can hold funds.  
   **Acceptance Criteria**:
    - POST /wallets with {userId} (UUID) succeeds with 201 Created and balance = 0.
    - User not found returns 404.
    - User already has wallet returns 400 Bad Request.

3. **Deposit Funds**  
   As a wallet owner, I want to deposit funds, so that my balance increases.  
   **Acceptance Criteria**:
    - POST /wallets/{id}/deposit with {amount > 0} updates balance atomically.
    - Transaction record created (type=DEPOSIT).
    - Invalid amount returns 400.
    - Success returns updated wallet.

4. **Withdraw Funds**  
   As a wallet owner, I want to withdraw funds, so that my balance decreases.  
   **Acceptance Criteria**:
    - POST /wallets/{id}/withdraw with {amount > 0} succeeds only if sufficient balance.
    - Insufficient funds returns 400, no change to balance.
    - Transaction record created (type=WITHDRAWAL).
    - Balance never goes negative.

5. **Peer-to-Peer Transfer**  
   As a wallet owner, I want to transfer funds to another wallet, so that I can pay others.  
   **Acceptance Criteria**:
    - POST /transfers with {fromWalletId, toWalletId, amount > 0} is atomic.
    - Insufficient funds rolls back entirely, returns 400.
    - Two transaction records created (TRANSFER_OUT, TRANSFER_IN).
    - Success returns transfer details.

6. **Balance Inquiry**  
   As a wallet owner, I want to view my balance, so that I can check available funds.  
   **Acceptance Criteria**:
    - GET /wallets/{id}/balance returns current balance.
    - Wallet not found returns 404.

Implement features incrementally, following [TDD](https://martinfowler.com/bliki/TestDrivenDevelopment.html): write failing tests first, then minimal code to pass, refactor.

## Code Quality and Fixes
The provided skeleton contains deliberate bugs and design smells, such as incomplete entity mappings, incorrect HTTP status codes, and unhandled constraint violations. Identify and fix these during development to ensure robust error handling, atomicity, and REST compliance. Prioritize fixes via TDD: add failing tests for observed issues, implement corrections, then refactor. Document fixes in commit messages (e.g., "Fix bidirectional Wallet-User mapping to prevent inconsistencies"). Evaluation includes verification of resolved issues alongside new features.

### Best Practices

- Adhere to SOLID principles in service and controller layers.
- Use @Transactional for atomic operations.
- Validate inputs with Jakarta Bean Validation.
- Handle exceptions globally with @ControllerAdvice.
- Ensure 100% test coverage for new features using JUnit 5, Mockito, and integration tests with Testcontainers.
- Follow REST conventions: appropriate HTTP methods, status codes, and response bodies.
- Use BigDecimal for monetary values.
- Keep classes focused (Single Responsibility Principle).
- Use of coding agents (e.g., GitHub Copilot) is permitted; document any significant AI assistance in commit messages.

### TDD and Git Workflow

- Commit after each TDD cycle: red (failing test), green (passing code), blue (refactor).
- Use clear, descriptive commit messages in an imperative mood, e.g.:
    - "Add test for wallet deposit endpoint"
    - "Implement deposit logic with balance update"
    - "Refactor service to extract transaction logging"
- Branch per feature if desired, but merge to main for submission.

### Submission

- Complete within the allotted time (e.g., 4-6 hours).
- Prepare the submission file using the following command:
  ```bash
  ./gradlew prepareSubmission
  ```
  or
   ```bash
  ./gradlew pS
  ```
  This will create a ZIP file named `submission-<your-username>-<date>.zip`.
- **Warning**: Check the generated file name. If it is named `submission-anonymous-<date>.zip`, your submission **won't be accepted**. This happens if your git author name is not set.
- To set your git author details, run:
  ```bash
  # Globally
  git config --global user.name "Your Name"
  git config --global user.email "your.email@example.com"
  
  # Or locally for this repository
  git config user.name "Your Name"
  git config user.email "your.email@example.com"
  ```
  After setting these, commit your changes and run `./gradlew prepareSubmission` again.
- Email the ZIP as a reply to the email."
- Include a brief note on challenges faced and key decisions.

### Evaluation Criteria

- Functionality: All acceptance criteria met, including edge cases and error handling.
- TDD Adherence: Git history shows incremental commits aligned with red-green-refactor cycles.
- Code Quality: Clean, readable code; adherence to best practices and SOLID.
- Unit test coverage: More than 90% for new code.
- Integration test coverage: More than 80% for new code.
- Code quality: Linting passes without warnings.
- Design: Scalable, maintainable architecture; proper separation of concerns.
- Documentation: Detailed swagger API documentation.

Contact the recruiter for clarifications. Good luck.