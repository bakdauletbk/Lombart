package kz.pillikan.lombart.common.utils;

public enum EpayLanguage {
    RUSSIAN ("rus"),
    KAZAKH ("kaz"),
    ENGLISH ("eng");

    private String lang;

    private EpayLanguage(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return lang;
    }
}
