package ohos.aafwk.ability;

import java.io.File;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.global.icu.impl.locale.LanguageTag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UIContent {
    private static final int INVALID_LAYOUT_RES_ID = -1;
    private static final LogLabel LABEL = LogLabel.create();
    private static final String PREFIX = "    ";
    volatile ComponentContainer curComponentContainer;
    private boolean latestUIAttached = false;
    private int layoutResId = -1;
    ComponentContainer preComponentContainer;
    private boolean uiAttachedAllowed = true;
    private boolean uiAttachedDisable = false;
    private UIContentType uiContentType = UIContentType.TYPE_UNKNOWN;
    private AbilityWindow windowProxy;

    /* access modifiers changed from: private */
    public enum UIContentType {
        TYPE_UNKNOWN,
        TYPE_LAYOUT_RES_ID,
        TYPE_COMPONENTCONTAINER_OBJECT
    }

    UIContent(AbilityWindow abilityWindow) {
        if (abilityWindow != null) {
            this.windowProxy = abilityWindow;
            return;
        }
        throw new IllegalArgumentException("input parameter window is null");
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateUIContent(ComponentContainer componentContainer) {
        if (this.uiContentType != UIContentType.TYPE_COMPONENTCONTAINER_OBJECT || !this.curComponentContainer.equals(componentContainer)) {
            this.uiContentType = UIContentType.TYPE_COMPONENTCONTAINER_OBJECT;
            this.preComponentContainer = this.curComponentContainer;
            this.curComponentContainer = componentContainer;
            attachToWindow();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateUIContent(int i) {
        if (this.uiContentType == UIContentType.TYPE_LAYOUT_RES_ID && this.layoutResId == i) {
            Log.error(LABEL, "same UI set, ignore", new Object[0]);
            return;
        }
        this.uiContentType = UIContentType.TYPE_LAYOUT_RES_ID;
        this.layoutResId = i;
        attachToWindow();
    }

    /* access modifiers changed from: package-private */
    public synchronized Component findComponentById(int i) {
        return this.windowProxy.findComponentById(i);
    }

    /* access modifiers changed from: package-private */
    public void ensureLatestUIAttached() {
        if (!isLatestUIAttached()) {
            applyLatestUI();
        } else if (Log.isDebuggable()) {
            Log.debug(LABEL, "latest UI has been applied already", new Object[0]);
        }
    }

    private void attachToWindow() {
        setLatestUIAttachedFlag(false);
        applyLatestUI();
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isLatestUIAttached() {
        return this.latestUIAttached;
    }

    /* access modifiers changed from: package-private */
    public synchronized void setLatestUIAttachedFlag(boolean z) {
        this.latestUIAttached = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isUiAttachedAllowed() {
        return this.uiAttachedAllowed;
    }

    /* access modifiers changed from: package-private */
    public synchronized void setUiAttachedAllowed(boolean z) {
        this.uiAttachedAllowed = z;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isUiAttachedDisable() {
        return this.uiAttachedDisable;
    }

    /* access modifiers changed from: package-private */
    public synchronized void setUiAttachedDisable(boolean z) {
        this.uiAttachedDisable = z;
    }

    private void applyLatestUI() {
        if (!isUiAttachedAllowed() || isUiAttachedDisable()) {
            Log.warn(LABEL, "attach UI not allowed for now, will be applied when foreground", new Object[0]);
            dumpUIContent();
            return;
        }
        setLatestUIAttachedFlag(attachUI());
        dumpUIContent();
    }

    private synchronized boolean attachUI() {
        if (this.windowProxy == null) {
            Log.error(LABEL, "can not set UI to window, fatal error", new Object[0]);
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$ohos$aafwk$ability$UIContent$UIContentType[this.uiContentType.ordinal()];
        if (i == 1) {
            this.windowProxy.setUIContent(this.curComponentContainer);
            return true;
        } else if (i == 2) {
            this.windowProxy.setUIContent(this.layoutResId);
            this.preComponentContainer = this.curComponentContainer;
            this.curComponentContainer = this.windowProxy.getCurrentAttachedUI();
            if (this.curComponentContainer == null) {
                Log.error(LABEL, "NOTE: null componentContainer got back from window when set layout res", new Object[0]);
            } else if (this.curComponentContainer.equals(this.preComponentContainer)) {
                Log.warn(LABEL, "NOTE: wrong componentContainer maybe got back from window when set layout res", new Object[0]);
            }
            return true;
        } else if (i != 3) {
            Log.error(LABEL, "missing handling for UI content type enum", new Object[0]);
            return false;
        } else {
            Log.error(LABEL, "unknown UI content type", new Object[0]);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.aafwk.ability.UIContent$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$aafwk$ability$UIContent$UIContentType = new int[UIContentType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.aafwk.ability.UIContent$UIContentType[] r0 = ohos.aafwk.ability.UIContent.UIContentType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.aafwk.ability.UIContent.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$UIContent$UIContentType = r0
                int[] r0 = ohos.aafwk.ability.UIContent.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$UIContent$UIContentType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.aafwk.ability.UIContent$UIContentType r1 = ohos.aafwk.ability.UIContent.UIContentType.TYPE_COMPONENTCONTAINER_OBJECT     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.aafwk.ability.UIContent.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$UIContent$UIContentType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.aafwk.ability.UIContent$UIContentType r1 = ohos.aafwk.ability.UIContent.UIContentType.TYPE_LAYOUT_RES_ID     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.aafwk.ability.UIContent.AnonymousClass1.$SwitchMap$ohos$aafwk$ability$UIContent$UIContentType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.aafwk.ability.UIContent$UIContentType r1 = ohos.aafwk.ability.UIContent.UIContentType.TYPE_UNKNOWN     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.UIContent.AnonymousClass1.<clinit>():void");
        }
    }

    public synchronized void reset() {
        this.curComponentContainer = null;
        this.preComponentContainer = null;
        this.layoutResId = -1;
        this.uiContentType = UIContentType.TYPE_UNKNOWN;
        setLatestUIAttachedFlag(false);
        setUiAttachedAllowed(true);
        setUiAttachedDisable(false);
    }

    private void dumpUIContent() {
        if (Log.isDebuggable()) {
            Log.debug(LABEL, "latest UI result: uiContent@%{public}d [%{public}s]", Integer.valueOf(hashCode()), toString());
        }
    }

    public String toString() {
        return " (" + "uiAttachedAllowed: " + this.uiAttachedAllowed + ", uiAttachedDisable: " + this.uiAttachedDisable + ", latestUIAttached: " + this.latestUIAttached + ", uiContentType: " + this.uiContentType + ", preComponentContainer = " + this.preComponentContainer + ", curComponentContainer = " + this.curComponentContainer + ", layoutResId = " + this.layoutResId + ")";
    }

    /* access modifiers changed from: package-private */
    public synchronized void dump(String str, PrintWriter printWriter, String str2) {
        printWriter.println(str + "uiContentType: " + this.uiContentType);
        if (this.uiContentType == UIContentType.TYPE_LAYOUT_RES_ID) {
            printWriter.println(str + "layoutResId: " + this.layoutResId);
        } else if (this.uiContentType != UIContentType.TYPE_COMPONENTCONTAINER_OBJECT) {
            printWriter.println(str + "unknown uiContentType");
        } else if (!str2.isEmpty()) {
            if (!dumpToXml(this.curComponentContainer, str2)) {
                printWriter.println("dump layout info to \"" + str2 + "\" failed.");
            }
        } else {
            printWriter.println(str + "<ComponentContainer>");
            dumpComponentContainer(str + "    ", printWriter, this.curComponentContainer);
        }
    }

    private void dumpComponentContainer(String str, PrintWriter printWriter, ComponentContainer componentContainer) {
        int childCount = componentContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Component componentAt = componentContainer.getComponentAt(i);
            if (componentAt instanceof ComponentContainer) {
                String str2 = i == childCount - 1 ? str + "    " : str + "|" + "    ";
                printWriter.println(str + "|-- <Components>");
                dumpComponentContainer(str2, printWriter, (ComponentContainer) componentAt);
            } else {
                Class<?> cls = componentAt.getClass();
                printWriter.print(str + "|-- [" + cls.getSimpleName());
                Class<? super Object> superclass = cls.getSuperclass();
                while (superclass != Component.class && superclass != Object.class) {
                    printWriter.print(LanguageTag.SEP + superclass.getSimpleName());
                    superclass = superclass.getSuperclass();
                }
                printWriter.print("] Position: [" + componentAt.getLeft() + ", " + componentAt.getTop());
                printWriter.println("] [Width, Height]: [" + componentAt.getWidth() + ", " + componentAt.getHeight() + "]");
            }
        }
    }

    private void buildElement(Document document, Element element, ComponentContainer componentContainer) {
        int childCount = componentContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Component componentAt = componentContainer.getComponentAt(i);
            if (componentAt instanceof ComponentContainer) {
                Element createElement = document.createElement("Components");
                buildElement(document, createElement, (ComponentContainer) componentAt);
                element.appendChild(createElement);
            } else {
                Class<?> cls = componentAt.getClass();
                StringBuilder sb = new StringBuilder(cls.getSimpleName());
                Class<? super Object> superclass = cls.getSuperclass();
                while (superclass != Component.class && superclass != Object.class) {
                    sb.append(LanguageTag.SEP);
                    sb.append(superclass.getSimpleName());
                    superclass = superclass.getSuperclass();
                }
                Element createElement2 = document.createElement("Component");
                createElement2.setAttribute("Type", sb.toString());
                createElement2.setAttribute("PositionX", String.valueOf(componentAt.getLeft()));
                createElement2.setAttribute("PositionY", String.valueOf(componentAt.getTop()));
                createElement2.setAttribute("Width", String.valueOf(componentAt.getWidth()));
                createElement2.setAttribute("Height", String.valueOf(componentAt.getHeight()));
                element.appendChild(createElement2);
            }
        }
    }

    private boolean dumpToXml(ComponentContainer componentContainer, String str) {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        try {
            newInstance.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (ParserConfigurationException unused) {
            Log.error(LABEL, "error set feature, ignore", new Object[0]);
        }
        try {
            Document newDocument = newInstance.newDocumentBuilder().newDocument();
            Element createElement = newDocument.createElement("ComponentContainer");
            buildElement(newDocument, createElement, componentContainer);
            newDocument.appendChild(createElement);
            TransformerFactory newInstance2 = TransformerFactory.newInstance();
            try {
                newInstance2.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                Transformer newTransformer = newInstance2.newTransformer();
                newTransformer.setOutputProperty("indent", "yes");
                newTransformer.transform(new DOMSource(newDocument), new StreamResult(new File(str)));
                return true;
            } catch (TransformerException e) {
                Log.error(LABEL, "dump layout info to xml failed: %{public}s", e);
                return false;
            }
        } catch (ParserConfigurationException e2) {
            Log.error(LABEL, "dump layout info to xml failed: %{public}s", e2);
            return false;
        }
    }
}
