# Tips & Tricks

## Removing all the imported  inputs

During your test you can decide to remove all inputs imported. The fastest way to do it is just executing this command from a terminal/console as administrator:

> NOTE: This will delete all the data in your Solr instance!

```
d:> cd d:\SolrCloud\solr-7.3.3\example\exampledocs

d:\SolrCloud\solr-7.3.3\example\exampledocs> java -Dc=interaction -Ddata=args -Dcommit=true -jar post.jar "<delete><query>*:*</query></delete>"
```

> NOTE: The path of your Solr installation can vary in your environment.

If you just want to remove a specific content type you, .e.g. `Ticket` you can use a new query:

```
d:> cd d:\SolrCloud\solr-7.3.3\example\exampledocs

d:\SolrCloud\solr-7.3.3\example\exampledocs> java -Dc=interaction -Ddata=args -Dcommit=true -jar post.jar "<delete><query>itag: Ticket</query></delete>"
```
