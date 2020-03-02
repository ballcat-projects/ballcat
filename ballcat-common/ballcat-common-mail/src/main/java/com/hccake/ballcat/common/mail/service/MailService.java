package com.hccake.ballcat.common.mail.service;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import org.springframework.util.StringUtils;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:05
 */
public interface MailService {


    /**
     * 发送邮件
     * @param mailDTO
     */
    void sendMail(MailDTO mailDTO);

    /**
     * 检查邮件是否符合标准
     * @param mailDTO
     */
    default void checkMail(MailDTO mailDTO) {
        if (StringUtils.isEmpty(mailDTO.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailDTO.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailDTO.getContent())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }
}
