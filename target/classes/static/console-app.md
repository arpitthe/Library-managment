Console App (Optional)

If you still want a console interface in addition to the web UI, you can build a small Java client that calls the REST endpoints using `HttpClient`.

Example snippets:

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:8080/api/books"))
    .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"Dune\",\"author\":\"Frank Herbert\",\"totalCopies\":3}"))
    .header("Content-Type","application/json")
    .build();
HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
System.out.println(resp.body());
```





