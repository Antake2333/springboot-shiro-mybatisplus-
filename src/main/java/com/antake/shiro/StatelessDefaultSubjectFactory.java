package com.antake.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author Antake
 * @date 2020/5/19
 * @description this is description
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        // 禁止 Subject 创建会话
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}

