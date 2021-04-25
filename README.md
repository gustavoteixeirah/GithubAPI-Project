# GitHub API
This is my implementation of the following Technical Challenge proposed by a company for a BackEnd Developer opportunity.

<h3 style="color:#adccf7">The challenge:</h3>

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

<h3 style="color:#adf7dd">Requirements:</h3>

1. Your API must be written using Java 8 or newer, ECMAscript 2015 or newer, or C# 8.0;  \[ Development status \] : <span style="color:#37eb34">OK</span>
2. Data must be retrieved from Github website by using web scraping techniques. Do not use Github’s API or download the source code as a ZIP file; do not use web scraping libraries. We would like to know your ideas on how it can be done; \[ Development status \] : <span style="color:#37eb34">OK</span>
3. Your API must support thousands of concurrent requests; \[ Development status \] : <span style="color:#f54761">Not implemented</span>
4. We think it’s ok if the first request to a particular repository takes some time to respond (since you depend on Github website response times), but we don’t expect the subsequent requests to be long; \[ Development status \] : <span style="color:#37eb34">OK</span>
5. We don’t expect to get timeout errors; \[ Development status \] : <span style="color:#eef772">Partially supported, depending on the quantity of files of the repository</span>
6. We must understand your code and use your API without having to ask you any questions. Our primary language is English so please use it on comments and documentation; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
7. We’d like to see SOLID principles in your solution; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
8. You are free to choose your API contracts (parameters and response formats) but we’d like to be able to integrate it with any other existing solutions; \[ Development status \] : <span style="color:#eef772">Partially implemented</span>
9. You don’t need to persist any data (but feel free to do it if you want);  \[ Development status \] : <span style="color:#37eb34">OK, implemented for performance increase after the first request of same repository</span>
10. We’d like to see at least one automated test;  \[ Development status \] : <span style="color:#f54761">Not implemented</span>
11. You must deploy your solution to a cloud provider like Amazon AWS or Heroku and send us the link to access it. It’s a plus if you publish a Docker image with your application (including its dependencies) in a registry like Docker Hub and let us know how to get it. \[ Development status \] : <span style="color:#37eb34">OK, on Heroku, please enter in contact with me to get the URL</span>
