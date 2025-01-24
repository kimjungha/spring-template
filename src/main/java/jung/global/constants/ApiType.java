package jung.global.constants;

public enum ApiType {
    /**
     * SELECT/FIND API
     */
    VIEW,
    /**
     * INSERT API
     */
    CREATE,
    /**
     * UPDATE API
     */
    UPDATE,
    /**
     * DELETE (Logical 포함)
     */
    DELETE,
    /**
     * INSERT API
     */
    SAVE,
    /**
     * Excel or File download API
     */
    DOWNLOAD,
    /**
     * LOGIN API
     */
    LOGIN,
    /**
     * 불명확한 TYPE
     */
    UNKOWN
}
