import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Gui {

	DefaultListModel<String> newstitleList = new DefaultListModel<String>();
	DefaultListModel<News> newsList = new DefaultListModel<News>();
	private JComboBox<RSS> newsComboBox;
	private JButton btnOk;
	private JList titleList;
	private JTextPane textDesc;
	private JFrame frame;
	private JScrollPane jPane;
	RSS selectedValue;
	JButton btnUp;
	private String currentDirString = System.getProperty("user.dir") + "\\RunnableJar\\Plugins";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gui() {
		initialize();
	}

	String UTFConverter(String s) throws UnsupportedEncodingException {
		byte ptext[] = s.getBytes();
		return new String(ptext, "UTF-8");
	}

	public void XMLParser() throws ParserConfigurationException, SAXException, IOException {
		selectedValue = (RSS) newsComboBox.getSelectedItem();
		String uriString = selectedValue.getLink();
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuildFact.newDocumentBuilder();
		Document document = docBuilder.parse(uriString);

		NodeList documetNodeList = document.getElementsByTagName("item");
		for (int i = 0; i < documetNodeList.getLength(); i++) {
			News news = new News();
			Node titleNode = documetNodeList.item(i);
			Node linkNode = documetNodeList.item(i);
			Node descNode = documetNodeList.item(i);

			if (titleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element titleElement = (Element) titleNode;
				String title = titleElement.getElementsByTagName("title").item(0).getTextContent();
				title = title.replace("&#39;","'" );
				title = title.replace("&quot;", "\"");
				news.setTitle(title);

				Element descriptionElement = (Element) descNode;
				String description = descriptionElement.getElementsByTagName("description").item(0).getTextContent();
				description = description.replace("&#39;","'" );
				description = description.replace("&quot;", "\"");
				news.setDescription(description);

				Element linkElement = (Element) linkNode;
				String link = linkElement.getElementsByTagName("link").item(0).getTextContent();
				news.setLink(link);
				// link = UTFConverter(link);
				newstitleList.addElement(news.getTitle());
				selectedValue.newsList.add(news);
			}
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1036, 635);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		newsComboBox = new JComboBox<RSS>();
		newsComboBox.setBounds(45, 80, 136, 20);
		frame.getContentPane().add(newsComboBox);
		newsComboBox.addItem(new Haberturk());
		newsComboBox.addItem(new Star());
		newsComboBox.addItem(new Haberturk());
		
		btnOk = new JButton("OK");
		btnOk.setBounds(243, 79, 89, 23);
		frame.getContentPane().add(btnOk);

		titleList = new JList(newstitleList);
		jPane = new JScrollPane();
		jPane.setViewportView(titleList);
		jPane.setBounds(23, 144, 436, 350);
		jPane.setVisible(false);
		frame.getContentPane().add(jPane);

		textDesc = new JTextPane();
		textDesc.setBounds(517, 144, 460, 222);
		textDesc.setVisible(false);
		frame.getContentPane().add(textDesc);

		btnUp = new JButton("Update");
		btnUp.setBounds(370, 79, 89, 23);
		frame.getContentPane().add(btnUp);

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					newstitleList.clear();
					XMLParser();
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textDesc.setVisible(true);
				jPane.setVisible(true);
			}
		});

		btnUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					PluginParse();
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
						| IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		titleList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList listData = (JList) e.getSource();
				// textDesc.setText(newsList.get(listData.getSelectedIndex()).getDescription());
				textDesc.setText(selectedValue.newsList.get(listData.getSelectedIndex()).getDescription());
				super.mouseClicked(e);
			}
		});

	}

	private void PluginParse() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		File file = new File(currentDirString);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			JarFile jarFile = new JarFile(files[i]);
			File file1 = files[i];
			Enumeration allEntries = jarFile.entries();
			while (allEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry) allEntries.nextElement();
				String name = entry.getName();
				if (name.contains("java")) {
					name = name.split(".java")[0];
					name = name.replace("/", ".");
					System.out.println(name);
					URLClassLoader child = new URLClassLoader(new URL[] { file1.toURI().toURL() },
							this.getClass().getClassLoader());
					Class<?> rss = Class.forName(name, true, child);
					if (rss.getSuperclass() != null && rss.getSuperclass().getName().toString().contains("RSS")) {
						Constructor<?> constructor = rss.getConstructor();
						RSS instance = (RSS) constructor.newInstance();
						newsComboBox.addItem(instance);
					}

				}
			}
		}
	}
}
