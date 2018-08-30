package com.passport.rpc.dto;

import java.io.Serializable;

/**
 * 数据传输dto
 */
public class MailInfoDto implements Serializable{
    /**标题*/
    private String title;
    /**发送者*/
    private String sender;
    /**接收者*/
    private String recipient;
    /**附件*/
    private byte[] attchMentFile;
    /**附件文件名*/
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**base64内容*/
    private String content;


    public String getTitle() {
        return title;
    }

    public byte[] getAttchMentFile() {
        return attchMentFile;
    }

    public void setAttchMentFile(byte[] attchMentFile) {
        this.attchMentFile = attchMentFile;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
