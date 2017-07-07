package com.win.test.jmeter;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;

public class MyResultCollector extends ResultCollector {

	private static final long serialVersionUID = 1L;

	@Override
	public void testStarted(String arg0) {
		super.testStarted(arg0);
	}

	@Override
	public void testEnded(String host) {
		super.testEnded(host);
	}

	@Override
	public void sampleOccurred(SampleEvent arg0) {
		super.sampleOccurred(arg0);
		System.out.println(arg0.getResult().getResponseCode());
	}

}
