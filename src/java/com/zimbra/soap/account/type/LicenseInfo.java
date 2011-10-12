/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 Zimbra, Inc.
 *
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.soap.account.type;

import com.google.common.base.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.zimbra.common.soap.AccountConstants;
import com.zimbra.soap.type.LicenseStatus;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = {})
public class LicenseInfo {

    @XmlAttribute(name=AccountConstants.A_STATUS /* status */, required=true)
    private LicenseStatus status;

    @XmlElement(name=AccountConstants.E_ATTR /* attr */, required=true)
    private LicenseAttr attr;

    public LicenseInfo() {
    }

    public void setStatus(LicenseStatus status) { this.status = status; }
    public void setAttr(LicenseAttr attr) { this.attr = attr; }
    public LicenseStatus getStatus() { return status; }
    public LicenseAttr getAttr() { return attr; }

    public Objects.ToStringHelper addToStringInfo(
                Objects.ToStringHelper helper) {
        return helper
            .add("status", status)
            .add("attr", attr);
    }

    @Override
    public String toString() {
        return addToStringInfo(Objects.toStringHelper(this))
                .toString();
    }
}
