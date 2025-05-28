
# Price Service

**Price Service**

Este microservicio simula un sistema de gestión de precios que determina el precio aplicable de un producto para una marca y fecha/hora dados. El proyecto cumple todos los requisitos funcionales y técnicos.

## Tecnologías utilizadas
- Java 17
- Spring Boot 3
- H2 (base de datos en memoria)
- Maven
- JUnit 5 + MockMvc
- Arquitectura Hexagonal (Ports & Adapters)
- API REST
- HTML + JS (interfaz estática para consumo del servicio)

## Estructura del Proyecto

src/
├── main/
│   ├── java/com/example/pricing
│   │   ├── adapter/in/web         --> Controlador REST (PriceController.java)
│   │   ├── adapter/out/persistence--> Repositorio JDBC (JdbcPriceRepository.java)
│   │   ├── application            --> Lógica de negocio (PriceService.java)
│   │   └── domain                 --> Modelo y puertos (Price, PriceRepositoryPort)
│   └── resources/
│       ├── application.yml        --> Configuración (H2, puerto)
│       ├── data.sql               --> Creación de tabla y datos de ejemplo
│       └── static/index.html      --> Interfaz web HTML
└── test/
    └── java/com/example/pricing/ --> Pruebas JUnit5 (PriceControllerTest.java)
```

## Funcionalidades y requisitos implementados

### Arquitectura Hexagonal

La arquitectura está organizada en tres capas principales:
1. **Dominio** (`domain.model`, `domain.port.in`, `domain.port.out`)
2. **Aplicación** (`application`)
3. **Adaptadores** (`adapter.in.web`, `adapter.out.persistence`)

Ejemplo de flujo: `PriceController` → `PriceService` → `PriceRepositoryPort`


### Escalabilidad y preparación para producción

Aunque esta solución utiliza la base de datos H2 en memoria con fines de simplicidad y testing, todo el proyecto ha sido diseñado desde cero para facilitar su escalabilidad y despliegue en entornos de producción reales.

**Preparado para entornos productivos**

| Componente              | Producción recomendada             |
|-------------------------|------------------------------------|
| Base de datos           | PostgreSQL, Oracle, MySQL, MariaDB |
| Contenedores            | Docker, Docker Compose, Kubernetes |
| Despliegue continuo     | Jenkins, GitHub Actions, GitLab CI |
| Observabilidad          | Spring Boot Actuator, Prometheus   |
| Seguridad               | Spring Security, OAuth2, JWT       |
| Escalabilidad horizontal| Microservicios, balanceo de carga  |


### Mantenimiento y extensión

Se pueden inyectar nuevas implementaciones de persistencia sin alterar la lógica de negocio gracias al uso de puertos (PriceRepositoryPort).
Preparado para escalar horizontalmente, separar front/backend y desplegar en infraestructura cloud.

**Nota importante**

Por requisito explícito de la prueba, se ha utilizado H2 como solución de persistencia temporal, descartando el uso de PostgreSQL, Oracle u otros motores externos. No obstante, la solución está alineada para funcionar sobre esos entornos sin reescribir la 	lógica de negocio.


### Testing

El proyecto incorpora pruebas automatizadas utilizando JUnit 5 en combinación con Spring Boot Test y MockMvc, validando el comportamiento funcional del servicio expuesto a través del endpoint REST.
 
**Objetivo**

Las pruebas aseguran que el sistema responde correctamente ante distintos escenarios de consulta, cubriendo tanto resultados válidos como casos sin coincidencias. Esto refuerza la robustez del microservicio ante cambios, refactorizaciones o regresiones.

**Alcance de las pruebas**

Se utiliza MockMvc para simular peticiones HTTP reales sin necesidad de desplegar la aplicación.
Las pruebas se centran en el endpoint /api/prices/search y sus parámetros.
Se verifican tanto el status HTTP devuelto como los valores del contenido JSON, usando expresiones jsonPath.

**Ubicación**

src/test/java/com/example/pricing/PriceControllerTest.java

**Cobertura**

Cada test simula una petición GET con diferentes fechas y espera como resultado:
Un único precio válido (por prioridad y vigencia).
O bien una respuesta 404 si no se encuentra ninguno aplicable.

**Ejemplo de prueba**

	mockMvc.perform(get("/api/prices/search")
	        .param("brandId", "1")
	        .param("productId", "35455")
	        .param("applicationDate", "2020-06-14T16:00:00"))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.price").value(44.00));

Este enfoque permite validar que la lógica de negocio está correctamente conectada al controlador REST, garantizando respuestas correctas para todas las combinaciones esperadas.


### Control de versiones

**Control de versiones**

El proyecto ha sido desarrollado siguiendo convenciones estándar de control de versiones tanto a nivel de estructura de código 	como de gestión con herramientas como Git.
 
 **Estructura Maven**

La organización del proyecto sigue el esquema estándar de Maven, lo cual garantiza:
Compatibilidad con herramientas de construcción como Jenkins, GitHub Actions o SonarQube.
Separación clara entre código fuente (src/main/java), recursos (src/main/resources) y pruebas (src/test/java).
Posibilidad de gestión de dependencias, plugins, perfiles y configuraciones desde un único archivo pom.xml.


### Convenciones Git

Durante el desarrollo se han seguido buenas prácticas de versionado:
Estructura limpia y autocontenida del proyecto en un repositorio.
Los cambios se agrupan por funcionalidad (por ejemplo: implementación del servicio, creación de tests, ajustes en la consulta 	SQL).
Los recursos y configuraciones necesarias (como data.sql, README.md, HTML, etc.) están incluidos y versionados en el mismo repositorio.
El código es reproducible y portable desde cualquier entorno de desarrollo con Java y Maven instalados.
Esta disciplina de control de versiones facilita tanto la colaboración en equipo como la revisión, despliegue y mantenimiento 	futuro del proyecto.


### Configuración

La configuración principal del microservicio se encuentra centralizada en el archivo application.yml, ubicado en src/main/resources/. Este archivo define el comportamiento del entorno de ejecución en términos de base de datos, servidor 	embebido y herramientas de desarrollo.
		
**Parámetros clave configurados:**
		
			server:
			  port: 8088
			spring:
			  datasource:
			    url: jdbc:h2:mem:prices
			    driver-class-name: org.h2.Driver
			    username: sa
			  sql:
			    init:
			      mode: always
			  h2:
			    console:
			      enabled: true
**Explicación:**

		server.port: 8088
Define el puerto HTTP en el que se despliega la aplicación, evitando conflictos con otras apps por defecto en 8080.
		
		spring.datasource.url: jdbc:h2:mem:prices
Indica que se utilizará una base de datos H2 en memoria, útil para pruebas automáticas sin requerimientos externos.
		
		spring.sql.init.mode: always
Fuerza la ejecución automática del script data.sql al arrancar, asegurando que la tabla prices y sus datos estén listos.
		
		spring.h2.console.enabled: true
Habilita la consola web de H2 para consultas manuales y verificación visual de datos:
		
		http://localhost:8088/h2-console
		
**Resultado**		

Gracias a esta configuración, el proyecto puede ejecutarse directamente tras compilar (mvn spring-boot:run), con base de datos precargada y herramientas de desarrollo habilitadas sin pasos adicionales.
  
**Devolver único resultado**

Uno de los requisitos clave de la aplicación es que, ante una combinación específica de brandId, productId y applicationDate, 		el sistema devuelva únicamente el precio más relevante.
¿Qué significa “más relevante”?
Entre los precios válidos para ese instante de tiempo, el más relevante es aquel que:
Tiene la mayor prioridad (priority DESC)
En caso de igualidad, tiene la fecha de inicio más reciente (start_date DESC)

**Implementación en SQL**

	SELECT brand_id, product_id, start_date, end_date, price_list, priority, price, currency
	FROM prices
	WHERE brand_id = ?
	  AND product_id = ?
	  AND start_date <= ?
	  AND end_date >= ?
	ORDER BY priority DESC, start_date DESC
	LIMIT 1;

**Beneficios de esta estrategia**

Unicidad del resultado: evita ambigüedades cuando hay múltiples tarifas aplicables.
Consistencia: garantiza el mismo comportamiento incluso con bases de datos grandes.
Determinismo: siempre se devuelve el mismo precio para la misma entrada, cumpliendo con REST.
Este enfoque ha sido validado tanto en ejecución como en pruebas automatizadas, cumpliendo así el criterio de retorno único e inequívoco por consulta.
  
**Parámetros de entrada** 

El microservicio recibe las solicitudes mediante una llamada HTTP GET al endpoint /api/prices/search, donde los parámetros 	requeridos se especifican como query parameters en la URL. Esta decisión sigue las buenas prácticas REST para operaciones de 	consulta.

**Parámetros esperados**  

|  Nombre	      | Tipo	    |    Descripción
|-----------------|-------------|-------------------------------
|brandId	      |Long	        |Identificador de la cadena comercial (ej. 1 para ZARA)
|productId	      |Long	        |Identificador del producto (ej. 35455)
|applicationDate  |String       |Fecha y hora de aplicación del precio. Formato: yyyy-MM-dd'T'HH:mm:ss
-------------------------------------------------------------
 
**Ejemplo de llamada válida**  

GET /api/prices/search?brandId=1&productId=35455&applicationDate=2020-06-14T16:00:00

**Objetivo**    

Estos tres parámetros son necesarios para localizar un precio vigente en la base de datos, aplicando filtros por entidad, producto 	y rango de vigencia. Si alguno está ausente, el sistema devolverá un error 400 Bad Request.
Esta estructura permite integraciones limpias desde navegadores, clientes HTTP, frontend o herramientas como Postman y cURL.

**Retorno completo** 

El microservicio responde a las consultas con una representación clara y completa del precio aplicable, cumpliendo con todos los campos funcionales requeridos.
Cuando la búsqueda es exitosa, el sistema devuelve un objeto JSON con la siguiente información:
	
	
|  Campo	      | Tipo	    |    Descripción
|-----------------|-------------|-------------------------------
|productId	      |Long	        |Identificador del producto consultado
|brandId	      |Long	        |Identificador de la cadena comercial
|priceList        |Integer      |Identificador de la tarifa aplicada
|startDate        |String       |Fecha y hora de inicio de validez de la tarifa
|endDate          |String       |Fecha y hora de fin de validez de la tarifa
|price            |Decimal      |Precio aplicable para la fecha y producto
|currency         |String       |Moneda del precio
-------------------------------------------------------------


**Ejemplo de respuesta** 

	{
	  "brandId": 1,
	  "productId": 35455,
	  "priceList": 4,
	  "startDate": "2020-06-14T16:00:00",
	  "endDate": "2020-06-14T20:00:00",
	  "price": 44.00,
	  "currency": "EUR"
	}
	
** Beneficios** 

Toda la información necesaria para mostrar al cliente final.
Transparencia de condiciones de vigencia de la tarifa.
Preparado para ser consumido por frontend, integraciones externas o auditorías.
En caso de no encontrar un precio aplicable, se devuelve un código 404 Not Found, sin cuerpo.


### Auditoría y seguimiento funcional

El sistema permite rastrear y verificar el comportamiento de la lógica de precios desde distintas capas, facilitando su validación 	y supervisión durante la ejecución.

** Auditoría visual (interfaz web)** 

La interfaz HTML (index.html) ofrece un formulario para simular cualquier consulta.
El resultado se presenta en forma de tabla, permitiendo al usuario verificar visualmente:
	El precio aplicado
	La fecha de validez de la tarifa
    La moneda y la tarifa utilizada
Esto facilita las tareas de demostración, validación y soporte funcional.

** Auditoría técnica (logs)** 

Cada solicitud enviada al endpoint /api/prices/search se refleja en los logs del backend, mostrando parámetros de entrada y resultados devueltos.
Esto permite trazar el camino de la petición hasta el resultado final.

** Auditoría de base de datos (consola H2)**	

Se puede acceder a la consola web de H2 en:
		
		http://localhost:8088/h2-console
		
Permite ejecutar consultas manuales sobre la tabla prices, como:
		
		SELECT * FROM prices WHERE product_id = 35455;
		
Esto permite inspeccionar directamente las tarifas cargadas y sus fechas de validez.
 
** Beneficio**

Estas tres vías de auditoría permiten validar que el sistema responde correctamente y justificadamente para cada combinación de entrada, tanto a nivel funcional como técnico, lo que es clave para entornos donde se requiere trazabilidad, como ecommerce, 	retail o pricing dinámico.


### Interfaz Web


La aplicación incluye una interfaz web ligera desarrollada en HTML y JavaScript que permite realizar consultas manuales al 	endpoint /api/prices/search de forma amigable, sin necesidad de herramientas externas como Postman.

** Acceso**

Una vez la aplicación está en ejecución, la interfaz es accesible desde:
	
	http://localhost:8088/index.html

** Funcionalidad**

Formulario con campos para:
	ID del producto (productId)
	ID de la cadena (brandId)
	Fecha de aplicación (applicationDate)
	Validación básica del formulario.
	Envío de la consulta mediante JavaScript (fetch).
 Visualización de resultados en una tabla estilizada, con los campos clave:
	Precio
	Fecha de inicio y fin de vigencia (en formato dd/MM/yyyy)
	Tarifa, moneda, IDs
 Objetivo
	Esta interfaz facilita:
	La prueba funcional directa por usuarios no técnicos.
	La validación visual del comportamiento esperado.
	La auditoría sin necesidad de inspeccionar logs ni ejecutar queries SQL.
 Permite seleccionar:
	ID de marca
	ID de producto
	Fecha y hora (datetime-local)
	Resultado se muestra como tabla HTML estilizada, con fecha en formato dd/MM/yyyy.


### Endpoint REST	
	

	GET /api/prices/search
 
** Parámetros**
	brandId: ID de la cadena (ej: 1)
	productId: ID del producto (ej: 35455)
	applicationDate: Fecha y hora en ISO 8601 (2020-06-14T10:00:00)
 
** Ejemplo de respuesta**
	
	{
	  "brandId": 1,
	  "productId": 35455,
	  "priceList": 2,
	  "startDate": "2020-06-14T10:00:00",
	  "endDate": "2020-06-14T16:00:00",
	  "price": 50.00,
	  "currency": "EUR"
	}
	
### Pruebas	

  Archivo: PriceControllerTest.java
  Se validan los siguientes casos:
  	Test
		Fecha y hora de petición
  	Esperado
	  1
		14/06/2020 10:00
		PriceList 1 o 2
	  2
		14/06/2020 16:00
		PriceList 4
	  3
		14/06/2020 21:00
		PriceList 5
	  4
		15/06/2020 10:00
		PriceList 6
	  5
		16/06/2020 21:00
		PriceList 8
		

### Escalabilidad y preparación para producción			

Aunque esta solución utiliza la base de datos H2 en memoria con fines de simplicidad y testing, todo el proyecto ha sido diseñado desde cero para facilitar su escalabilidad y despliegue en entornos de producción reales.

** Preparado para entornos productivos**

Gracias al uso de una arquitectura limpia y desacoplada (Hexagonal), el sistema puede migrarse fácilmente a entornos más robustos

** Componente	Producción recomendada**

| Componente              | Producción recomendada             |
|-------------------------|------------------------------------|
| Base de datos           | PostgreSQL, Oracle, MySQL, MariaDB |
| Contenedores            | Docker, Docker Compose, Kubernetes |
| Despliegue continuo     | Jenkins, GitHub Actions, GitLab CI |
| Observabilidad          | Spring Boot Actuator, Prometheus   |
| Seguridad               | Spring Security, OAuth2, JWT       |
| Escalabilidad horizontal| Microservicios, balanceo de carga  |


** Adaptabilidad técnica**

La capa de persistencia (PriceRepositoryPort) permite inyectar fácilmente otras implementaciones (por JPA, Mongo, API externa, 	etc.) sin modificar el dominio ni la lógica de negocio.
El uso de Maven y Spring Boot permite empaquetar y desplegar rápidamente en cualquier entorno cloud (AWS, Azure, GCP).

** Ejemplo de escalado futuro**

En un entorno con millones de precios activos:
Se pueden aplicar índices SQL en campos como brand_id, product_id, start_date, end_date, priority para mejorar el rendimiento.
Se puede distribuir el backend en instancias escalables con balanceador de carga.
Se pueden exponer métricas de salud y uso para monitoreo en tiempo real.

Por los requisitos de esta prueba, se utiliza H2 en memoria. Sin embargo, el diseño y estructura están preparados para funcionar directamente sobre infraestructuras de producción sin necesidad de refactorización.

 | Cambio a base de datos real (PostgreSQL, Oracle)           |
 | Inyección de dependencias (Hexagonal)                      |
 | Consultas eficientes (LIMIT 1, índices posible             |
 | HTML y API desacoplados (posibilidad de separar frontend   |
 | Preparado para contenerización (Docker, Kubernetes         |
 -------------------------|------------------------------------
 
 **¿Cómo ejecutar?**

	mvn clean instal
	mvn spring-boot:run
Luego acceder 

	Web UI: http://localhost:8088/index.htm
	H2 Console: http://localhost:8088/h2-consol
	JDBC URL: jdbc:h2:mem:price
	User: sa
	
### Dockerización del proyecto

**Etapa de construcción**

FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

**Etapa de ejecución**

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]

Este Dockerfile usa un build multi-stage para reducir el tamaño de la imagen final.
	
**Crear .dockerignore para evitar copiar archivos innecesarios**

target
.git
*.md
*.iml
.idea

**Compilar la imagen Docker**

Ejecutar desde la raíz del proyecto

	docker build -t pricing-service

**Ejecutar el contenedor**

	docker run -p 8088:8088 pricing-service
	
Acceder a:	

Web UI: http://localhost:8088/index.html
H2 Console: http://localhost:8088/h2-console

**Verificación de ejecución**

Tomcat started on port(s): 8088
Started PricingApplication in 4.123 seconds

  **Estado final**

✔️ Todos los tests pasan✔️ Resultado único y correcto✔️ Interfaz clara y usable✔️ Cumple requisitos del enunciado✔️ Preparado para escalar✔️ Documentación completa✔️ Dokerización
