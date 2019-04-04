package com.dx.util.mq.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.pay.common.util.SpringContextUtil;

/**
 * @description 队列消息生产者，发送消息到队列
 * @author zhangsir
 * @Date 2018年12月11日 下午1:29:00
 * @version 1.0.0
 * 
 */
@Component
public class MchMq4ATProduct {

	private static final Log _log = LogFactory.getLog(MchMq4ATProduct.class);

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;// 通过@Qualifier修饰符来注入对应的bean

	/**
	 * 非延时发送
	 * 
	 * @param queueName
	 *            队列名称
	 * @param message
	 *            消息内容
	 */
	public void send(String queueName, final String message) {
		_log.info("VVVV 发送MQ消息:msg=" + message);
		jmsTemplate.convertAndSend(queueName, message);
	}

	/**
	 * 延时发送
	 * 
	 * @param queueName
	 *            队列名称
	 * @param message
	 *            消息内容
	 */

	public void send(String queueName, final String message, final long delay) {
		_log.info("VVVV 发送MQ延时消息:msg=" + message + ",delay_time={" + delay + "}");

		JmsTemplate mqTemplate = SpringContextUtil.getBean("jmsQueueTemplate");

		mqTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage tm = session.createTextMessage(message);
				tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
				tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1 * 1000);
				tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
				return tm;
			}
		});
	}
	/**
	 * activemq.xml 增加
	 *<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true">
	 */

}
