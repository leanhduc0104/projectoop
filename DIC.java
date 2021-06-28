import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.Paint;
import java.awt.TextArea;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.google.common.base.Function;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
public class DIC {
	static int pre;
	static int tmp1,tmp2;
	static int t;
	static int cur;
	static int mark;
	static int stop;
	static int k;
	static int n;
	static int myArray[][];
	static LinkedList<Integer> d;
	static ArrayList<Integer> d1;
	static ArrayList<Integer> d2;
    Graph<Integer,Integer> graph;
    VisualizationViewer<Integer,Integer> vv;
    public DIC() {
        graph = new DirectedSparseGraph<Integer,Integer>();
        Integer[] v = createVertices(n);
        createEdges(v);
        vv =  new VisualizationViewer<Integer,Integer>(new FRLayout2<Integer,Integer>(graph));
        vv.getRenderContext().setVertexLabelTransformer(new Function<Integer,String>(){

			public String apply(Integer v) {
				return "";
			}});
        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
        vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));

        vv.getRenderContext().setVertexIconTransformer(new Function<Integer,Icon>() {
			public Icon apply(final Integer v) {
				return new Icon() {

					public int getIconHeight() {
						return 20;
					}

					public int getIconWidth() {
						return 20;
					}

					public void paintIcon(Component c, Graphics g,
							int x, int y) {
						if(vv.getPickedVertexState().isPicked(v)||((mark==2||mark==3)&&v==cur+1)) {
							g.setColor(Color.yellow);
						} else {
							g.setColor(Color.red);
						}
						g.fillOval(x, y, 20, 20);
						if(vv.getPickedVertexState().isPicked(v)) {
							g.setColor(Color.black);
						} else {
							g.setColor(Color.white);
						}
						g.drawString(""+v, x+6, y+15);
						
					}};
			}});
        final Function Change=new Function<Integer,Paint>(){
        	public Paint apply(Integer q) {
        		if(mark==1) {
        		for(int i=d.size()-1;i>=1;i--)
        		if (q==n*d.get(i)+d.get(i-1)) return Color.blue;
        	
        		return Color.darkGray;}
        		else if(mark==2) {
        			for(int i=d.size()-1;i>=1;i--)
                		if (q==n*d.get(i)+d.get(i-1)) return Color.blue;
        			for(int i=0;i<d1.size();i++) {
        				if(q==d1.get(i)) return Color.green;}
        				return Color.darkGray;
        			
        		}
        		else if(mark==3) {
        			for (Integer element : d2) {
        				if(q==element) return Color.blue;}
        				return Color.darkGray;
        			
        		}
        		return Color.darkGray;
        	}
        };

        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), Color.white,  Color.yellow));
       //vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Integer>(vv.getPickedEdgeState(), Color.black, Color.lightGray));
        vv.getRenderContext().setEdgeDrawPaintTransformer(Change);
        vv.getRenderContext().setVertexFillPaintTransformer(new Function<Integer,Paint>(){
        	public Paint apply(Integer q) {
        		if(mark==2) {
        			if(q==(cur+1)) return Color.yellow;
        		}
        		return Color.red;
        		
        	}
        });
        vv.setBackground(Color.white);

        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        
        // create a frome to hold the graph
        final JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        final JTextArea jt = new JTextArea(10,10);
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final DefaultModalGraphMouse<Integer, Integer> gm
        	= new DefaultModalGraphMouse<Integer,Integer>();
        vv.setGraphMouse(gm);
        
        final ScalingControl scaler = new CrossoverScalingControl();
        final ArrayList<String> getver=new ArrayList();
        for(int i=0;i<n;i++) {
        	if (myArray[cur][i]>0)
        	getver.add(String.valueOf(i+1));
        }
        String[] ver=new String[getver.size()];
        for(int i=0;i<getver.size();i++) {
        	ver[i]=getver.get(i);
        }      
        final JComboBox cb=new JComboBox(ver);    
        cb.setBounds(50, 50,90,20);

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });
        JButton f=new JButton("PrintAllPath");
        f.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		final Bfs g= new Bfs(n+1);
        		for(int i=0;i<n;i++)
        			for(int j=0;j<n;j++)
        				if(myArray[i][j]>0) g.add(i+1, j+1);
        		final JFrame fp = new JFrame();
        		Container c = fp.getContentPane();
        		c.setLayout(new FlowLayout());        		
        		DefaultListModel<Integer> fromList = new DefaultListModel<Integer>();
        		DefaultListModel<Integer> toList = new DefaultListModel<Integer>();
        		for (int i = 1; i <= n; i++) {
        			fromList.addElement(i);
        			toList.addElement(i);
        		}
        		final JList<Integer> list1 = new JList<Integer>(fromList);
        		list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        		list1.setSelectedIndex(0);
        		list1.setVisibleRowCount(3);       		
        		final JList<Integer> list2 = new JList<Integer>(fromList);
        		list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        		list2.setSelectedIndex(0);
        		list2.setVisibleRowCount(3);
        		JButton btn = new JButton("OK");
        		c.add(new JLabel("From "));
        		c.add(list1);
        		c.add(new JLabel(" to "));
        		c.add(list2);
        		c.add(btn);
        		fp.setTitle("All Path");
        		fp.pack();
        		fp.setAlwaysOnTop(true);
        		fp.setLocationRelativeTo(null);
        		fp.setVisible(true);
        		btn.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				mark = 4;
        				if (mark == 4) {
        					String a = "";
        					a += "All paths from " + list1.getSelectedValue() + " to " + list2.getSelectedValue() +" are/is: \n"; 
        					
        	        		jt.setText(a);
        	        		Bfs tmp=new Bfs(n+1);
        	        		for(int i=0;i<n;i++) {
        	        			for(int j=0;j<n;j++) if(myArray[i][j]>0) tmp.add(i+1, j+1);}
        	        			tmp.AllPaths(list1.getSelectedValue(),list2.getSelectedValue() , jt);
        				}
        				fp.dispose();
        			}
        		
        		});
        		cur=0;
        		cb.removeAllItems();
        		for(int i=0;i<n;i++) {
        			if(myArray[cur][i]>0) {
        				String next=String.valueOf(i+1);
        				cb.addItem(next);}}
        	}});	
        JButton f1=new JButton("ShortestPath");
        f1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 
        		final ArrayList<ArrayList<Integer>> adj =
                         new ArrayList<ArrayList<Integer>>(n);
        		 for (int i = 0; i < n; i++) {
        			 adj.add(new ArrayList<Integer>());
        		 	}
        		 for(int i=0;i<n;i++)
        			 for(int j=0;j<n;j++)
        				 if(myArray[i][j]>0) pathUnweighted.addE(adj,i,j);
        		final JFrame fp = new JFrame();
         		Container c = fp.getContentPane();
         		c.setLayout(new FlowLayout());        		
         		DefaultListModel<Integer> fromList = new DefaultListModel<Integer>();
         		DefaultListModel<Integer> toList = new DefaultListModel<Integer>();
         		for (int i = 1; i <= n; i++) {
         			fromList.addElement(i);
         			toList.addElement(i);
         		}
         		final JList<Integer> list1 = new JList<Integer>(fromList);
         		list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         		list1.setSelectedIndex(0);
         		list1.setVisibleRowCount(3);       		
         		final JList<Integer> list2 = new JList<Integer>(fromList);
         		list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         		list2.setSelectedIndex(0);
         		list2.setVisibleRowCount(3);
         		JButton btn = new JButton("OK");
         		c.add(new JLabel("From "));
         		c.add(list1);
         		c.add(new JLabel(" to "));
         		c.add(list2);
         		c.add(btn);
         		fp.setTitle("Shortest Path");
         		fp.pack();
         		fp.setAlwaysOnTop(true);
         		fp.setLocationRelativeTo(null);
         		fp.setVisible(true);
         		btn.addActionListener(new ActionListener() {
         			public void actionPerformed(ActionEvent e) {
         				mark = 5;
         				if (mark == 5) {
         					{	tmp1=list1.getSelectedValue()-1;
         						tmp2=list2.getSelectedValue()-1;
                		        int pred[] = new int[n];
                		        int dist[] = new int[n];
                		        if (list1.getSelectedValue() == list2.getSelectedValue()) {
                		        	jt.setText("Shortest path length is: 0\n"
                		        			+ "Path is: " + list1.getSelectedValue());
                		        	return;
                		        }
                		        if (Check(adj, list1.getSelectedValue() - 1, list2.getSelectedValue() - 1, n, pred, dist) == false) {
                		            jt.setText("Given source and destination are not connected");
                		            return;
                		        }
                		        LinkedList<Integer> path = new LinkedList<Integer>();
                		        int crawl = list2.getSelectedValue() - 1;
                		        path.add(crawl);
                		        while (pred[crawl] != -1) {
                		            path.add(pred[crawl]);
                		            crawl = pred[crawl];
                		        }
                		        String a = "";
                		        a+="Shortest path length is: " + dist[list2.getSelectedValue()-1];
                		        a+="\nPath is: ";
                		        for (int i = path.size() - 1; i >= 0; i--) {
                		            a+=path.get(i)+1 + " ";   
                		            d = path;
                		        fp.dispose();
                		        }
                		        cur=0;
                        		cb.removeAllItems();
                        		for(int i=0;i<n;i++) {
                        			if(myArray[cur][i]>0) {
                        				String next=String.valueOf(i+1);
                        				cb.addItem(next);}}
                		        jt.setText(a);
                		 mark=1;
                		 cur=0;
                		 vv.repaint();
                	}
         				}
         			}
         		});
        		 
        	}

			private boolean Check(ArrayList<ArrayList<Integer>> adj, int src, int dest, int v, int[] pred, int[] dist) {
		        LinkedList<Integer> queue = new LinkedList<Integer>();
		        boolean visited[] = new boolean[v];
		        for (int i = 0; i < v; i++) {
		            visited[i] = false;
		            dist[i] = Integer.MAX_VALUE;
		            pred[i] = -1;
		        }
		        visited[src] = true;
		        dist[src] = 0;
		        queue.add(src);
		        while (!queue.isEmpty()) {
		            int u = queue.remove();
		            for (int i = 0; i < adj.get(u).size(); i++) {
		                if (visited[adj.get(u).get(i)] == false) {
		                    visited[adj.get(u).get(i)] = true;
		                    dist[adj.get(u).get(i)] = dist[u] + 1;
		                    pred[adj.get(u).get(i)] = u;
		                    queue.add(adj.get(u).get(i));
		                    if (adj.get(u).get(i) == dest)
		                        return true;
		                }
		            }
		        }
		        return false;
			}});
        JButton f2=new JButton("Show");
        f2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(mark!=2) {jt.setText("");
        		cur=0;
        		d1.clear();}
        		
        		mark=2;
        		System.out.print(cb.getSelectedItem()+" ");
        		Integer tmp=Integer.parseInt((String) (cb.getSelectedItem()))-1;
        		d1.add(cur*n+tmp);
        		vv.repaint();
        		jt.append(String.valueOf(tmp+1)+" ");
        		cur=tmp;
        		cb.removeAllItems();
        		for(int i=0;i<n;i++) {
        			if(myArray[cur][i]>0) {
        				String next=String.valueOf(i+1);
        				cb.addItem(next);
        			}
        		}
        		pathUnweighted m=new pathUnweighted(n);
       		 ArrayList<ArrayList<Integer>> adj =
                        new ArrayList<ArrayList<Integer>>(n);
       		 for (int i = 0; i < n; i++) {
       			 adj.add(new ArrayList<Integer>());
       		 	}
       		 for(int i=0;i<n;i++)
       			 for(int j=0;j<n;j++)
       				 if(myArray[i][j]>0) pathUnweighted.addE(adj,i,j);
       		 m.ShortestDistance(adj, cur, n-1, n);
       		 d=m.p;
       		 
        	}
        });
        JButton f3=new JButton("toPNG");
        f3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		VisualizationImageServer<Integer, Integer> vis =
        			    new VisualizationImageServer<Integer, Integer>(vv.getGraphLayout(),
        			        vv.getGraphLayout().getSize());
        			vis.getRenderContext().setEdgeDrawPaintTransformer(Change);
        			vis.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        			vis.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), Color.white,  Color.yellow));
        			vis.getRenderContext().setVertexIconTransformer(new Function<Integer,Icon>() {
        				public Icon apply(final Integer v) {
        					return new Icon() {

        						public int getIconHeight() {
        							return 20;
        						}

        						public int getIconWidth() {
        							return 20;
        						}

        						public void paintIcon(Component c, Graphics g,
        								int x, int y) {
        							if(vv.getPickedVertexState().isPicked(v)||(mark==2&&v==cur+1)) {
        								g.setColor(Color.yellow);
        							} else {
        								g.setColor(Color.red);
        							}
        							g.fillOval(x, y, 20, 20);
        							if(vv.getPickedVertexState().isPicked(v)) {
        								g.setColor(Color.black);
        							} else {
        								g.setColor(Color.white);
        							}
        							g.drawString(""+v, x+6, y+15);
        							
        						}};
        				}});
        			
 //       			vis.getRenderer().getVertexLabelRenderer()
 //       			    .setPosition(Renderer.VertexLabel.Position.CNTR);

        			// Create the buffered image
        			BufferedImage image = (BufferedImage) vis.getImage(
        			    new Point2D.Double(vv.getGraphLayout().getSize().getWidth() / 2,
        			    vv.getGraphLayout().getSize().getHeight() / 2),
        			    new Dimension(vv.getGraphLayout().getSize()));

        			// Write image to a png file
        			String fileout="graph"+String.valueOf(t)+".png";
        			File outputfile = new File(fileout);
        			t++;

        			try {
        			    ImageIO.write(image, "png", outputfile);
        			    JOptionPane.showMessageDialog(null,"Save sucessfully","Report",JOptionPane.INFORMATION_MESSAGE);
        			} catch (IOException e1) {
        				 JOptionPane.showMessageDialog(null,"Save failed","Report",JOptionPane.INFORMATION_MESSAGE);
        				
        			}
        	}
        });
        JButton f4=new JButton("Back");
        f4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(mark==2) {
        		int tmp=d1.get(d1.size()-1);
        		d1.remove(d1.size()-1);
        		cur=(tmp-cur)/n;
        		pathUnweighted m=new pathUnweighted(n);
          		 ArrayList<ArrayList<Integer>> adj =
                           new ArrayList<ArrayList<Integer>>(n);
          		 for (int i = 0; i < n; i++) {
          			 adj.add(new ArrayList<Integer>());
          		 	}
          		 for(int i=0;i<n;i++)
          			 for(int j=0;j<n;j++)
          				 if(myArray[i][j]>0) pathUnweighted.addE(adj,i,j);
          		 m.ShortestDistance(adj, cur, n-1, n);
          		 d=m.p;
          		 cb.removeAllItems();
          		for(int i=0;i<n;i++) {
        			if(myArray[cur][i]>0) {
        				String next=String.valueOf(i+1);
        				cb.addItem(next);
        			}
        			if(d1.size()==0) mark=0;
        		}
          		String text = jt.getText();
          		if(text.length()>=2) {
    			jt.setText(text.substring(0, text.length() - 2));}
          		vv.repaint();}
        		
        		
        	}
        });
        JButton f5=new JButton("Auto");
        f5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mark=3;
        		stop=0;
        		if(mark==3) {
        			jt.setText("");
        			d2.clear();
        			final pathUnweighted m=new pathUnweighted(n);
            		 ArrayList<ArrayList<Integer>> adj =
                             new ArrayList<ArrayList<Integer>>(n);
            		 for (int i = 0; i < n; i++) {
            			 adj.add(new ArrayList<Integer>());
            		 	}
            		 for(int i=0;i<n;i++)
            			 for(int j=0;j<n;j++)
            				 if(myArray[i][j]>0) pathUnweighted.addE(adj,i,j);
            		 m.ShortestDistance(adj, tmp1, tmp2, n);
        			new Thread(new Runnable() {
        			      public void run() {
        			    	pre=tmp1;
        			        for (int i = m.p.size()-1; i >=1; i--) {
        			        	final int k=i;
        			        	if(stop==1) break;
        			          // Runs inside of the Swing UI thread
        			          SwingUtilities.invokeLater(new Runnable() {
        			            public void run() {
        			            	
        			            	d2.add(n*m.p.get(k)+m.p.get(k-1));
        			            	cur=m.p.get(k-1);
        	             			vv.repaint();
        	             			jt.append(String.valueOf(pre+1)+"--->"+String.valueOf(cur+1)+'\n');
        	             			pre=cur;
        			            }
        			          });

        			          try {
        			            java.lang.Thread.sleep(1000);
        			          }
        			          catch(Exception e) { }
        			        }
        			      }
        			    }).start();
        	
        		}}});
        JButton f7=new JButton("Stop");
        f7.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		stop=1;
        	}
        });
        JButton f6=new JButton("Clear");
        f6.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		d.clear();
        		d1.clear();
        		d2.clear();
        		tmp1=0;
        		tmp2=0;
        		jt.setText("");
        		cur=0;
        		cb.removeAllItems();
        		for(int i=0;i<n;i++) {
        			if(myArray[cur][i]>0) {
        				String next=String.valueOf(i+1);
        				cb.addItem(next);
        			}
        		}
        		mark=0;
        		vv.repaint();
        		
        	}
        });
        JPanel controls = new JPanel();
//        controls.add(jt);
        controls.add(plus);
        controls.add(minus);
        controls.add(f6);
        controls.add(f);
        controls.add(f1);
        controls.add(f5);
        controls.add(f7);
        controls.add(cb);
        controls.add(f2);
        controls.add(f3);
        controls.add(f4);
        controls.add(gm.getModeComboBox());
        content.add(controls, BorderLayout.NORTH);
        content.add(new JScrollPane(jt),BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private Integer[] createVertices(int count) {
        Integer[] v = new Integer[count];
        for (int i = 0; i < count; i++) {
            v[i] = new Integer(i+1);
            graph.addVertex(v[i]);
        }
        return v;
    }
    void createEdges(Integer[] v) {
    	for(int i=0;i<n;i++)
    		for(int j=0;j<n;j++)
    			if(myArray[i][j]>0)
    				graph.addEdge(n*i+j, v[i], v[j], EdgeType.DIRECTED);
    }

    public static void main(String[] args) throws FileNotFoundException
    {	String filePath = new File("").getAbsolutePath(); 
    	Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath+"\\src\\main\\java\\oop")));
    int k=0;
    while(sc.hasNextLine()) {
    	k++;
    	sc.nextLine();
    }
    n=k;
    sc.close();
    Scanner s = new Scanner(new BufferedReader(new FileReader(filePath+"\\src\\main\\java\\oop")));
    myArray = new int[n][n];
    while(s.hasNextLine()) {
       for (int i=0; i<myArray.length; i++) {
          String[] line = s.nextLine().trim().split(" ");
          for (int j=0; j<line.length; j++) {
             myArray[i][j] = Integer.parseInt(line[j]);
          }
       }
    }
    s.close();
    pathUnweighted m=new pathUnweighted(n);
    ArrayList<ArrayList<Integer>> adj =
            new ArrayList<ArrayList<Integer>>(n);
for (int i = 0; i < n; i++) {
adj.add(new ArrayList<Integer>());
}
    for(int i=0;i<n;i++)
    	for(int j=0;j<n;j++)
    		if(myArray[i][j]>0) pathUnweighted.addE(adj,i,j);
    m.ShortestDistance(adj, 0, n-1, n);
    t=0;
    d=m.p;
    cur=0;
    tmp1=0;
    tmp2=0;
    stop=0;
    d1=new ArrayList();
    d2=new ArrayList();
        new DIC();
    }
}
