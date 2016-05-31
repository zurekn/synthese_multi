package com.mygdx.game.javacompiler;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.com.Hadoop;
import com.mygdx.game.data.Data;
import com.mygdx.game.game.Character;

import org.apache.hadoop.fs.Path;

import static java.nio.file.StandardCopyOption.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import static com.mygdx.game.data.Data.*;

public class CompileString {
    private static final String TAG = "CompileString";
    private static final String LABEL = "CompileString" ;
    static Boolean debug = false;
    static String className = "";
    public static final String pathLog = "AILogs"+File.separator;
    public static final String pathHist = "AIHistory"+File.separator;
    public static final String pathClass = "ai"+File.separator;//"/core/src/com/mygdx/game/";//"Synthese/src/game/";
    public static final String destPathClass = "aiFiles"+File.separator;//"/core/build/classes/main/com/mygdx/game/ai/";//"target/classes/game/";
    public static final String classTestName = "IAScript";
    public static final String serializePrefix = "serialize_";
    public static final String packageName = "ai"+File.separator;
    static String characType = "t_character";
    static String intType = "t_int";
    static String floatType = "t_float";
    static String booleanType = "t_boolean";
    static String stringType = "t_string";
    static boolean aRisque = false;
    static Class<?> c = null;
    static int nbLignesCode = 2;
    static boolean advanced = true;
    private static ArrayList<String> code;
    private static ArrayList<String> comp;
    private static ArrayList<String> var;
    private static ArrayList<String> cond;
    private static ArrayList<String> func;
    private static ArrayList<String> funcFloat;
    private static ArrayList<String> funcString;
    private static ArrayList<String> funcInt;
    private static ArrayList<String> funcBoolean;
    public static String JDK_PATH = "C:\\Java\\jdk1.8.0_45\\jre";

    @Deprecated
    public static void generate(String geneticName, int generation)
    {
        SetGenerateCompiler();
        aRisque = false;
        rootDir =  File.separator;

        debugSys("\n===========   GENERATE MOB "+geneticName+"  ===========");
        Node root = advanced?DecodeScript(pathClass+File.separator+"AdvancedAIScriptDatas.txt"):DecodeScript(pathClass+File.separator+"AIScriptDatas.txt");
        ArrayList<String> contentCode = new ArrayList<String>();
        contentCode = root.TreeToArrayList(contentCode);
        className = geneticName + (aRisque ? "_Arisque" : "");
        ReadWriteCode(contentCode, className);
        try {
            serializeObject(serializePrefix + geneticName+"_"+generation, root, destPathClass+pathLog+File.separator);
            //deserializeObject("serialized_"+geneticName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateTree(String geneticName) {
        SetGenerateCompiler();
        debugSys("\n===========   GENERATE MOB " + geneticName + "  ===========");
        Node root = advanced?DecodeScript(pathClass+"AdvancedAIScriptDatas.txt"):DecodeScript(pathClass+"AIScriptDatas.txt");
        try {
            serializeObject(serializePrefix + geneticName, root, destPathClass+Data.poolToTestDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * S�rialization d'un objet
     */
    public static void serializeObject(String name, Node root, String serializePath) throws IOException
    {
        File f = new File(serializePath + name +".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(f));
        objectOutputStream.writeObject(root);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    /*
     * D�-s�rialize un objet
     */
    public static Node deserializeObject(String name, String deserializePath) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(deserializePath + name));
        Node readJSON = (Node) objectInputStream.readObject();
        objectInputStream.close();
        return readJSON;
    }
	/**
	 *  Store a script into Pool a Tester from the tree of this script
	 *
	 * @param treeName : the tree to store as script
	 */
	public static void storeToPool(String treeName){
		try {
			Node node =	deserializeObject(treeName, destPathClass+Data.poolToTestDir+serializePrefix);
			// Save result
			ArrayList<String> contentCode = new ArrayList<String>();
			contentCode = node.TreeToArrayList(contentCode);
			ReadWriteCode(contentCode,"toMove_"+treeName);
			File file = new File(pathClass + packageName+className +".java");
			java.nio.file.Path source = Paths.get(pathClass + packageName + className + ".java");
			java.nio.file.Path target = Paths.get(pathClass + packageName + "PoolATester/" + className + ".java");
			Files.move(source, target, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


    public static void loadGenetic(String name, String geneticName)
    {
        try {
            Node treeMob = deserializeObject(name, destPathClass+Data.poolToTestDir);
            ArrayList<String> contentCode = new ArrayList<String>();
            contentCode = treeMob.TreeToArrayList(contentCode);
            ReadWriteCode(contentCode, geneticName);
            //copier fichier contenant l'arbre sérializé dans un autre répertoire
           // Data.switchTestPool(1,name,name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /** This method combine 2 genetic IA scripts into a random fusion of both.
     *
     * @param name1 : String, true ID of first serialized IA
     * @param name2 : String, true ID of second serialized IA
     * @param name : String, file name of resulting IA
     */
    public static void combineTrees(String name1, String name2, String name){
        SetGenerateCompiler();
        debugSys("Combining Trees "+name1+" and "+name2+" into "+name);
        Node root1;
        Node root2;
        Node resRoot;
        Node tmpRoot1 = null;
        Node tmpRoot2 = null;
        try {
            // Get monster 1 & 2 trees
            root1 = deserializeObject(name1, destPathClass+pathLog+File.separator);
            root2 = deserializeObject(name2, destPathClass+pathLog+File.separator);
            boolean done = false;
            // While we didn't combined...
            while(!done){
                tmpRoot1 = root1.getSubTree(-1); // get Random subtree from root1
                System.out.println("#Random root1. found"+tmpRoot1.getValue());
                //if we get a 'if' node, we search another 'if' node
                if(tmpRoot1.getValue().equals("if")){

                    tmpRoot2 = root2.searchSubTreeByValue("if"); // search 'if' if 'if' found
                    if(tmpRoot2.getValue().equals("if"))
                        done = true;
                }else if(tmpRoot1.getValue().equals("else")){

                    tmpRoot2 = root2.searchSubTreeByValue("else"); // search 'else' if 'else' found
                    if(tmpRoot2.getValue().equals("else"))
                        done = true;
                }else{
                    tmpRoot2 = root2.getSubTree(-1);

                    if(!tmpRoot2.getValue().equals("else") ){
                        if( !(tmpRoot1.getParent().getValue().equals("if") &&
                                tmpRoot1.getParent().getChildren().get(0).getValue().equals(tmpRoot1.getValue())) ){
                            done = true;
                        }
                    }
                }
                debugSys("##found "+tmpRoot2.getValue()+" in root2");
            }
            debugSys("\t========= Replacing ==========" );
            tmpRoot1.displayTree();
            debugSys("\t========= With =============" );
            tmpRoot2.displayTree();

            // Replace tmpRoot1 with tmpRoot2
            if(tmpRoot1.hasParent()){
                resRoot = tmpRoot1.getParent();
                resRoot.replaceChild(tmpRoot1, tmpRoot2);
                while(resRoot.hasParent()){ // Retourner a la racine (run)
                    resRoot = resRoot.getParent();
                }
            }
            else{ // s tmpRoot1 n'a pas de parent, resRoot = tmpRoot1
                resRoot = tmpRoot1;
            }
            debugSys("###### COmbining end. Result is : ");

            // Save result
            ArrayList<String> contentCode = new ArrayList<String>();
            contentCode = resRoot.TreeToArrayList(contentCode);
            resRoot.displayTree();
		/*	for (String st : contentCode)
				System.out.println(st);*/
            ReadWriteCode(contentCode, name);
            //serializeObject(name,lastRoot);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** Creation of genetic AI tree from a text file.
     * 	Creation stages :
     * - Decode text file and store datas in variables
     * - Choose randomly one condition branch and build its condition
     * - Choose randomly NbCodeLine code lines in the condition branch
     * - Choose randomly NbCodeLine code lines outside the condition branch
     * @param scriptPath : the text file containing all needed datas
     * @return Node : the resulting script tree
     */
    public static Node DecodeScript(String scriptPath) {
        //Gdx.app.log(TAG, "Loading script from file ["+scriptPath+"]");
        File fichier = Gdx.files.internal(scriptPath).file();
        Node root = new Node("run(Character ch)");
        cond = new ArrayList<String>();
        var = new ArrayList<String>();
        comp = new ArrayList<String>();
        code = new ArrayList<String>();
        func = new ArrayList<String>();
        funcInt = new ArrayList<String>();
        funcFloat = new ArrayList<String>();
        funcBoolean = new ArrayList<String>();
        funcString = new ArrayList<String>();
        boolean condBool = false;
        boolean compBool = false;
        boolean varBool = false;
        boolean codeBool = false;
        boolean funcBool = false;
        // ====== Lecture du fichier texte ==========
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(Gdx.files.internal(scriptPath).read());
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (condBool) {
                    if (ligne.contains("}"))
                        condBool = false;
                    else
                        cond.add(ligne);
                }
                if (varBool) {
                    if (ligne.contains("}"))
                        varBool = false;
                    else
                        var.add(ligne);
                }

                if (compBool) {
                    if (ligne.contains("}"))
                        compBool = false;
                    else
                        comp.add(ligne);
                }

                if (codeBool) {
                    if (ligne.contains("}"))
                        codeBool = false;
                    else
                        code.add(ligne);
                }
                if (funcBool) {
                    if (ligne.contains("}"))
                        funcBool = false;
                    else
                    {
                        func.add(ligne);
                        if(ligne.contains(intType)){
                            funcInt.add(ligne);
                        }
                        if(ligne.contains(floatType)){
                            funcFloat.add(ligne);
                        }
                        if(ligne.contains(stringType)){
                            funcString.add(ligne);
                        }
                        if(ligne.contains(booleanType)){
                            funcBoolean.add(ligne);
                        }
                    }
                }

                if (ligne.contains("cond") && !condBool)
                    condBool = true;

                if (ligne.contains("comp") && !compBool)
                    compBool = true;

                if (ligne.contains("var") && !varBool)
                    varBool = true;

                if (ligne.contains("code") && !codeBool)
                    codeBool = true;
                if (ligne.contains("func") && !funcBool)
                    funcBool = true;
                // script.add(ligne);
            }
            debugSys("DecodeScript : var="+var);
            debugSys("DecodeScript : code="+code);
            debugSys("DecodeScript : comp="+comp);
            debugSys("DecodeScript : cond="+cond);
            debugSys("DecodeScript : cond="+func);
            // ======= Construction de l'arbre ==========
            root = addCodeLineAlea(code,nbLignesCode,root);// Ajout des lignes de code � la racinde du run()
            root = addvancedFullCondition(root,2); // Ajout de conditions et code � l'int�rieur de ces conditions
            br.close();// fermeture du fichier txt

        } catch (Exception e) {
            System.out.println("Decode : "+e.toString());
        }
        return root;
    }

    /** Add a full condition branch node with code lines inside. Advanced
     *
     *  Should be used like : node = addFullCondition(node)
     *
     * @param resNode : node where we append condition
     * @return : resulting node with conditions appended
     */
    public static Node addvancedFullCondition(Node resNode, int maxDepth)
    {
        if(maxDepth > 0){ // Si on n'a pas atteint la profondeur max
            int rand = 0;
            String[] partsRandomCond = getParam(cond, -1); // condition al�atoire (if, for, etc.)
            Node nodeCond = new Node(""); // init. supernoeud de condition
            String conditionFull = ""; // init. valeur textuelle de la condition
            if (partsRandomCond[0].contains("if") // test du cas if / while
                    || partsRandomCond[0].contains("while")) {
                if (partsRandomCond[0].contains("while")) { // Risqu� si while
                    nodeCond.setValue("while");
                    aRisque = true;
                } else
                    nodeCond.setValue("if");
                /** modif */
                String[] partsRandomVar = getParam(var, -1); // LIGNE de variables al�atoire
                if(partsRandomVar[0].contains("character"))
                {
                    String[] partsRandomFunc = getParam(func,-1);
                    String type = partsRandomFunc[1];
                    debugSys("addvanced char. : param founded = "+partsRandomVar[1]+", type = "+type);
                    conditionFull+= replaceDefault(true,partsRandomVar[1],type,"");
                    conditionFull+= replaceDefault(false,partsRandomFunc[0],type,partsRandomVar[1]);
                    conditionFull += getCompInCond(true,type);
                    debugSys("addvanced char. : full condition = "+conditionFull);
                }else{
                    conditionFull += partsRandomVar[1]; // concat�ner nom de la variable
                    debugSys("addvanced other. : param 0 = "+partsRandomVar[1]+", type = "+partsRandomVar[0]);
                    conditionFull += getCompInCond(false,partsRandomVar[0]);
                    debugSys("addvanced other. : full condition = "+conditionFull);
                }
            }
            resNode.addChild(nodeCond); // lier les supernoeuds
            Node condFullNode = new Node(conditionFull); // cr�ation du sous-noeud
            nodeCond.addChild(condFullNode); // Ajout du sous-noeud au supernoeud de condition
            nodeCond = addCodeLineAlea(code,nbLignesCode,nodeCond); // *** Ajout des lignes de code dans la branche
            boolean moreDeep = (new Random().nextInt(2)==0?false:true); // 50% de chances d'ajouter une branche conditionnelle
            if(moreDeep)nodeCond = addvancedFullCondition(nodeCond,maxDepth-1);// Ajout d'une autre branche conditionnelle
            if (partsRandomCond[0].contains("if")  // Cas du if avec un else
                    && partsRandomCond[2].contains("else")) {
                Node nodeElse = new Node("else");
                resNode.addChild(nodeElse);
                nodeElse = addCodeLineAlea(code,nbLignesCode,nodeElse); // *** Ajout des lignes de code dans le ELSE
                moreDeep = (new Random().nextInt(2)==0?false:true);
                if(moreDeep)nodeElse = addvancedFullCondition(nodeElse,maxDepth-1);// Ajout d'une autre branche conditionnelle
            }
        }
        return resNode;

    }


    /** R�cup�ration d'un param�tre
     *
     * @param //cond
     *            : toutes les possibilit�s pour chaque param
     * @param pos
     *            : la position du param�tre que l'on veut. si n�gatif, on en
     *            prend un al�atoire.
     * @return un param al�atoire
     */
    public static String[] getParam(ArrayList<String> param, int pos) {
        int randPosition = 0;
        String dataLine;
        if (pos < 0) {
            randPosition = new Random().nextInt(param.size()); // random sur les positions
            dataLine = (param.get(randPosition)); // recuperation de la condition a la
            // position rand
        } else {
            dataLine = (param.get(pos));
        }
        if (debug)
            System.out.println("random : pos= "+randPosition+" string= " + dataLine);
        dataLine = Data.supressUselessShit(dataLine);
        String[] partsRandom = dataLine.split(","); // implode des params de la
        // condition dans parts
        return partsRandom;
    }

    /** Get a LINE parameter by its type (int, bool, etc.)
     *
     * @param type
     *            : Param type
     * @return Table containing splitted line
     */
    public static String[] getParamByType(ArrayList<String> param, String type) {
        String dataLine = "";
        int randPosition ;
        debugSys("\t\tgetParamByType : researching a "+type+" in "+param.size()+" elements");
        do{
            randPosition = new Random().nextInt(param.size()); // param random
            dataLine = (param.get(randPosition).toString()); // recuperation de la valeur
        }while(!(dataLine.contains(type) || dataLine.contains(characType)));

        debugSys("\t\tgetParamByType : line = "+dataLine);
        dataLine = Data.supressUselessShit(dataLine);
        String[] partsRandom = dataLine.split(","); // implode des params de la
        // condition dans parts
        return partsRandom;
    }

    /** used for advanced AI Script.
     * Replace var default type by corresponding attribute.
     * Replace function Character parameter by corresponding character.
     *
     * @param isVar : do we replace a var ? or a func.
     * @param toReplace : original string with value to replace
     * @param type : t_int, t_boolean etc. used when isVar = true
     * @param varChoosen : character choosed in var or nothing if isVar = true
     * @return Replaced string
     */
    public static String replaceDefault(boolean isVar, String toReplace, String type, String varChoosen){
        String returnString;
        returnString = toReplace;
        debugSys("replaceDefault : isVar = "+isVar+", toReplace = "+toReplace+", type = "+type+", varChoosen = "+varChoosen);
        if(isVar)
        {
            switch(type){
                case "t_int":
                    debugSys("dans le t int");
                    returnString = returnString.replace("defaultString", "defaultInt");
                    break;
                case "t_boolean":
                    debugSys("dans le t boolean");
                    returnString = returnString.replace("defaultString", "defaultBoolean");
                    break;
                case "t_float":
                    debugSys("dans le t float");
                    returnString = returnString.replace("defaultString","defaultFloat");
                    break;
            }
        }else{
            if(returnString.contains("choosen"))
                returnString = returnString.replace("choosen", varChoosen);
        }
        debugSys("replaceDefault : replaced. result = "+returnString);
        return returnString;
    }

    /**
     * Construit un bout de code pour comparer 2 variables.
     * Ne construit pas la partie gauche de la comparaison
     *
     * @param //pos
     * @param //param
     * @return bout de code (comparateur) � concat�ner
     */
    public static String getCompInCond(boolean characAtLeft,String type) {
        String[] partsRandomComp;
        String[] partsRandomVar;
        String[] partsRandomFunc;
        String returnString;
        String rightVar;
        ArrayList<String> funcParam = new ArrayList<String>();
        debugSys("\tgetCompInCond : récupération du comparateur pour le type "+type);
        partsRandomComp = getParamByType(comp, type);
        int rand = 0;
        rand = new Random().nextInt(partsRandomComp.length); // Choix random du comparateur (==, <=, >=, >, <, != )
        rand = (rand == 0) ? 1 : rand;
        debugSys("\tgetCompInCond : récupération de variable comparante ");
        if(type.equals(intType)){
            funcParam  = funcInt;
        }else if(type.equals(floatType)){
            funcParam  = funcFloat;
        }else if(type.equals(stringType)){
            funcParam  = funcString;
        }else if(type.equals(booleanType)){
            funcParam  = funcBoolean;
        }

        partsRandomVar = getParamByType(var, (characAtLeft?type:characType)); // recupere la variable � droite du comparateur
        if(advanced && partsRandomVar[0].contains(characType)){
            rightVar=replaceDefault(true,partsRandomVar[1],type,"");
            partsRandomFunc = getParamByType(funcParam, type);
            rightVar+=replaceDefault(false,partsRandomFunc[0],type,partsRandomVar[1]);

        }else if(advanced)
            rightVar= partsRandomVar[1];
        else
            rightVar= partsRandomVar[0];
        debugSys("\tgetCompInCond : rightVar = "+rightVar);
        returnString = (partsRandomComp[rand] + " "+rightVar+" "+(partsRandomComp[rand].contains("(")?")":""));
        // concat�nation du comparateur et de la valeur comparante
        debugSys("\tgetCompInCond :Return String = "+returnString);
        return returnString;
    }

    /**
     * Construit un bout de code pour comparer 2 chiffres.
     * Ne construit pas la partie gauche de la comparaison
     *
     * @param //pos
     * @param //param
     * @return bout de code (comparateur) � concat�ner
     */
    public static String getCompInt(String[] partsRandomComp,
                                    String[] partsRandomVar) {
        // Choix du comparateur (==, <=, >=, >, <, != )
        int rand = 0;
        rand = new Random().nextInt(partsRandomComp.length);
        rand = (rand == 0) ? 1 : rand;

        if (debug)
            System.out.println("getComptInt random : " + rand);

        // calcul de la variable comparante (� droite du comparateur)
        String[] testVar = getParam(var,-1);
        String rightVar = testVar[0];
        debugSys("getComptInt : rightVar = "+rightVar);
        // concat�nation du comparateur et de la valeur comparante
        return (partsRandomComp[rand] + " " + rightVar);
    }

    /**
     * Construit un bout de code pour comparer 2 bool�ens.
     * Ne construit pas le bool�en de gauche.
     *
     * @param partsRandomComp : la ligne "comparateur"
     * @return bout de code (comparateur) � concat�ner
     */
    public static String getCodeBool(String[] partsRandomComp) {
        return partsRandomComp[1] + " "
                + ((new Random().nextInt(1) == 1) ? "true" : "false");

    }

    /**  Ajoute un certain nombre de lignes de code .
     * Les lignes sont contenues dans la section "code" de la base
     * Les lignes sont ajout�es au param�tre Node resNode
     *
     * @param code
     * @param nbMaxLine
     * @param resNode
     * @return Node resNode
     */
    public static Node addCodeLineAlea(ArrayList<String> code,int nbMaxLine,Node resNode){
        int nbLnCode = new Random().nextInt(nbMaxLine) + 1;
        int rand=0;
        // Choix al�atoire de la ligne de code
        for (int i = 0; i < nbLnCode; i++) {
            rand = new Random().nextInt(code.size());
            resNode.addChild(new Node(code.get(rand)));
        }
        return resNode;
    }

    /** print message only when debugging
     *
     * @param message : message to print
     */
    public static void debugSys(String message)
    {
        if(debug)/**/
            System.out.println(message);
    }

    /*
     * Compilation, d�placement, et instanciation d'une classe � partir d'un
     * String
     */
    public static IAGenetic CompileAndInstanciateClass(String className) {
        // Compilation de la classe du joueur IA
        JavaCompiler compiler = GenerateCompiler();
        @SuppressWarnings("unused")
        int result = compiler.run(null, null, null, destPathClass + File.separator + className + ".java");

        //System.out.println("Compile result code = " + result);

        // D�placement du fichier .CLASS du r�pertoire /src au /bin
       /* File afile = new File( pathClass + className + ".CLASS");
        File destFile = new File(destPathClass + afile.getName());

        if (destFile.exists())
            destFile.delete();
        afile.renameTo(new File( destPathClass + afile.getName()));*/



        // Instanciation de la classe du joueur IA
        Class<?> c = null;
        Object obj = null;
        try {
            File f = new File(destPathClass);
            //c = Class.forName(destPathClass.substring(0, destPathClass.length()-1) + "."+ className);
			/*Cette partie permet d'accéder aux classes externes*/
            URL url = f.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader classloader = new URLClassLoader(urls);
            Class classloaders = classloader.loadClass(className);
            c = classloaders;
            obj = c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new IAGenetic(c,obj, className);
    }

    /** Read skelet class (classTestName), construct then write text content in "className" class by calling WriteCode
     *
     * @param codeContent : Generated code to push in "run" method
     * @param className : should correspond to the monster id which will run this script.
     */
    public static void ReadWriteCode(ArrayList<String> codeContent,String className) {
        Boolean inRun = false;
        Boolean isAdded = false;
        //Gdx.files.internal()
        File fichier = Gdx.files.internal(pathClass + File.separator + classTestName +".java").file();
        ArrayList<String> content = new ArrayList<String>();
        // lecture du fichier java
        try {
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.contains(classTestName))
                    ligne = ligne.replace(classTestName, className);
                content.add(ligne);
                if (inRun && !isAdded && ligne.contains("{")) {
                    // Ajout des lignes de code d�sir�es dans la ArrayList
                    for (String ln : codeContent)
                        content.add("\t\t" + ln);
                    isAdded = true;
                } else {
                    if (ligne.contains("run("))
                        inRun = true;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("ReadWriteCode" + e.toString());
        }

        WriteCode(content, className);
    }

    /**
     * Write code content in "className" class
     * @param content : all code to write in file.
     * @param className : should correspond to the monster id which will run this script.
     */
    public static void WriteCode(ArrayList<String> content, String className) {
        // cr�ation du fichier qui va �craser l'ancien fichier java
        try {
            FileWriter fw = new FileWriter(new File(destPathClass + className + ".java"));
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter fichierSortie = new PrintWriter(bw);
            for (String ln : content) {
                fichierSortie.println(ln);
            }
            fichierSortie.close();
        } catch (Exception e) {
            //System.out.println("WriteCode : "+e.toString());
            e.printStackTrace();
        }
    }

    public static JavaCompiler GenerateCompiler()
    {
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0) {
            System.setProperty("java.home", JDK_PATH);
        }
        return ToolProvider.getSystemJavaCompiler();
    }

    public static void SetGenerateCompiler()
    {
        if(System.getProperty("os.name").toLowerCase().indexOf("win")>=0)
            System.setProperty("java.home", JDK_PATH);

    }
}