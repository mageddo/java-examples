How to deploy a web application with embedded tomcat without need Spring Boot

# Testando 

	$ ./gradlew clean build bootRun
	# access contact list page: http://localhost:9093/sbjt/customer?name=John

# Conclusoes
* Gasta pouca ram 40mb~ sem rodar sem tomcat, se for pra rodar com tomcat melhor usar o 
spring mesmo que com todas as dependencias gastou a mesma quantidade de RAM 

# Referencia
* http://stackoverflow.com/questions/24915333/how-to-embed-tomcat-in-java
* http://blog.sortedset.com/embedded-tomcat-jersey/
* http://www.jofre.de/?p=1227
