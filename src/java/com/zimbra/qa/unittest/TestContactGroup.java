package com.zimbra.qa.unittest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.zimbra.common.account.Key.AccountBy;
import com.zimbra.common.mailbox.ContactConstants;
import com.zimbra.common.util.CliUtil;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.ldap.entry.LdapAccount;
import com.zimbra.cs.mailbox.Contact;
import com.zimbra.cs.mailbox.ContactGroup;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.mailbox.MailboxManager;
import com.zimbra.cs.mailbox.OperationContext;
import com.zimbra.cs.mailbox.ContactGroup.ContactRefMember;
import com.zimbra.cs.mailbox.ContactGroup.GalRefMember;
import com.zimbra.cs.mailbox.ContactGroup.InlineMember;
import com.zimbra.cs.mailbox.ContactGroup.Member;
import com.zimbra.cs.mailbox.ContactGroup.MemberId;
import com.zimbra.cs.mime.ParsedContact;
import com.zimbra.cs.util.Zimbra;

public class TestContactGroup {
    private static final String CONTACT_REF_VALUE = "contact ref";
    private static final String GAL_REF_VALUE = "gal ref";
    private static final String INLINE_VALUE = "inline";
    
    private static class MemberData {
        private Member.Type type;
        private String value;
        
        private MemberData(Member.Type type, String value) {
            this.type = type;
            this.value = value;
        }
    }
        
    private ContactGroup createContactGroup(MemberData[] members) throws Exception {
        ContactGroup contactGroup = ContactGroup.init();
        for (MemberData member : members) {
            contactGroup.addMember(member.type, member.value);
        }
        return reEncode(contactGroup);
    }
    
    // encode, then return decoded, so encode/decode get tested
    private ContactGroup reEncode(ContactGroup contactGroup) throws Exception {
        String encoded = contactGroup.encode();
        return ContactGroup.init(encoded);
    }
    
    @BeforeClass
    public static void init() throws Exception {
        Zimbra.startupCLI();
        CliUtil.toolSetup();
        
        /*
        ZimbraLog.contact.setLevel(Log.Level.debug);
        ZimbraLog.gal.setLevel(Log.Level.debug);
        ZimbraLog.index.setLevel(Log.Level.debug);
        ZimbraLog.search.setLevel(Log.Level.debug);
        */
    }
    
    @Test
    public void getMemberById() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE)});
        
        Member member = contactGroup.getMembers().get(0);
        MemberId memberId = member.getId();
        
        Member memberById = contactGroup.getMemberById(memberId);
        assertEquals(Member.Type.CONTACT_REF, memberById.getType());
        assertEquals(CONTACT_REF_VALUE, memberById.getValue());
    }
        
    @Test
    public void deleteAllMembers() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE),
                new MemberData(Member.Type.GAL_REF, GAL_REF_VALUE),
                new MemberData(Member.Type.INLINE, INLINE_VALUE)});
        
        contactGroup.deleteAllMembers();
        
        contactGroup = reEncode(contactGroup);
        
        List<Member> members = contactGroup.getMembers();
        assertEquals(0, members.size());
    }
    
    @Test
    public void getMembers() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE),
                new MemberData(Member.Type.GAL_REF, GAL_REF_VALUE),
                new MemberData(Member.Type.INLINE, INLINE_VALUE)});
        
        List<Member> members = contactGroup.getMembers();
        assertEquals(3, members.size());
        assertTrue(members.get(0) instanceof ContactRefMember);
        assertEquals(CONTACT_REF_VALUE, members.get(0).getValue());
        assertTrue(members.get(1) instanceof GalRefMember);
        assertEquals(GAL_REF_VALUE, members.get(1).getValue());
        assertTrue(members.get(2) instanceof InlineMember);
        assertEquals(INLINE_VALUE, members.get(2).getValue());
    }
    
    @Test
    public void addMember() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE),
                new MemberData(Member.Type.GAL_REF, GAL_REF_VALUE),
                new MemberData(Member.Type.INLINE, INLINE_VALUE)});
        
        MemberData memberToAdd = new MemberData(Member.Type.INLINE, "added");
        Member addedMember = contactGroup.addMember(memberToAdd.type, memberToAdd.value);
        
        contactGroup = reEncode(contactGroup);
        
        List<Member> members = contactGroup.getMembers();
        assertEquals(4, members.size());
        
        Member memberById = contactGroup.getMemberById(addedMember.getId());
        assertEquals(memberToAdd.type, memberById.getType());
        assertEquals(memberToAdd.value, memberById.getValue());
    }
    
    @Test
    public void removeMember() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE),
                new MemberData(Member.Type.GAL_REF, GAL_REF_VALUE),
                new MemberData(Member.Type.INLINE, INLINE_VALUE)});
        
        Member memberToRemove = contactGroup.getMembers().get(0);
        MemberId memberId = memberToRemove.getId();
        contactGroup.removeMember(memberId);
        
        contactGroup = reEncode(contactGroup);
        Member memberById = contactGroup.getMemberById(memberId);
        assertNull(memberById);
        
        List<Member> members = contactGroup.getMembers();
        assertEquals(2, members.size());
    }
    
    @Test
    public void modifyMember() throws Exception {
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, CONTACT_REF_VALUE)});
        
        Member member = contactGroup.getMembers().get(0);
        MemberId memberId = member.getId();
        
        // type not change
        MemberData modifiedMemberData = new MemberData(null, "111");
        contactGroup.modifyMember(memberId, modifiedMemberData.type, modifiedMemberData.value);
        contactGroup = reEncode(contactGroup);
        
        Member modifiedMember = contactGroup.getMemberById(memberId);
        assertEquals(member.getType(), modifiedMember.getType());
        assertEquals(modifiedMemberData.value, modifiedMember.getValue());
        
        // change to a new type
        modifiedMemberData = new MemberData(Member.Type.INLINE, "222");
        contactGroup.modifyMember(memberId, modifiedMemberData.type, modifiedMemberData.value);
        contactGroup = reEncode(contactGroup);
        
        modifiedMember = contactGroup.getMemberById(memberId);
        assertEquals(modifiedMemberData.type, modifiedMember.getType());
        assertEquals(modifiedMemberData.value, modifiedMember.getValue());
    }
    
    @Test
    public void insertOrder() throws Exception {
        int NUM_MEMBERS = 5;
        
        ContactGroup contactGroup = createContactGroup(new MemberData[0]);
        
        for (int i=1; i<=NUM_MEMBERS; i++) {
            MemberData memberData = new MemberData(Member.Type.INLINE, "" + i);
            contactGroup.addMember(memberData.type, memberData.value);
        }
        
        contactGroup = reEncode(contactGroup);
        
        List<Member> members = contactGroup.getMembers();
        
        Member firstMember = members.get(0);
        MemberData memberData = new MemberData(Member.Type.CONTACT_REF, "" + 1);
        contactGroup.modifyMember(firstMember.getId(), memberData.type, memberData.value);
        
        contactGroup = reEncode(contactGroup);
        
        // verify order is not changed by the modify, which remove the member and readd while 
        // MemberId is not changed
        
        members = contactGroup.getMembers();
        for (int i=1; i<=NUM_MEMBERS; i++) {
            Member member = members.get(i-1);
            assertEquals("" + i, member.getValue());
            // System.out.println(member.getId().toString());
        }
    }
    
    @Test
    public void derefContact() throws Exception {
        Account account = Provisioning.getInstance().get(AccountBy.name, TestUtil.getAddress("user1"));
        Mailbox mbox = MailboxManager.getInstance().getMailboxByAccount(account);
        OperationContext octxt = null;  //TODO
        
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put(ContactConstants.A_fileAs, ContactConstants.FA_FIRST_LAST);
        fields.put(ContactConstants.A_firstName, "test");
        fields.put(ContactConstants.A_email, "test1@zimbra.com");
        fields.put(ContactConstants.A_workEmail1, "test2@zimbra.com");
        Contact contact = mbox.createContact(octxt, new ParsedContact(fields), Mailbox.ID_FOLDER_CONTACTS, null);
        
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.CONTACT_REF, "" + contact.getId()),
                new MemberData(Member.Type.INLINE, "aaa@test.com"), 
                new MemberData(Member.Type.INLINE, "zzz@test.com")});
        
        contactGroup.derefAllMembers(mbox, octxt);
        
        boolean gotContactRefMember = false;
        String prevMemberKey = null;
        for (Member member : contactGroup.getDerefedMembers()) {
            String memberKey = member.getDerefedKey();
            if (prevMemberKey != null) {
                assertTrue(prevMemberKey.compareTo(memberKey) < 0);
            }
            prevMemberKey = memberKey;
            
            Member.Type type = member.getType();
            if (type == Member.Type.CONTACT_REF) {
                assertEquals("test", memberKey);
                gotContactRefMember = true;
            }
            // System.out.println(memberKey);
        }
        
        List<String> emailAddrs = contactGroup.getEmailAddresses(false, mbox, octxt, false);
        assertEquals(4, emailAddrs.size());
        assertTrue(emailAddrs.contains("test1@zimbra.com"));
        assertTrue(emailAddrs.contains("test2@zimbra.com"));
        assertTrue(emailAddrs.contains("aaa@test.com"));
        assertTrue(emailAddrs.contains("zzz@test.com"));
        
        assertTrue(gotContactRefMember);
    }

    @Test
    public void derefGal() throws Exception {
        Account account = Provisioning.getInstance().get(AccountBy.name, TestUtil.getAddress("user1"));
        Mailbox mbox = MailboxManager.getInstance().getMailboxByAccount(account);
        OperationContext octxt = null;  //TODO
        
        Account galMember = Provisioning.getInstance().get(AccountBy.name, TestUtil.getAddress("user2"));
        LdapAccount ldapAccount = (LdapAccount) galMember;
        String dn = ldapAccount.getDN();
        String email = galMember.getName();
        
        ContactGroup contactGroup = createContactGroup(new MemberData[] {
                new MemberData(Member.Type.GAL_REF, dn),
                new MemberData(Member.Type.INLINE, "aaa@test.com"), 
                new MemberData(Member.Type.INLINE, "zzz@test.com")});
        
        contactGroup.derefAllMembers(mbox, octxt);
        
        boolean gotGalRefMember = false;
        String prevMemberKey = null;
        for (Member member : contactGroup.getDerefedMembers()) {
            String memberKey = member.getDerefedKey();
            if (prevMemberKey != null) {
                assertTrue(prevMemberKey.compareTo(memberKey) < 0);
            }
            prevMemberKey = memberKey;
            
            Member.Type type = member.getType();
            if (type == Member.Type.GAL_REF) {
                assertEquals(email, memberKey);
                gotGalRefMember = true;
            }
            // System.out.println(memberKey);
        }
        
        List<String> emailAddrs = contactGroup.getEmailAddresses(false, mbox, octxt, false);
        assertEquals(3, emailAddrs.size());
        assertTrue(emailAddrs.contains(email));
        assertTrue(emailAddrs.contains("aaa@test.com"));
        assertTrue(emailAddrs.contains("zzz@test.com"));
        
        assertTrue(gotGalRefMember);
    }
    
}
