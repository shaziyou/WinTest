package com.win.test.jmeter;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;

public class MyResultCollector extends ResultCollector {

	private static final long serialVersionUID = 1L;
	private int successNum = 0;

	@Override
	public void testStarted(String arg0) {
		super.testStarted(arg0);
	}

	@Override
	public void testEnded(String host) {
		super.testEnded(host);
		System.out.println("\nSuccess Num : " + successNum);
	}

	@Override
	public void sampleOccurred(SampleEvent event) {
		super.sampleOccurred(event);
		SampleResult result = event.getResult();
		System.out.println(result.getThreadName() + "   " + result.getEndTime() + "   -->" + result.getResponseCode());
		if (result.isSuccessful())
			successNum++;
	}

}
