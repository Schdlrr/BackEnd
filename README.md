This repository will store BackEnd code of Schdlrr project

To run this part of the project you will need to download Apache Maven https://maven.apache.org/download.cgi 
after downloading and setting up apache maven (assuming you have docker already installed), you will be able to package the program
by using the command mvn clean package in the directory where the code is present. Then do docker build --no-cache -t *imageName* and then
docker-compose up -d to run the full application.

Important uris
http//:localhost:8080/api/user/ for the swagger api documentation
http//:localhost:8080/api/user/signin for confirming signins
http//:localhost:8080/api/user/signup for confirming signups