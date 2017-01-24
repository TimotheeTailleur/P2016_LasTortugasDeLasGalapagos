# Las Tortugas de las Galapagos - Java programming project

Student programming project. 
This app provides new ways of looking at StackOverflow data to improve site users' experience with an emphasis on personalization.

## Getting Started

### Packages
MVC based App : 
#### Model : 
* model.database : Database managers for each user story
* model.datarecovery : Handles data recovery from the StackExchangeApi using the Java sdk by SanjivSingh (see credits)
* model.schema : Complementary classes to the StackExchangeApi SDK (for instance: TopUser represents a top Answerer in given tags)
* model.user : User Classes through which access to the ApiManagers methods should be done

#### View :
* view.main : Main package with project entry point
* view.ref : Login page and common elements of GUI windows (ex : footer with the exit button)
* view.users : GUI for each user stories ; each displayed in a different tab

#### Controller :
* controller : UserFactory
* controller.userParameter : Classes handling each user story's parameters

### User Stories
* Alice : Get recent unanswered questions in your top tags. Find out which users have more badges than you. Sort the questions you've answered to by their popularity
* Bob : Get recent answered questions in your top tags. Suggest experts to follow. Suggestion tool for post titles and tags based on similar questions and common tag pairings.
* Dave : Find top answerers in a given single tag or the top contributor in a list of tags. Find the top tag in a given tag.

### Importing this project
Simply clone this git repo and run mvm install

## Maven dependencies and credits

pom.xml :
```
<dependencies>
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
	<dependency>
		<groupId>commons-lang</groupId>
		<artifactId>commons-lang</artifactId>
		<version>2.6</version>
	</dependency> 
	<dependency>
		<groupId>commons-configuration</groupId>
		<artifactId>commons-configuration</artifactId>
		<version>1.10</version>
</dependency>
</dependencies>
```
Apache Derby : Embedded database containing tables of StackOverflowData (i.e : list of the 47k tags on StackOverflow & some user's tagscores & postcounts)

StackOverflow Java Sdk Release : StackExchange Api Java SDK released by [SanjivSingh] (https://github.com/sanjivsingh/stackoverflow-java-sdk) based on work by a [google code user] (http://code.google.com/p/stackoverflow-java-sdk/) 

Commons-lang : Used for different methods of String manipulation

Commons-configuration : Used for saving and loading of parameters

Also google's GSON library is used through our dependency towards the aforementioned stackoverflow Sdk

## Javadoc

Javadoc located [here](http://lastortugas-javadoc.pagesperso-orange.fr/) (last update : 01/24/2017)

## Credits
Sanjivsingh for his StackExchange Api Java SDK [github repo] (https://github.com/sanjivsingh/stackoverflow-java-sdk)