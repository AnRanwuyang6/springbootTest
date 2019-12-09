package com.bingo.invoice.invoice.InvoiceService.impl;

import com.bingo.invoice.invoice.InvoiceService.LdapService;
import com.bingo.invoice.invoice.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;

/**
 * @Auther: lizk
 * @Date: 2019/5/13 16:23
 * @Description:
 */
@Service
public class LdapServiceImpl implements LdapService {
    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${ldap.domainName}")
    private String ldapDomainName;

    @Value("${ldap.base}")
    private String ldapBaseDn;

    /*
    * 身份认证
    */
    @Override
    public boolean authenticate(String userName, String password) {
        //String userDomainName = getDnForUser(userName);
        String userDomainName = String.format(ldapDomainName, userName);
        DirContext ctx = null;
        try {
            ctx = ldapTemplate.getContextSource().getContext(userDomainName,password);
            System.out.println("ctx:"+ctx);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            LdapUtils.closeContext(ctx);
        }
        return false;
    }
}
