/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012 VMware, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.soap.mail.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.google.common.base.Objects;
import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.type.GrantGranteeType;

@XmlAccessorType(XmlAccessType.NONE)
public class ShareGrantee {

    /**
     * @zm-api-field-tag rights
     * @zm-api-field-description Rights - Some combination of (r)ead, (w)rite, (i)nsert, (d)elete, (a)dminister,
     * workflow action (x), view (p)rivate, view (f)reebusy, (c)reate subfolder
     */
    @XmlAttribute(name = MailConstants.A_RIGHTS /* perm */, required = true)
    private String perm;

    /**
     * @zm-api-field-tag grantee-type
     * @zm-api-field-description The type of Grantee:
     * <pre>
     *     "usr",
     *     "grp",
     *     "dom" (domain),
     *     "cos",
     *     "all" (all authenticated users), "pub" (public authenticated and unauthenticated access),
     *     "guest" (non-Zimbra email address and password),
     *     "key" (non-Zimbra email address and access key)
     * </pre>
     */
    @XmlAttribute(name = MailConstants.A_GRANT_TYPE /* gt */, required = true)
    private GrantGranteeType type;

    /**
     * @zm-api-field-tag grantee-display-name
     * @zm-api-field-description Name of the principal that has been granted rights.  Optional if
     * {grantee-type} is "all"/"guest"/"pub".
     */
    @XmlAttribute(name = MailConstants.A_NAME /* name */, required = false)
    private String name;

    /**
     * @zm-api-field-tag grantee-email
     * @zm-api-field-description Email address of the principal that has been granted rights.  Optional if
     * {grantee-type} is not "usr"/"grp".
     */
    @XmlAttribute(name = MailConstants.A_EMAIL /* email */, required = false)
    private String email;

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public void setGranteeType(GrantGranteeType granteeType) {
        this.type = granteeType;
    }

    public void setGranteeName(String granteeName) {
        this.name = granteeName;
    }

    public void setGranteeEmail(String granteeEmail) {
        this.email = granteeEmail;
    }

    public String getPerm() {
        return perm;
    }

    public GrantGranteeType getGranteeType() {
        return type;
    }

    public String getGranteeName() {
        return name;
    }

    public String getGranteeEmail() {
        return email;
    }

    public Objects.ToStringHelper addToStringInfo(Objects.ToStringHelper helper) {
        return helper
            .add("perm", perm)
            .add("granteeType", type)
            .add("granteeName", name)
            .add("granteeEmail", email);
    }

    @Override
    public String toString() {
        return addToStringInfo(Objects.toStringHelper(this)).toString();
    }

}
