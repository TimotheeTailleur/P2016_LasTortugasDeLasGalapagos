# Las Tortugas de las Galapagos - Projet Informatique 

Student programming project. 
This app provides new ways of looking at StackOverflow data to improve site users' experience with an emphasis on personalization.

## Getting Started

### Packages
* project.database : Database managers for each user story
* project.datarecovery : Handles data recovery from the StackExchangeApi using the Java sdk by SanjivSingh (see credits)
* project.main : Main package with project entry point
* project.schema : Classes not in the StackExchangeApi sdk that we needed
* project.user : Implementations of user Oauth authentication and functionnalities

### User Stories
* Alice : Get recent unanswered questions in your top tags. Find out which users have more badges than you. Sort the questions you've answered to by their poppularity
* Bob : Get recent answered questions in your top tags. Suggest experts to follow. Suggestion tool for post titles and tags based on similar questions and common tag pairings.
* Dave : Find top answerers in a given single tag or the top contributor in a list of tags. Find the top tag in a given tag.

### Importing this project
Simply clone this git repo and run mvm install

## Maven dependencies and credits

pom.xml :
```
<dependencies>
	<dependency>
		<groupId>org.json</groupId>
		<artifactId>json</artifactId>
		<version>20090211</version>
	</dependency>
	<dependency>
		<groupId>org.apache.derby</groupId>
		<artifactId>derby</artifactId>
		<version>10.12.1.1</version>
	</dependency>
	<dependency>  
		<groupId>com.googlecode.stackexchange</groupId>  
		 <artifactId>stackoverflow-java-sdk-release</artifactId>  
		  <version>2.2.0</version>  
		</dependency>  
</dependencies>
```
Apache Derby : Embedded database containing static tables of StackOverflowData (i.e : list of the 47k tags on StackOverflow)

Org.Json : Used to parse JSON data returned by the StackExchangeApi

StackOverflow Java Sdk Release : StackExchange Api Java SDK released by [SanjivSingh] (https://github.com/sanjivsingh/stackoverflow-java-sdk) based on work by a [google code user] (http://code.google.com/p/stackoverflow-java-sdk/) 

## Javadoc

Javadoc located [here](http://lastortugas-javadoc.pagesperso-orange.fr/) (last update : 08/12/2016)

