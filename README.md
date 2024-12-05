# Barber scheduling app
An application for reserving barber appointments. Users can sign-up and sign-in with their personal credentials or JWT.

## Features
- User authentication with JWT (http//:localhost:8080/api/user/signup for sign-ups 
and http//:localhost:8080/api/user/signin for sign-ins).
- Secure storage of appointment data.
- API documentation with swagger (http//:localhost:8080/api/user/).
## Tech Stack
- Backend: Java (Spring Boot)
- Database: PostgreSQL
- Tools: Docker, pgAdmin, JWT
## Setup Instructions
1. Clone the repository:
   git clone https://github.com/Schdlrr/BackEnd.git
2. Navigate to the project directory
3. Create a .env file that specifies these variables 
   POSTGRES_USER=?
   POSTGRES_PASSWORD=?
   PGADMIN_DEFAULT_EMAIL=?
   PGADMIN_DEFAULT_PASSWORD=?
   SPRING_DATASOURCE_USERNAME=?(Same as POSTGRES_USER)
   SPRING_DATASOURCE_PASSWORD=?(Same as POSTGRES_PASSWORD)
   Replace the ? with desired log in information
4. Use the command
   mvn clean package
5. Use the command 
   docker build --no-cache -t *imageName* .
6. Use the command 
   docker-compose up -d
## Architecture
The application follows the MVC architecture:
- **Model**: Represents the database schema.
- **View**: REST APIs for client interaction.
- **Controller**: Handles business logic and routing.
## Future Enhancements
- Add sign-up and sign-in for Bussiness Owners.
- OAuth2.
- Sign-in as a guest.
