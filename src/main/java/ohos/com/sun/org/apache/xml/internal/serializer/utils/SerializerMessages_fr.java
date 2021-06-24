package ohos.com.sun.org.apache.xml.internal.serializer.utils;

import java.util.ListResourceBundle;

public class SerializerMessages_fr extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][]{new Object[]{MsgKey.BAD_MSGKEY, "La clé de message ''{0}'' ne figure pas dans la classe de messages ''{1}''"}, new Object[]{MsgKey.BAD_MSGFORMAT, "Echec du format de message ''{0}'' dans la classe de messages ''{1}''."}, new Object[]{"ER_SERIALIZER_NOT_CONTENTHANDLER", "La classe de serializer ''{0}'' n''implémente pas ohos.org.xml.sax.ContentHandler."}, new Object[]{"ER_RESOURCE_COULD_NOT_FIND", "La ressource [ {0} ] est introuvable.\n {1}"}, new Object[]{"ER_RESOURCE_COULD_NOT_LOAD", "La ressource [ {0} ] n''a pas pu charger : {1} \n {2} \t {3}"}, new Object[]{"ER_BUFFER_SIZE_LESSTHAN_ZERO", "Taille du tampon <=0"}, new Object[]{"ER_INVALID_UTF16_SURROGATE", "Substitut UTF-16 non valide détecté : {0} ?"}, new Object[]{"ER_OIERROR", "Erreur d'E/S"}, new Object[]{"ER_ILLEGAL_ATTRIBUTE_POSITION", "Impossible d''ajouter l''attribut {0} après des noeuds enfant ou avant la production d''un élément. L''attribut est ignoré."}, new Object[]{"ER_NAMESPACE_PREFIX", "L''espace de noms du préfixe ''{0}'' n''a pas été déclaré."}, new Object[]{MsgKey.ER_STRAY_ATTRIBUTE, "Attribut ''{0}'' en dehors de l''élément."}, new Object[]{"ER_STRAY_NAMESPACE", "La déclaration d''espace de noms ''{0}''=''{1}'' est à l''extérieur de l''élément."}, new Object[]{"ER_COULD_NOT_LOAD_RESOURCE", "Impossible de charger ''{0}'' (vérifier CLASSPATH), les valeurs par défaut sont donc employées"}, new Object[]{"ER_ILLEGAL_CHARACTER", "Tentative de sortie d''un caractère avec une valeur entière {0}, non représenté dans l''encodage de sortie spécifié pour {1}."}, new Object[]{"ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Impossible de charger le fichier de propriétés ''{0}'' pour la méthode de sortie ''{1}'' (vérifier CLASSPATH)"}, new Object[]{"ER_INVALID_PORT", "Numéro de port non valide"}, new Object[]{"ER_PORT_WHEN_HOST_NULL", "Impossible de définir le port quand l'hôte est NULL"}, new Object[]{"ER_HOST_ADDRESS_NOT_WELLFORMED", "Le format de l'adresse de l'hôte n'est pas correct"}, new Object[]{"ER_SCHEME_NOT_CONFORMANT", "Le modèle n'est pas conforme."}, new Object[]{"ER_SCHEME_FROM_NULL_STRING", "Impossible de définir le modèle à partir de la chaîne NULL"}, new Object[]{"ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Le chemin d'accès contient une séquence d'échappement non valide"}, new Object[]{"ER_PATH_INVALID_CHAR", "Le chemin contient un caractère non valide : {0}"}, new Object[]{"ER_FRAG_INVALID_CHAR", "Le fragment contient un caractère non valide"}, new Object[]{"ER_FRAG_WHEN_PATH_NULL", "Impossible de définir le fragment quand le chemin d'accès est NULL"}, new Object[]{"ER_FRAG_FOR_GENERIC_URI", "Le fragment ne peut être défini que pour un URI générique"}, new Object[]{"ER_NO_SCHEME_IN_URI", "Modèle introuvable dans l'URI"}, new Object[]{"ER_CANNOT_INIT_URI_EMPTY_PARMS", "Impossible d'initialiser l'URI avec des paramètres vides"}, new Object[]{"ER_NO_FRAGMENT_STRING_IN_PATH", "Le fragment ne doit pas être indiqué à la fois dans le chemin et dans le fragment"}, new Object[]{"ER_NO_QUERY_STRING_IN_PATH", "La chaîne de requête ne doit pas figurer dans un chemin et une chaîne de requête"}, new Object[]{"ER_NO_PORT_IF_NO_HOST", "Le port peut ne pas être spécifié si l'hôte ne l'est pas"}, new Object[]{"ER_NO_USERINFO_IF_NO_HOST", "Userinfo peut ne pas être spécifié si l'hôte ne l'est pas"}, new Object[]{MsgKey.ER_XML_VERSION_NOT_SUPPORTED, "Avertissement : la version du document de sortie doit être ''{0}''. Cette version XML n''est pas prise en charge. La version du document de sortie sera ''1.0''."}, new Object[]{"ER_SCHEME_REQUIRED", "Modèle obligatoire."}, new Object[]{MsgKey.ER_FACTORY_PROPERTY_MISSING, "L''objet de propriétés transmis à SerializerFactory ne comporte aucune propriété ''{0}''."}, new Object[]{"ER_ENCODING_NOT_SUPPORTED", "Avertissement : l''encodage ''{0}'' n''est pas pris en charge par l''exécution Java."}};
    }
}