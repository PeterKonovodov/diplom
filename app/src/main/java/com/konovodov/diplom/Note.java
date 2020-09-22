package com.konovodov.diplom;

import java.time.*;

public class Note {

    private String headerText;
    private String bodyText;
    private boolean hasDeadLine;
    private LocalDateTime deadLineDate;
    private LocalDateTime modifyDate;

//    public Note(String headerText, String bodyText, boolean hasDeadLine, Date deadLineDate, Date modifyDate) {
    public Note(String headerText, String bodyText, boolean hasDeadLine) {
        this.headerText = headerText;
        this.bodyText = bodyText;
        this.hasDeadLine = hasDeadLine;
//        this.deadLineDate = deadLineDate;
//        this.modifyDate = modifyDate;
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

    public LocalDateTime getDeadLineDate() {
        return deadLineDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
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

    public void setDeadLineDate(LocalDateTime deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

}
