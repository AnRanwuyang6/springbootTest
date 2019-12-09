package com.bingo.invoice.invoice.InvoiceService;


import com.bingo.invoice.invoice.entity.User;
import org.springframework.ldap.filter.Filter;

import java.util.List;

/**
 * @Auther: lizk
 * @Date: 2019/5/13 16:22
 * @Description:
 */
public interface LdapService {
    boolean authenticate(String userName, String password);

}
