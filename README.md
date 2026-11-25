# SLA Multi-Tenant

A multi-tenant SLA ticketing system built with Spring Boot. One backend serves many companies (tenants). Each tenant runs its own support desk with its own tickets, SLA rules and branding, and never sees another tenant's data.

> Work in progress. This is a personal project I'm building to go deeper into Spring Boot and multi-tenant design.

## The idea

Support teams promise customers a response and resolution time (an SLA). Most small companies track this by hand or with tools that are too heavy for them. This project is a SaaS backend where a company signs up as a tenant and gets:

- **Its own workspace.** Data is isolated per tenant, so one company can never read or change another company's tickets.
- **Ticket management.** Customers or staff open tickets with a priority, status and assignee, and the team works them through a clear lifecycle.
- **SLA policies.** Each tenant defines response and resolution targets per priority (for example, urgent tickets get a first reply within 1 hour).
- **SLA tracking.** The system measures every ticket against its policy, flags tickets close to breaching, and records the ones that breached.
- **Tenant branding.** Each tenant has its own logo, favicon and colors. Branding is versioned (draft, active, archived), so changes can be prepared and rolled back.
- **Tenant lifecycle.** A tenant can be pending, active, suspended or inactive, so the platform owner controls who has access.
- **Roles and permissions.** Platform admins, tenant admins and agents each get the access they need.

## Design goals

- Strict tenant isolation on every request.
- Clean layered structure: controller, service, repository, model.
- A simple REST API that a frontend or another service can use.
- Easy to run locally with a single MySQL database.

## Tech stack

- Java, Spring Boot (Web MVC, Data JPA, Bean Validation)
- MySQL
- Lombok
- Maven

## Run locally

Requirements: JDK and MySQL 8+.

1. Create the database:
   ```sql
   CREATE DATABASE tenantflow_central;
   ```
2. Set your MySQL username and password in `src/main/resources/application.properties`.
3. Start the app:
   ```bash
   ./mvnw spring-boot:run
   ```

## Author

Usman Ahmed, [GitHub](https://github.com/USmanFathy)
