package ohos.aafwk.ability;

public class FormException extends Exception {
    private static final int CODE_FORM_OFFSET = 8585216;
    private static final int ERR_ADD_INVALID_PARAM = 8585223;
    private static final int ERR_BIND_PROVIDER_FAILED = 8585226;
    private static final int ERR_CFG_NOT_MATCH_ID = 8585224;
    private static final int ERR_CODE_COMMON = 8585217;
    private static final int ERR_FORM_DUPLICATE_ADDED = 8585247;
    private static final int ERR_FORM_FA_NOT_INSTALLED = 8585236;
    private static final int ERR_FORM_NO_SUCH_ABILITY = 8585234;
    private static final int ERR_FORM_NO_SUCH_DIMENSION = 8585235;
    private static final int ERR_FORM_NO_SUCH_MODULE = 8585233;
    private static final int ERR_GET_BMS_RPC = 8585249;
    private static final int ERR_GET_BUNDLE_FAILED = 8585221;
    private static final int ERR_GET_FMS_RPC = 8585246;
    private static final int ERR_GET_INFO_FAILED = 8585220;
    private static final int ERR_GET_LAYOUT_FAILED = 8585222;
    private static final int ERR_IN_RECOVER = 8585252;
    private static final int ERR_MAX_FORMS_PER_CLIENT = 8585231;
    private static final int ERR_MAX_INSTANCES_PER_FORM = 8585228;
    private static final int ERR_MAX_SYSTEM_FORMS = 8585227;
    private static final int ERR_MAX_SYSTEM_TEMP_FORMS = 8585232;
    private static final int ERR_NOT_EXIST_ID = 8585225;
    private static final int ERR_OPERATION_FORM_NOT_SELF = 8585229;
    private static final int ERR_PERMISSION_DENY = 8585218;
    private static final int ERR_PROVIDER_DEL_FAIL = 8585230;
    private static final int ERR_SEND_BMS_MSG = 8585250;
    private static final int ERR_SEND_FMS_MSG = 8585248;
    private static final long serialVersionUID = -402480972442410815L;
    private String briefMsg;
    private FormError formError;

    public enum FormError {
        INPUT_PARAM_INVALID(FormException.ERR_ADD_INVALID_PARAM, "invalid params received on operating form"),
        FMS_RPC_ERROR(FormException.ERR_GET_FMS_RPC, "get fms rpc failed"),
        BMS_RPC_ERROR(FormException.ERR_GET_BMS_RPC, "get bms rpc failed"),
        SEND_FMS_MSG_ERROR(FormException.ERR_SEND_FMS_MSG, "send request to fms failed"),
        SEND_BMS_MSG_ERROR(FormException.ERR_SEND_BMS_MSG, "send request to bms failed"),
        PERMISSION_DENY(FormException.ERR_PERMISSION_DENY, "check permission deny, need to request ohos.permission.REQUIRE_FORM"),
        FORM_INFO_NOT_FOUND(FormException.ERR_GET_INFO_FAILED, "can't get form info by the formName"),
        GET_BUNDLE_FAILED(FormException.ERR_GET_BUNDLE_FAILED, "the requested bundle name does not exist"),
        INIT_LAYOUT_FAILED(FormException.ERR_GET_LAYOUT_FAILED, "can't get layout with requested dimension and orientation"),
        BIND_PROVIDER_FAILED(FormException.ERR_BIND_PROVIDER_FAILED, "fms bind provider failed"),
        FORM_DUPLICATE_ADDED(FormException.ERR_FORM_DUPLICATE_ADDED, "form do not support acquire same id twice"),
        FORM_CFG_NOT_MATCH_ID(FormException.ERR_CFG_NOT_MATCH_ID, "the form id and form config are not matched"),
        FORM_ID_NOT_EXIST(FormException.ERR_NOT_EXIST_ID, "the requested form id is not existed on fms"),
        EXCEED_MAX_SYSTEM_FORMS(FormException.ERR_MAX_SYSTEM_FORMS, "exceed max forms in system, current limit is 512"),
        EXCEED_MAX_INSTANCES_PER_FORM(FormException.ERR_MAX_INSTANCES_PER_FORM, "exceed max instances per form, limit is 256"),
        FORM_NOT_SELF_OWNED(FormException.ERR_OPERATION_FORM_NOT_SELF, "the form to be operated is not self-owned or has been deleted already"),
        PROVIDER_DELETE_FAIL(FormException.ERR_PROVIDER_DEL_FAIL, "fms notify provider to delete failed"),
        EXCEED_MAX_FORMS_PER_CLIENT(FormException.ERR_MAX_FORMS_PER_CLIENT, "exceed max forms per client, limit is 256"),
        EXCEED_MAX_SYSTEM_TEMP_FORMS(FormException.ERR_MAX_SYSTEM_TEMP_FORMS, "exceed max temp forms in system, limit is 32"),
        MODULE_NOT_EXIST(FormException.ERR_FORM_NO_SUCH_MODULE, "the module not exist in the bundle."),
        ABILITY_NOT_EXIST(FormException.ERR_FORM_NO_SUCH_ABILITY, "the ability not exist in the module."),
        DIMENSION_NOT_EXIST(FormException.ERR_FORM_NO_SUCH_DIMENSION, "the dimension not exist in the form."),
        FA_NOT_INSTALLED(FormException.ERR_FORM_FA_NOT_INSTALLED, "the ability not installed,need install first."),
        FORM_IN_RECOVER(FormException.ERR_IN_RECOVER, "form is in recover status, can't do action on form"),
        INTERNAL_ERROR(FormException.ERR_CODE_COMMON, "some internal server occurs");
        
        private int errCode;
        private String errMsg;

        private FormError(int i, String str) {
            this.errCode = i;
            this.errMsg = str;
        }

        /* access modifiers changed from: package-private */
        public int getErrCode() {
            return this.errCode;
        }

        /* access modifiers changed from: package-private */
        public String getErrMsg() {
            return this.errMsg;
        }

        static FormError fromErrCode(int i) {
            FormError[] values = values();
            for (FormError formError : values) {
                if (formError.getErrCode() == i) {
                    return formError;
                }
            }
            return null;
        }
    }

    FormException(FormError formError2) {
        this(formError2, formError2.getErrMsg());
    }

    FormException(FormError formError2, String str) {
        super(str);
        this.briefMsg = "";
        this.briefMsg = formError2.toString();
        this.formError = formError2;
    }

    public String getMessage() {
        return this.briefMsg + ": " + super.getMessage();
    }

    public FormError getErrorCode() {
        return this.formError;
    }
}
