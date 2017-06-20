package com.zimbra.qa.unittest;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;

import com.zimbra.common.localconfig.ConfigException;
import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;

/**
 * Test the IMAP server provided by the IMAP daemon, doing the necessary configuration to make it work.
 *
 * Note: Currently bypasses Proxy, the tests connect directly to the IMAP daemon's port
 *
 * The actual tests that are run are in {@link SharedImapTests}
 */
public class TestImapViaImapDaemon extends SharedImapTests {

    @Before
    public void setUp() throws ServiceException, IOException, DocumentException, ConfigException  {
        saveImapConfigSettings();
        TestUtil.setLCValue(LC.imap_always_use_remote_store, String.valueOf(false));
        getLocalServer();
        imapServer.setReverseProxyUpstreamImapServers(new String[] {imapServer.getServiceHostname()});
        super.sharedSetUp();
        TestUtil.assumeTrue("remoteImapServerEnabled false for this server", imapServer.isRemoteImapServerEnabled());
    }

    @After
    public void tearDown() throws ServiceException, DocumentException, ConfigException, IOException  {
        super.sharedTearDown();
        restoreImapConfigSettings();
    }

    @Override
    protected int getImapPort() {
        return imapServer.getRemoteImapBindPort();
    }
}