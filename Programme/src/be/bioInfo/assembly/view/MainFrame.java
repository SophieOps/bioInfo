package be.bioInfo.assembly.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
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
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class MainFrame extends JFrame implements ActionListener
{

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
					System.out.println("Lecture du fichier");
					ArrayList<Node> nodeList = FragmentManager.readFile(fc.getSelectedFile(), withcompl);//booleen pour savoir si il faut calculer les compl�mentaires ou non
					System.out.println("Fin lecture fichier");
					/*for(int i = 0; i < nodeList.size(); i++)
					{
						System.out.println(nodeList.get(i).getData().getCode());
					}*/
					System.out.println("Construction du graph");
					Graph graph = GraphManager.constructGraph(nodeList, withcompl);
					System.out.println("Fin construction du graph");

					/*for(int i = 0; i < graph.getEdgeList().size(); i++)
					{
						System.out.println("Arc de "+ graph.getEdgeList().get(i).getSource().getData().getCode()+" � "+graph.getEdgeList().get(i).getDestination().getData().getCode()+" poids = "+graph.getEdgeList().get(i).getWeight());
					}*/
					System.out.println("Lancement Greedy");
					ArrayList<Edge> edgeList = GreedyAlgo.execute(graph);
					System.out.println("Fin de greedy");
					/*for(int i = 0; i < edgeList.size(); i++)
					{
						System.out.println("Arc de la source " + edgeList.get(i).getSource().getId() + " : " + edgeList.get(i).getSource().getData().getCode());
						System.out.println("A la destination " + edgeList.get(i).getDestination().getId() + " : " + edgeList.get(i).getDestination().getData().getCode());
						System.out.println("De poids : "+edgeList.get(i).getWeight());
					}*/
					System.out.println("Construction super chaine");
					ChainManager.constructChain(edgeList);	
					System.out.println("Fin construction super chaine");
				}
				catch(FragmentException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
				} 
				catch (FileNotFoundException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Fichier introuvable", JOptionPane.ERROR_MESSAGE);
				} 
				catch (GreedyException e) 
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur dans le Greedy", JOptionPane.ERROR_MESSAGE);
				}
			}
			JOptionPane.showMessageDialog(null, "Résultat trouvé", "InfoBox: Traitement fini", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
	} 
}
