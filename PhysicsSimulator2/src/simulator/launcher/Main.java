package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStateBuilder;
import simulator.factories.Factory;
import simulator.factories.MassEqualStateBuilder;
import simulator.factories.MassLossingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _expOutFile = null;
	private static int _steps = 0;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;
	private static String _modeValue = "batch";

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		
		//inicializamos la factoria de bodies con el basic body builder y el mass lossing body builder
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLossingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		
		//inicializamos la factoria de fuerzas con el builder de la fuerza de Newton, el builder de la fuerza hacia un punto fijo y el de fuerza cero
		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);
		
		//inicializamos la factoria de comparadores con el mass equal state builder y el epsilon equal state builder
		ArrayList<Builder<StateComparator>> stateComparators = new ArrayList<>();
		stateComparators.add(new MassEqualStateBuilder());
		stateComparators.add(new EpsilonEqualStateBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(stateComparators);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			
			//support of -o, -eo, and -s (define corresponding parse methods)
			parseOutputOption(line);
			parseExpectedOutputOption(line);
			parseStepsOption(line);
			
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseModeOption(line); //para parsear el modo -m

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// TODO add support for -o, -eo, and -s and -m (add corresponding information to
		// cmdLineOptions)
		
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Bodies JSON output file.").build());
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected output").hasArg().desc("Bodies JSON expected output file.").build());
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("Number of steps").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution mode").build());
		
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		
		if(line.hasOption("i")) {
			_inFile = line.getOptionValue("i");
			if (_inFile == null) {
				throw new ParseException("In batch mode an input file of bodies is required");
			}
		}
		
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}
	
	private static void parseOutputOption(CommandLine line) throws ParseException{
		_outFile = line.getOptionValue("o"); //captura la opcion -o (output) de los argumentos del main
	}
	
	private static void parseExpectedOutputOption(CommandLine line) throws ParseException{
		_expOutFile = line.getOptionValue("eo"); //captura la opcion -eo (expected output) de los argumentos del main
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException{
		
		if(line.hasOption("s")) {
			_steps = Integer.valueOf(line.getOptionValue("s")); //captura la opcion -s (que es el numero de pasos de simulacion a ejecutar) de los argumentos del main
			
			if (_steps == 0) { // si el numero de pasos es 0 salta una excepcion
				throw new ParseException("In batch mode a number of steps greater than 0 is required");
			}
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException { 
		
		_modeValue = line.getOptionValue("m");
		
		if(_modeValue == null) { //si el valor es null se coge por defecto batch
			_modeValue = "batch";
		}
		else if(!_modeValue.equalsIgnoreCase("GUI") && !_modeValue.equalsIgnoreCase("batch")) {
			throw new ParseException("Incorrect mode"); //si no es ni batch ni gui salta excepcion
		}
	}

	private static void startBatchMode() throws Exception {
		ForceLaws fl = _forceLawsFactory.createInstance(_forceLawsInfo); //inicializamos la ley de fuerza y creamos su instancia
		StateComparator sc = null;
		
		PhysicsSimulator ps = new PhysicsSimulator(fl, _dtime); //inicializamos el simulador fisico
		Controller c = new Controller(ps, _bodyFactory, _forceLawsFactory); //inicializamos el controlador
		InputStream in = new FileInputStream(_inFile); //inicializamos el input
		OutputStream out;
		InputStream expOut = null;

		if(_outFile != null) { //si se especifica un output en los argumentos del main
			out = new FileOutputStream(_outFile);
		}
		else { //si no se especifica sale por consola
			out = System.out;
		}
		
		if(_expOutFile != null) { //si se especifica un expected output
			expOut = new FileInputStream(new File(_expOutFile)); //inicializamos expected output
			sc = _stateComparatorFactory.createInstance(_stateComparatorInfo); //inicializamos comparador
		}
		
		c.loadBodies(in); //cargamos los bodies a partir del input
		c.run(_steps, out, expOut, sc); //ejecutamos el programa
	}

	private static void startGUIMode() throws Exception {
		
		Controller c;
		PhysicsSimulator ps;
		
		if(_forceLawsInfo != null) { //si nos dan inicialmente la ley de fuerza, iniciamos con ella
			ForceLaws fl = _forceLawsFactory.createInstance(_forceLawsInfo);
			ps = new PhysicsSimulator(fl, _dtime);
			c = new Controller(ps, _bodyFactory, _forceLawsFactory);
		}
		else { //sino se queda a null la ley de fuerzas (se annadira mas adelante)
			ps = new PhysicsSimulator(null, _dtime);
			c = new Controller(ps, _bodyFactory, _forceLawsFactory);
		}
		
		if(_inFile != null) { //si hay fichero de bodies iniciamos con el
			InputStream in = new FileInputStream(_inFile); //inicializamos el input
			c.loadBodies(in);
		}
		
		SwingUtilities.invokeAndWait(new Runnable() { //abre la ventana pricipal
            @Override
            public void run() {
                new MainWindow(c);
            }
        });
	}
	
	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if(_modeValue.equalsIgnoreCase("GUI")) { //modo gui
			startGUIMode();
		}
		else { //modo batch
			startBatchMode();
		}
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
