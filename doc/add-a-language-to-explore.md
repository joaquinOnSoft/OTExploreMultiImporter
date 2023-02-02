# Adding configuration to manage text in Spanish (OPTIONAL)
To add an additional language to Explore you must follow these steps:

## schema.xml
Add the following sections just before the `<field name="language">` and `</schema>` tags:

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

## solrconfig.xml
Navigate to **<root>:/SolrCloud/solr-7.3.1/server/solr/configsets/interaction_config** and open **solrconfig.xml** in a text editor.

Search for otcaBatchUpdateHandler and do the following for each language you expect to process.

   - Locate the `<str name="otca.language.map">eng:en,spa:es</str>` field and add the two-character and three-character language codes 
   - Locate the `<str name="otca.language.whitelist">en,es</str>` field and add the two-character language code

	
I was missing the part related to solrconfig.xml. The section related to schemaxml was in place.
	
```xml

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

