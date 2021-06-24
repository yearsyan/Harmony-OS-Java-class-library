package ohos.com.sun.org.apache.xalan.internal.xsltc.runtime;

import java.util.ListResourceBundle;

public class ErrorMessages_ca extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{BasisLibrary.RUN_TIME_INTERNAL_ERR, "S''ha produït un error intern de temps d''execució a ''{0}''"}, new Object[]{BasisLibrary.RUN_TIME_COPY_ERR, "Es produeix un error de temps d'execució en executar <xsl:copy>."}, new Object[]{"DATA_CONVERSION_ERR", "La conversió de ''{0}'' a ''{1}'' no és vàlida."}, new Object[]{BasisLibrary.EXTERNAL_FUNC_ERR, "XSLTC no dóna suport a la funció externa ''{0}''."}, new Object[]{BasisLibrary.EQUALITY_EXPR_ERR, "L'expressió d'igualtat conté un tipus d'argument desconegut."}, new Object[]{BasisLibrary.INVALID_ARGUMENT_ERR, "La crida a ''{1}'' conté un tipus d''argument ''{0}'' no vàlid."}, new Object[]{BasisLibrary.FORMAT_NUMBER_ERR, "S''ha intentat donar format al número ''{0}'' mitjançant el patró ''{1}''."}, new Object[]{BasisLibrary.ITERATOR_CLONE_ERR, "No es pot clonar l''iterador ''{0}''."}, new Object[]{BasisLibrary.AXIS_SUPPORT_ERR, "L''iterador de l''eix ''{0}'' no té suport."}, new Object[]{BasisLibrary.TYPED_AXIS_SUPPORT_ERR, "L''iterador de l''eix escrit ''{0}'' no té suport."}, new Object[]{"STRAY_ATTRIBUTE_ERR", "L''atribut ''{0}'' es troba fora de l''element."}, new Object[]{BasisLibrary.STRAY_NAMESPACE_ERR, "La declaració d''espai de noms ''{0}''=''{1}'' es troba fora de l''element."}, new Object[]{BasisLibrary.NAMESPACE_PREFIX_ERR, "L''espai de noms del prefix ''{0}'' no s''ha declarat."}, new Object[]{BasisLibrary.DOM_ADAPTER_INIT_ERR, "DOMAdapter s'ha creat mitjançant un tipus incorrecte de DOM d'origen."}, new Object[]{BasisLibrary.PARSER_DTD_SUPPORT_ERR, "L'analitzador SAX que feu servir no gestiona esdeveniments de declaració de DTD."}, new Object[]{BasisLibrary.NAMESPACES_SUPPORT_ERR, "L'analitzador SAX que feu servir no dóna suport a espais de noms XML."}, new Object[]{BasisLibrary.CANT_RESOLVE_RELATIVE_URI_ERR, "No s''ha pogut resoldre la referència d''URI ''{0}''."}};
    }
}
