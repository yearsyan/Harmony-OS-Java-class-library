package ohos.data.orm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ohos.data.orm.AllChangeToTarget;
import ohos.data.orm.ObjectId;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmObject;

/* access modifiers changed from: package-private */
public class AllChangeToThread {
    private OrmContext changeContext;
    private Map<String, AllChangeToTarget> entityMap = new HashMap();
    private Map<ObjectId, AllChangeToTarget> manageObjectIDMap = new HashMap();
    private Map<OrmContext, AllChangeToTarget> ormContextMap = new HashMap();
    private Map<String, AllChangeToTarget> persistStoreMap = new HashMap();

    public AllChangeToThread(List<OrmObject> list, List<OrmObject> list2, List<OrmObject> list3, OrmContext ormContext) {
        this.changeContext = ormContext;
        analysisChange(list, list2, list3);
    }

    public AllChangeToThread(OrmContext ormContext) {
        this.changeContext = ormContext;
    }

    private void analysisChange(List<OrmObject> list, List<OrmObject> list2, List<OrmObject> list3) {
        List<OrmObject> list4 = (List) list.stream().filter($$Lambda$AllChangeToThread$927D1qgp27dEaofS5nvlhPMwgGk.INSTANCE).collect(Collectors.toList());
        if (list4.size() != 0) {
            analysisInsertList(list4);
        }
        List<OrmObject> list5 = (List) list2.stream().filter($$Lambda$AllChangeToThread$mxgxelpO_txvAT98RQk0q3IKnc.INSTANCE).collect(Collectors.toList());
        if (list5.size() != 0) {
            analysisUpdateList(list5);
        }
        List<OrmObject> list6 = (List) list3.stream().filter($$Lambda$AllChangeToThread$Rb86_nC_ucFxpWaB1TD_f6wZLc.INSTANCE).collect(Collectors.toList());
        if (list6.size() != 0) {
            analysisDeleteList(list6);
        }
    }

    static /* synthetic */ boolean lambda$analysisChange$0(OrmObject ormObject) {
        return (ormObject == null || ormObject.getObjectId() == null) ? false : true;
    }

    static /* synthetic */ boolean lambda$analysisChange$1(OrmObject ormObject) {
        return (ormObject == null || ormObject.getObjectId() == null) ? false : true;
    }

    static /* synthetic */ boolean lambda$analysisChange$2(OrmObject ormObject) {
        return (ormObject == null || ormObject.getObjectId() == null) ? false : true;
    }

    private void analysisDeleteList(List<OrmObject> list) {
        for (OrmObject ormObject : list) {
            String storeAlias = ormObject.getStoreAlias();
            AllChangeToTarget allChangeToTarget = this.persistStoreMap.get(storeAlias);
            if (allChangeToTarget != null) {
                allChangeToTarget.addToDeletedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget2 = new AllChangeToTarget();
                allChangeToTarget2.addToDeletedList(ormObject);
                this.persistStoreMap.put(storeAlias, allChangeToTarget2);
            }
            AllChangeToTarget allChangeToTarget3 = this.ormContextMap.get(this.changeContext);
            if (allChangeToTarget3 != null) {
                allChangeToTarget3.addToDeletedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget4 = new AllChangeToTarget();
                allChangeToTarget4.addToDeletedList(ormObject);
                this.ormContextMap.put(this.changeContext, allChangeToTarget4);
            }
            String entityName = ormObject.getEntityName();
            AllChangeToTarget allChangeToTarget5 = this.entityMap.get(entityName);
            if (allChangeToTarget5 != null) {
                allChangeToTarget5.addToDeletedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget6 = new AllChangeToTarget();
                allChangeToTarget6.addToDeletedList(ormObject);
                this.entityMap.put(entityName, allChangeToTarget6);
            }
            ObjectId objectId = ormObject.getObjectId();
            AllChangeToTarget allChangeToTarget7 = this.manageObjectIDMap.get(objectId);
            if (allChangeToTarget7 != null) {
                allChangeToTarget7.addToDeletedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget8 = new AllChangeToTarget();
                allChangeToTarget8.addToDeletedList(ormObject);
                this.manageObjectIDMap.put(objectId, allChangeToTarget8);
            }
        }
    }

    private void analysisUpdateList(List<OrmObject> list) {
        for (OrmObject ormObject : list) {
            String storeAlias = ormObject.getStoreAlias();
            AllChangeToTarget allChangeToTarget = this.persistStoreMap.get(storeAlias);
            if (allChangeToTarget != null) {
                allChangeToTarget.addToUpdatedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget2 = new AllChangeToTarget();
                allChangeToTarget2.addToUpdatedList(ormObject);
                this.persistStoreMap.put(storeAlias, allChangeToTarget2);
            }
            AllChangeToTarget allChangeToTarget3 = this.ormContextMap.get(this.changeContext);
            if (allChangeToTarget3 != null) {
                allChangeToTarget3.addToUpdatedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget4 = new AllChangeToTarget();
                allChangeToTarget4.addToUpdatedList(ormObject);
                this.ormContextMap.put(this.changeContext, allChangeToTarget4);
            }
            String entityName = ormObject.getEntityName();
            AllChangeToTarget allChangeToTarget5 = this.entityMap.get(entityName);
            if (allChangeToTarget5 != null) {
                allChangeToTarget5.addToUpdatedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget6 = new AllChangeToTarget();
                allChangeToTarget6.addToUpdatedList(ormObject);
                this.entityMap.put(entityName, allChangeToTarget6);
            }
            ObjectId objectId = ormObject.getObjectId();
            AllChangeToTarget allChangeToTarget7 = this.manageObjectIDMap.get(objectId);
            if (allChangeToTarget7 != null) {
                allChangeToTarget7.addToUpdatedList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget8 = new AllChangeToTarget();
                allChangeToTarget8.addToUpdatedList(ormObject);
                this.manageObjectIDMap.put(objectId, allChangeToTarget8);
            }
        }
    }

    private void analysisInsertList(List<OrmObject> list) {
        for (OrmObject ormObject : list) {
            String storeAlias = ormObject.getStoreAlias();
            AllChangeToTarget allChangeToTarget = this.persistStoreMap.get(storeAlias);
            if (allChangeToTarget != null) {
                allChangeToTarget.addToInsertList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget2 = new AllChangeToTarget();
                allChangeToTarget2.addToInsertList(ormObject);
                this.persistStoreMap.put(storeAlias, allChangeToTarget2);
            }
            AllChangeToTarget allChangeToTarget3 = this.ormContextMap.get(this.changeContext);
            if (allChangeToTarget3 != null) {
                allChangeToTarget3.addToInsertList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget4 = new AllChangeToTarget();
                allChangeToTarget4.addToInsertList(ormObject);
                this.ormContextMap.put(this.changeContext, allChangeToTarget4);
            }
            String entityName = ormObject.getEntityName();
            AllChangeToTarget allChangeToTarget5 = this.entityMap.get(entityName);
            if (allChangeToTarget5 != null) {
                allChangeToTarget5.addToInsertList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget6 = new AllChangeToTarget();
                allChangeToTarget6.addToInsertList(ormObject);
                this.entityMap.put(entityName, allChangeToTarget6);
            }
            ObjectId objectId = ormObject.getObjectId();
            AllChangeToTarget allChangeToTarget7 = this.manageObjectIDMap.get(objectId);
            if (allChangeToTarget7 != null) {
                allChangeToTarget7.addToInsertList(ormObject);
            } else {
                AllChangeToTarget allChangeToTarget8 = new AllChangeToTarget();
                allChangeToTarget8.addToInsertList(ormObject);
                this.manageObjectIDMap.put(objectId, allChangeToTarget8);
            }
        }
    }

    public Map<String, AllChangeToTarget> getPsMap() {
        return this.persistStoreMap;
    }

    public Map<OrmContext, AllChangeToTarget> getOrmContextMap() {
        return this.ormContextMap;
    }

    public Map<String, AllChangeToTarget> getEntityMap() {
        return this.entityMap;
    }

    public Map<ObjectId, AllChangeToTarget> getManageObjMap() {
        return this.manageObjectIDMap;
    }

    public OrmContext getChangeContext() {
        return this.changeContext;
    }
}
