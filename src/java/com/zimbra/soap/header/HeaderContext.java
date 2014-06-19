/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2013 Zimbra Software, LLC.
 *
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.soap.header;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.zimbra.soap.type.AuthTokenControl;

@XmlRootElement(namespace="urn:zimbra", name="context")
@XmlType(namespace="urn:zimbra", name="HeaderContext", propOrder = {})
public class HeaderContext {
    @XmlElement(name="authToken", required=false) private String authToken;
    @XmlElement(name="sessionId", required=false) private String sessionId;
    @XmlElement(name="account", required=false) private String account;
    @XmlElement(name="change", required=false) private String change;
    @XmlElement(name="targetServer", required=false) private String targetServer;
    @XmlElement(name="userAgent", required=false) private String userAgent;
    @XmlElement(name="authTokenControl", required=false) private AuthTokenControl authTokenControl;

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getChange() {
        return change;
    }
    public void setChange(String change) {
        this.change = change;
    }
    public String getTargetServer() {
        return targetServer;
    }
    public void setTargetServer(String targetServer) {
        this.targetServer = targetServer;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public void setAuthTokenControlVoidOnExpired(boolean voidExpired) {
        authTokenControl = new AuthTokenControl(voidExpired);
    }
    public AuthTokenControl getAuthTokenControl() {
        return authTokenControl;
    }
}
