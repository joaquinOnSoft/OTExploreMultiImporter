# OTExploreMultiImporter
Multiple content types importer for OpenText Explore (Voice of the customer solution).
This command-line application read an input (Excel file, Twitter messages, or a Reddit threat) to ingest the information of our interest, into OpenText Explore. 

These text messages are inserted into the Solr Server used by **OpenText Explore**. 

Once the messages are available in **OpenText Explore** you can create your own dashboards to analyze the information listened to.

> [OpenText� Explore](https://www.opentext.com/products-and-solutions/products/customer-experience-management/contact-center-workforce-optimization/opentext-explore) is a business discovery solution that allows business and call center professionals to view cross-channel interactions collectively for a comprehensive picture of customer behaviors and relationships. 

## OpenText Explore importers
This project contains 3 auto-executable jar file to import different type of contents:
 * [OpenText Explore Excel Importer](doc/ot-explore-excel-importer.md)
 * [OpenText Explore Twitter Importer](doc/ot-explore-twitter-importer.md)
 * [OpenText Explore Reddit Importer](doc/ot-explore-reddit-importer.md)
 
## Tips & Tricks 
Some [tips & tricks](doc/tips-and-tricks.md) that you can apply in your import process.

## Applying changes on your instance
Once you have configured _Explore_ to import a new content type you must [apply the changes on your instance](doc/applying-changes-on-your-instance.md)