# GitHub API
This is my implementation of the following Technical Challenge proposed by a company for a BackEnd Developer opportunity.

## The challenge:

Develop an API that returns the file count, the total number of lines and the total number of bytes grouped by file extension, of a given public Github repository. As in the example below:
```
[
    {
        "extension": "java",
        "count": 23,
        "lines": 763,
        "bytes": 26931
    },
    {
        "extension": "yml",
        "count": 3,
        "lines": 31,
        "bytes": 660
    },
    {
        "extension": "properties",
        "count": 1,
        "lines": 1,
        "bytes": 23
    }
]
```

### Requirements:

1. Your API must be written using Java 8 or newer, ECMAscript 2015 or newer, or C# 8.0;  \[ Development status \] : <span style="color:#37eb34">OK</span>
2. Data must be retrieved from Github website by using web scraping techniques. Do not use Github’s API or download the source code as a ZIP file; do not use web scraping libraries. We would like to know your ideas on how it can be done; \[ Development status \] : <span style="color:#37eb34">OK</span>
3. Your API must support thousands of concurrent requests; \[ Development status \] : <span style="color:#f54761">Not implemented</span>
4. We think it’s ok if the first request to a particular repository takes some time to respond (since you depend on Github website response times), but we don’t expect the subsequent requests to be long; \[ Development status \] : <span style="color:#37eb34">OK</span>
5. We don’t expect to get timeout errors; \[ Development status \] : <span style="color:#eef772">Partially supported, depending on the quantity of files of the repository</span> Please do not use the torvalds/linux repository or any other gigantic repository for now... :)
6. We must understand your code and use your API without having to ask you any questions. Our primary language is English so please use it on comments and documentation; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
7. We’d like to see SOLID principles in your solution; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
8. You are free to choose your API contracts (parameters and response formats) but we’d like to be able to integrate it with any other existing solutions; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
9. You don’t need to persist any data (but feel free to do it if you want);  \[ Development status \] : <span style="color:#37eb34">OK, implemented for performance increase after the first request of same repository</span>
10. We’d like to see at least one automated test;  \[ Development status \] : <span style="color:#f54761">Not implemented</span>
11. You must deploy your solution to a cloud provider like Amazon AWS or Heroku and send us the link to access it. It’s a plus if you publish a Docker image with your application (including its dependencies) in a registry like Docker Hub and let us know how to get it. \[ Development status \] : <span style="color:#37eb34">OK. Deployed on Heroku. Dockerfile OK and image available on DockerHub <a href="https://hub.docker.com/r/teixeira963/github-api">teixeira/github-api</a>.</span>


## API Documentation
The API documentation is available in the Swagger format. It is a human friendly and easy to navigate way to visualize the API’s resources documentation.
You can access it through this link:
<h5> <a href="https://github-api-microservice.herokuapp.com/swagger-ui.html">Documentation</a></h5>

<img src="https://teixeira983-images.s3.amazonaws.com/github-api-project-swagger-ui.png" alt="Screenshot of the Swagger UI documentation">

## Guide to perform a request

A simple and easy way to perform a request is to use <a href="https://curl.se/">curl</a> in a terminal.
The following example shows a curl command to get the statistics of the "iwhrim/qa-ninja-automacao-180" GitHub repository.

```
curl --location --request GET 'https://github-api-microservice.herokuapp.com/statistics' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://github.com/iwhrim/qa-ninja-automacao-180"
}'
```
The expected output in this case is a array containing the file count, the total number of lines and the total number of bytes grouped by file extension of all the files of the given repository:

```
[
    {
        "extension": "rb",
        "count": 29,
        "lines": 851,
        "bytes": 20243
    },
    {
        "extension": "jpg",
        "count": 28,
        "lines": 21056,
        "bytes": 3031412
    },
    {
        "extension": "txt",
        "count": 1,
        "lines": 35,
        "bytes": 826
    },
    {
        "extension": "rspec",
        "count": 1,
        "lines": 4,
        "bytes": 133
    },
    {
        "extension": "feature",
        "count": 5,
        "lines": 152,
        "bytes": 6347
    },
    {
        "extension": "gitignore",
        "count": 1,
        "lines": 3,
        "bytes": 24
    },
    {
        "extension": "md",
        "count": 1,
        "lines": 3,
        "bytes": 132
    },
    {
        "extension": "Rakefile",
        "count": 1,
        "lines": 42,
        "bytes": 1048
    },
    {
        "extension": "Gemfile",
        "count": 2,
        "lines": 20,
        "bytes": 430
    },
    {
        "extension": "lock",
        "count": 2,
        "lines": 184,
        "bytes": 4822
    },
    {
        "extension": "yml",
        "count": 5,
        "lines": 75,
        "bytes": 1587
    }
] 
``` 

## Running with Docker

I have provided a Dockerfile that you can build this application with a provider like Docker, for example.
However, if you do not want to build this application from zero, you can just type the following command in your terminal.

```
docker run -p 8080:8080 -p 27017:27017 teixeira963/github-api:latest
```

Attention: Please be sure that you do not have any other applications running on port 8080 and 27017).
Attention: You must have Docker installed and that you are able to access it though a terminal. If you haven't, you can follow <a href="https://docs.docker.com/desktop/">this</a> tutorial.
<p>
Now please skip to the "Testing" section or, to build the Docker image from zero, follow the next steps, starting with the prerequisites.

### Prerequisites
In order to use, first you are going to need a MongoDB connection string (I recommend MongoDB Atlas, that is the one I used in this project) in the following format:

```
URI="mongodb+srv://<MONGO_USER_NAME>:<MONGO_USER_PASSWORD>@mongodb-cluster.xqjcn.gcp.mongodb.net/GitHubApi?retryWrites=true&w=majority"

```

Please remember to substitute the following parameter with your own credentials.

```
<MONGO_USER_NAME>
<MONGO_USER_PASSWORD>
```

<p>
For the next steps, I assume you have Docker installed and that you are able to access it though a terminal. If you haven't, you can follow <a href="https://docs.docker.com/desktop/">this</a> tutorial.
<p>
You also need to have a Java SDK version 11, Maven version 3.8.1 or superior and git installed.
<p>
I suggest installing Amazon Corretto, which is a jdk provided by Amazon Web Services.
<p>
To verify that your setup is okey, run the following command in you favorite terminal.

```
mvn --version
```

This should return something like this:

```
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: D:\apache-maven-3.8.1\bin\..
Java version: 11.0.10, vendor: Amazon.com Inc., runtime: C:\Program Files\Amazon Corretto\jdk11.0.10_9
Default locale: pt_BR, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

If you couldn't get an output like that, please check out these tutorials:
1. <a href="https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/windows-7-install.html">Java 11</a> on Windows
2. <a href="https://maven.apache.org/install.html">Maven</a>

To check if git is installed, run:

```
git --version
```

You should see a output like this:

```
git version 2.31.1.windows.1
```

The version doesn't need to be exactly this one.
If you didn't get the output like that, please check out <a href="https://git-scm.com/downloads">this</a> link:

### Steps
First let clone the repository. Open your terminal, nagivate to a folder of your preference using the cd command, and then enter the following command:

```
git clone https://github.com/iwhrim/GithubAPI-Project.git
cd GithubAPI-Project/
```

Next, we need to generate a .jar file of the project.
Run the following command on the terminal:

```
mvn package
```

This should generate a .jar file on the /target folder. The last few lines of the output should be something like this:

```
[INFO] BUILD SUCCESS
[INFO] --------------------------------------------
[INFO] Total time:  8.234 s
[INFO] Finished at: 2021-04-25T14:32:29-03:00
[INFO] ------------------------------------------
```

Now, let's build the image:

```
docker build --build-arg URI="<SUBSTITUTE THIS WITH YOUR URI>" -t <YOUR DOCKER USERNAME>/github-api:latest .
```

You are going to see a output that should contain a line just like this:

```
[+] Building 4.4s (9/9) FINISHED
```

The next step is to run the image (please be sure that you do not have any other applications running on port 8080 and 27017):

```
docker run -p 8080:8080 -p 27017:27017 <YOUR DOCKER USERNAME>/github-api:latest
```

 ### Testing
Since the image is running on your local machine, we are going to use the localhost url.
First your can make a request through the terminal using curl:

```
curl --location --request GET 'localhost:8080/statistics' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "https://github.com/iwhrim/qa-ninja-automacao-180"
}'
```

You should see an output like this:

```
[{"extension":"rb","count":29,"lines":851,"bytes":20243},{"extension":"jpg","count":28,"lines":21056,"bytes":3031412},{"extension":"txt","count":1,"lines":35,"bytes":826},{"extension":"rspec","count":1,"lines":4,"bytes":133},{"extension":"feature","count":5,"lines":152,"bytes":6347},{"extension":"gitignore","count":1,"lines":3,"bytes":24},{"extension":"md","count":1,"lines":3,"bytes":132},{"extension":"Rakefile","count":1,"lines":42,"bytes":1048},{"extension":"Gemfile","count":2,"lines":20,"bytes":430},{"extension":"lock","count":2,"lines":184,"bytes":4822},{"extension":"yml","count":5,"lines":75,"bytes":1587}]
```

## Docker Compose
I have provided a docker compose file with a mongo container. You can use it running

```
docker-compose up -d
```

Also, you need to set your DB_URI environment variable to:

```
mongodb://localhost:27017/GitHubApi
```

Now, if you run the project locally, the application should be able to connect locally with mongo