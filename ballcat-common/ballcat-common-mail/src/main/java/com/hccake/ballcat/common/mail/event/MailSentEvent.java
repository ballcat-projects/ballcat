package com.hccake.ballcat.common.mail.event;

import com.hccake.ballcat.common.mail.dto.MailDTO;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 18:00
 */
@ToString
public class MailSentEvent extends ApplicationEvent {

    public MailSentEvent(MailDTO mailDTO) {
        super(mailDTO);
    }
}
