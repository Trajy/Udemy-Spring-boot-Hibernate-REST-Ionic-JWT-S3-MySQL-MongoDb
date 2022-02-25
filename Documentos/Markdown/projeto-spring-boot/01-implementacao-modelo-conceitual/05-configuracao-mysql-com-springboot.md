# __CONFIGURANDO MYSQL NO SPRING BOOT__

Iremos assumir neste exemplo que o MySQL (_database_) e o DBeaver (gerencidador do banco de dados) ja estao devidamente instalados e configurados.

No arquivo `pom.xml` as seguintes dependencias devem ser adcionadas.

```xml
<!-- JPA Data (Vamos usar Repositórios, Entidades, Hibernate e etc.) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.19</version>
</dependency>
```
Por padrão, o Spring Boot fornece configurações de banco de dados para o banco de dados H2. Para usar o MySQL para nosso aplicativo, precisamos substituir essas configurações padrão. Assim que definirmos as propriedades do banco de dados em `resources/application.properties` do projeto, o Spring Boot não configurará mais o banco de dados padrão.

```
# Propriedades do bando de dados
spring.datasource.url=jdbc:mysql://localhost/testedb?useSSL=false&allowMultiQueries=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create

# Propriedades de Hibernate
# O dialeto SQL faz com que o Hibernate gere um SQL melhor para o banco de dados escolhido
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect

# propriedades para mostrar o sql no console e formatar em uma forma mais legivel
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```
A propriedade `spring.jpa.hibernate.ddl-auto` é para inicialização do banco de dados. É uma boa prática de segurança que, depois que seu banco de dados estiver no estado de produção, esteja com o valor `none`. Aqui estão alguns valores ddl comumente usados:

- none: O padrão para MySQL. Não fazemos nenhuma alteração na estrutura do banco de dados.
- update: O Hibernate altera o banco de dados de acordo com as estruturas da entidade.
- create: Cria o banco de dados todas as vezes, mas não o descarta ao fechar.
- create-drop: Cria o banco de dados e o descarta quando SessionFactory fecha.

crie o banco de dados manualmente utilizando a seguinte query SQL no gerenciador do bando de dados (neste caso DBeaver)

```SQL
CREATE DATABASE testedb;
```
e inicie a aplicacao para testar a conexao.

```log
10:35:51.084 [Thread-0] DEBUG org.springframework.boot.devtools.restart.classloader.RestartClassLoader - Created RestartClassLoader org.springframework.boot.devtools.restart.classloader.RestartClassLoader@2e6363a3

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.3)

2022-02-05 10:35:51.496  INFO 146281 --- [  restartedMain] b.c.e.s.p.ProjetoSpringBootApplication   : Starting ProjetoSpringBootApplication using Java 17.0.1 on trajy with PID 146281 (/home/trajy/Documentos/Contmatic/Workspace/Estagio/Estudos/Frameworks/Spring Boot/Udemy - Spring, Hibernate, REST, Ionic, JWT, S3, MySQL, MongoDb/Codigos Fonte/Exemplos sobre topicos/Back-end/ProjetoSpringBoot/ProjetoSpringBoot/target/classes started by trajy in /home/trajy/Documentos/Contmatic/Workspace/Estagio/Estudos/Frameworks/Spring Boot/Udemy - Spring, Hibernate, REST, Ionic, JWT, S3, MySQL, MongoDb/Codigos Fonte/Exemplos sobre topicos/Back-end/ProjetoSpringBoot/ProjetoSpringBoot)
2022-02-05 10:35:51.497  INFO 146281 --- [  restartedMain] b.c.e.s.p.ProjetoSpringBootApplication   : No active profile set, falling back to default profiles: default
2022-02-05 10:35:51.561  INFO 146281 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2022-02-05 10:35:51.561  INFO 146281 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2022-02-05 10:35:52.162  INFO 146281 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2022-02-05 10:35:52.174  INFO 146281 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 5 ms. Found 0 JPA repository interfaces.
2022-02-05 10:35:52.710  INFO 146281 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-02-05 10:35:52.719  INFO 146281 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-02-05 10:35:52.720  INFO 146281 --- [  restartedMain] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.56]
2022-02-05 10:35:52.801  INFO 146281 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-02-05 10:35:52.801  INFO 146281 --- [  restartedMain] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1239 ms
2022-02-05 10:35:52.971  INFO 146281 --- [  restartedMain] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2022-02-05 10:35:53.000  INFO 146281 --- [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.4.Final
2022-02-05 10:35:53.105  INFO 146281 --- [  restartedMain] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
2022-02-05 10:35:53.174  INFO 146281 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2022-02-05 10:35:53.280  INFO 146281 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2022-02-05 10:35:53.290  INFO 146281 --- [  restartedMain] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
2022-02-05 10:35:53.477  INFO 146281 --- [  restartedMain] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2022-02-05 10:35:53.484  INFO 146281 --- [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2022-02-05 10:35:53.538  WARN 146281 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2022-02-05 10:35:53.827  INFO 146281 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2022-02-05 10:35:53.848  INFO 146281 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-02-05 10:35:53.860  INFO 146281 --- [  restartedMain] b.c.e.s.p.ProjetoSpringBootApplication   : Started ProjetoSpringBootApplication in 2.76 seconds (JVM running for 3.234)
```

