package com.jaan.irpc.common;

import java.io.Serializable;

public class RpcProtocol implements Serializable {

    private static final long serialVersionUID = 3328522580018480502L;

    private short magicNumber = Constant.MAGIC_NUMBER;
    private int contentLength;
    //这个字段其实是RpcInvocation类的字节数组，在RpcInvocation中包含了更多的调用信息，详情见下方介绍
    private byte[] content;


    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    public RpcProtocol(){

    }

    public RpcProtocol(byte[] content) {
        this.content = content;
        contentLength = content.length;
    }
}
