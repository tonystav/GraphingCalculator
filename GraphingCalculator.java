import com.badlogic.gdx.math.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.*;
import org.mariuszgromada.math.mxparser.Expression;

public class GraphingCalculator extends JPanel implements ItemListener {
	private static final long serialVersionUID = 1L;

	// Window sections
    private static JFrame gcFrame;
    private static JPanel graphPanel, formulaPanel;
    private static JTextArea formulaText = new JTextArea(1, 45);

    // Window parameters
    private static Integer screenSize = 1600, displaySize = screenSize/2, graphCenter = displaySize/2;
    private static Double targetValue = 0D;
    private static Graphics grphcs;
    private static Graphics2D graphics2d;
    private static Boolean solidLines = false;

	public GraphingCalculator() {
	    super(new BorderLayout());

	    gcFrame = new JFrame();
		gcFrame.setTitle("Graphing Calculator");
		gcFrame.setPreferredSize(new Dimension(screenSize+10, displaySize+33));
		gcFrame.setSize(screenSize+10, displaySize+33);
		gcFrame.setLayout(new BorderLayout());
		gcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gcFrame.add(createFormulaPanel(), BorderLayout.WEST);
		gcFrame.add(createGraphPanel(), BorderLayout.EAST);
		gcFrame.pack();
		gcFrame.setVisible(true);

		/*gcFrame.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				//clearGraphPanel();
				//graphFormula(formulaText.getText());
			}

			@Override
			public void windowGainedFocus(WindowEvent we) {
				clearGraphPanel();
				graphFormula(formulaText.getText());
				System.out.println("Got here");
				try { TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); };
			}
		});*/
	}

	/*@Override
	public void paint(Graphics g) {
		//super.paint(g);
		clearGraphPanel();
		graphFormula(formulaText.getText());
	}*/

	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		clearGraphPanel();
		graphFormula(formulaText.getText());
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		super.setIgnoreRepaint(true);
		clearGraphPanel();
		graphFormula(formulaText.getText());
	}

	public static JPanel createGraphPanel() {
		graphPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

		    @Override
	    	public void paint(Graphics g) {
		    	super.paint(g);
		    	Graphics2D graphics2d = (Graphics2D)g;

		    	graphics2d.setColor(Color.BLACK);
		    	graphics2d.drawLine(0, displaySize/2, displaySize, displaySize/2);	// Horizontal graph center line
		    	graphics2d.drawLine(displaySize/2, 0, displaySize/2, displaySize);	// Vertical graph center line
		    	//graphFormula(formulaText.getText());
		    }

		    /*@Override
	    	public void paintComponent(Graphics g) {
				super.paintComponent(g);
			}*/
	    };

		graphPanel.setPreferredSize(new Dimension(displaySize, displaySize));
	    graphPanel.setSize(displaySize+10, displaySize+40);
	    graphPanel.setBounds(0, 0, displaySize, displaySize);
	    graphPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    graphPanel.setBackground(Color.WHITE);
	    graphPanel.validate();
		graphPanel.repaint();

        return graphPanel;
	}

    public static JPanel createFormulaPanel() {
        formulaPanel = new JPanel();
        formulaPanel.setPreferredSize(new Dimension(displaySize, displaySize));
        formulaPanel.setSize(displaySize, displaySize);
        formulaPanel.setBounds(0, 0, displaySize, displaySize);
        formulaPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        formulaPanel.setBackground(Color.GRAY);
        formulaPanel.setLayout(new GridLayout(0, 1));

        JLabel groupTitle = new JLabel("Create Formula", JLabel.CENTER);
        groupTitle.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        formulaPanel.add(groupTitle);

        final JPanel formulaTextPanel = new JPanel();
        formulaTextPanel.setBackground(Color.GRAY);
        formulaText = new JTextArea(1, 45);
        formulaText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
        formulaText.setEditable(true);
        formulaTextPanel.add(formulaText);
        JButton keyClearFormula = new JButton();
        keyClearFormula.setText("Clear Formula Bar");
        keyClearFormula.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formulaText.setText(null);
			}
        });
        formulaTextPanel.add(keyClearFormula);
        formulaPanel.add(formulaTextPanel, BorderLayout.NORTH);

        JPanel keypadPanel04 = new JPanel();
        keypadPanel04.setBackground(Color.GRAY);
        JButton key0 = new JButton();
        setupButton(key0, keypadPanel04, "0", "0", 65, 25);
        JButton key1 = new JButton();
        setupButton(key1, keypadPanel04, "1", "1", 65, 25);
        JButton key2 = new JButton();
        setupButton(key2, keypadPanel04, "2", "2", 65, 25);
        JButton key3 = new JButton();
        setupButton(key3, keypadPanel04, "3", "3", 65, 25);
        JButton key4 = new JButton();
        setupButton(key4, keypadPanel04, "4", "4", 65, 25);
        formulaPanel.add(keypadPanel04);

        JPanel keypadPanel59 = new JPanel();
        keypadPanel59.setBackground(Color.GRAY);
        JButton key5 = new JButton();
        setupButton(key5, keypadPanel59, "5", "5", 65, 25);
        JButton key6 = new JButton();
        setupButton(key6, keypadPanel59, "6", "6", 65, 25);
        JButton key7 = new JButton();
        setupButton(key7, keypadPanel59, "7", "7", 65, 25);
        JButton key8 = new JButton();
        setupButton(key8, keypadPanel59, "8", "8", 65, 25);
        JButton key9 = new JButton();
        setupButton(key9, keypadPanel59, "9", "9", 65, 25);
        formulaPanel.add(keypadPanel59);

        JPanel keypadPanelArith1 = new JPanel();
        keypadPanelArith1.setBackground(Color.GRAY);
        JButton keyAdd = new JButton();
        setupButton(keyAdd, keypadPanelArith1, "+", "+", 65, 25);
        JButton keySubtract = new JButton();
        setupButton(keySubtract, keypadPanelArith1, "-", "-", 65, 25);
        JButton keyMultiply = new JButton();
        setupButton(keyMultiply, keypadPanelArith1, "*", "*", 65, 25);
        JButton keyDivide = new JButton();
        setupButton(keyDivide, keypadPanelArith1, "/", "/", 65, 25);
        JButton keyExponent = new JButton();
        setupButton(keyExponent, keypadPanelArith1, "^", "^", 65, 25);
        formulaPanel.add(keypadPanelArith1);

        JPanel keypadPanelArith2 = new JPanel();
        keypadPanelArith2.setBackground(Color.GRAY);
        JButton keyPeriod = new JButton();
        setupButton(keyPeriod, keypadPanelArith2, ".", ".", 65, 25);
        JButton keySemicolon = new JButton();
        setupButton(keySemicolon, keypadPanelArith2, ";", ";", 65, 25);
        JButton keyLP = new JButton();
        setupButton(keyLP, keypadPanelArith2, "(", "(", 65, 25);
        JButton keyRP = new JButton();
        setupButton(keyRP, keypadPanelArith2, ")", ")", 65, 25);
        JButton keyEquals = new JButton();
        setupButton(keyEquals, keypadPanelArith2, "=", "=", 65, 25);
        formulaPanel.add(keypadPanelArith2);

        JPanel keypadPanelFnctn0 = new JPanel();
        keypadPanelFnctn0.setBackground(Color.GRAY);
        JButton keyX = new JButton();
        setupButton(keyX, keypadPanelFnctn0, "x", "x", 65, 25);
        JButton keyY = new JButton();
        setupButton(keyY, keypadPanelFnctn0, "y", "y", 65, 25);
        JButton keyZ = new JButton();
        setupButton(keyZ, keypadPanelFnctn0, "z", "z", 65, 25);
        JButton keyR = new JButton();
        setupButton(keyR, keypadPanelFnctn0, "r", "r", 65, 25);
        JButton keyE = new JButton();
        setupButton(keyE, keypadPanelFnctn0, "e", "e", 65, 25);
        formulaPanel.add(keypadPanelFnctn0);

        JPanel keypadPanelFnctn1 = new JPanel();
        keypadPanelFnctn1.setBackground(Color.GRAY);
        JButton keyTheta = new JButton();
        setupButton(keyTheta, keypadPanelFnctn1, "θ", "theta", 65, 25);
        JButton keyPi = new JButton();
        setupButton(keyPi, keypadPanelFnctn1, "π", "pi", 65, 25);
        JButton keyPhi = new JButton();
        setupButton(keyPhi, keypadPanelFnctn1, "φ", "[phi]", 65, 25);
        JButton keyLn = new JButton();
        setupButton(keyLn, keypadPanelFnctn1, "ln", "ln", 65, 25);
        JButton keyLog10 = new JButton();
        setupButton(keyLog10, keypadPanelFnctn1, "log10", "log10", 65, 25);
        formulaPanel.add(keypadPanelFnctn1);

        JPanel keypadPanelFnctn2 = new JPanel();
        keypadPanelFnctn2.setBackground(Color.GRAY);
        JButton keyMod = new JButton();
        setupButton(keyMod, keypadPanelFnctn2, "mod", "mod", 65, 25);
        JButton keyAbs = new JButton();
        setupButton(keyAbs, keypadPanelFnctn2, "abs", "abs", 65, 25);
        JButton keySgn = new JButton();
        setupButton(keySgn, keypadPanelFnctn2, "sgn", "sgn", 65, 25);
        JButton keyFloor = new JButton();
        setupButton(keyFloor, keypadPanelFnctn2, "floor", "floor", 65, 25);
        JButton keyCeil = new JButton();
        setupButton(keyCeil, keypadPanelFnctn2, "ceil", "ceil", 65, 25);
        formulaPanel.add(keypadPanelFnctn2);

        JPanel keypadPanelFnctn3 = new JPanel();
        keypadPanelFnctn3.setBackground(Color.GRAY);
        JButton keyFctrl = new JButton();
        setupButton(keyFctrl, keypadPanelFnctn3, "!", "!", 65, 25);
        JButton keyGamma = new JButton();
        setupButton(keyGamma, keypadPanelFnctn3, "! (Gamma)", "Gamma", 95, 25);
        formulaPanel.add(keypadPanelFnctn3);

        JPanel keypadPanelTrig1 = new JPanel();
        keypadPanelTrig1.setBackground(Color.GRAY);
        JButton keySin = new JButton();
        setupButton(keySin, keypadPanelTrig1, "sin", "sin", 70, 25);
        JButton keyCos = new JButton();
        setupButton(keyCos, keypadPanelTrig1, "cos", "cos", 70, 25);
        JButton keyTan = new JButton();
        setupButton(keyTan, keypadPanelTrig1, "tan", "tan", 70, 25);
        JButton keyCsc = new JButton();
        setupButton(keyCsc, keypadPanelTrig1, "csc", "csc", 70, 25);
        JButton keySec = new JButton();
        setupButton(keySec, keypadPanelTrig1, "sec", "sec", 70, 25);
        JButton keyCot = new JButton();
        setupButton(keyCot, keypadPanelTrig1, "cot", "cot", 70, 25);
        formulaPanel.add(keypadPanelTrig1);


        JPanel keypadPanelTrig2 = new JPanel();
        keypadPanelTrig2.setBackground(Color.GRAY);
        JButton keyASin = new JButton();
        setupButton(keyASin, keypadPanelTrig2, "asin", "asin", 70, 25);
        JButton keyACos = new JButton();
        setupButton(keyACos, keypadPanelTrig2, "acos", "acos", 70, 25);
        JButton keyATan = new JButton();
        setupButton(keyATan, keypadPanelTrig2, "atan", "atan", 70, 25);
        JButton keyACsc = new JButton();
        setupButton(keyACsc, keypadPanelTrig2, "acsc", "acsc", 70, 25);
        JButton keyASec = new JButton();
        setupButton(keyASec, keypadPanelTrig2, "asec", "asec", 70, 25);
        JButton keyACot = new JButton();
        setupButton(keyACot, keypadPanelTrig2, "acot", "acot", 70, 25);
        formulaPanel.add(keypadPanelTrig2);

        JPanel keypadPanelTrig3 = new JPanel();
        keypadPanelTrig3.setBackground(Color.GRAY);
        JButton keySinh = new JButton();
        setupButton(keySinh, keypadPanelTrig3, "sinh", "sinh", 70, 25);
        JButton keyCosh = new JButton();
        setupButton(keyCosh, keypadPanelTrig3, "cosh", "cosh", 70, 25);
        JButton keyTanh = new JButton();
        setupButton(keyTanh, keypadPanelTrig3, "tanh", "tanh", 70, 25);
        JButton keyCsch = new JButton();
        setupButton(keyCsch, keypadPanelTrig3, "csch", "csch", 70, 25);
        JButton keySech = new JButton();
        setupButton(keySech, keypadPanelTrig3, "sech", "sech", 70, 25);
        JButton keyCoth = new JButton();
        setupButton(keyCoth, keypadPanelTrig3, "coth", "coth", 70, 25);
        formulaPanel.add(keypadPanelTrig3);


        JPanel keypadPanelTrig4 = new JPanel();
        keypadPanelTrig4.setBackground(Color.GRAY);
        JButton keyASinh = new JButton();
        setupButton(keyASinh, keypadPanelTrig4, "asinh", "asinh", 70, 25);
        JButton keyACosh = new JButton();
        setupButton(keyACosh, keypadPanelTrig4, "acosh", "acosh", 70, 25);
        JButton keyATanh = new JButton();
        setupButton(keyATanh, keypadPanelTrig4, "atanh", "atanh", 70, 25);
        JButton keyACsch = new JButton();
        setupButton(keyACsch, keypadPanelTrig4, "acsch", "acsch", 70, 25);
        JButton keyASech = new JButton();
        setupButton(keyASech, keypadPanelTrig4, "asech", "asech", 70, 25);
        JButton keyACoth = new JButton();
        setupButton(keyACoth, keypadPanelTrig4, "acoth", "acoth", 70, 25);
        formulaPanel.add(keypadPanelTrig4);

        JPanel keypadPanelCntrl1 = new JPanel();
        keypadPanelCntrl1.setBackground(Color.GRAY);

        JCheckBox solid = new JCheckBox("Solid Lines");
        solid.setSelected(false);
        solid.setBackground(Color.GRAY);
        solid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox sldLnsCB = (JCheckBox) e.getSource();

				if (sldLnsCB.isSelected()) { solidLines = true; }
				else { solidLines = false; }

				clearGraphPanel();
				graphFormula(formulaText.getText());
			}
        });
        keypadPanelCntrl1.add(solid);

        JButton keyGraph = new JButton();
        keyGraph.setText("Graph Formula");
        keyGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphFormula(formulaText.getText());
			}
        });
        keypadPanelCntrl1.add(keyGraph);
        JButton keyClearGraph = new JButton();
        keyClearGraph.setText("Clear Graph");
        keyClearGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearGraphPanel();
			}
        });
        keypadPanelCntrl1.add(keyClearGraph);
        formulaPanel.add(keypadPanelCntrl1);

        JPanel fillerPanel = new JPanel();
        fillerPanel.setBackground(Color.GRAY);
        formulaPanel.add(fillerPanel, BorderLayout.SOUTH);

        return formulaPanel;
    }

    private static void graphFormula(String frml) {
		grphcs = graphPanel.getGraphics();
		grphcs.setColor(Color.RED);
		graphics2d = (Graphics2D)grphcs;
		graphics2d.setStroke(new BasicStroke(2));

		// Perform basic check for equal number of open & close parentheses
		/*if (StringUtils.countMatches(frml, "(") != StringUtils.countMatches(frml, ")")) {
			popupErrorMessage("Unequal number of open and close parentheses.");
		}
		else {*/
		if (noInputErrors(frml)) {
			// Use regular expressions to parse exponents properly
			String frmlParsed = parseExponents(frml);

			// Determine what type of equation was entered
			// Polar coordinates: 'r=f(θ)'
			if ((StringUtils.countMatches(frmlParsed.trim(), "r=") > 0) || (StringUtils.countMatches(frmlParsed.trim(), "=r") > 0)) {
				graphPolarEquation(frmlParsed, graphics2d);
			}
			// 2-parameters: x AND y
			else if ((StringUtils.countMatches(frmlParsed, "x") > 0) && (StringUtils.countMatches(frmlParsed, "y") > 0)) {
				// Parametric: x=f1(z) AND y=f2(z)
				if (StringUtils.countMatches(frmlParsed, ";") > 0) {
					graphParametericEquations(frmlParsed, graphics2d);
				}
				// Cartesian: z=f(x,y)
				else {
					graphCartesianFunction(graphics2d, frmlParsed);
				}
			}
			// 1-parameter x OR y
			else {
				// Plain straight line: x=n OR y=n
				if ((StringUtils.countMatches(frmlParsed, "x=") > 0)
				|| (StringUtils.countMatches(frmlParsed, "=x") > 0)
				|| (StringUtils.countMatches(frmlParsed, "y=") > 0)
				|| (StringUtils.countMatches(frmlParsed, "=y") > 0)) {
					graphStraightLine(frmlParsed, graphics2d);
				}
				// 1-parameter linear: 'y=f(x)' OR 'x=f(y)'
				else {
					graph1ParameterEquation(frmlParsed, graphics2d);
				}
			}
		}
    }

    private static void graphCartesianFunction(Graphics2D grphcs2D, String function) {
		String frmNoEquals = "";
		Integer gridSize = graphCenter+1; // Accommodates quadtree total but maps plot storage to graph size
		QTGrid qtG = new QTGrid(gridSize);

		long startTime = System.currentTimeMillis();

		// String processing on equation occurs outside of Cartesian processing because quadtree algorithm is recursive
		if (StringUtils.isNumeric(StringUtils.substringBefore(function, "="))) {
			frmNoEquals = "(" + StringUtils.substringAfter(function, "=") + ") - (" + StringUtils.substringBefore(function, "=") + ")";
		}
		else if (StringUtils.isNumeric(StringUtils.substringAfter(function, "="))) {
			frmNoEquals = "(" + StringUtils.substringBefore(function, "=") + ") - (" + StringUtils.substringAfter(function, "=") + ")";
		}
		else {
			frmNoEquals = "(" + StringUtils.substringBefore(function, "=") + ") - (" + StringUtils.substringAfter(function, "=") + ")";
		}

		// 1. Use quadtree-style algorithm to populate array list of 2d coordinates & related equation results
		quadtreeStylePlot(graphics2d, frmNoEquals, 0, displaySize, displaySize, qtG);

		// 2. Use marching squares algorithm to create graph from array list
		marchingSquares(graphics2d, qtG);

		long endTime = System.currentTimeMillis();
		System.out.println("graphCartesianFunction & marchingSquares took " + (endTime - startTime) + " milliseconds");
    }

    // Use quadtree-style approach: Start with level 0, calculate function result based on coordinates provided,
    // graph function at coordinates if result is inside given threshold, subdivide screen into quadrants,
    // calculate new coordinates & next level, then call same method with new parameters for each quadrant (recursion)
    private static void quadtreeStylePlot(Graphics2D grphcs2D, String function, int level, int xCoordinate, int yCoordinate, QTGrid qtG) {
		String frmlRplc = "";
		int nextLevel = 0, levelLimit = 7, newDivisor = 0, newAmount = 0, ulx =0, uly=0, urx=0, ury=0, llx=0, lly=0, lrx=0, lry=0;
		double x=0D, y=0D, resultA = 0D;
		Expression expression;
		//System.out.println("level: " + level + ", xCoordinate: " + xCoordinate + ", yCoordinate: " + yCoordinate);

		// Cartesian coordinates: test cases
		// (((x^2) + (y^2) - 2)^3) / (x^2) = 4 // Horizontal Nephroid
		// (((x^2) + (y^2) - 2)^3) / (y^(2)) = 4 // Vertical Nephroid
		// (((x^2) + (y^2))^2) = 4 * ((x^2) - (y^2)) // Horizontal Lemniscate
		// (((x^2) + (y^2))^2) = 4 * ((y^2) - (x^2)) // Vertical Lemniscate
		// (y^2) - (x^2) = 5 // Horizontal Hyperbola
		// (x^2) - (y^2) = 5 // Vertical Hyperbola
		// (x^2) + (y^2) = 20 // Circle
		// ((x^2) ^ 4) + ((y^2) ^ 4) = 150 // Rectircle (round-cornered square)
		// abs(x+y) + abs(x-y) = 10 // Square
		// abs(abs(x)-abs(y)) = 2 // X sign / 2-directional v shapes (corners pointing inward)
		// abs(abs(x + y) - abs(x - y)) = 2 // Plus sign / Inverted square (corners pointing inward)
		// abs(x+y) * abs(x-y) = 2 // 2-directional hyperbolic shapes
		// sin((x^3)*(y^2)) - cos((x^2)*(y^3)) = 0 // Inverted quartered spiderweb
		// (x^2)*(y^3) - (x^4)*y = y // Quartered hyperbolic curves pointing towards origin
		// (y^2)*((y^2) - 4) = (x^2)*((x^2) - 9) // "devilish" curve
		// (x^4)*((x^2) + (y^2)) - (3*(x^2) - 2)^2 = 0 // Atriphtaloid
		// 144*((x^4) + (y^4)) - 225*((x^2) + (y^2)) + 350*(x^2)*(y^2) + 81 = 0 // Trott curve
		// ((x^2) - 4) * ((x - 2)^2) + (((y^2) - 4)^2) = 0 // Bicuspid curve
		// ((y^2) - (x^2)) * (x - 1) * (2*x - 3) = 4 * (((x^2) + (y^2) - (2 * x))^2) // Ampersand curve
		// x*y*((x^2)-(y^2)) = (x^2)+(y^2) // Maltese cross curve
		// (y^2)-(x*((x^4)-1)) = 0 // Burnside curve
		// (((x^2)-1)^2) = ((y^2)*(y-1)*(y-2)*(y+5)) // Stirrup curve
		// Left/right half only
		// (x^4)+(y^4) = +-(3*x*(y^2)) // Bifoliate
		// ((x^2)+(y^2))*(((x^2)+(y^2)+2*x)^2)-(9*((x^2)-(y^2))^2) = 0 // Scarabaeus curve
		// y=x*cot(pi*x/.5) // Hippias' quadratic
		// y=x*cot(x/.25) // Dinostratus quadratix
		// (x^8)+(4*x^7)+(4*x^6*y^2)+(6*x^6)+(12*x^5*y^2)+(6*x^5)+(6*x^4*y^4)+(14*x^4*y^2)+(5*x^4)+(12*x^3*y^4)+(4*x^3*y^2)
		// +(2*x^3)+(4*x^2*y^6)+(10*x^2*y^4)+(2*x^2*y^2)+(x^2)+(4*x*y^6)-(2*x*y^4)+(2*x*y^2)+(y^8)+(2*y^6)-(3*y^4)+(y^2)-(9) = 0 // Pear curve
		// Upper/lower half only
		// (y^2)*(4-(x^2)) = +-(((x^2) + 4*y - 4)^2) // Bicorn curve
		// 4*(x^2)*((x^2)+(y^2)) - 8*(2*y - (x^2) - (y^2))^2 = 0 // Capricornoid
		// Single quadrant only
		// (x^y) = (y^x)
		// cot(x^y)=tan(y^x)
		// Diagonally symmetric
		// (x^3)+(y^3)=3*2*x*y // Folium of Descartes
		// ((x-1)^2) * ((x^2)+(y^2)) = 9*(x^2) // Nicomedes' Conchoid
		// y^2 * (1+x) = x^2 * (3-x) // Maclaurin's Trisectrix
		// abs(Gamma(y))=abs(Gamma(x)) // "Leaning Flower"
		// (4*x^3)+(9*x*y^2)-(9*y^3)-(36*x)+(36*y) = 0 // "Almost yin/yang"
		// x^2*y = (y-1)^3 // Cubical hyperbola
		// Asymmetric
		// ((x^3)*y) + ((y^3)*2) + (8*x) = 0 // Klein Quartic
		// (2*(y^2)*((x^2)+(y^2))) - (2*(y^2)*(x+y)) + ((-26)*(y^2)) - (9*(x^2)) + (18*(x+y)) + 72 = 0 // Durer's Conchoid
		// y*((x^2)+(y^2)) = 2*((x^2)-(y^2)) + 6*x*y // Strophoid
		// x*y + x^3 + x^2 + x = .5 // Trident curve
		// 0 = x^2*y + .1*x^5 - y^2 // Keratoid cusp

		// Use this to slow down plotting enough to see it while it happens
		//try { TimeUnit.MILLISECONDS.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }

		// NOTE: May need to use "Math.abs" here, together with "+-" -> "-" replacement.
		/*x = Math.abs(xCoordinate - graphCenter) / 100D;
		y = Math.abs(graphCenter - yCoordinate) / 100D;*/
		x = (xCoordinate - displaySize) / 100D;
		y = (displaySize - yCoordinate) / 100D;

		// Need 2 versions of replacement equation so as to compensate for any functions included in equation
		if (containsFunction(function)) {
			frmlRplc = function.replaceAll("x", String.valueOf(x)).replaceAll("y", String.valueOf(y))
							.replaceAll(" ",  "").replaceAll("--", "-").replaceAll("\\+-", "\\+");
		}
		else {
			frmlRplc = function.replaceAll("x", String.valueOf(x)).replaceAll("y", String.valueOf(y))
					.replaceAll(" ",  "").replaceAll("--", "-").replaceAll("\\+-", "\\-");
		}
		expression = new Expression(frmlRplc);
		resultA = expression.calculate();
		//System.out.println("point:: level : " + level + ", xCoordinate: " + xCoordinate + ", yCoordinate: " + yCoordinate + ", x: " + x + ", y: " + y + ", frmlRplc: " + frmlRplc + ", resultA: " + resultA);

		if ((0 >= resultA) && (Double.POSITIVE_INFINITY != resultA) && (Double.NEGATIVE_INFINITY != resultA)) {
			//System.out.println("graphing:: level : " + level + ", xCoordinate: " + xCoordinate + ", yCoordinate: " + yCoordinate + ", x: " + x + ", y: " + y + ", frmlRplc: " + frmlRplc + ", resultA: " + resultA);

			// Store only points inside the curve perimeter: may not need anything else to graph curve
			if (levelLimit == level) { // Maintains most of plot precision but accommodates plot storage as well
				//qtG[xCoordinate/4][yCoordinate/4] = (new QTContent(xCoordinate, yCoordinate, resultA));
				//grphcs2D.fillRect((int) (xCoordinate), (int) (yCoordinate), 2, 2);
				// NOTE: This shrinks & centerizes plot, but need to expand internal grid in order to expand plot
				grphcs2D.fillRect((int) (xCoordinate/2), (int) (yCoordinate/2), 2, 2);
			}
		}

		if (level < levelLimit) {
			// Calculate new values
			nextLevel = level + 1;
			newDivisor = (int) Math.pow(2D, nextLevel);
			newAmount = displaySize / newDivisor;
			//System.out.println("nextLevel: " + nextLevel + ", newDivisor: " + newDivisor + ", newAmount: " + newAmount);

			// Process upper left quadrant
			ulx = xCoordinate - newAmount; uly = yCoordinate - newAmount;
			quadtreeStylePlot(grphcs2D, function, nextLevel, ulx, uly, qtG);
			// Process upper right quadrant
			urx = xCoordinate + newAmount; ury = yCoordinate - newAmount;
			quadtreeStylePlot(grphcs2D, function, nextLevel, urx, ury, qtG);
			// Process lower left quadrant
			llx = xCoordinate - newAmount; lly = yCoordinate + newAmount;
			quadtreeStylePlot(grphcs2D, function, nextLevel, llx, lly, qtG);
			// Process lower right quadrant
			lrx = xCoordinate + newAmount; lry = yCoordinate + newAmount;
			quadtreeStylePlot(grphcs2D, function, nextLevel, lrx, lry, qtG);
		}

		// Store all visited points: may need everything so as to find curve perimeter
		//if ((0 >= resultA) && (Double.POSITIVE_INFINITY != resultA) && (Double.NEGATIVE_INFINITY != resultA)) {
			if (levelLimit == level) {
				//qtG[xCoordinate/4][yCoordinate/4] = (new QTContent(xCoordinate, yCoordinate, resultA));
				// NOTE: This shrinks & centerizes plot, but need to expand internal grid in order to expand plot
				//qtG[xCoordinate/8][yCoordinate/8] = (new QTContent((xCoordinate/2), (yCoordinate/2), resultA));
				qtG.setGridElement(xCoordinate/8, yCoordinate/8, xCoordinate/2, yCoordinate/2, resultA);
				//System.out.println("level: " + level + ", qtG:: x: " + xCoordinate/4 + ", y: " + yCoordinate/4 + ", QContent:: x: " + xCoordinate + ", y: " + yCoordinate);
			}
		//}
    }

    private static void marchingSquares(Graphics2D grphcs2D, QTGrid qtG) {
    	QTContent nextQTC = new QTContent(0, 0, 0D);
    	StringBuilder nextLine = new StringBuilder();
    	int xBetween = 0, yBetween = 0;
    	// Isogrid, which is 1 element smaller than screen grid. Each isogrid element is at center of 4 contiguous screen grid elements,
    	// and holds 1 of 16 values, computed based on result value of each surrounding screen grid element. (ex. if only bottom right's
    	// value >= 0 then isogrid value = 2, if only bottom right's value < 0 then isogrid value = 13)
    	Integer[][] msGrid = new Integer[graphCenter-1][graphCenter-1];

    	// Initialize isogrid
		for (int row = 0; row < msGrid.length; row++) {
		    for (int col = 0; col < msGrid.length; col++) {
		    	msGrid[row][col] = 0;
		    }
		}

		// Simple nested loop to test if values are being transferred and used properly.
    	// See if can use quadtree-style approach here also (maybe not?)
    	for (int i = 0; i < 200; i++) {
    		for (int j = 0; j < 200; j++) {
    			//nextQTC = qtG[j][i];
    			nextQTC = qtG.getGridElement(j, i);
        		// This proves that the grid is available & readable: it simply plots the same image as created previously
    			if ((null != nextQTC) && (0>= nextQTC.getResult())) {
    				grphcs2D.fillRect((int) (nextQTC.getX()), (int) (nextQTC.getY()), 2, 2);

    				nextLine.append('X');
    			}
    			else { nextLine.append(' '); }
    		}

			System.out.println(nextLine);
			nextLine.setLength(0);
    	}

		// NOTE: need to increase size of squares being tested,because if test each individual dot then algorithm draws lines inside figures
		// Use lowest level of points alone: produces more regular grid arrangement; using all points produces quincunx of 5 dots.
    	// Step 1: Determine each isogrid element's value by checking each set of surrounding screen grid elements' values.
		// Start from upper left, then move clockwise through upper right, then lower right, finally to lower left.
		// Original nested loop
		for (int iRow = 0; iRow < graphCenter-3; iRow++) {
    		for (int iCol = 0; iCol < graphCenter-3; iCol++) {
    			try {
	    			/*if ((null != qtG[iRow][iCol].getResult())		&& (0 > qtG[iRow][iCol].getResult()))		{ msGrid[iRow][iCol] += 8; }	// Upper left screen grid element
	    			if ((null != qtG[iRow][iCol+3].getResult())		&& (0 > qtG[iRow][iCol+3].getResult()))		{ msGrid[iRow][iCol] += 4; }	// Upper right screen grid element
	    			if ((null != qtG[iRow+3][iCol+3].getResult())	&& (0 > qtG[iRow+3][iCol+3].getResult()))	{ msGrid[iRow][iCol] += 2; }	// Lower right screen grid element
	    			if ((null != qtG[iRow+3][iCol].getResult())		&& (0 > qtG[iRow+3][iCol].getResult()))		{ msGrid[iRow][iCol] += 1; }	// Lower left screen grid element
	    			*/
	    			if ((null != qtG.getGridElement(iRow, iCol).getResult())		&& (0 > qtG.getGridElement(iRow, iCol).getResult()))		{ msGrid[iRow][iCol] += 8; }	// Upper left screen grid element
	    			if ((null != qtG.getGridElement(iRow, iCol+3).getResult())		&& (0 > qtG.getGridElement(iRow, iCol+3).getResult()))		{ msGrid[iRow][iCol] += 4; }	// Upper right screen grid element
	    			if ((null != qtG.getGridElement(iRow+3, iCol+3).getResult())	&& (0 > qtG.getGridElement(iRow+3, iCol+3).getResult()))	{ msGrid[iRow][iCol] += 2; }	// Lower right screen grid element
	    			if ((null != qtG.getGridElement(iRow+3, iCol).getResult())		&& (0 > qtG.getGridElement(iRow+3, iCol).getResult()))		{ msGrid[iRow][iCol] += 1; }	// Lower left screen grid element
	    			
	    		}
    			catch (NullPointerException npe) {
    				continue;	// Should be able to ignore nulls
    			}
    			if (0 < msGrid[iRow][iCol]) {System.out.println("msGrid[" + iRow + "][" + iCol + "]: " + msGrid[iRow][iCol]);}
	    	}
		}

		// Step 2: Match each isogrid element's value with appropriate case value, then draw the correct lines.
    	// NOTE: Instead of 1 line between screen points, check if can add extra point between them for more
    	// precision. Need to determine new point location: usually not on same straight line between start & end.
    	for (int iRow = 0; iRow < graphCenter-1; iRow++) {
    		for (int iCol = 0; iCol < graphCenter-1; iCol++) {
    			try {
	    			switch (msGrid[iCol][iRow]) {
						case 0 :	break;	// All squares empty, so do nothing
						case 1 :	// Lower left square filled, so draw from lower left top edge center to lower left right edge center
									/*xBetween = Math.abs(qtG[iCol][iRow].getX() - qtG[iCol+1][iRow+1].getX()); yBetween = Math.abs(qtG[iCol][iRow].getY() - qtG[iCol+1][iRow+1].getY());
									grphcs2D.drawLine(qtG[iCol][iRow].getX(), qtG[iCol][iRow].getY(), qtG[iCol][iRow].getX()+xBetween, qtG[iCol][iRow].getY()+yBetween);
									grphcs2D.drawLine(qtG[iCol][iRow].getX()+xBetween, qtG[iCol][iRow].getY()+yBetween, qtG[iCol+1][iRow+1].getX(), qtG[iCol+1][iRow+1].getY());*/
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x1.5, y1.5);
									//grphcs2D.drawLine(x1.5, y1.5, x2, y2);
									break;
						case 2 :	// Lower right square filled, so draw from lower right top edge center to lower right left edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 3 :	// Both lower squares filled, so draw from lower left top edge center to lower right top edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 4 :	// Upper right square filled, so draw from upper right left edge center to upper right bottom edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 5 :	// Upper right & lower left filled, which is ambiguous, so need to check if squares somehow connect
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 6 :	// Both right squares filled, so draw from upper right left edge center to lower right left edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 7 :	// Upper left square empty, so draw from upper right left edge center to lower left top edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 8 :	// Upper left square filled, so draw from upper left bottom edge center to upper left right edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 9 :	// Both left squares filled, so draw from upper left right edge center to lower left right edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 10 :	// Upper left & lower right filled, which is ambiguous, so need to check if squares somehow connect
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 11 :	// Upper right square empty, so draw from upper left right edge center to lower right top edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 12 :	// Both upper squares filled, so draw from upper left bottom edge center to upper right bottom edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 13 :	// Lower right square empty, so draw from lower left right edge center to upper right left edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow+1).getX()+2, qtG.getGridElement(iCol, iRow+1).getY()+2, qtG.getGridElement(iCol+1, iRow).getX()+2, qtG.getGridElement(iCol+1, iRow).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 14 :	// Lower left square empty, so draw from upper left bottom edge center to lower right left edge center
									grphcs2D.drawLine(qtG.getGridElement(iCol, iRow).getX()+2, qtG.getGridElement(iCol, iRow).getY()+2, qtG.getGridElement(iCol+1, iRow+1).getX()+2, qtG.getGridElement(iCol+1, iRow+1).getY()+2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									//grphcs2D.drawLine(x1, y1, x2, y2);
									break;
						case 15 :	break;	// All squares filled, so do nothing
	    			}
	    		}
    			catch (NullPointerException npe) {
    				continue;	// Should be able to ignore nulls
    			}
    		}
    	}
    }

    private static void graphStraightLine(String frml, Graphics2D grphcs2D) {
    	String frmlRplc = frml.replaceAll("x=", "").replaceAll("=x", "").replaceAll("y=", "").replaceAll("=y", "").replaceAll("--", "");
		Expression expression = new Expression(frmlRplc);
		int result = (int) expression.calculate() * 50;

    	if (frml.contains("x")) {
    		grphcs2D.drawLine((graphCenter) + result, 0, (graphCenter)  + result, displaySize);
    	}
    	else {
    		grphcs2D.drawLine(0, (graphCenter) - result, displaySize, (graphCenter) - result);
    	}
    }

    private static void graph1ParameterEquation(String frml, Graphics2D grphcs2D) {
		String frmlRplc = "";
		int resultX = 0, resultY = 0, prvsX = 0, prvsY = 0;
		Expression expression;

    	// 1-parameter equation: 'y=f(x)', 'x=f(y)'
		// +/-(1-(x^2))^.5 // Circle
		// sgn(tan(x)-cot(x)) // 2 parallel dashed lines

		for (float i=-graphCenter*10; i<=graphCenter*10; i++) {
			float fi = i / 100;

			// Vertical equation: f(y)
			if (frml.contains("y")) {
				// Need multiplier on 'fi', scaled to circle radius, to calibrate size with polar & parametric versions of circle
				resultY = (int) (graphCenter + (fi*50));

				if (frml.replaceAll(" ", "").contains("^-")) {
					// Need multiplier appended to 'frmlRplc, scaled to circle radius, to calibrate size with polar & parametric versions of circle
					frmlRplc = "(" + frml.replaceAll("y", String.valueOf(fi)) + ") * 500";
				}
				else {
					// Need multiplier appended to 'frmlRplc, scaled to circle radius, to calibrate size with polar & parametric versions of circle
					frmlRplc = "(" + frml.replaceAll("y", String.valueOf(fi)) + ") * 50";
				}

				frmlRplc = frmlRplc.replaceAll("--", "");
				expression = new Expression(frmlRplc);
				resultX = (int) ((graphCenter) + expression.calculate());
			}
			// Horizontal equation: f(x)
			else {
				// Need multiplier on 'fi', scaled to circle radius, to calibrate size with polar & parametric versions of circle
				resultX = (int) (graphCenter + (fi*50));

				if (frml.replaceAll(" ", "").contains("^-")) {
					// Need multiplier appended to 'frmlRplc, scaled to circle radius, to calibrate size with polar & parametric versions of circle
					frmlRplc = "(" + frml.replaceAll("x", String.valueOf(fi)) + ") * 500";
				}
				else {
					// Need multiplier appended to 'frmlRplc, scaled to circle radius, to calibrate size with polar & parametric versions of circle
					frmlRplc = "(" + frml.replaceAll("x", String.valueOf(fi)) + ") * 50";
				}

				frmlRplc = frmlRplc.replaceAll("--", "");
				expression = new Expression(frmlRplc);
				resultY = (int) ((graphCenter) - expression.calculate());
			}

			grphcs2D.fillRect(resultX, resultY, 1, 1);

			// Interpolate extra point between basic points
			grphcs2D.fillRect((resultX - prvsX), (resultY - prvsY), 1, 1);

			// Connect any gaps. Check test conditions either too lenient or too restrictive
			if (solidLines) {
				if ((prvsX != 0 && prvsY != 0) && (Math.abs(resultY - prvsY) < displaySize) && (Math.abs(resultX - prvsX) < displaySize)) {
					grphcs2D.drawLine((int) (prvsX), (int) (prvsY), (int) (resultX), (int) (resultY));
				}

				prvsX = resultX; prvsY = resultY;
			}
		}
    }

    private static void graphPolarEquation(String frml, Graphics2D grphcs2D) {
		String frmlNoEquals = "", frmlRplc = "";
		Double resultX = 0D, resultY = 0D, resultA = 0D, prvsX = 0D, prvsY = 0D, threshold = 50D;
		Expression expression;

		// r=(abs(mod(theta, 1)))*5 // Pinwheel
		// r=(abs(mod(theta, 1))^.05)*5 // Inverted Pinwheel
		// r=(1+cos(theta)) // Cardioid
		// r=sin(5*theta)*6 // Rose (also cos)
		// r=4*sec(5*theta) // Epispiral (also csc)
		// r= +/-(cos(theta/2)^.67 + sin(theta/2)^.67)^1.5 // Nephroid
		// r=(10*cos(2*theta))^.5 // Lemniscate
		// r=2/((cos(4*theta)^.5)) // Maltese cross
		// r=1+2*cos(2*theta) // Ceva cycloid
		// r=.5*(sec(theta/3)^3) // Tschirnhausen cubic
		// r=2+sec(theta) // Nicomedes' conchoid
		// r=3*cos(2*theta) - 2*cos(theta) // Scarabaeus curve
		// r=cos(theta)*sin(theta)*theta // Simple butterfly
		// r=(e^sin(theta)) - (2*cos(4*theta)) + (sin(((2*theta) - pi) / 24))^5 // Complex butterfly
		// Upper/lower half only
		// r=+-(3*sin(theta)) / (1+cos(theta)*cos(2*theta)) // Pretzel
		// Left/right half only
		// r=(3*cos(theta)) / (1+sin(theta)*sin(2*theta)) // Boomerang
		// r=4*cot(e*theta) // Flower
		// r=+-(4*cot(theta/2))^.5 // Serpentine curve
		// r=3*cos((4*theta)-(5*pi/18))+(3*0.07*sin(80*theta)) // Jagged leaved flower
		// r=(sin(theta) + sin(2.5*theta)^3)*4 // Scallop shell
		// r=2*(1+e*cos(pi*theta)) // Cyclic harmonic
		// Determine if equation has fixed value & which side of equation has that value.
		if ("r".equalsIgnoreCase(StringUtils.substringBefore(frml, "="))) {
			frmlNoEquals = StringUtils.substringAfter(frml, "=");
		}
		else if ("r".equalsIgnoreCase(StringUtils.substringAfter(frml, "="))) {
			frmlNoEquals = StringUtils.substringBefore(frml, "=");
		}
		else {
			// Popup warning
			popupErrorMessage("Fixed value needed for this equation.");
		}

		for (float angle=0; angle<=36000; angle++) {
			float thetaR2D = (float) Math.toRadians(angle) / 10;
			//frmlRplc = "(" + frmlNoEquals.replaceAll("theta", String.valueOf(thetaR2D) + "*[deg]") + ")";
			frmlRplc = "(" + frmlNoEquals.replaceAll("theta", String.valueOf(thetaR2D)) + ")";
			frmlRplc = frmlRplc.replaceAll("--", "");
			expression = new Expression(frmlRplc);
			resultA = expression.calculate() * 50;
			resultX = (resultA * Math.cos(thetaR2D));
			resultY = (resultA * Math.sin(thetaR2D));
			grphcs2D.fillRect((int) (graphCenter + (resultX)), (int) (graphCenter - (resultY)), 1, 1);

			// Optionally connect any gaps
			if (solidLines) {
				if ((prvsX != 0 && prvsY != 0)
				&& (belowThreshold(resultX, prvsX, (double) threshold))
				&& (belowThreshold(resultY, prvsY, (double) threshold))) {
					grphcs2D.drawLine((int) (graphCenter + (prvsX)), (int) (graphCenter - (prvsY)), (int) (graphCenter + (resultX)), (int) (graphCenter - (resultY)));
				}

				prvsX = resultX; prvsY = resultY;
			}
		}
    }

	private static void graphParametericEquations(String frml, Graphics2D grphcs2D) {
		String leftEquation = "", rightEquation = "", xEquation = "", yEquation = "", frmlRplc = "", mltplrX = "", mltplrY = "";
		int resultX = 0, resultY = 0, prvsX = 0, prvsY = 0;
		Double threshold = 50D;
		Expression expression;

    	// Parametric equations: 'f(x)' AND 'f(y)'
		// x=.5*(z-sin(z));y=.5*(1-cos(z)) // Cycloid
		// x=cos(12*z) * 5; y=sin(7*z) * 5 // Lissajous
		// x=2*cos(z); y=3*sin(z) // Ellipse
		// x=2*cos(z)+cos(2*z); y=2*sin(z)-sin(2*z) // Deltoid
		// x=2*cos(z)-cos(2*z); y=2*sin(z)+sin(2*z) // Horizontally reversed deltoid
		// x=(cos(z)^3)*5; y=-(sin(z)^3)*5 // Astroid
		// x=-((z^2) - 2*z + 5) / ((z^2) - 2*z - 3) ; y=((z^2) - 2*z + 5) / (2*z - 2) // Cruciform curve
		// Also x=1/cos(z) ; y=1/sin(z)
		// x=+/-(cos(z)^2)*5; y=+/-(sin(z)^2)*5 // Diamond
		// x=+/-(cos(z)^.5)*5; y=+/-(sin(z)^.5)*5 // Rectircle (round-cornered square)
		// x=6*cos(z) - 4*(cos(z)^3) ; y= 4*(sin(z)^3) // Nephroid
		// Also y=6*sin(z) - 4*(sin(z)^3) ; x= 4*(cos(z)^3)
		// x=((2^.5) * cos(z)) / (sin(z)^2) ; y=((2^.5) * cos(z) * sin(z)) / (sin(z)^2) // Lemniscate
		// x=(4*cos(z)) - ((4*sin(z)^2) / (2^.5)) ; y = 4*cos(z)*sin(z) // Fish curve
		// x=4*cos(z)*(1-2*sin(z)^2) ; y=4*sin(z)*(1+2*cos(z)^2) // Cornoid (change exponents to change shape)
		// x=8*sin(z)^3 ; y=6*cos(z) - 2*cos(2*z) - 1*cos(3*z) - cos(4*z) // Heart curve
		// x=4*sin(z) ; y=4*((cos(z)^2)*((2+cos(z))/(3+(sin(z)^2)))) // Bicorn curve
		// x=.5*(1-(3*(z^2))) ; y=.5*z*(3-(z^2)) // Tschirnhausen cubic
		// x=(1+ .75*sin(z)^2)*cos(z) *4; y=(1-.75 -.75*cos(z)^2)*sin(z)*4 // Talbot's curve
		// x=cos(z)+0.2*sin(e*z) ; y=sin(z)+0.2*cos(e*z) // Spirograph ring
		// x=((1*cos(z)) / (cos(z) - sin(z))) + 3*cos(z) ; y = 3*sin(z) // Durer's Conchoid
		// x=cos(z)+(cos(12*z)/4)-(sin(-28*z)/9) ; y=sin(z)+(sin(12*z)/4)+(cos(-28*z)/9) // Complex Spirograph ring
		// x=2*cos(z)-2*cos(pi*z); y=2*sin(z)-2*sin(pi*z) // Complex Epicycloid
		// x=2.5*cos(z) ; y=2.5*sin(z)^3 // "Puckered lips"
		// x=sin(z)* ((e^cos(z)) - (2*cos(4*z)) - (sin(z/12))^5) ; y=cos(z)* ((e^cos(z)) - (2*cos(4*z)) - (sin(z/12))^5) // Complex butterfly
		if ((StringUtils.countMatches(frml, "=") != 2)) {
			// Popup warning
			popupErrorMessage("Wrong number of '=' signs for these equations.");
		}
		else {
			frml = frml.replaceAll(" ", "");
			// Store each equation separately
			leftEquation = StringUtils.substringBefore(frml, ";");
			rightEquation = StringUtils.substringAfter(frml, ";");

			// Determine x and y equations
			if (StringUtils.contains(leftEquation, "x")) {
				xEquation = leftEquation;	yEquation = rightEquation;
			}
			else {
				yEquation = leftEquation;	xEquation = rightEquation;
			}

			// Extract equation formulas: remove parameter assignments ("x=", "=x", "y=", "=y")
			if (StringUtils.substringBefore(xEquation, "=").contains("x")) {
				xEquation = StringUtils.substringAfter(xEquation, "=");
			}
			else {
				xEquation = StringUtils.substringBefore(xEquation, "=");
			}

			if (StringUtils.substringBefore(yEquation, "=").contains("y")) {
				yEquation = StringUtils.substringAfter(yEquation, "=");
			}
			else {
				yEquation = StringUtils.substringBefore(yEquation, "=");
			}

			// Determine which multiplier to use: use 100 for any negative exponents, 10 otherwise
			if (xEquation.replaceAll(" ", "").contains("^-")) { mltplrX = " * 500"; }
			else  { mltplrX = " * 50"; }

			if (frml.replaceAll(" ", "").contains("^-")) { mltplrY = " * 500"; }
			else  { mltplrY = " * 50"; }

			// Final formatting
			xEquation.replaceAll("--", "");
			xEquation = "(" + xEquation + ")" + mltplrX;

			yEquation.replaceAll("--", "");
			yEquation = "(" + yEquation + ")" + mltplrY;
		}

		// Loop through range of values to graph equations
		for (float i=-(5*displaySize); i<=(5*displaySize); i++) {
			float fi = i / 150;

			frmlRplc = xEquation.replaceAll("z", String.valueOf(fi));
			expression = new Expression(frmlRplc);
			resultX = (int) (graphCenter + expression.calculate());

			frmlRplc = yEquation.replaceAll("z", String.valueOf(fi));
			expression = new Expression(frmlRplc);
			resultY = (int) (graphCenter - expression.calculate());

			grphcs2D.fillRect(resultX, resultY, 1, 1);

			// Optionally connect any gaps
			if (solidLines) {
				if ((prvsX != 0 && prvsY != 0)
				&& (belowThreshold((double)resultX, (double)prvsX, (double) threshold))
				&& (belowThreshold((double)resultY, (double)prvsY, (double) threshold))) {
					grphcs2D.drawLine((int) (prvsX), (int) (prvsY), (int) (resultX), (int) (resultY));
				}

				prvsX = resultX; prvsY = resultY;
			}
		}
    }

	public static boolean noInputErrors(String equation) {
		boolean isInputOkay = false;
		String inputNoWhitespace = equation.replaceAll("\\s", "");

		String regexDups =  ".*([xyzre\\+\\-\\=\\^\\.\\;])\\1+.*";
		Pattern patternDups = Pattern.compile(regexDups);
		Matcher matcherDups = patternDups.matcher(inputNoWhitespace);

		String regexNumParmOnly =  "[xyzre0-9]+";
		Pattern patternNumParmOnly = Pattern.compile(regexNumParmOnly);
		Matcher matcherNumParmOnly = patternNumParmOnly.matcher(inputNoWhitespace);

		String regexOpsOnly =  "[\\+\\-\\=\\^\\.\\;\\)\\(]+";
		Pattern patternOpsOnly = Pattern.compile(regexOpsOnly);
		Matcher matcherOpsOnly = patternOpsOnly.matcher(inputNoWhitespace);

		if (StringUtils.countMatches(inputNoWhitespace, "(") != StringUtils.countMatches(inputNoWhitespace, ")")) {
			popupErrorMessage("Unbalanced parentheses.");
		}
		else if (StringUtils.contains(inputNoWhitespace, "()")) {
			popupErrorMessage("Empty parentheses.");
		}
		else if (matcherDups.find()) {
			popupErrorMessage("Duplicate characters.");
		}
		// Check last for missing items because otherwise other tests could fail
		else if ((!matcherNumParmOnly.find())
			&& (StringUtils.countMatches(inputNoWhitespace, "pi") == 0)
			&& (StringUtils.countMatches(inputNoWhitespace, "[phi]") == 0)) {
			popupErrorMessage("Missing numbers / parameters.");
		}
		/*else if ((!matcherOpsOnly.find()) && (!("x".equalsIgnoreCase(equation)) && (!("y".equalsIgnoreCase(equation))))) {
				popupErrorMessage("Missing operators.");
		}*/
		else { isInputOkay = true; }

		return isInputOkay;
	}

	// Use regular expressions to parse exponents properly. Ex: convert "x^N", "y^N", "z^N" to "(x^N)", "(y^N)", "(z^N)"
	// Makes equation evaluation more robust, because user can input "(x^2.3 + y^-4)" instead of "((x^2.3) + (y^-4))",
	// and program can parse it correctly. Include further values in regex if add any parameter buttons to keyboard.
	public static String parseExponents(String equation) {
		String regex = "(([xyzre]\\^)([-+]?([0-9]*\\.[0-9]+|[0-9]+)))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(equation);
		StringBuilder replacement = new StringBuilder();

		while(matcher.find()) {
			matcher.appendReplacement(replacement, "(" + matcher.group() + ")");
		}
		matcher.appendTail(replacement);

		return replacement.toString();
	}

	// Popup warning
	public static void popupErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE, null);
	}

	public static boolean belowThreshold(Double firstNumber, Double secondNumber, Double threshold) {
    	boolean belowThrshld = false;

   		if (Math.abs(firstNumber - secondNumber) <= threshold) { belowThrshld = true; }

    	return belowThrshld;
    }

    public static boolean aboveThreshold(Double firstNumber, Double secondNumber, Double threshold) {
    	boolean aboveThrshld = false;

   		if (Math.abs(firstNumber - secondNumber) > threshold) { aboveThrshld = true; }

    	return aboveThrshld;
    }

    private static Double slope(int startX, int startY, int endX, int endY) {
    	Double slopeValue = 0D;

    	try { slopeValue = (double) ((endY - startY) / (endX - startX)); }
    	catch (ArithmeticException ae) { slopeValue = Double.POSITIVE_INFINITY; }

    	return slopeValue;
    }

    // Checks whether equation uses any of listed operations. Include new operations here if add any to keyboard
    public static boolean containsFunction(String inputString) {
    	String[] operations = {"sin", "cos", "tan", "csc", "sec", "cot", "abs", "mod", "sgn", "ln", "log10", "floor", "ceil", "Gamma"};
    	boolean hasOperation = false;

    	for (String prtr : operations) {
    		if (inputString.contains(prtr)) { hasOperation = true; }
    	}

    	return hasOperation;
    }

    private static void setupButton(JButton jbtn, JPanel jpnl, String btnTxt, String frmlTxt, int width, int height) {
    	jbtn.setText(btnTxt);
    	jbtn.setPreferredSize(new Dimension(width, height));
    	jbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formulaText.insert(frmlTxt, formulaText.getCaretPosition());
			}
        });
    	jpnl.add(jbtn);
    }

    private static void clearGraphPanel() {
		Graphics g = graphPanel.getGraphics();
		g.setColor(Color.WHITE);

		g.fillRect(0, 0, displaySize, displaySize);
    	g.setColor(Color.BLACK);
    	g.drawLine(0, displaySize/2, displaySize, displaySize/2);
    	g.drawLine(displaySize/2, 0, displaySize/2, displaySize);
    }

    public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		GraphingCalculator grphngClcltr = new GraphingCalculator();
		gcFrame.validate();
    	gcFrame.repaint();
	}
}
