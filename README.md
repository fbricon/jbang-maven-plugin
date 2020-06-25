# jbang-maven-plugin

THIS IS A QUICK'N DIRTY POC. TONE DOWN YOUR EXPECTATIONS  

Maven plugin to execute a J'Bang script.

Requires J'Bang to be installed. 

Build with 

    mvn install
 
Then, in your project:
 
 - Create a J'Bang script under `src/jbang/java` (e.g. hello.java):
 
 - in your project's pom.xml, configure jbang-maven-plugin
 
 ```xml
 ...
 <build>
   <plugins>
   ...
      <plugin>
        <groupId>io.sidespin.maven.plugins</groupId>
        <artifactId>jbang-maven-plugin</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <configuration>
          <jbangScript>hello.java</jbangScript>    
        </configuration>
      </plugin>
   </plugins>
 </build>   
 ```
 
 if `<jbangScript>` is omitted, J'Bang will try to execute `src/jbang/java/script.java`.
 
  `<jbangScript>` can point to a URL
 
 To execute it, run : 
 
    mvn jbang:jbang
 
 or, to override the `<jbangScript>` configuration:
 
    mvn jbang:jbang -Djbang.source.script=/path/or/url/to/script
    
    
 
 
 
 