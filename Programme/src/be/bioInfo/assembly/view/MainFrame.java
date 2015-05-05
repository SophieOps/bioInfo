package be.bioInfo.assembly.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import be.bioInfo.assembly.algorithm.ChainManager;
import be.bioInfo.assembly.algorithm.GreedyAlgo;
import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.graph.Edge;
import be.bioInfo.assembly.graph.Graph;
import be.bioInfo.assembly.graph.GraphManager;
import be.bioInfo.assembly.graph.Node;
import be.bioInfo.assembly.model.FragmentManager;

/**
 * Interface of the program
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class MainFrame extends JFrame implements ActionListener
{
	private JPanel pan = new JPanel();
	private JButton btOK = new JButton("Ok");
	private JButton btCancel = new JButton("Cancel");
	private JButton btFileChooser = new JButton("Parcourir");
	private JTextArea txtFileChooser = new JTextArea("Fichier à importer : ");
	private JTextArea txtComplementary = new JTextArea("Faut-il calculer les fragments complémentaires inversés ?");
	private JTextField filePath = new JTextField("");
	private JTextArea txtNumColl = new JTextArea("Quel est le numéro de la collection ?");
	private JTextField numColl = new JTextField("");
	private JRadioButton rbtMustComputeComplementary = new JRadioButton("Oui");
	private JRadioButton rbtComplementaryUnused = new JRadioButton("Non");
	//static private JProgressBar bar;
	private int result = -1;
	private JFileChooser fc;

	/**
	 * JFrame with the interface of the program
	 * is call once from the class Main.java
	 */
	public MainFrame()
	{
		this.setTitle("Algorithmique et bioinformatique");
		this.setSize(500, 200);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Nous ajoutons notre fenêtre à la liste des auditeurs de notre bouton
		btOK.addActionListener(this);
		btCancel.addActionListener(this);
		btFileChooser.addActionListener(this);

		//Gestion des radio boutton
		ButtonGroup group = new ButtonGroup();
		rbtMustComputeComplementary.setSelected(true);
		rbtMustComputeComplementary.setBackground(Color.WHITE);
		rbtComplementaryUnused.setBackground(Color.WHITE);
		group.add(rbtMustComputeComplementary);
		group.add(rbtComplementaryUnused);
		
//		bar  = new JProgressBar();
//	    bar.setMaximum(1000);
//	    bar.setMinimum(0);
//	    bar.setStringPainted(true);

		JPanel pan1bis = new JPanel();
		//On définit le layout en lui indiquant qu'il travaillera en ligne
		pan1bis.setLayout(new BoxLayout(pan1bis, BoxLayout.PAGE_AXIS));
		pan1bis.setBackground(Color.WHITE);
		pan1bis.add(txtFileChooser);
		pan1bis.add(filePath);
		JPanel pan1 = new JPanel();
		pan1.setLayout(new BoxLayout(pan1, BoxLayout.LINE_AXIS));
		pan1.setBackground(Color.WHITE);
		pan1.add(pan1bis);
		pan1.add(btFileChooser);

		JPanel pan2 = new JPanel();
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.LINE_AXIS));
		pan2.setBackground(Color.WHITE);
		pan2.add(txtComplementary);
		pan2.add(rbtComplementaryUnused);
		pan2.add(rbtMustComputeComplementary);

		JPanel pan3 = new JPanel();
		pan3.setLayout(new BoxLayout(pan3, BoxLayout.LINE_AXIS));
		pan3.setBackground(Color.WHITE);
		pan3.add(btOK);
		pan3.add(btCancel);
		
//		JPanel pan4 = new JPanel();
//		pan4.setLayout(new BoxLayout(pan4, BoxLayout.LINE_AXIS));
//		pan4.setBackground(Color.WHITE);
//		pan4.add(bar);
		
		JPanel pan5 = new JPanel();
		pan5.setLayout(new BoxLayout(pan5, BoxLayout.LINE_AXIS));
		pan5.setBackground(Color.WHITE);
		pan5.add(txtNumColl);
		pan5.add(numColl);

		//On positionne maintenant ces trois lignes en colonne
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		pan.setBackground(Color.WHITE);
		pan.add(pan1);
		pan.add(pan2);
		//pan.add(pan4);
		pan.add(pan5);
		pan.add(pan3);

		this.getContentPane().add(pan);
		this.setVisible(true);

		return;
	}
	
//	public static void setValueProgressBar(int value){
//		bar.setValue(value);
//	}
	//TODO : Faire de ceci un singloton pour ajouter une progressbar à l'interface
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btFileChooser){
			fc = new JFileChooser();
			result = fc.showOpenDialog(this);
			txtFileChooser.setText("Fichier choisi : ");
			filePath.setText(fc.getSelectedFile().getPath());
		}else if(evt.getSource() == btOK){
			boolean mustComputeComplementary = false;
			if(rbtMustComputeComplementary.isSelected()){
				mustComputeComplementary = true;
			}
			if (result == JFileChooser.APPROVE_OPTION) 
			{
				try{
					System.out.println("Lecture du fichier");
					ArrayList<Node> nodeList = FragmentManager.readFile(fc.getSelectedFile(), mustComputeComplementary);//booleen pour savoir si il faut calculer les compl�mentaires ou non
					System.out.println("Fin lecture du fichier");
					/*for(int i = 0; i < nodeList.size(); i++){
						System.out.println(nodeList.get(i).getData().getCode());
					}*/
					System.out.println("Construction du graphe");
					Graph graph = GraphManager.constructGraph(nodeList, mustComputeComplementary);
					System.out.println("Fin construction du graphe");

					/*for(int i = 0; i < graph.getEdgeList().size(); i++){
						System.out.println("Arc de "+ graph.getEdgeList().get(i).getSource().getData().getCode()+" � "+graph.getEdgeList().get(i).getDestination().getData().getCode()+" poids = "+graph.getEdgeList().get(i).getWeight());
					}*/
					System.out.println("Lancement Greedy");
					ArrayList<Edge> edgeList = GreedyAlgo.execute(graph, mustComputeComplementary);
					System.out.println("Fin de greedy");
					/*for(int i = 0; i < edgeList.size(); i++){
						System.out.println("Arc de la source " + edgeList.get(i).getSource().getId() + " : " + edgeList.get(i).getSource().getData().getCode());
						System.out.println("A la destination " + edgeList.get(i).getDestination().getId() + " : " + edgeList.get(i).getDestination().getData().getCode());
						System.out.println("De poids : "+edgeList.get(i).getWeight());
					}*/
					System.out.println("Construction super chaine");
					ChainManager.constructChain(edgeList, numColl.getText());	
					System.out.println("Fin construction super chaine");
				}catch(FragmentException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}catch (FileNotFoundException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Fichier introuvable", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}catch (GreedyException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur dans le Greedy", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}catch (Exception e){
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur inconnue", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				JOptionPane.showMessageDialog(null, "Traitement fini", "InfoBox: Traitement fini", JOptionPane.INFORMATION_MESSAGE);
			}
			System.exit(0);
		}else if (evt.getSource() == btCancel){
			System.exit(0);
		}
	}

}
