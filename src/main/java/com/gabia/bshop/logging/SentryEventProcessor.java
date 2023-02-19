package com.gabia.bshop.logging;

import java.util.Objects;

import org.springframework.stereotype.Component;

import io.sentry.EventProcessor;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import io.sentry.SpanStatus;
import io.sentry.protocol.SentryTransaction;

@Component
public class SentryEventProcessor implements EventProcessor {

	public static final String TAG_KEY = "type";
	public static final String TAG_VALUE = "request-api";

	@Override
	public SentryEvent process(final SentryEvent event, final Hint hint) {
		if (TAG_VALUE.equals(event.getTag(TAG_KEY))) {
			return event;
		} else {
			return null;
		}
	}

	@Override
	public SentryTransaction process(final SentryTransaction event, final Hint hint) {
		if (Objects.equals(event.getStatus(), SpanStatus.OK) && TAG_VALUE.equals(event.getTag(TAG_KEY))) {
			return event;
		} else {
			return null;
		}
	}
}
