package com.zimbra.soap.mail.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.google.common.base.Objects;
import com.zimbra.common.soap.MailConstants;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = MailConstants.E_SAVE_PROFILE_IMAGE_REQUEST)
public class SaveProfileImageRequest {
    
    /**
     * @zm-api-field-tag uid
     * @zm-api-field-description Upload ID of uploaded image to use
     */
    @XmlAttribute(name=MailConstants.A_UID /* uid */, required=false)
    private String uploadId;

    /**
     * @zm-api-field-tag base64-encoded-image-data
     * @zm-api-field-description Base64 encoded image data
     */
    @XmlValue
    private String imageB64Data;

    public String getImageB64Data() {
        return imageB64Data;
    }

    public void setImageB64Data(String imageB64Data) {
        this.imageB64Data = imageB64Data;
    }

    public String getUploadId() {
        return uploadId;
    }
    
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    
    public Objects.ToStringHelper addToStringInfo(Objects.ToStringHelper helper) {
        return helper.add("uploadId", uploadId)
            .add("binaryB64Data", imageB64Data);
    }

    @Override
    public String toString() {
        return addToStringInfo(Objects.toStringHelper(this)).toString();
    }
}
