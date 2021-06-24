package ohos.com.sun.org.apache.xalan.internal.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_fr extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{BasisLibrary.RUN_TIME_INTERNAL_ERR, "Erreur interne d''exécution dans ''{0}''"}, new Object[]{BasisLibrary.RUN_TIME_COPY_ERR, "Erreur d'exécution de <xsl:copy>."}, new Object[]{"DATA_CONVERSION_ERR", "Conversion de ''{0}'' à ''{1}'' non valide."}, new Object[]{BasisLibrary.EXTERNAL_FUNC_ERR, "Fonction externe ''{0}'' non prise en charge par XSLTC."}, new Object[]{BasisLibrary.EQUALITY_EXPR_ERR, "Type d'argument inconnu dans l'expression d'égalité."}, new Object[]{BasisLibrary.INVALID_ARGUMENT_ERR, "Type d''argument ''{0}'' non valide dans l''appel de ''{1}''"}, new Object[]{BasisLibrary.FORMAT_NUMBER_ERR, "Tentative de formatage du nombre ''{0}'' à l''aide du modèle ''{1}''."}, new Object[]{BasisLibrary.ITERATOR_CLONE_ERR, "Impossible de cloner l''itérateur ''{0}''."}, new Object[]{BasisLibrary.AXIS_SUPPORT_ERR, "Itérateur de l''axe ''{0}'' non pris en charge."}, new Object[]{BasisLibrary.TYPED_AXIS_SUPPORT_ERR, "Itérateur de l''axe saisi ''{0}'' non pris en charge."}, new Object[]{"STRAY_ATTRIBUTE_ERR", "Attribut ''{0}'' en dehors de l''élément."}, new Object[]{BasisLibrary.STRAY_NAMESPACE_ERR, "La déclaration d''espace de noms ''{0}''=''{1}'' est à l''extérieur de l''élément."}, new Object[]{BasisLibrary.NAMESPACE_PREFIX_ERR, "L''espace de noms du préfixe ''{0}'' n''a pas été déclaré."}, new Object[]{BasisLibrary.DOM_ADAPTER_INIT_ERR, "DOMAdapter créé avec le mauvais type de DOM source."}, new Object[]{BasisLibrary.PARSER_DTD_SUPPORT_ERR, "L'analyseur SAX que vous utilisez ne gère pas les événements de déclaration DTD."}, new Object[]{BasisLibrary.NAMESPACES_SUPPORT_ERR, "L'analyseur SAX que vous utilisez ne prend pas en charge les espaces de noms XML."}, new Object[]{BasisLibrary.CANT_RESOLVE_RELATIVE_URI_ERR, "Impossible de résoudre la référence d''URI ''{0}''."}, new Object[]{"UNSUPPORTED_XSL_ERR", "Elément XSL ''{0}'' non pris en charge"}, new Object[]{"UNSUPPORTED_EXT_ERR", "Extension XSLTC ''{0}'' non reconnue"}, new Object[]{BasisLibrary.UNKNOWN_TRANSLET_VERSION_ERR, "Le translet spécifié, ''{0}'', a été créé à l''aide d''une version de XSLTC plus récente que la version de l''exécution XSLTC utilisée. Vous devez recompiler la feuille de style ou utiliser une version plus récente de XSLTC pour exécuter ce translet."}, new Object[]{"INVALID_QNAME_ERR", "Un attribut dont la valeur doit être un QName avait la valeur ''{0}''"}, new Object[]{"INVALID_NCNAME_ERR", "Un attribut dont la valeur doit être un NCName avait la valeur ''{0}''"}, new Object[]{BasisLibrary.UNALLOWED_EXTENSION_FUNCTION_ERR, "L''utilisation de la fonction d''extension ''{0}'' n''est pas autorisée lorsque la fonctionnalité de traitement sécurisé est définie sur True."}, new Object[]{BasisLibrary.UNALLOWED_EXTENSION_ELEMENT_ERR, "L''utilisation de l''élément d''extension ''{0}'' n''est pas autorisée lorsque la fonctionnalité de traitement sécurisé est définie sur True."}};
    }
}