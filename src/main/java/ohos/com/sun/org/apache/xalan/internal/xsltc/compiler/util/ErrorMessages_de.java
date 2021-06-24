package ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.ListResourceBundle;

public class ErrorMessages_de extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{ErrorMsg.MULTIPLE_STYLESHEET_ERR, "Mehrere Stylesheets in derselben Datei definiert."}, new Object[]{ErrorMsg.TEMPLATE_REDEF_ERR, "Vorlage \"{0}\" bereits in diesem Stylesheet definiert."}, new Object[]{ErrorMsg.TEMPLATE_UNDEF_ERR, "Vorlage \"{0}\" nicht in diesem Stylesheet definiert."}, new Object[]{ErrorMsg.VARIABLE_REDEF_ERR, "Variable \"{0}\" ist mehrmals in demselben Gültigkeitsbereich definiert."}, new Object[]{ErrorMsg.VARIABLE_UNDEF_ERR, "Variable oder Parameter \"{0}\" ist nicht definiert."}, new Object[]{ErrorMsg.CLASS_NOT_FOUND_ERR, "Klasse \"{0}\" kann nicht gefunden werden."}, new Object[]{ErrorMsg.METHOD_NOT_FOUND_ERR, "Externe Methode \"{0}\" kann nicht gefunden werden (muss \"public\" sein)."}, new Object[]{ErrorMsg.ARGUMENT_CONVERSION_ERR, "Konvertierung von Argument-/Rückgabetyp in Aufruf von Methode \"{0}\" nicht möglich"}, new Object[]{ErrorMsg.FILE_NOT_FOUND_ERR, "Datei oder URI \"{0}\" nicht gefunden."}, new Object[]{ErrorMsg.INVALID_URI_ERR, "Ungültiger URI \"{0}\"."}, new Object[]{ErrorMsg.FILE_ACCESS_ERR, "Datei oder URI \"{0}\" kann nicht geöffnet werden."}, new Object[]{ErrorMsg.MISSING_ROOT_ERR, "<xsl:stylesheet>- oder <xsl:transform>-Element erwartet."}, new Object[]{ErrorMsg.NAMESPACE_UNDEF_ERR, "Namespace-Präfix \"{0}\" ist nicht deklariert."}, new Object[]{ErrorMsg.FUNCTION_RESOLVE_ERR, "Aufruf kann nicht in Funktion \"{0}\" aufgelöst werden."}, new Object[]{ErrorMsg.NEED_LITERAL_ERR, "Argument für \"{0}\" muss eine literale Zeichenfolge sein."}, new Object[]{ErrorMsg.XPATH_PARSER_ERR, "Fehler beim Parsen von XPath-Ausdruck \"{0}\"."}, new Object[]{ErrorMsg.REQUIRED_ATTR_ERR, "Erforderliches Attribut \"{0}\" fehlt."}, new Object[]{ErrorMsg.ILLEGAL_CHAR_ERR, "Ungültiges Zeichen \"{0}\" in XPath-Ausdruck."}, new Object[]{ErrorMsg.ILLEGAL_PI_ERR, "Ungültiger Name \"{0}\" für Verarbeitungsanweisung."}, new Object[]{"STRAY_ATTRIBUTE_ERR", "Attribut \"{0}\" außerhalb des Elements."}, new Object[]{ErrorMsg.ILLEGAL_ATTRIBUTE_ERR, "Unzulässiges Attribut \"{0}\"."}, new Object[]{ErrorMsg.CIRCULAR_INCLUDE_ERR, "Zyklisches import/include. Stylesheet \"{0}\" bereits geladen."}, new Object[]{ErrorMsg.RESULT_TREE_SORT_ERR, "Ergebnisbaumfragmente können nicht sortiert werden (<xsl:sort>-Elemente werden ignoriert). Sie müssen die Knoten sortieren, wenn Sie den Ergebnisbaum erstellen."}, new Object[]{ErrorMsg.SYMBOLS_REDEF_ERR, "Dezimalformatierung \"{0}\" ist bereits definiert."}, new Object[]{ErrorMsg.XSL_VERSION_ERR, "XSL-Version \"{0}\" wird nicht von XSLTC unterstützt."}, new Object[]{ErrorMsg.CIRCULAR_VARIABLE_ERR, "Zyklische Variablen-/Parameterreferenz in \"{0}\"."}, new Object[]{ErrorMsg.ILLEGAL_BINARY_OP_ERR, "Unbekannter Operator für Binärausdruck."}, new Object[]{ErrorMsg.ILLEGAL_ARG_ERR, "Unzulässige Argumente für Funktionsaufruf."}, new Object[]{ErrorMsg.DOCUMENT_ARG_ERR, "Zweites Argument für document()-Funktion muss ein NodeSet sein."}, new Object[]{ErrorMsg.MISSING_WHEN_ERR, "Mindestens ein <xsl:when>-Element in <xsl:choose> erforderlich."}, new Object[]{ErrorMsg.MULTIPLE_OTHERWISE_ERR, "Nur ein <xsl:otherwise>-Element in <xsl:choose> zulässig."}, new Object[]{ErrorMsg.STRAY_OTHERWISE_ERR, "<xsl:otherwise> kann nur in <xsl:choose> verwendet werden."}, new Object[]{ErrorMsg.STRAY_WHEN_ERR, "<xsl:when> kann nur in <xsl:choose> verwendet werden."}, new Object[]{ErrorMsg.WHEN_ELEMENT_ERR, "Nur <xsl:when>- und <xsl:otherwise>-Elemente in <xsl:choose> zulässig."}, new Object[]{ErrorMsg.UNNAMED_ATTRIBSET_ERR, "Bei <xsl:attribute-set> fehlt das \"name\"-Attribut."}, new Object[]{ErrorMsg.ILLEGAL_CHILD_ERR, "Ungültiges untergeordnetes Element."}, new Object[]{ErrorMsg.ILLEGAL_ELEM_NAME_ERR, "Elemente dürfen nicht den Namen \"{0}\" haben"}, new Object[]{ErrorMsg.ILLEGAL_ATTR_NAME_ERR, "Attribute dürfen nicht den Namen \"{0}\" haben"}, new Object[]{ErrorMsg.ILLEGAL_TEXT_NODE_ERR, "Textdaten außerhalb des <xsl:stylesheet>-Elements der obersten Ebene."}, new Object[]{ErrorMsg.SAX_PARSER_CONFIG_ERR, "JAXP-Parser nicht korrekt konfiguriert"}, new Object[]{ErrorMsg.INTERNAL_ERR, "Nicht behebbarer interner XSLTC-Fehler: \"{0}\""}, new Object[]{"UNSUPPORTED_XSL_ERR", "Nicht unterstütztes XSL-Element \"{0}\"."}, new Object[]{"UNSUPPORTED_EXT_ERR", "Unbekannte XSLTC-Erweiterung \"{0}\"."}, new Object[]{ErrorMsg.MISSING_XSLT_URI_ERR, "Das Eingabedokument ist kein Stylesheet (der XSL-Namespace ist nicht im Root-Element deklariert)."}, new Object[]{ErrorMsg.MISSING_XSLT_TARGET_ERR, "Stylesheet-Ziel \"{0}\" konnte nicht gefunden werden."}, new Object[]{ErrorMsg.ACCESSING_XSLT_TARGET_ERR, "Stylesheet-Ziel \"{0}\" konnte nicht gelesen werden, weil der \"{1}\"-Zugriff wegen einer von der Eigenschaft accessExternalStylesheet festgelegten Einschränkung nicht zulässig ist."}, new Object[]{ErrorMsg.NOT_IMPLEMENTED_ERR, "Nicht implementiert: \"{0}\"."}, new Object[]{ErrorMsg.NOT_STYLESHEET_ERR, "Das Eingabedokument enthält kein XSL-Stylesheet."}, new Object[]{ErrorMsg.ELEMENT_PARSE_ERR, "Element \"{0}\" konnte nicht geparst werden"}, new Object[]{ErrorMsg.KEY_USE_ATTR_ERR, "Das \"use\"-Attribut von <key> muss \"node\", \"node-set\", \"string\" oder \"number\" sein."}, new Object[]{ErrorMsg.OUTPUT_VERSION_ERR, "Ausgabe-XML-Dokumentversion muss 1.0 sein"}, new Object[]{ErrorMsg.ILLEGAL_RELAT_OP_ERR, "Unbekannter Operator für Vergleichsausdruck"}, new Object[]{ErrorMsg.ATTRIBSET_UNDEF_ERR, "Versuch, nicht vorhandene Attributgruppe \"{0}\" zu verwenden."}, new Object[]{ErrorMsg.ATTR_VAL_TEMPLATE_ERR, "Attributwertvorlage \"{0}\" kann nicht geparst werden."}, new Object[]{ErrorMsg.UNKNOWN_SIG_TYPE_ERR, "Unbekannter Datentyp in Signatur für Klasse \"{0}\"."}, new Object[]{"DATA_CONVERSION_ERR", "Datentyp \"{0}\" kann nicht in \"{1}\" konvertiert werden."}, new Object[]{ErrorMsg.NO_TRANSLET_CLASS_ERR, "Dieses \"Templates\" enthält keine gültige Translet-Klassendefinition."}, new Object[]{ErrorMsg.NO_MAIN_TRANSLET_ERR, "Dieses \"Templates\" enthält keine Klasse mit dem Namen \"{0}\"."}, new Object[]{ErrorMsg.TRANSLET_CLASS_ERR, "Translet-Klasse \"{0}\" konnte nicht geladen werden."}, new Object[]{ErrorMsg.TRANSLET_OBJECT_ERR, "Translet-Klasse geladen, Translet-Instanz kann aber nicht erstellt werden."}, new Object[]{ErrorMsg.ERROR_LISTENER_NULL_ERR, "Versuch, ErrorListener für \"{0}\" auf null zu setzen"}, new Object[]{ErrorMsg.JAXP_UNKNOWN_SOURCE_ERR, "Nur StreamSource, SAXSource und DOMSource werden von XSLTC unterstützt"}, new Object[]{ErrorMsg.JAXP_NO_SOURCE_ERR, "An \"{0}\" übergebenes Source-Objekt hat keinen Inhalt."}, new Object[]{ErrorMsg.JAXP_COMPILE_ERR, "Stylesheet konnte nicht kompiliert werden"}, new Object[]{ErrorMsg.JAXP_INVALID_ATTR_ERR, "TransformerFactory erkennt Attribut \"{0}\" nicht."}, new Object[]{ErrorMsg.JAXP_INVALID_ATTR_VALUE_ERR, "Falscher Wert für Attribut \"{0}\" angegeben."}, new Object[]{ErrorMsg.JAXP_SET_RESULT_ERR, "setResult() muss vor startDocument() aufgerufen werden."}, new Object[]{ErrorMsg.JAXP_NO_TRANSLET_ERR, "Der Transformer hat kein gekapseltes Translet-Objekt."}, new Object[]{ErrorMsg.JAXP_NO_HANDLER_ERR, "Kein definierter Ausgabe-Handler für Transformationsergebnis."}, new Object[]{ErrorMsg.JAXP_NO_RESULT_ERR, "An \"{0}\" übergebenes Result-Objekt ist ungültig."}, new Object[]{ErrorMsg.JAXP_UNKNOWN_PROP_ERR, "Versuch, auf ungültige Transformer-Eigenschaft \"{0}\" zuzugreifen."}, new Object[]{ErrorMsg.SAX2DOM_ADAPTER_ERR, "SAX2DOM-Adapter \"{0}\" konnte nicht erstellt werden."}, new Object[]{ErrorMsg.XSLTC_SOURCE_ERR, "XSLTCSource.build() ohne festgelegte systemID aufgerufen."}, new Object[]{"ER_RESULT_NULL", "Ergebnis darf nicht null sein"}, new Object[]{ErrorMsg.JAXP_INVALID_SET_PARAM_VALUE, "Wert von Parameter {0} muss ein gültiges Java-Objekt sein"}, new Object[]{ErrorMsg.COMPILE_STDIN_ERR, "Die Option \"-i\" muss mit der Option \"-o\" verwendet werden."}, new Object[]{ErrorMsg.COMPILE_USAGE_STR, "SYNOPSIS\n   java ohos.com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile [-o <Ausgabe>]\n      [-d <Verzeichnis>] [-j <JAR-Datei>] [-p <Package>]\n      [-n] [-x] [-u] [-v] [-h] { <Stylesheet> | -i }\n\nOPTIONS\n   -o <Ausgabe>    weist den Namen <Ausgabe> dem generierten\n                  Translet zu. Standardmäßig wird der Translet-Name\n                  vom <Stylesheet>-Namen abgeleitet. Diese Option\n                  wird ignoriert, wenn mehrere Stylesheets kompiliert werden.\n   -d <Verzeichnis> gibt ein Zielverzeichnis für das Translet an\n   -j <JAR-Datei>   verpackt Translet-Klassen in einer JAR-Datei mit dem\n                  als <jarfile> angegebenen Namen\n   -p <package>   gibt ein Packagenamenspräfix für alle generierten\n                  Translet-Klassen an.\n   -n             aktiviert das Vorlagen-Inlining (Standardverhalten durchschnittlich\n                  besser).\n   -x             schaltet die zusätzliche Debugging-Meldungsausgabe ein\n   -u             interpretiert <Stylesheet>-Argumente als URLs\n   -i             erzwingt, dass der Compiler das Stylesheet aus stdin liest\n   -v             druckt die Version des Compilers\n   -h             druckt diese Verwendungsanweisung\n"}, new Object[]{ErrorMsg.TRANSFORM_USAGE_STR, "SYNOPSIS \n   java ohos.com.sun.org.apache.xalan.internal.xsltc.cmdline.Transform [-j <JAR-Datei>]\n      [-x] [-n <Iterationen>] {-u <document_url> | <Dokument>}\n      <Klasse> [<param1>=<value1> ...]\n\n   verwendet die Translet-<Klasse> zur Transformation eines XML-Dokuments, \n   das als <Dokument> angegeben wird. Die Translet-<Klasse> befindet sich entweder im\n   CLASSPATH des Benutzers oder in der optional angegebenen <JAR-Datei>.\nOPTIONS\n   -j <JAR-Datei>    gibt eine JAR-Datei an, aus der das Translet geladen werden soll\n   -x              schaltet die zusätzliche Debugging-Meldungsausgabe ein\n   -n <Iterationen> führt die Transformation so oft aus, wie in <Iterationen> angegeben und\n                   zeigt Profilinformationen an\n   -u <document_url> gibt das XML-Eingabedokument als URL an\n"}, new Object[]{ErrorMsg.STRAY_SORT_ERR, "<xsl:sort> kann nur in <xsl:for-each> oder <xsl:apply-templates> verwendet werden."}, new Object[]{ErrorMsg.UNSUPPORTED_ENCODING, "Ausgabecodierung \"{0}\" wird auf dieser JVM nicht unterstützt."}, new Object[]{ErrorMsg.SYNTAX_ERR, "Syntaxfehler in \"{0}\"."}, new Object[]{ErrorMsg.CONSTRUCTOR_NOT_FOUND, "Externer Constructor \"{0}\" kann nicht gefunden werden."}, new Object[]{ErrorMsg.NO_JAVA_FUNCT_THIS_REF, "Das erste Argument für die nicht-\"static\"-Java-Funktion \"{0}\" ist keine gültige Objektreferenz."}, new Object[]{ErrorMsg.TYPE_CHECK_ERR, "Fehler beim Prüfen des Typs des Ausdrucks \"{0}\"."}, new Object[]{ErrorMsg.TYPE_CHECK_UNK_LOC_ERR, "Fehler beim Prüfen des Typs eines Ausdrucks an einer unbekannten Stelle."}, new Object[]{ErrorMsg.ILLEGAL_CMDLINE_OPTION_ERR, "Die Befehlszeilenoption \"{0}\" ist nicht gültig."}, new Object[]{ErrorMsg.CMDLINE_OPT_MISSING_ARG_ERR, "Bei der Befehlszeilenoption \"{0}\" fehlt ein erforderliches Argument."}, new Object[]{ErrorMsg.WARNING_PLUS_WRAPPED_MSG, "WARNING:  ''{0}''\n       :{1}"}, new Object[]{ErrorMsg.WARNING_MSG, "WARNING:  ''{0}''"}, new Object[]{ErrorMsg.FATAL_ERR_PLUS_WRAPPED_MSG, "FATAL ERROR:  ''{0}''\n           :{1}"}, new Object[]{ErrorMsg.FATAL_ERR_MSG, "FATAL ERROR:  ''{0}''"}, new Object[]{ErrorMsg.ERROR_PLUS_WRAPPED_MSG, "ERROR:  ''{0}''\n     :{1}"}, new Object[]{ErrorMsg.ERROR_MSG, "ERROR:  ''{0}''"}, new Object[]{ErrorMsg.TRANSFORM_WITH_TRANSLET_STR, "Transformation mit Translet \"{0}\" "}, new Object[]{ErrorMsg.TRANSFORM_WITH_JAR_STR, "Transformation mit Translet \"{0}\" aus JAR-Datei \"{1}\""}, new Object[]{ErrorMsg.COULD_NOT_CREATE_TRANS_FACT, "Es konnte keine Instanz der TransformerFactory-Klasse \"{0}\" erstellt werden."}, new Object[]{ErrorMsg.TRANSLET_NAME_JAVA_CONFLICT, "Der Name \"{0}\" konnte nicht als Name der Translet-Klasse verwendet werden, da er Zeichen enthält, die nicht im Namen einer Java-Klasse zulässig sind. Der Name \"{1}\" wurde stattdessen verwendet."}, new Object[]{ErrorMsg.COMPILER_ERROR_KEY, "Compiler-Fehler:"}, new Object[]{ErrorMsg.COMPILER_WARNING_KEY, "Compiler-Warnungen:"}, new Object[]{ErrorMsg.RUNTIME_ERROR_KEY, "Translet-Fehler:"}, new Object[]{"INVALID_QNAME_ERR", "Ein Attribut, dessen Wert ein QName oder eine durch Leerstellen getrennte Liste mit QNames sein muss, hatte den Wert \"{0}\""}, new Object[]{"INVALID_NCNAME_ERR", "Ein Attribut, dessen Wert ein NCName sein muss, hatte den Wert \"{0}\""}, new Object[]{ErrorMsg.INVALID_METHOD_IN_OUTPUT, "Das \"method\"-Attribut eines <xsl:output>-Elements hatte den Wert \"{0}\". Der Wert muss \"xml\", \"html\", \"text\" oder qname-but-not-ncname sein"}, new Object[]{ErrorMsg.JAXP_GET_FEATURE_NULL_NAME, "Der Featurename darf nicht null in TransformerFactory.getFeature(Zeichenfolgenname) sein."}, new Object[]{ErrorMsg.JAXP_SET_FEATURE_NULL_NAME, "Der Featurename darf nicht null in TransformerFactory.setFeature(Zeichenfolgenname, boolescher Wert) sein."}, new Object[]{ErrorMsg.JAXP_UNSUPPORTED_FEATURE, "Das Feature \"{0}\" kann nicht für diese TransformerFactory festgelegt werden."}, new Object[]{ErrorMsg.JAXP_SECUREPROCESSING_FEATURE, "FEATURE_SECURE_PROCESSING: Feature kann nicht auf \"false\" gesetzt werden, wenn Security Manager vorhanden ist."}, new Object[]{ErrorMsg.OUTLINE_ERR_TRY_CATCH, "Interner XSLTC-Fehler: Der generierte Bytecode enthält einen Try-Catch-Finally-Block. Outline nicht möglich."}, new Object[]{ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS, "Interner XSLTC-Fehler: Die Marker OutlineableChunkStart und OutlineableChunkEnd müssen ausgeglichen und ordnungsgemäß platziert sein."}, new Object[]{ErrorMsg.OUTLINE_ERR_DELETED_TARGET, "Interner XSLTC-Fehler: Eine Anweisung, die Teil eines Bytecodeblocks war, für den ein Outline erstellt wurde, wird nach wie vor in der Originalmethode referenziert."}, new Object[]{ErrorMsg.OUTLINE_ERR_METHOD_TOO_BIG, "Interner XSLTC-Fehler: Eine Methode im Translet überschreitet die Java Virtual Machine-Längeneinschränkung einer Methode von 64 KB. Ursache hierfür sind in der Regel sehr große Vorlagen in einem Stylesheet. Versuchen Sie, das Stylesheet mit kleineren Vorlagen umzustrukturieren."}, new Object[]{ErrorMsg.DESERIALIZE_TRANSLET_ERR, "Wenn die Java-Sicherheit aktiviert ist, ist die Unterstützung für das Deserialisieren von TemplatesImpl deaktiviert. Dies kann durch Setzen der Systemeigenschaft jdk.xml.enableTemplatesImplDeserialization auf \"True\" außer Kraft gesetzt werden."}};
    }
}