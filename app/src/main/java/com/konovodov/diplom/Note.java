package com.konovodov.diplom;

public class Note {

    private String headerText;
    private String bodyText;
    private boolean hasDeadLine;
    private long EpochDeadLineDate;
    private long EpochModifyDate;

    public Note(String headerText, String bodyText, boolean hasDeadLine, long EpochDeadLineDate, long EpochModifyDate) {
        this.headerText = headerText;
        this.bodyText = bodyText;
        this.hasDeadLine = hasDeadLine;
        this.EpochDeadLineDate = EpochDeadLineDate;
        this.EpochModifyDate = EpochModifyDate;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public boolean isHasDeadLine() {
        return hasDeadLine;
    }

    public long getEpochDeadLineDate() {
        return EpochDeadLineDate;
    }

    public long getEpochModifyDate() {
        return EpochModifyDate;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public void setHasDeadLine(boolean hasDeadLine) {
        this.hasDeadLine = hasDeadLine;
    }

    public void setEpochDeadLineDate(long epochDeadLineDate) {
        this.EpochDeadLineDate = epochDeadLineDate;
    }

    public void setEpochModifyDate(long epochModifyDate) {
        this.EpochModifyDate = epochModifyDate;
    }

}
