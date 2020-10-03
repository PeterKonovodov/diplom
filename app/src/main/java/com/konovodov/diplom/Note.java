package com.konovodov.diplom;

public class Note {

    private String headerText;
    private String bodyText;
    private boolean hasDeadLine;
    private long epochDeadLineDate;
    private long epochModifyDate;
    private boolean isCompleted;
    private long id;

    public Note(long id, String headerText, String bodyText, boolean hasDeadLine,
                long EpochDeadLineDate, long EpochModifyDate, boolean isCompleted) {
        this.id = id;
        this.headerText = headerText;
        this.bodyText = bodyText;
        this.hasDeadLine = hasDeadLine;
        this.epochDeadLineDate = EpochDeadLineDate;
        this.epochModifyDate = EpochModifyDate;
        this.isCompleted = isCompleted;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public boolean hasDeadLine() {
        return hasDeadLine;
    }

    public long getEpochDeadLineDate() {
        return epochDeadLineDate;
    }

    public long getEpochModifyDate() {
        return epochModifyDate;
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
        this.epochDeadLineDate = epochDeadLineDate;
    }

    public void setEpochModifyDate(long epochModifyDate) {
        this.epochModifyDate = epochModifyDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
