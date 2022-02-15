# OpenText Explore Twitter Importer

This command-line application listens to a Twitter Stream applying the filters, defined in twitter4j.properties, to select the tweets of our interest.

These tweets are inserted into the Solr Server used by **OpenText Explore**. 

Once the tweets are available in **OpenText Explore** you can create your owns dashboards to analyze the information listened.


> [OpenText� Explore](https://www.opentext.com/products-and-solutions/products/customer-experience-management/contact-center-workforce-optimization/opentext-explore) is a business discovery solution that allows business and call center professionals to view cross-channel interactions collectively for a comprehensive picture of customer behaviors and relationships. 

## Command line execution 

This utility is distributed as a runnable .jar file.

These are the accepted parameters:


usage: java -jar OTExploreTwitterImporter-22.01.24.jar
 * -c, --config						Define config file path

### Example of invocation

```

$ java -jar OTExploreTwitterImporter-22.01.jar --config "C:\ProgramFiles\OTExploreTwitterImporter\twitter-importer.properties"
```

## Configuration file: twitter-importer.properties

Configuration file that specifies the filter to apply to the Twitter stream

It supports theses properties:

 - **languages** - Specifies the tweets language of the stream (You can specify more than one separated by commas)
 - keywords - Specifies keywords to track (You can specify more than one separated by commas). 
 - **follow** - Twitter account names separated by commas (Don't include the @), e.g. madrid to follow @madrid
 - **ignoreretweet** - Ignore retweet. Possible values: true (Retweets ignored) or false (retweets imported). Default value: true 
 - **itag** - Text to be used as tag (Importer tag) to be added automatically to each twitt. Default value: "Twitter Importer"
 - **verbose** - Verbose mode. Possible values: true (messages are shown in the console) or false (messages are NOT shown in the console)
 - **host** - (optional) Solr server URL. Default value: http://localhost:8983
 - **content_type** - (optional) Content type. By default "Twitter", other possible values: "Micro Media"

This **twitter-importer.properties** file shows an example to listen to tweets about Madrid City Hall in Spanish: 
 
```
languages=es
keywords=Ayuntamiento de Madrid,Ayto Madrid,@MADRID
verbose=true
host=http://localhost:8983
follow=madrid,lineaMadrid
itag=Twitter Importer
ignoreretweet=true
content_type=Twitter
```

## Twitter stream API

There are a number of properties available for configuring Twitter4J (required to listen the Twitter stream). You can specify properties via **twitter4j.properties** file as follows : 

### via twitter4j.properties
Standard properties file named **"twitter4j.properties"**. Place it to either the current directory, root of the CLASSPATH directory.

```
oauth.consumerKey=*********************
oauth.consumerSecret=******************************************
oauth.accessToken=**************************************************
oauth.accessTokenSecret=******************************************
```

> SEE: [Twitter4J: Generic properties](http://twitter4j.org/en/configuration.html)

> NOTE: You need a [Twitter Developer](https://developer.twitter.com/en) account to get access to the Twitter Stream.


# Explore configuration

## Explore.Configuration.xml

The configuration file **Explore.Configuration.xml** is located at **<EXPLORE_HOME>\Explore.Configuration.xml**, e.g. 

```
D:\Program Files (x86)\OpenText\Explore\Explore.Configuration.xml 
```

### Twitter DocType

We must add a new DocType tag under the **<DocTypes>** in Explore.Configuration.xml in order to identify Twitter as a new input/document type analyzed by Explore:

```xml
  <DocTypes>
    ... 
    <DocType> 
      <Name>Twitter</Name>
      <GridFields>
        <Field column="Source">
          <Name>Followers</Name>
          <Tag>followers</Tag>
        </Field>
        <Field column="Source">
          <Name>Following</Name>
          <Tag>following</Tag>
        </Field>
        <Field column="Source">
          <Name>Favorite count</Name>
          <Tag>favorite_count</Tag>
        </Field>
        <Field column="Source">
          <Name>Retweet count</Name>
          <Tag>retweet_count</Tag>
        </Field>
        <Field column="Source">
          <Name>Latitude</Name>
          <Tag>latitude</Tag>
        </Field>
        <Field column="Source">
          <Name>Longitude</Name>
          <Tag>longitude</Tag>
        </Field>
        <Field column="Source">
          <Name>Importer Tag</Name>
          <Tag>itag</Tag>
        </Field>
      </GridFields>	          
    </DocType>            
  </DocTypes>
```

![Twitter doc type](../img/explore-doc-types.png "Twitter doc type")


### Group Twitter


We must add a new **Group** tag under the **<DoCriteriaItemscTypes>** in Explore.Configuration.xml in order to identify Twitter as a new group that can be used to filter by:

```xml
  <!--<CriteriaItem parametric="true" advancedSearch="true" trendWidget="true" autoPopulate="true" reloadUserData="true" groupBy="single" numberBuckets="6">
    parametric:     Show criteria item in the filter section on the search tab. Default value: false
    advancedSearch: Show criteria in the advanced search dialog. . Default value: true
    trendWidget:    Display criteria in the trend widget settings dialog. Only to be used with numeric criterias. Default value: false
    reloadUserData: Allows to reload user values for a parametric criteria. EG MAS Source. Default value: false
    groupBy:        Allows to group values in 3 ways: "single", "numeric" or "alphabetical"
    numberBuckets:  Number of buckets when gruping using numeric or alphabetical. Default value: 5
    numericStats:   Numeric criteria to be used in the Statistical Summary or in the High and Low Comparison widget.    
    -->

  <CriteriaItems>
  
    ...
      
    <Group name="Twitter">	             
      <CriteriaItem parametric="true" groupBy ="numeric" numberBuckets="10" advancedSearch="true" numericStats="true">
        <Name>Following</Name>
        <Tag>following</Tag>
        <ComparatorGroup>numeric</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Twitter</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>

      <CriteriaItem parametric="true" groupBy ="numeric" numberBuckets="10" advancedSearch="true" numericStats="true">
        <Name>Followers</Name>
        <Tag>followers</Tag>
        <ComparatorGroup>numeric</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Twitter</DocType>
        </AssociatedDocTypes>		
      </CriteriaItem>
	  
      <CriteriaItem parametric="true" groupBy ="numeric" numberBuckets="10" advancedSearch="true" numericStats="true">
        <Name>Favorite count</Name>
        <Tag>favorite_count</Tag>
        <ComparatorGroup>numeric</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Twitter</DocType>
        </AssociatedDocTypes>		
      </CriteriaItem>
	  
      <CriteriaItem parametric="true" groupBy ="numeric" numberBuckets="10" advancedSearch="true" numericStats="true">
        <Name>Retweet count</Name>
        <Tag>retweet_count</Tag>
        <ComparatorGroup>numeric</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Twitter</DocType>
        </AssociatedDocTypes>		
      </CriteriaItem>	  
    </Group>  
  
    ...
  
  </CriteriaItems>    
```
![Twitter groups](../img/explore-groups.png "Twitter groups")

## schema.xml (Solr)

The Solr configuration file **schema.xml** is located at **<SOLR_HOME>\solr-7.3.1\server\solr\configsets\interaction_config** e.g. 

```
D:\SolrCloud\solr-7.3.1\server\solr\configsets\interaction_config
```

### New Twitter fields on Solr

We must define new fields to be able to import extra metadata related with each Twitt 

```xml

  <!-- ADD YOUR CUSTOM FIELDS HERE -->

  <field name="followers" type="pint" indexed="true" stored="false" docValues="true" />
  <field name="followers_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="followers" dest="followers_search" />

  <field name="following" type="pint" indexed="true" stored="false" docValues="true" />
  <field name="following_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="following" dest="following_search" />

  <field name="favorite_count" type="pint" indexed="true" stored="false" docValues="true" />
  <field name="favorite_count_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="favorite_count" dest="favorite_count_search" />

  <field name="retweet_count" type="pint" indexed="true" stored="false" docValues="true" />
  <field name="retweet_count_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="retweet_count" dest="retweet_count_search" />
    
  <field name="latitude" type="pfloat" indexed="true" stored="false" docValues="true" />
  <field name="latitude_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="latitude" dest="latitude_search" />

  <field name="longitude" type="pfloat" indexed="true" stored="false" docValues="true" />
  <field name="longitude_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="longitude" dest="longitude_search" />
    
  <field name="itag" type="string" indexed="true" stored="false" docValues="true" />
  <field name="itag_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="itag" dest="itag_search" />


  <!-- END CUSTOM FIELDS -->
```

> **NOTE:** Field must be named using lower case


## Add languages to Explore

If you are going to import Twitter's message for any language different to **English** or **German*, 
you must follow the instructions given at [Add languages to Explore](doc/add-a-language-to-explore.md)
