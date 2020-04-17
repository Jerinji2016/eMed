package com.dev.emed.qrCode.models;

public class DocQrObject {
    String docUserId, consultId;

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
