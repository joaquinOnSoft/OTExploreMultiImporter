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
