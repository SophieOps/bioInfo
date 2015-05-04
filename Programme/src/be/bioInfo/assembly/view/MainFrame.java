package be.bioInfo.assembly.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import be.bioInfo.assembly.algorithm.GreedyAlgo;
import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.model.ChainManager;
import be.bioInfo.assembly.model.Edge;
import be.bioInfo.assembly.model.Fragment;
import be.bioInfo.assembly.model.FragmentManager;
import be.bioInfo.assembly.model.Graph;
import be.bioInfo.assembly.model.GraphManager;
import be.bioInfo.assembly.model.Node;

/**
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class MainFrame extends JFrame implements ActionListener
{
	private FragmentManager fragmentManager;
	private GraphManager graphManager;
	private GreedyAlgo greedyAlgo;
	private ChainManager chainManager;

	private JPanel pan = new JPanel();
	private JButton btOK = new JButton("Ok");
	private JButton btFileChooser = new JButton("Parcourir");
	private JTextArea txtFileChooser = new JTextArea("Fichier à importer : ");
	private JTextArea txtComplementary = new JTextArea("Contient-il les fichiers complémentaires ?");
	private JRadioButton rbtwithCom = new JRadioButton("Oui");
	private JRadioButton rbtwithoutCom = new JRadioButton("Non");

	private int result;
	private JFileChooser fc;

	/**
	 * 
	 */
	public MainFrame()
	{
		fragmentManager = new FragmentManager();
		graphManager = new GraphManager();
		greedyAlgo = new GreedyAlgo();
		chainManager = new ChainManager();
		
		this.setTitle("Algorithmique et bioinformatique");
		this.setSize(400, 200);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		btOK.setPreferredSize(new Dimension(100, 40));
		btFileChooser.setPreferredSize(new Dimension(100, 40));
		txtFileChooser.setPreferredSize(new Dimension(100, 80));
		txtComplementary.setPreferredSize(new Dimension(100, 40));
		//Nous ajoutons notre fenêtre à la liste des auditeurs de notre bouton
		btOK.addActionListener(this);
		btFileChooser.addActionListener(this);

		//Gestion des radio boutton
		ButtonGroup group = new ButtonGroup();
		rbtwithCom.setSelected(true);
		group.add(rbtwithCom);
		group.add(rbtwithoutCom);

		JPanel pan1 = new JPanel();
		//On définit le layout en lui indiquant qu'il travaillera en ligne
		pan1.setLayout(new BoxLayout(pan1, BoxLayout.LINE_AXIS));
		pan1.add(txtFileChooser);
		pan1.add(btFileChooser);

		JPanel pan2 = new JPanel();
		//Idem pour cette ligne
		pan2.setLayout(new BoxLayout(pan2, BoxLayout.LINE_AXIS));
		pan2.add(txtComplementary);
		pan2.add(rbtwithCom);
		pan2.add(rbtwithoutCom);

		JPanel pan3 = new JPanel();
		//Idem pour cette ligne
		pan3.setLayout(new BoxLayout(pan3, BoxLayout.LINE_AXIS));
		pan3.add(btOK);

		//On positionne maintenant ces trois lignes en colonne
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		pan.add(pan1);
		pan.add(pan2);
		pan.add(pan3);

		this.getContentPane().add(pan);
		this.setVisible(true);


		return;
	}
	
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

	//Méthode qui sera appelée lors d'un clic sur le bouton
	public void actionPerformed(ActionEvent evt) {      
		//Lorsque l'on clique sur le bouton, on met à jour le JLabel
		if(evt.getSource() == btFileChooser){
			fc = new JFileChooser();
			result = fc.showOpenDialog(this);
			txtFileChooser.setText("Fichier choisi");
		}
		if(evt.getSource() == btOK){
			boolean withcompl = false;
			if(rbtwithCom.isSelected()){
				withcompl = true;
			}
			if (result == JFileChooser.APPROVE_OPTION) 
			{
				try
				{
					System.out.println("read file");

					ArrayList<Node> nodeList = fragmentManager.readFile(fc.getSelectedFile(), withcompl);


					/*for(int i = 0; i < nodeList.size(); i++)
					{
						System.out.println(nodeList.get(i).getData().getCode());
					}*/

					System.out.println("construct graph");
					Graph graph = graphManager.constructGraph(nodeList);

					/*for(int i = 0; i < graph.getEdgeList().size(); i++)
					{
						System.out.println("Arc de "+ graph.getEdgeList().get(i).getSource().getData().getCode()+" � "+graph.getEdgeList().get(i).getDestination().getData().getCode()+" poids = "+graph.getEdgeList().get(i).getWeight());
					}*/
					System.out.println("Greedy");
					ArrayList<Edge> edgeList = greedyAlgo.execute(graph);

					/*for(int i = 0; i < edgeList.size(); i++)
					{
						System.out.println("Arc de la source " + edgeList.get(i).getSource().getId() + " : " + edgeList.get(i).getSource().getData().getCode());
						System.out.println("A la destination " + edgeList.get(i).getDestination().getId() + " : " + edgeList.get(i).getDestination().getData().getCode());
						System.out.println("De poids : "+edgeList.get(i).getWeight());
					}*/
					System.out.println("construct chain");
					chainManager.constructChain(edgeList);
				}
				catch(FragmentException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
				} 
				catch (FileNotFoundException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
				} 
				catch (GreedyException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur dans le Greedy", JOptionPane.ERROR_MESSAGE);
				}
			}
			infoBox("Résultat trouvé", "Traitement fini");
			System.exit(0);
		}
	} 
}
