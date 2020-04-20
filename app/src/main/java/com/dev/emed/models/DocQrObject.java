package com.dev.emed.models;

public class DocQrObject {
    public String docUserId, consultId;

    public DocQrObject() {
    }

    public String getDocUserId() {
        return docUserId;
    }

    public void setDocUserId(String docUserId) {
        this.docUserId = docUserId;
    }

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }
}
