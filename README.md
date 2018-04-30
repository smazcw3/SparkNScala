SparkNScala
-----------
 Setting Up Spark in Scala IDE for Eclipse
 
 1. Open Scala IDE for Eclipse. Select `File -> New -> Project`. Under `Maven`, select `Maven Project`
 
 2. In the new maven project, Select `Create a simple project (skip archetype selection)`, Click Next. You can then provide the details:
    ```
    Group Id: com.experiment.spark
    Artifact Id: sparkTest
    ```
    Click Finish
    
 3. Open `pom.xml` and insert the dependency for spark. In the end the `pom.xml` will look like below:
 
     ```
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
     <groupId>com.experiment.spark</groupId>
     <artifactId>sparkTest</artifactId>
     <version>0.0.1-SNAPSHOT</version>
  
    <dependencies>
  	<!-- https://mvnrepository.com/artifact/org.apache.spark/spark-core -->
	<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-core_2.11</artifactId>
    <version>2.3.0</version>
	  </dependency>	
    </dependencies>
  
    </project>
    ```
    
 4. Add Scala Nature to the Project. Right click on project -> `Configure` -> `Add Scala nature`
 
 5. Go to `Properties` -> `Scala Compiler` -> Select `Use Project Settings`. 
     Select `Scala Installation`: `Fixed Scala Installation 2.11.11 (built-in)`
     
 6. Go to `Propertis` -> `Java Build Path` -> `Click on Add Folder` -> Select `main` -> Click on `Create New Folder...` -> Type `scala` ->     Click on `Next`
 
    In the Inclusion and exclusion patterns, click `Add` -> `**/*.scala`, OK, FINISH, OK, APPLY, CLOSE
    
   
 
 
