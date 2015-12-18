package com.twitterassistant.service.mail;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class VelocityEmailSender<K> {

	// Constants ---------------------------------------------------------------------------------------------- Constants
	private static final transient Logger LOG = LoggerFactory.getLogger(VelocityEmailSender.class);

	private final VelocityEngine velocityEngine;
	private final JavaMailSender mailSender;

	// this is a default email from
	@Value("#{twitProperties['mail.from.support']}")
	String from;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	@Autowired
	public VelocityEmailSender(VelocityEngine velocityEngine, JavaMailSender mailSender) {
		this.velocityEngine = velocityEngine;
		this.mailSender = mailSender;
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	/**
	 * Sends e-mail using Velocity template for the body and the properties passed in as Velocity variables.
	 * 
	 * @param <K>
	 * @param <V>
	 * 
	 * @param msg
	 *          The e-mail message to be sent, except for the body.
	 * @param hTemplateVariables
	 *          Variables to use when processing the template.
	 */

	@SuppressWarnings("hiding")
	public <K, V> boolean send(final SimpleMailMessage msg, final String template, final Map<K, V> hTemplateVariables) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {

				String text = null;
				String html = null;

				// replace commas as it breaks it into multiple recipients
				msg.setFrom(msg.getFrom().replaceAll(",", " "));
				String[] to = msg.getTo();
				for (int i = 0; i < to.length; i++)
					to[i] = to[i].replaceAll(",", " ");

				String tmplFile = "/" + template + ".text.vm";
				// prepare text block
				if (velocityEngine.resourceExists(tmplFile))
					text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, tmplFile, "UTF-8", (Map<String, Object>) hTemplateVariables);

				// prepare html block
				tmplFile = "/" + template + ".html.vm";
				if (velocityEngine.resourceExists(tmplFile))
					html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, tmplFile, "UTF-8", (Map<String, Object>) hTemplateVariables);

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, html != null && text != null ? true : false, "UTF-8");
				message.setTo(msg.getTo());
				message.setFrom(msg.getFrom() == null ? from : msg.getFrom());
				message.setReplyTo(msg.getFrom() == null ? from : msg.getFrom());
				message.setSubject(msg.getSubject());

				// LOG.info("html={}", html);
				// LOG.info("text={}", text);

				if (text != null && html != null)
					message.setText(text, html);
				else if (text != null)
					message.setText(text);
				else if (html != null)
					message.setText(html, true);
			}
		};

		mailSender.send(preparator);

		LOG.info("Sent e-mail to '{}'.", msg.getTo());

		return true;
	}

}
