package com.win.test.jmeter;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.DistributedRunner;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.RemoteThreadsListenerTestElement;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class TestMain {

	public static void main(String[] args) {
		JMeterUtils.loadJMeterProperties("jmeter.properties");

		HashTree tree = new HashTree();
		TestPlan plan = new TestPlan();
		tree.add(plan);

		HTTPSampler http = new HTTPSampler();
		http.setProtocol("http");
		http.setDomain("www.baidu.com");
		http.setPort(80);
		http.setPath("/");
		http.setMethod("GET");
		http.setUseKeepAlive(true);
		http.setFollowRedirects(true);

		LoopController loop = new LoopController();
		loop.setFirst(true);
		loop.setLoops(1);
		loop.addTestElement(http);

		ThreadGroup group = new ThreadGroup();
		group.setNumThreads(100);
		group.setRampUp(1);
		group.setSamplerController(loop);

		ResultCollector result = new MyResultCollector();

		tree.add(plan, group);
		tree.add(plan, new RemoteThreadsListenerTestElement());

		HashTree groupTree = tree.getTree(plan).getTree(group);
		groupTree.add(http);
		groupTree.add(result);

		DistributedRunner runner = new DistributedRunner();
		runner.init(getRemoteHosts(), tree);
		runner.start();

	}

	private static List<String> getRemoteHosts() {
		String remoteHostsString = JMeterUtils.getPropDefault("remote_hosts", "127.0.0.1");
		StringTokenizer st = new StringTokenizer(remoteHostsString, ",");
		List<String> list = new LinkedList<>();
		while (st.hasMoreElements()) {
			list.add((String) st.nextElement());
		}
		return list;
	}

}
