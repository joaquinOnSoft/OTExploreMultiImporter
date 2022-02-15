# OTExploreExcelImporter
Excel importer for OpenText Explore (Voice of the customer solution).
This command-line application read an Excel file to ingest the columns of our interest, usually text messages, into OpenText Explore. 

These text messages are inserted into the Solr Server used by **OpenText Explore**. 

Once the messages are available in **OpenText Explore** you can create your owns dashboards to analyze the information listened.


> [OpenText™ Explore](https://www.opentext.com/products-and-solutions/products/customer-experience-management/contact-center-workforce-optimization/opentext-explore) is a business discovery solution that allows business and call center professionals to view cross-channel interactions collectively for a comprehensive picture of customer behaviors and relationships. 

## Command line execution 

This utility is distributed as a runnable .jar file.

These are the accepted parameters:

usage: java -jar OTExploreExcelImporter-21.1.jar
 - **-h, --host**			(Optional)		Solr host URL (used by OpenText Explore). Default value: http://localhost:8983
 - **-t, --tag**			(Optional)		Explore Importer tag. Added to each article importer. Default value: "Excel Importer"
 - **-e, --excel**			(Mandatory)		Excel file to be imported  
 - **-c, --config**			(Mandatory)		JSON file that defines the mapping between excel columns and Solr fields
 - **-k, --contentType**	(Mandatory)		Explore's content type, i.e. Ticket, Reddit, Twitter, Survey, Call...

### Examples of invocation

```
$ java -jar OTExploreExcelImporter.21.1.jar --excel input_example.xlsx --config excel_mapping.json --contentType Ticket

$ java -jar OTExploreExcelImporter.21.1.jar --excel input_example.xlsx --config excel_mapping.json --host http://localhost:8983 --contentType Ticket 

$ java -jar OTExploreExcelImporter.21.1.jar --excel input_example.xlsx --config excel_mapping.json --host http://localhost:8983 --tag "Excel Importer" --contentType Ticket

$ java -jar OTExploreExcelImporter.21.1.jar --excel input_example.xlsx --config excel_mapping.json --tag "Excel Importer" --contentType Ticket
```


## Configuration file: excel_mapping.json

Configuration file that define the mapping between excel fields and Solr fields:

There are two main section on the configuration file: 
   * **fields**: Define the mapping between an excel field and a Solr field
   * **fieldHandlers**: Define which field handler must be used to set a specific Solr field

### Fields      
Each *field* supports theses properties:

 * **excelName**: Excel column name
 * **solrName**: Solr field name


> These are the key Solr fields that must be mapped in order to import the information into Explore:
> 
> - **reference_id**: Reference identifier 
> - **interaction_id**: Interaction identifier
> - **title**: Interaction title
> - **author_name**: Author name (usually an Explore user)
> - **ID**: Identifier
> - **type**: Content type. You can reuse pre-existing types or you can create your own
> - **published_date**: Interactio's publication date
> - **date_time**:  Interactio's creation date
> - **content**: Interaction content (call transcription, Twitter message, e-mail...)
>
> You can reference your owns Solr fields. It will require additional configuration on Explore
    
 * **type**: Data type. Supported values: integer, string, date
 * **skip**: Flag to indicate that this file must be ignored or not. Valid values: true, false
 * **format**: (Optional) Date format to apply on a data fields. [Check valid formats](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html)

### Field Handlers

Field Handlers are classes that extends *com.opentext.explore.importer.excel.fieldhandlers.AbstractFieldHandler* and set a Solr field based on a information contained in a given Excel row.

Each *fieldHanler* supports theses properties:

 * **inputSolrNames**: List of solr field names used as **INPUT** fields
 * **outputSolrNames**: List of solr field names used as **OUTPUT** fields
 * **javaClass**: class that extends *com.opentext.explore.importer.excel.fieldhandlers.AbstractFieldHandler* 

#### com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerConcat
Concatenate the value of the input fields and assign the value to the output fields.

#### com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerCopy
Copy the value of the 1st input field and assign the value to all the output fields.

#### com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerRandomTeamMember
Choose a team member name, randomly, from a predefined list of 8 users (team members)

### Configuration example
This **excel_mapping.json** file shows an example: 

```json

{
	"fields": [
		{
			"excelName": "Identificadorticket",
			"solrName": "reference_id",
			"type": "integer",
			"skip": false			
		},
		{
			"excelName": "FechaApertura",
			"solrName": "published_date",
			"type": "date",			
			"skip": false,
			"format": "MM/dd/yy"					
		},
		{
			"excelName": "FechaCierre",
			"solrName": "FechaCierre",
			"type": "date",					
			"skip": false,
			"format": "MM/dd/yy"								
		},
		{
			"excelName": "EsContactar",
			"solrName": "EsContactar",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "CPI",
			"solrName": "CPI",
			"type": "integer",
			"skip": false					
		},
		{
			"excelName": "Producto",
			"solrName": "Producto",
			"type": "string",
			"skip": false					
		},		
		{
			"excelName": "TipoPeticion",
			"solrName": "TipoPeticion",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Comentarios del back office tramitador",
			"solrName": "content",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Comentarios_Oficina sobre la queja",
			"solrName": "ComentariosOficina",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Departamento",
			"solrName": "Departamento",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Estado",
			"solrName": "Estado",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "CPI",
			"solrName": "CPI",
			"type": "integer",
			"skip": true					
		},
		{
			"excelName": "Familia",
			"solrName": "Familia",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Producto",
			"solrName": "Producto",
			"type": "integer",
			"skip": true					
		},		
		{
			"excelName": "TipoPeticion",
			"solrName": "TipoPeticion",
			"type": "string",
			"skip": true					
		},
		{
			"excelName": "Motivo_Queja",
			"solrName": "MotivoQueja",
			"type": "string",
			"skip": false					
		},
		{
			"excelName": "Origen_Queja_2",
			"solrName": "OrigenQueja",
			"type": "string",
			"skip": false					
		}																												
	],
	"fieldHandlers":[
		{
			"inputSolrNames": ["reference_id"],
			"outputSolrNames": ["interaction_id", "ID"],
			"javaClass": "com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerCopy"
		},
		{
			"inputSolrNames": ["content", "ComentariosOficina"],
			"outputSolrNames": ["content"],
			"javaClass": "com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerConcat"
		},
		{
			"inputSolrNames": ["reference_id", "Producto", "TipoPeticion"],
			"outputSolrNames": ["title"],
			"javaClass": "com.opentext.explore.importer.excel.fieldhandlers.FieldHandlerConcat"
		}
	]
}
```

## Explore configuration

If you want to create a new "document type" to be analyze by OpenText Explore you must modify Explore and Solr configuration.

Next section describe the step to be followed to create a new document type called 'Ticket'. 

> **NOTE:** The JSON configuration file located at `src/main/resources/excel_mapping.json` will be used as reference in this example
>
> The steps described on this section are only required if you want to create your own document type.
> 
> This utility includes and app that generates the sections to be included in the XML configuration files
>
> Execute: 
>
>   java com.opentext.explore.importer.excel.configgenerator.ConfigGeneratorLauncher --config src\main\resources\excel_mapping.json --doctype Ticket 

### Explore.Configuration.xml

The configuration file **Explore.Configuration.xml** is located at **<EXPLORE_HOME>\Explore.Configuration.xml**, e.g. 

```
D:\Program Files (x86)\OpenText\Explore\Explore.Configuration.xml 
```

#### Ticket DocType

We must add a new DocType tag under the **<DocTypes>** in **Explore.Configuration.xml** in order to identify 'Ticket' as a new input/document type analyzed by Explore. In our example: 

```xml
  <DocTypes>
    ...
    <DocType>
      <Name>Ticket</Name>
      <GridFields>
        <Field column="Source">
          <Name>fecha cierre</Name>
          <Tag>fecha_cierre</Tag>
        </Field>
        <Field column="Source">
          <Name>es contactar</Name>
          <Tag>es_contactar</Tag>
        </Field>
        <Field column="Source">
          <Name>cpi</Name>
          <Tag>cpi</Tag>
        </Field>
        <Field column="Source">
          <Name>producto</Name>
          <Tag>producto</Tag>
        </Field>
        <Field column="Source">
          <Name>tipo peticion</Name>
          <Tag>tipo_peticion</Tag>
        </Field>
        <Field column="Source">
          <Name>comentarios oficina</Name>
          <Tag>comentarios_oficina</Tag>
        </Field>
        <Field column="Source">
          <Name>departamento</Name>
          <Tag>departamento</Tag>
        </Field>
        <Field column="Source">
          <Name>estado</Name>
          <Tag>estado</Tag>
        </Field>
        <Field column="Source">
          <Name>familia</Name>
          <Tag>familia</Tag>
        </Field>
        <Field column="Source">
          <Name>motivo queja</Name>
          <Tag>motivo_queja</Tag>
        </Field>
        <Field column="Source">
          <Name>origen queja</Name>
          <Tag>origen_queja</Tag>
        </Field>
      </GridFields>
    </DocType>
  </DocTypes>
```


#### Group Ticket

We must add a new **Group** tag under the **<DoCriteriaItemscTypes>** in **Explore.Configuration.xml** in order to identify *Ticket* entries as a new group that can be used to filter by. In our example: 

```xml
<CriteriaItems>

	...
	
    <Group name="Ticket">
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>fecha  cierre</Name>
        <Tag>fecha_cierre</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>es  contactar</Name>
        <Tag>es_contactar</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="numeric" advancedSearch="true" numericStats="true" numberBuckets="20">
        <Name>cpi</Name>
        <Tag>cpi</Tag>
        <ComparatorGroup>numeric</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>producto</Name>
        <Tag>producto</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>tipo  peticion</Name>
        <Tag>tipo_peticion</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>comentarios  oficina</Name>
        <Tag>comentarios_oficina</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>departamento</Name>
        <Tag>departamento</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>estado</Name>
        <Tag>estado</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>familia</Name>
        <Tag>familia</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>motivo  queja</Name>
        <Tag>motivo_queja</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
      <CriteriaItem parametric="true" groupBy="alphabetical" numberBuckets="20">
        <Name>origen  queja</Name>
        <Tag>origen_queja</Tag>
        <ComparatorGroup>string</ComparatorGroup>
        <AssociatedDocTypes>
          <DocType>Ticket</DocType>
        </AssociatedDocTypes>
      </CriteriaItem>
    </Group>
  </CriteriaItems>
```

### schema.xml (Solr)

The Solr configuration file **schema.xml** is located at **<SOLR_HOME>\solr-7.3.1\server\solr\configsets\interaction_config** e.g. 

```
D:\SolrCloud\solr-7.3.1\server\solr\configsets\interaction_config
```

#### New `Ticket` fields on Solr

We must define new fields to be able to import extra metadata related with each Ticket input 

```xml
  <field name="fecha_cierre" type="string" indexed="true" docValues="true" />
  <field name="fecha_cierre_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="fecha_cierre" dest="fecha_cierre_search" />
  
  <field name="es_contactar" type="string" indexed="true" docValues="true" />
  <field name="es_contactar_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="es_contactar" dest="es_contactar_search" />
  
  <field name="cpi" type="pint" indexed="true" docValues="true" />
  <field name="cpi_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="cpi" dest="cpi_search" />
  
  <field name="producto" type="string" indexed="true" docValues="true" />
  <field name="producto_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="producto" dest="producto_search" />
  
  <field name="tipo_peticion" type="string" indexed="true" docValues="true" />
  <field name="tipo_peticion_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="tipo_peticion" dest="tipo_peticion_search" />
  
  <field name="comentarios_oficina" type="string" indexed="true" docValues="true" />
  <field name="comentarios_oficina_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="comentarios_oficina" dest="comentarios_oficina_search" />
  
  <field name="departamento" type="string" indexed="true" docValues="true" />
  <field name="departamento_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="departamento" dest="departamento_search" />
  
  <field name="estado" type="string" indexed="true" docValues="true" />
  <field name="estado_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="estado" dest="estado_search" />
  
  <field name="familia" type="string" indexed="true" docValues="true" />
  <field name="familia_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="familia" dest="familia_search" />
  
  <field name="motivo_queja" type="string" indexed="true" docValues="true" />
  <field name="motivo_queja_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="motivo_queja" dest="motivo_queja_search" />
  
  <field name="origen_queja" type="string" indexed="true" docValues="true" />
  <field name="origen_queja_search" type="explore_filter_text" indexed="true" stored="false" multiValued="true" />
  <copyField source="origen_queja" dest="origen_queja_search" />
```

> **NOTE:** Field must be named using lowercase

#### Adding configuration to manage text in Spanish (OPTIONAL)

##### schema.xml
Add the following sections just before the &lt;field name="language"&gt; and *&lt;/schema&gt;* tags:

```xml
<schema name="default-config" version="1.6">

   ...

   <field name="es_title" type="es_explore_text" indexed="true" stored="true" />
   <field name="es_content" type="es_explore_text" indexed="true" stored="true" />
   <field name="es_content_1" type="es_explore_text" indexed="true" stored="true" />
   <field name="es_content_2" type="es_explore_text" indexed="true" stored="true" />
   <dynamicField name="es_survey_answer_text_*" type="es_explore_text" indexed="false" stored="true" />
   <field name="es_survey_answer_text" type="es_explore_text" indexed="true" stored="true" multiValued="true" />
   
   <field name="es_search_fields" type="es_explore_text" indexed="true" stored="false" multiValued="true" />
   <copyField source="es_content_1" dest="es_search_fields" />
   <copyField source="es_content_2" dest="es_search_fields" />
   <copyField source="es_content" dest="es_search_fields" />
   <copyField source="es_title" dest="es_search_fields" />
   <copyField source="es_survey_answer_text_*" dest="es_search_fields" />
   
   <field name="es_tf_search_fields" type="es_explore_terms_text" indexed="true" stored="false" multiValued="true" />
   <copyField source="es_content_1" dest="es_tf_search_fields" />
   <copyField source="es_content_2" dest="es_tf_search_fields" />
   <copyField source="es_content" dest="es_tf_search_fields" />
   <copyField source="es_title" dest="es_tf_search_fields" />
   <copyField source="es_survey_answer_text_*" dest="es_tf_search_fields" />
   
   <field name="language" type="string" indexed="true" stored="false" docValues="true" />
   
   ...
   
   <fieldType name="es_explore_text" class="solr.TextField" positionIncrementGap="100">
      <analyzer>
         <tokenizer class="solr.StandardTokenizerFactory" />
         <filter class="solr.LowerCaseFilterFactory" />
         <filter class="solr.StopFilterFactory" ignoreCase="true" words="lang/stopwords_es.txt" format="snowball" />
         <!--filter class="solr.GermanNormalizationFilterFactory"/ -->
         <filter class="solr.KeywordMarkerFilterFactory" protected="protwords.txt" />
         <filter class="solr.SpanishLightStemFilterFactory" />
         <!-- more aggressive: <filter class="solr.SnowballPorterFilterFactory" language="Spanish"/> -->
      </analyzer>
   </fieldType>
   <fieldType name="es_explore_terms_text" class="solr.TextField" positionIncrementGap="100">
      <analyzer>
         <tokenizer class="solr.StandardTokenizerFactory" />
         <filter class="solr.LowerCaseFilterFactory" />
         <filter class="solr.StopFilterFactory" ignoreCase="true" words="lang/stopwords_es.txt" format="snowball" />
         <!--filter class="solr.GermanNormalizationFilterFactory"/ -->
      </analyzer>
   </fieldType>
   <!-- Similarity is the scoring routine for each document vs. a query.
	       A custom Similarity or SimilarityFactory may be specified here, but 
	       the default is fine for most applications.  
	       For more info: http://lucene.apache.org/solr/guide/other-schema-elements.html#OtherSchemaElements-Similarity
   -->
   <!--
	     <similarity class="com.example.solr.CustomSimilarityFactory">
	       <str name="paramkey">param value</str>
	     </similarity>
   -->
</schema>
```

##### solrconfig.xml
Navigate to **<root>:/SolrCloud/solr-7.3.1/server/solr/configsets/interaction_config** and open **solrconfig.xml** in a text editor.

Search for otcaBatchUpdateHandler and do the following for each language you expect to process.

   - Locate the `<str name="otca.language.map">eng:en,spa:es</str>` field and add the two-character and three-character language codes 
   - Locate the `<str name="otca.language.whitelist">en,es</str>` field and add the two-character language code

```xml
I was missing the part related to solrconfig.xml. The section related to schemaxml was in place.

  <requestHandler name="/otcaBatchUpdate" class="com.opentext.solr.handler.OTCABatchProcUpdateRequestHandler">
    <lst name="defaults">
      <str name="update.chain">exploreIndexChain</str>
	  <str name="exploreDoc.fl">diarisation_speaker_1,diarisation_speaker_2</str>
      <str name="otca">true</str>
      <str name="otca.batchSize">25</str>
	  <str name="otca.url">http://localhost:40002/rs/</str>
	  <str name="otca.methods">languagedetector,diarisation,nsentiment,nsummarizer</str>
	  <str name="otca.readingTimeout">360000</str>
	  <str name="otca.connectionTimeout">360000</str>
	  <str name="otca.excludedTypes">excludedType1,excludedType2</str>
	  <str name="otca.language.whitelist">en,es</str>
	  <str name="otca.language.fallback">en</str>
	  <str name="otca.language.map">eng:en,spa:es</str>
	  <str name="otca.language.mode">text</str>
	  <str name="otca.language.outputParagraphs">false</str>
	  <str name="otca.language.overwrite">false</str>
	  <str name="otca.language.excludedTypes">excludedType1,excludedType2</str>
	  <str name="otca.diarisation.KBID">Diarisation</str>
	  <str name="otca.diarisation.resolveSameIdentification">true</str>
	  <str name="otca.diarisation.overwrite">false</str>
	  <str name="otca.diarisation.excludedTypes">excludedType1,excludedType2</str>
	  <str name="otca.sentiment.textLength">6000</str>
	  <str name="otca.sentiment.overwrite">false</str>
	  <str name="otca.sentiment.excludedTypes">excludedType1,excludedType2</str>
	  <str name="otca.summary.KBID">IPTC</str>
	  <str name="otca.summary.numSentences">2</str>
	  <str name="otca.summary.textLength">6000</str>
	  <str name="otca.summary.populateEmpty">true</str>
	  <str name="otca.summary.overwrite">false</str>
	  <str name="otca.summary.excludedTypes">excludedType1,excludedType2</str>
    </lst>
  </requestHandler>

```

#### Customize the icon of your Doc Type

Each document type defined in Explore has his own icon. All the icons are stored on **[EXPLORE_HOME]\ExploreWeb\resources\images\icons** and follows this pattern:

```
explore_custom_icon_[DOC_TYPE_NAME_IN_LOWERCASE]_doc_type.png
```

where:

 * explore_custom_icon_: Prefix
 * [[DOC_TYPE_NAME_IN_LOWERCASE]]: Document type name in lowercase
 * _doc_type.png: Suffix
 
In our example:

```
icn_multichannel_ticket_16.png
```

> A 32x32 pixel image is recommended. 

You must copy the ticket logo, called **icn_multichannel_ticket_16.png**, from the **img** folder in this project and paste on the **[[EXPLORE_HOME]]\ExploreWeb\resources\images\icons** of your Explore instance, e.g.

```
D:\Program Files (x86)\OpenText\Explore\ExploreWeb\resources\images\icons
```

### Applying changes on your instance

Once you have modified **Explore.Configuration.xml** and **schema.xml** files you must follow these steps:

 - Execute this command from a terminal/console as Administrator:

```
d:> cd d:\SolrCloud\solr-7.3.1\bin

d:\SolrCloud\solr-7.3.1\bin> solr.cmd zk -z 127.0.0.1 upconfig -d d:\SolrCloud\solr-7.3.1\server\solr\configsets\interaction_config -n interaction_config 
```

- Open a browser and access to this URL: 

```
http://localhost:8983/solr/admin/collections?action=RELOAD&name=interaction&wt=xml
```

![alt text](img/solr-config-reload.png  "Solr configuration reload")
		
- Reset IIS from a terminal/console as administrator:

```
c:> iisreset
```

Once the configuration has been updated Explore will look like this:

![alt text](img/explore-doc-types-and-extra-fields-in-group.png "Explore Doc types and extra fields in group")

### Create a new Project in Explore

In order to manage content from different client in the same instance in a demo environment you can create projects that keep together the info for each client. 

Follow this steps:

 - Open your Explore instance.
 - Click on **Administer** in the top menu.
 - Click on **Projects**
 - Click on **New** (+ icon)
 - Set the **Project name**: MyProject
 
 ![alt text](img/explore-new-project.png "Explore New Project")
 
 - Click on **Search Criteria**
 - Provide the required fields:
    - Search expression: *
    - Types:  Ticket
    - Include results that have "all" of the followings:
       - Imported tag: is Ticket 
 
 ![alt text](img/explore-project-filter.png "Explore Select criteria on Project")
 
> NOTE: the value used on the "Imported tag" is the same value that you have used in the **itag** parameter.

## Tips & Tricks

### Removing all the imported `Ticket` inputs

During your test you can decide to remove all the Ticket inputs imported. The fastest way to do it is just executing this command from a terminal/console as administrator:

> NOTE: This will delete all the data in your Solr instance!

```
d:> cd d:\SolrCloud\solr-7.3.1\example\exampledocs

d:\SolrCloud\solr-7.3.1\example\exampledocs> java -Dc=interaction -Ddata=args -Dcommit=true -jar post.jar "<delete><query>*:*</query></delete>"
```

> NOTE: The path of your Solr installation can vary in your environment.
