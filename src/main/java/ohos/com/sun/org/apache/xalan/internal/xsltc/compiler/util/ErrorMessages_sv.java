package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.ListResourceBundle;

public class ErrorMessages_sv extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{ErrorMsg.MULTIPLE_STYLESHEET_ERR, "Fler än en formatmall har definierats i samma fil."}, new Object[]{ErrorMsg.TEMPLATE_REDEF_ERR, "Mallen ''{0}'' har redan definierats i denna formatmall."}, new Object[]{ErrorMsg.TEMPLATE_UNDEF_ERR, "Mallen ''{0}'' har inte definierats i denna formatmall."}, new Object[]{ErrorMsg.VARIABLE_REDEF_ERR, "Variabeln ''{0}'' har definierats flera gånger i samma omfattning."}, new Object[]{ErrorMsg.VARIABLE_UNDEF_ERR, "Variabeln eller parametern ''{0}'' har inte definierats."}, new Object[]{ErrorMsg.CLASS_NOT_FOUND_ERR, "Hittar inte klassen ''{0}''."}, new Object[]{ErrorMsg.METHOD_NOT_FOUND_ERR, "Hittar inte den externa metoden ''{0}'' (måste vara allmän)."}, new Object[]{ErrorMsg.ARGUMENT_CONVERSION_ERR, "Kan inte konvertera argument/returtyp vid anrop till metoden ''{0}''"}, new Object[]{ErrorMsg.FILE_NOT_FOUND_ERR, "Fil eller URI ''{0}'' hittades inte."}, new Object[]{ErrorMsg.INVALID_URI_ERR, "Ogiltig URI ''{0}''."}, new Object[]{ErrorMsg.FILE_ACCESS_ERR, "Kan inte öppna filen eller URI ''{0}''."}, new Object[]{ErrorMsg.MISSING_ROOT_ERR, "Förväntade <xsl:stylesheet>- eller <xsl:transform>-element."}, new Object[]{ErrorMsg.NAMESPACE_UNDEF_ERR, "Namnrymdsprefixet ''{0}'' har inte deklarerats."}, new Object[]{ErrorMsg.FUNCTION_RESOLVE_ERR, "Kan inte matcha anrop till funktionen ''{0}''."}, new Object[]{ErrorMsg.NEED_LITERAL_ERR, "Argument till ''{0}'' måste vara en litteral sträng."}, new Object[]{ErrorMsg.XPATH_PARSER_ERR, "Fel vid tolkning av XPath-uttrycket ''{0}''."}, new Object[]{ErrorMsg.REQUIRED_ATTR_ERR, "Det obligatoriska attributet ''{0}'' saknas."}, new Object[]{ErrorMsg.ILLEGAL_CHAR_ERR, "Otillåtet tecken ''{0}'' i XPath-uttrycket."}, new Object[]{ErrorMsg.ILLEGAL_PI_ERR, "''{0}'' är ett otillåtet namn i bearbetningsinstruktion."}, new Object[]{"STRAY_ATTRIBUTE_ERR", "Attributet ''{0}'' finns utanför elementet."}, new Object[]{ErrorMsg.ILLEGAL_ATTRIBUTE_ERR, "''{0}'' är ett otillåtet attribut."}, new Object[]{ErrorMsg.CIRCULAR_INCLUDE_ERR, "Cirkulär import/include. Formatmallen ''{0}'' har redan laddats."}, new Object[]{ErrorMsg.RESULT_TREE_SORT_ERR, "Resultatträdfragment kan inte sorteras (<xsl:sort>-element ignoreras). Du måste sortera noderna när resultatträdet skapas."}, new Object[]{ErrorMsg.SYMBOLS_REDEF_ERR, "Decimalformateringen ''{0}'' har redan definierats."}, new Object[]{ErrorMsg.XSL_VERSION_ERR, "XSL-versionen ''{0}'' understöds inte i XSLTC."}, new Object[]{ErrorMsg.CIRCULAR_VARIABLE_ERR, "Cirkulär variabel-/parameterreferens i ''{0}''."}, new Object[]{ErrorMsg.ILLEGAL_BINARY_OP_ERR, "Okänd operator för binärt uttryck."}, new Object[]{ErrorMsg.ILLEGAL_ARG_ERR, "Otillåtna argument för funktionsanrop."}, new Object[]{ErrorMsg.DOCUMENT_ARG_ERR, "Andra argumentet för document()-funktion måste vara en noduppsättning."}, new Object[]{ErrorMsg.MISSING_WHEN_ERR, "Minst ett <xsl:when>-element krävs i <xsl:choose>."}, new Object[]{ErrorMsg.MULTIPLE_OTHERWISE_ERR, "Endast ett <xsl:otherwise>-element är tillåtet i <xsl:choose>."}, new Object[]{ErrorMsg.STRAY_OTHERWISE_ERR, "<xsl:otherwise> används endast inom <xsl:choose>."}, new Object[]{ErrorMsg.STRAY_WHEN_ERR, "<xsl:when> används endast inom <xsl:choose>."}, new Object[]{ErrorMsg.WHEN_ELEMENT_ERR, "Endast <xsl:when>- och <xsl:otherwise>-element är tillåtna i <xsl:choose>."}, new Object[]{ErrorMsg.UNNAMED_ATTRIBSET_ERR, "<xsl:attribute-set> saknar 'name'-attribut."}, new Object[]{ErrorMsg.ILLEGAL_CHILD_ERR, "Otillåtet underordnat element."}, new Object[]{ErrorMsg.ILLEGAL_ELEM_NAME_ERR, "Du kan inte anropa elementet ''{0}''"}, new Object[]{ErrorMsg.ILLEGAL_ATTR_NAME_ERR, "Du kan inte anropa attributet ''{0}''"}, new Object[]{ErrorMsg.ILLEGAL_TEXT_NODE_ERR, "Textdata utanför toppnivåelementet <xsl:stylesheet>."}, new Object[]{ErrorMsg.SAX_PARSER_CONFIG_ERR, "JAXP-parser har inte konfigurerats korrekt"}, new Object[]{ErrorMsg.INTERNAL_ERR, "Oåterkalleligt internt XSLTC-fel: ''{0}''"}, new Object[]{"UNSUPPORTED_XSL_ERR", "XSL-elementet ''{0}'' stöds inte."}, new Object[]{"UNSUPPORTED_EXT_ERR", "XSLTC-tillägget ''{0}'' är okänt."}, new Object[]{ErrorMsg.MISSING_XSLT_URI_ERR, "Indatadokumentet är ingen formatmall (XSL-namnrymden har inte deklarerats i rotelementet)."}, new Object[]{ErrorMsg.MISSING_XSLT_TARGET_ERR, "Hittade inte formatmallen ''{0}''."}, new Object[]{ErrorMsg.ACCESSING_XSLT_TARGET_ERR, "Kunde inte läsa formatmallen ''{0}'', eftersom ''{1}''-åtkomst inte tillåts på grund av begränsning som anges av egenskapen accessExternalStylesheet."}, new Object[]{ErrorMsg.NOT_IMPLEMENTED_ERR, "Inte implementerad: ''{0}''."}, new Object[]{ErrorMsg.NOT_STYLESHEET_ERR, "Indatadokumentet innehåller ingen XSL-formatmall."}, new Object[]{ErrorMsg.ELEMENT_PARSE_ERR, "Kunde inte tolka elementet ''{0}''"}, new Object[]{ErrorMsg.KEY_USE_ATTR_ERR, "use-attribut för <key> måste vara node, node-set, string eller number."}, new Object[]{ErrorMsg.OUTPUT_VERSION_ERR, "XML-dokumentets utdataversion måste vara 1.0"}, new Object[]{ErrorMsg.ILLEGAL_RELAT_OP_ERR, "Okänd operator för relationsuttryck"}, new Object[]{ErrorMsg.ATTRIBSET_UNDEF_ERR, "Försöker använda en icke-befintlig attributuppsättning ''{0}''."}, new Object[]{ErrorMsg.ATTR_VAL_TEMPLATE_ERR, "Kan inte tolka attributvärdemallen ''{0}''."}, new Object[]{ErrorMsg.UNKNOWN_SIG_TYPE_ERR, "Okänd datatyp i signaturen för klassen ''{0}''."}, new Object[]{"DATA_CONVERSION_ERR", "Kan inte konvertera datatyp ''{0}'' till ''{1}''."}, new Object[]{ErrorMsg.NO_TRANSLET_CLASS_ERR, "Templates innehåller inte någon giltig klassdefinition för translet."}, new Object[]{ErrorMsg.NO_MAIN_TRANSLET_ERR, "Templates innehåller inte någon klass med namnet {0}."}, new Object[]{ErrorMsg.TRANSLET_CLASS_ERR, "Kunde inte ladda translet-klassen ''{0}''."}, new Object[]{ErrorMsg.TRANSLET_OBJECT_ERR, "Translet-klassen har laddats, men kan inte skapa instans av translet."}, new Object[]{ErrorMsg.ERROR_LISTENER_NULL_ERR, "Försöker ställa in ErrorListener för ''{0}'' på null"}, new Object[]{ErrorMsg.JAXP_UNKNOWN_SOURCE_ERR, "Endast StreamSource, SAXSource och DOMSource stöds av XSLTC"}, new Object[]{ErrorMsg.JAXP_NO_SOURCE_ERR, "Source-objektet som överfördes till ''{0}'' saknar innehåll."}, new Object[]{ErrorMsg.JAXP_COMPILE_ERR, "Kunde inte kompilera formatmall"}, new Object[]{ErrorMsg.JAXP_INVALID_ATTR_ERR, "TransformerFactory känner inte igen attributet ''{0}''."}, new Object[]{ErrorMsg.JAXP_INVALID_ATTR_VALUE_ERR, "Fel värde har angetts för attributet ''{0}''."}, new Object[]{ErrorMsg.JAXP_SET_RESULT_ERR, "setResult() måste anropas före startDocument()."}, new Object[]{ErrorMsg.JAXP_NO_TRANSLET_ERR, "Transformer saknar inkapslat objekt för translet."}, new Object[]{ErrorMsg.JAXP_NO_HANDLER_ERR, "Det finns ingen definierad utdatahanterare för transformeringsresultat."}, new Object[]{ErrorMsg.JAXP_NO_RESULT_ERR, "Result-objekt som överfördes till ''{0}'' är ogiltigt."}, new Object[]{ErrorMsg.JAXP_UNKNOWN_PROP_ERR, "Försöker få åtkomst till ogiltig Transformer-egenskap, ''{0}''."}, new Object[]{ErrorMsg.SAX2DOM_ADAPTER_ERR, "Kunde inte skapa SAX2DOM-adapter: ''{0}''."}, new Object[]{ErrorMsg.XSLTC_SOURCE_ERR, "XSLTCSource.build() anropades utan angivet systemId."}, new Object[]{"ER_RESULT_NULL", "Result borde inte vara null"}, new Object[]{ErrorMsg.JAXP_INVALID_SET_PARAM_VALUE, "Parametervärdet för {0} måste vara giltigt Java-objekt"}, new Object[]{ErrorMsg.COMPILE_STDIN_ERR, "Alternativet -i måste användas med alternativet -o."}, new Object[]{ErrorMsg.COMPILE_USAGE_STR, "SYNOPSIS\n   java ohos.com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile [-o <utdata>]\n      [-d <katalog>] [-j <jarfile>] [-p <paket>]\n      [-n] [-x] [-u] [-v] [-h] { <formatmall> | -i }\n\nALTERNATIV\n   -o <utdata>    tilldelar namnet <utdata> till genererad\n                  translet. Som standard tas namnet på translet\n                  från namnet på <formatmallen>. Alternativet\n                  ignoreras vid kompilering av flera formatmallar.\n   -d <katalog> anger en destinationskatalog för translet\n   -j <jarfile>   paketerar transletklasserna i en jar-fil med\n                  namnet <jarfile>\n   -p <paket>   anger ett paketnamnprefix för alla genererade\n                  transletklasser.\n   -n             aktiverar mallinfogning (ger ett bättre genomsnittligt\n                  standardbeteende).\n   -x             ger ytterligare felsökningsmeddelanden\n   -u             tolkar argument i <formatmall> som URL:er\n   -i             tvingar kompilatorn att läsa formatmallen från stdin\n   -v             skriver ut kompilatorns versionsnummer\n   -h             skriver ut denna syntaxsats\n"}, new Object[]{ErrorMsg.TRANSFORM_USAGE_STR, "SYNOPSIS \n   java ohos.com.sun.org.apache.xalan.internal.xsltc.cmdline.Transform [-j <jarfile>]\n      [-x] [-n <iterationer>] {-u <dokument_url> | <dokument>}\n      <klass> [<param1>=<värde1> ...]\n\n   använder translet <klass> vid transformering av XML-dokument \n   angivna som <dokument>. Translet-<klass> finns antingen i\n   användarens CLASSPATH eller i valfritt angiven <jarfile>.\nALTERNATIV\n   -j <jarfile>    anger en jar-fil varifrån translet laddas\n   -x              ger ytterligare felsökningsmeddelanden\n   -n <iterationer> kör <iterations>-tider vid transformering och\n                   visar profileringsinformation\n   -u <dokument_url> anger XML-indatadokument som URL\n"}, new Object[]{ErrorMsg.STRAY_SORT_ERR, "<xsl:sort> kan användas endast i <xsl:for-each> eller <xsl:apply-templates>."}, new Object[]{ErrorMsg.UNSUPPORTED_ENCODING, "Utdatakodning ''{0}'' understöds inte i JVM."}, new Object[]{ErrorMsg.SYNTAX_ERR, "Syntaxfel i ''{0}''."}, new Object[]{ErrorMsg.CONSTRUCTOR_NOT_FOUND, "Hittar inte den externa konstruktorn ''{0}''."}, new Object[]{ErrorMsg.NO_JAVA_FUNCT_THIS_REF, "Det första argumentet för den icke-statiska Java-funktionen ''{0}'' är inte någon giltig objektreferens."}, new Object[]{ErrorMsg.TYPE_CHECK_ERR, "Fel vid kontroll av typ av uttrycket ''{0}''."}, new Object[]{ErrorMsg.TYPE_CHECK_UNK_LOC_ERR, "Fel vid kontroll av typ av ett uttryck på okänd plats."}, new Object[]{ErrorMsg.ILLEGAL_CMDLINE_OPTION_ERR, "Ogiltigt kommandoradsalternativ: ''{0}''."}, new Object[]{ErrorMsg.CMDLINE_OPT_MISSING_ARG_ERR, "Kommandoradsalternativet ''{0}'' saknar obligatoriskt argument."}, new Object[]{ErrorMsg.WARNING_PLUS_WRAPPED_MSG, "WARNING:  ''{0}''\n       :{1}"}, new Object[]{ErrorMsg.WARNING_MSG, "WARNING:  ''{0}''"}, new Object[]{ErrorMsg.FATAL_ERR_PLUS_WRAPPED_MSG, "FATAL ERROR:  ''{0}''\n           :{1}"}, new Object[]{ErrorMsg.FATAL_ERR_MSG, "FATAL ERROR:  ''{0}''"}, new Object[]{ErrorMsg.ERROR_PLUS_WRAPPED_MSG, "ERROR:  ''{0}''\n     :{1}"}, new Object[]{ErrorMsg.ERROR_MSG, "ERROR:  ''{0}''"}, new Object[]{ErrorMsg.TRANSFORM_WITH_TRANSLET_STR, "Transformering via translet ''{0}'' "}, new Object[]{ErrorMsg.TRANSFORM_WITH_JAR_STR, "Transformering via translet ''{0}'' från jar-filen ''{1}''"}, new Object[]{ErrorMsg.COULD_NOT_CREATE_TRANS_FACT, "Kunde inte skapa en instans av TransformerFactory-klassen ''{0}''."}, new Object[]{ErrorMsg.TRANSLET_NAME_JAVA_CONFLICT, "''{0}'' kunde inte användas som namn på transletklassen eftersom det innehåller otillåtna tecken för Java-klassnamn. Namnet ''{1}'' användes istället."}, new Object[]{ErrorMsg.COMPILER_ERROR_KEY, "Kompileringsfel:"}, new Object[]{ErrorMsg.COMPILER_WARNING_KEY, "Kompileringsvarningar:"}, new Object[]{ErrorMsg.RUNTIME_ERROR_KEY, "Transletfel:"}, new Object[]{"INVALID_QNAME_ERR", "Ett attribut vars värde måste vara ett QName eller en blankteckenavgränsad lista med QNames hade värdet ''{0}''"}, new Object[]{"INVALID_NCNAME_ERR", "Ett attribut vars värde måste vara ett NCName hade värdet ''{0}''"}, new Object[]{ErrorMsg.INVALID_METHOD_IN_OUTPUT, "Metodattributet för ett <xsl:output>-element hade värdet ''{0}''. Endast något av följande värden kan användas: ''xml'', ''html'', ''text'' eller qname-but-not-ncname i XML"}, new Object[]{ErrorMsg.JAXP_GET_FEATURE_NULL_NAME, "Funktionsnamnet kan inte vara null i TransformerFactory.getFeature(namn på sträng)."}, new Object[]{ErrorMsg.JAXP_SET_FEATURE_NULL_NAME, "Funktionsnamnet kan inte vara null i TransformerFactory.setFeature(namn på sträng, booleskt värde)."}, new Object[]{ErrorMsg.JAXP_UNSUPPORTED_FEATURE, "Kan inte ställa in funktionen ''{0}'' i denna TransformerFactory."}, new Object[]{ErrorMsg.JAXP_SECUREPROCESSING_FEATURE, "FEATURE_SECURE_PROCESSING: Funktionen kan inte anges till false om säkerhetshanteraren används."}, new Object[]{ErrorMsg.OUTLINE_ERR_TRY_CATCH, "Internt XSLTC-fel: den genererade bytekoden innehåller ett try-catch-finally-block och kan inte göras till en disposition."}, new Object[]{ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS, "Internt XSLTC-fel: markörerna OutlineableChunkStart och OutlineableChunkEnd måste vara balanserade och korrekt kapslade."}, new Object[]{ErrorMsg.OUTLINE_ERR_DELETED_TARGET, "Internt XSLTC-fel: originalmetoden refererar fortfarande till en instruktion som var en del av ett bytekodsblock som gjordes till en disposition."}, new Object[]{ErrorMsg.OUTLINE_ERR_METHOD_TOO_BIG, "Internt XSLTC-fel: en metod i transleten överstiger Java Virtual Machines längdbegränsning för en metod på 64 kilobytes.  Det här orsakas vanligen av mycket stora mallar i en formatmall. Försök att omstrukturera formatmallen att använda mindre mallar."}, new Object[]{ErrorMsg.DESERIALIZE_TRANSLET_ERR, "När Java-säkerheten är aktiverad är stödet för avserialisering av TemplatesImpl avaktiverat. Du kan åsidosätta det här genom att ställa in systemegenskapen jdk.xml.enableTemplatesImplDeserialization till sant."}};
    }
}
