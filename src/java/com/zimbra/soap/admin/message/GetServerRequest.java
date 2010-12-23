/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010 Zimbra, Inc.
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

package com.zimbra.soap.admin.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.zimbra.common.soap.AdminConstants;
import com.zimbra.soap.admin.type.ServerSelector;
import com.zimbra.soap.admin.type.AttributeSelectorImpl;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AdminConstants.E_GET_SERVER_REQUEST)
@XmlType(propOrder = {})
public class GetServerRequest extends AttributeSelectorImpl {

    @XmlAttribute(name=AdminConstants.A_APPLY_CONFIG, required=false)
    private boolean applyConfig = true;
    @XmlElement(name=AdminConstants.E_SERVER)
    private ServerSelector server;

    public GetServerRequest() {
    }

    public void setApplyConfig(boolean applyConfig) {
        this.applyConfig = applyConfig;
    }

    public boolean isApplyConfig() {
        return applyConfig;
    }

    public void setServer(ServerSelector server) {
        this.server = server;
    }

    public ServerSelector getServer() {
        return server;
    }

}
