package ohos.data.distributed.common;

import java.util.ArrayList;
import java.util.List;
import ohos.utils.fastjson.JSONArray;
import ohos.utils.fastjson.JSONObject;

public class Schema {
    private static final String DEFAULT_SCHEMA_VERSION = "1.0";
    private static final String LABEL = "Schema";
    private static final int MIN_COMPOSITE_INDEX_NUM = 2;
    private static final String SCHEMA_DEFINE = "SCHEMA_DEFINE";
    private static final String SCHEMA_INDEXES = "SCHEMA_INDEXES";
    private static final String SCHEMA_MODE = "SCHEMA_MODE";
    private static final String SCHEMA_SKIPSIZE = "SCHEMA_SKIPSIZE";
    private static final String SCHEMA_VERSION = "SCHEMA_VERSION";
    private List<List<String>> compositeIndexes = new ArrayList();
    private List<String> indexes = new ArrayList();
    private final FieldNode rootFieldNode = new FieldNode(SCHEMA_DEFINE);
    private SchemaMode schemaMode;

    public String getVersion() {
        return "1.0";
    }

    public void setSchemaMode(SchemaMode schemaMode2) {
        this.schemaMode = schemaMode2;
    }

    public boolean setIndexes(List<String> list) {
        if (list == null) {
            LogPrint.error(LABEL, "setIndexes input param is null", new Object[0]);
            return false;
        }
        this.indexes = list;
        return true;
    }

    public SchemaMode getSchemaMode() {
        return this.schemaMode;
    }

    public List<String> getIndexes() {
        return this.indexes;
    }

    public List<List<String>> getCompositeIndexes() {
        return this.compositeIndexes;
    }

    public boolean setCompositeIndexes(List<List<String>> list) {
        if (list == null || list.isEmpty()) {
            LogPrint.error(LABEL, "setCompositeIndex input param is null", new Object[0]);
            return false;
        }
        for (List<String> list2 : list) {
            if (list2.size() < 2) {
                LogPrint.error(LABEL, "index num error", new Object[0]);
                return false;
            }
        }
        this.compositeIndexes = list;
        return true;
    }

    public FieldNode getRootFieldNode() {
        return this.rootFieldNode;
    }

    private JSONArray getSchemaJSONArray() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.indexes);
        arrayList.addAll(this.compositeIndexes);
        return new JSONArray(arrayList);
    }

    public String toJsonString() {
        if (this.schemaMode == null) {
            LogPrint.error(LABEL, "toJsonString schemaMode has not been set", new Object[0]);
            return "";
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(SCHEMA_VERSION, (Object) getVersion());
        jSONObject.put(SCHEMA_MODE, (Object) this.schemaMode.getCode());
        jSONObject.put(this.rootFieldNode.getFieldName(), this.rootFieldNode.getValueForJson());
        jSONObject.put(SCHEMA_INDEXES, (Object) getSchemaJSONArray());
        jSONObject.put(SCHEMA_SKIPSIZE, (Object) 1);
        return jSONObject.toJSONString();
    }
}
