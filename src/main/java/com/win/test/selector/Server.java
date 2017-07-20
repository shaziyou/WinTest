package com.win.test.selector;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class Server {

	static int PORT = 8080;
	static int BUFFER_SIZE = 1024;
	static String CHARSET = "utf-8"; // 默认编码
	CharsetDecoder decoder; // 解码

	private final int port;
	private ServerSocketChannel channel;
	private Selector selector;
	private final ByteBuffer buffer;

	public Server(int port) throws IOException {
		this.port = port;
		this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
		this.decoder = Charset.forName(CHARSET).newDecoder();
		this.selector = Selector.open();// 打开选择器
	}

	/**
	 * 单线程服务，通过单一个线程同时为多路复用IO流服务 1、此方式适合：IO密集型的操作：如代理服务. 2、相信大家写过：使用socket的聊天程序:
	 * 即accept()一个socket后，new一个Thread为该socket服务， 此方式适合：CPU密集型的操作，如需要处理大量业务、计算
	 *
	 * @throws IOException
	 */
	public void listen() throws IOException {

		// 打开一个服务通道
		this.channel = ServerSocketChannel.open();
		// 绑定服务端口
		ServerSocket socket = channel.socket();
		socket.bind(new InetSocketAddress(port));
		// 使用非阻塞模式，使用多道io操作
		channel.configureBlocking(false);

		System.out.println("服务运行中...");
		while (true) {
			// 非阻塞，没有连接，立即返回null，与socket.accept()方法(阻塞)不同，
			SocketChannel client = channel.accept();
			System.out.println(client);
			if (client != null) {
				System.out.println("客户端口-->" + client.getRemoteAddress());
				registerClient(client);
			}

			service();
		}
	}

	/**
	 * 将客户端channel注册到selector上 四个事件：Connect、Accept、Read、Write
	 *
	 * @param client
	 * @throws IOException
	 */
	private void registerClient(SocketChannel client) throws IOException {
		// 设置非阻塞
		client.configureBlocking(false);
		// 将客户端channel注册到selector上
		client.register(selector, SelectionKey.OP_READ);

	}

	/**
	 * 遍历各客户端通道 select()阻塞到至少有一个通道在你注册的事件上就绪了 select(long timeout) 多设置一个阻塞时间(毫秒)
	 * selectNow() 不阻塞，有无都返回。
	 */
	private void service() throws IOException {
		if (selector.selectNow() > 0) {
			// 客户端channel的键集合
			Set<SelectionKey> keys = selector.selectedKeys();
			System.out.println("selector长度-->" + keys.size());
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				// SocketChannel client = (SocketChannel) key.channel();

				if (key.isReadable()) {
					read(key);
				} else if (key.isWritable()) {
					write(key);
				}
			}

		}
	}

	// 读信息
	private void read(SelectionKey key) throws IOException {
		SocketChannel client = (SocketChannel) key.channel();
		buffer.clear();

		int c = client.read(buffer);
		if (c > 0) {
			// flip方法将Buffer从写模式切换到读模式
			buffer.flip();
			CharBuffer charBuffer = decoder.decode(buffer);
			// 接收请求
			System.out.println(charBuffer.toString());

			key.attach("ack syn...");
			// 改变自身关注事件，可以用位或操作|组合时间
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

		} else {
			client.close();
		}
		buffer.clear();
	}

	// 响应信息
	private void write(SelectionKey key) throws IOException {
		SocketChannel client = (SocketChannel) key.channel();
		String handle = (String) key.attachment();// 取出read方法传递的信息。
		String res = Response.getMsg();
		if (handle != null) {
			res = res + "\r\n" + handle;
		}

		ByteBuffer block = ByteBuffer.wrap(res.getBytes());
		client.write(block);
		client.close();
		// 改变自身关注事件，可以用位或操作|组合时间
		// key.interestOps(SelectionKey.OP_READ);
	}

	public static void main(String[] args) {
		try {
			System.out.println("正在启动服务...");
			Server server = new Server(PORT);
			server.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Response {

	public static String getMsg() {
		return "HTTP/1.1 200 OK" + "\r\n";
	}
}