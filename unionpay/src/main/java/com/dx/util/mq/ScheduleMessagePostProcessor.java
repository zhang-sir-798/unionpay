package com.dx.util.mq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.ScheduledMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.MessagePostProcessor;

public class ScheduleMessagePostProcessor implements MessagePostProcessor{
	private long delay = 0l;
	 
    private String corn = null;
 
    public ScheduleMessagePostProcessor(long delay) {
        this.delay = delay;
    }
 
    public ScheduleMessagePostProcessor(String cron) {
        this.corn = cron;
    }
    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        if (delay > 0) {
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
        }
        if (!StringUtils.isEmpty(corn)) {
            message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, corn);
        }
        return message;
    }



}
