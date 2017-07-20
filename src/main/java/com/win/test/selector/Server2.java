package com.win.test.selector;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class Server2 {
	private Selector selector;
	private final ByteBuffer buffer;
	private final CharsetDecoder decoder;
	private final int port;

	public Server2(int port) throws IOException {
		this.port = port;
		buffer = ByteBuffer.allocate(1024);
		decoder = Charset.forName("UTF-8").newDecoder();
		selector = Selector.open();
	}

	public void initServer() throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.bind(new InetSocketAddress(port));
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_ACCEPT);
	}

	public void start() {
		while (true) {
			try {
				if (selector.selectNow() > 0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> keyIterator = keys.iterator();
					while (keyIterator.hasNext()) {
						SelectionKey key = keyIterator.next();
						keyIterator.remove();
						if (key.isAcceptable())
							accept(key);
						else if (key.isReadable())
							read(key);
						else if (key.isWritable())
							write(key);
						else
							System.out.println(key.toString());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		try {
			SocketChannel client = serverChannel.accept();
			System.out.println(client.getLocalAddress());
			System.out.println(client.getRemoteAddress());
			registerClient(client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerClient(SocketChannel client) throws IOException {
		// 设置非阻塞
		client.configureBlocking(false);
		// 将客户端channel注册到selector上
		client.register(selector, SelectionKey.OP_READ);
	}

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
			// key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			key.interestOps(SelectionKey.OP_WRITE);

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
			Server2 server = new Server2(8088);
			server.initServer();
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
