package codegen.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import codegen.ArrayValueSetter;
import symboltable.*;

public class ProgramGenerator extends CodeGenerator {
	private static ProgramGenerator instance = null;
	private Stack<String> nameStack = new Stack<String>();
	private Map<String, String> arrayNameMap = new HashMap<String, String>();
	private boolean isInitArguments = false;
	private ArrayList<String> structNameList = new ArrayList<String>();
	private int branch_count = 0;
	private int branch_out = 0;
	private String embedded = "";
	private String comparingCmd = "";
	
	private int loopCount = 0;
	
	public int getIfElseEmbedCount() {
		return embedded.length();
	}
	
	public void incraseIfElseEmbed() {
		embedded += "i";
	}
	
	public void decraseIfElseEmbed() {
		embedded = embedded.substring(1);
	}
	
	public void setComparingCommand(String cmd) {
		comparingCmd = cmd;
	}
	
	public void emitComparingCommand() {
		emitString(comparingCmd);
	}
	
	public void  emitBranchOut() {
		String s = "\n" + embedded+"branch_out" + branch_out + ":\n";
		this.emitString(s);
		branch_out++;
	}
	
	public String getBranchOut() {
		String s = embedded + "branch_out" + branch_out;
		return s;
	}
	
	public String getCurrentBranch() {
		String str = embedded + "branch" + branch_count;
		return str;
	}
	
	public void emitLoopBranch() {
	    String s = "\n"+"loop" + loopCount +":"+"\n";
	    emitString(s);
	}
	
	public String getLoopBranch() {
		return  "loop" + loopCount;
	}
	
	public void increaseLoopCount() {
		loopCount++;
	}
	
	public void increaseBranch() {
		branch_count++;
	}
	
	public String getAheadBranch(int ahead) {
		String str =  embedded + "branch" + branch_count + ahead + ":";
		this.emitString(str);
		return str;
	}
	
	
	public void putStructToClassDeclaration(Symbol symbol) {
		//判断传入的Symbol变量是否是结构体变量，不是的话立刻返回
		Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
		if (sp == null) {
			return;
		}
		
		/*
		 * 在队列structNameList中查询Symbol对应的结构体名字是否已经存储在队列中，如果在队列中有了
		 * 那表明该结构体已经被转换成java类，并且类的定义已经转换成java汇编语言了
		 */
		StructDefine struct = sp.getStruct();
		if (structNameList.contains(struct.getTag())) {
			return;
		} else {
			structNameList.add(struct.getTag());
		}
		
		/*
		 * 输出相应指令，把结构体转换成java类
		 */
		//如果当前声明的是结构体数组，那么就不用在堆栈上构建一个实例,而是直接从数组中把实例加载到堆栈上
		if (symbol.getValueSetter() == null) {
		    this.emit(Instruction.NEW, struct.getTag());
		    this.emit(Instruction.DUP);
		    this.emit(Instruction.INVOKESPECIAL, struct.getTag()+"/"+"<init>()V");
		    int idx = this.getLocalVariableIndex(symbol);
		    this.emit(Instruction.ASTORE, ""+idx);
		}  
		
		declareStructAsClass(struct);
	}
	
	private void declareStructAsClass(StructDefine struct) {
		//这条语句的作用是，把接下来生成的指令先缓存起来，而不是直接写入到文件里
	    this.setClassDefinition(true);
				
	    this.emitDirective(Directive.CLASS_PUBLIC, struct.getTag());
		this.emitDirective(Directive.SUPER, "java/lang/Object");
				
		/*
		* 把结构体中的每个成员转换成相应的具有public性质的java类成员
		*/
		Symbol fields = struct.getFields();
		do {
				String fieldName = fields.getName() + " ";
				if (fields.getDeclarator(Declarator.ARRAY) != null) {
				    fieldName += "[";
				}
				    
				if (fields.hasType(Specifier.INT)) {
				    fieldName += "I";
				} else if (fields.hasType(Specifier.CHAR)) {
				    fieldName += "C";
				} else if (fields.hasType(Specifier.CHAR) && fields.getDeclarator(Declarator.POINTER) != null) {
				    fieldName += "Ljava/lang/String;";
				}
				    
				this.emitDirective(Directive.FIELD_PUBLIC, fieldName);
				fields = fields.getNextSymbol();
	        }while (fields != null);
				
		/*
		 * 实现类的初始构造函数，它调用父类的构造函数后，接下来通过putfield指令，把类的每个成员都初始化为0
		*/
		this.emitDirective(Directive.METHOD_PUBLIC, "<init>()V");
	    this.emit(Instruction.ALOAD, "0");
	    String superInit = "java/lang/Object/<init>()V";
	    this.emit(Instruction.INVOKESPECIAL, superInit);
		
		fields = struct.getFields();
		do {
			this.emit(Instruction.ALOAD, "0");
			String fieldName = struct.getTag() + "/" + fields.getName();
			String fieldType = "";
			if (fields.hasType(Specifier.INT)) {
				fieldType = "I";
				this.emit(Instruction.SIPUSH, "0");
			} else if (fields.hasType(Specifier.CHAR)) {
				fieldType = "C";
				this.emit(Instruction.SIPUSH, "0");
			} else if (fields.hasType(Specifier.CHAR) && fields.getDeclarator(Declarator.POINTER) != null) {
				fieldType = "Ljava/lang/String;";
				this.emit(Instruction.LDC, " ");
			}
					
			String classField = fieldName + " " + fieldType;
			this.emit(Instruction.PUTFIELD, classField);
					
			fields = fields.getNextSymbol();
		}while (fields != null);
				
		this.emit(Instruction.RETURN);
		this.emitDirective(Directive.END_METHOD);			
		this.emitDirective(Directive.END_CLASS);
			
		this.setClassDefinition(false);
	}
	
	public void assignValueToStructMember(Symbol structSym, Symbol field, Object val) {
		//先把类的实例压入堆栈顶部
	    int idx = getLocalVariableIndex(structSym);
	    this.emit(Instruction.ALOAD, ""+idx);
	    
	    /*
	     * field是要写入的结构体成员对象，假设我们要对myTag.x 赋值，那么下面的代码把myTag.x转换为
	     * CTag/x  I
	     */
	    String value = "";
	    String fieldType = "";
	    if (field.hasType(Specifier.INT)) {
			fieldType = "I";
			value += (Integer)val;
			this.emit(Instruction.SIPUSH, value);
		} else if (field.hasType(Specifier.CHAR)) {
			fieldType = "C";
			value += (Integer)val;
			this.emit(Instruction.SIPUSH, value);
		} else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
			fieldType = "Ljava/lang/String;";
			value += (String)val;
			this.emit(Instruction.LDC, value);
		}
	    
	    //执行putfield指令，把要修改的值写入结构体成员变量
	    Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
		StructDefine struct = sp.getStruct();
	    String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
	    this.emit(Instruction.PUTFIELD, fieldContent);   
	}
	
	public void assignValueToStructMemberFromArray(Object obj, Symbol field, Object val) {
		ArrayValueSetter setter = (ArrayValueSetter)obj;
		int idx = (int) setter.getIndex();
		Symbol symbol = setter.getSymbol();
		
		//先把数组对象加载到堆栈
		int i = getLocalVariableIndex(symbol);
		this.emit(Instruction.ALOAD, ""+i);
		//把数组中的对象加载到堆栈
		this.emit(Instruction.SIPUSH, ""+idx);
		this.emit(Instruction.AALOAD);
		
		//把数值存入对象的成员变量
		String value = "";
		String fieldType = "";
		if (field.hasType(Specifier.INT)) {
		    fieldType = "I";
            value += (Integer)val;
			this.emit(Instruction.SIPUSH, value);
		} else if (field.hasType(Specifier.CHAR)) {
			fieldType = "C";
			value += (Integer)val;
			this.emit(Instruction.SIPUSH, value);
		} else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
			fieldType = "Ljava/lang/String;";
			value += (String)val;
			this.emit(Instruction.LDC, value);
		}
		
		//执行putfield指令，把要修改的值写入结构体成员变量
		Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
		StructDefine struct = sp.getStruct();
	    String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
	    this.emit(Instruction.PUTFIELD, fieldContent);
	}
	
	public void createStructArray(Symbol structSymArray) {
		//先判断数组是否已经创建过，如果创建过，那么直接返回
		Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
		/*
		 * 在队列structNameList中查询Symbol对应的结构体名字是否已经存储在队列中，如果在队列中有了
		 * 那表明该结构体已经被转换成java类，并且类的定义已经转换成java汇编语言了
		 */
		StructDefine struct = sp.getStruct();
		if (structNameList.contains(struct.getTag())) {
			return;
		} else {
			structNameList.add(struct.getTag());
		}
		
		Declarator declarator = structSymArray.getDeclarator(Declarator.ARRAY);
		int eleCount = declarator.getElementNum();
		this.emit(Instruction.SIPUSH, "" + eleCount);
		this.emit(Instruction.ANEWARRAY, struct.getTag());
		
		int idx = getLocalVariableIndex(structSymArray);
		this.emit(Instruction.ASTORE, ""+idx);
		
		declareStructAsClass(struct);
	}
	
	public void createInstanceForStructArray(Symbol structSymArray, int idx) {
		//先把结构体数组对象加载到堆栈上
		int i = getLocalVariableIndex(structSymArray);
		this.emit(Instruction.ALOAD, ""+i);
		
		//把构造的实例对象放置到数组对应下标
		this.emit(Instruction.SIPUSH, ""+idx);
		
		//先构造一个结构体实例对象
		Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
		StructDefine struct = sp.getStruct();
		this.emit(Instruction.NEW, struct.getTag());
		this.emit(Instruction.DUP);
		this.emit(Instruction.INVOKESPECIAL, struct.getTag()+"/"+"<init>()V");
		
		this.emit(Instruction.AASTORE);
	}
	
	public void readValueFromStructMember(Symbol structSym, Symbol field) {
		
		ArrayValueSetter vs = (ArrayValueSetter)structSym.getValueSetter();
		if (vs != null) {
			//结构体对象来自于结构体数组，此时需要把结构体数组对象加载到堆栈
			structSym = vs.getSymbol();
		}
		/*
		 * 先把类的实例加载到堆栈顶部, 如果结构体来自于数组，那么这里加载到堆栈的就是结构体数组对象
		 */
		int idx = getLocalVariableIndex(structSym);
	    this.emit(Instruction.ALOAD, ""+idx);
	    
	    if (vs != null) {
	    	//把要修改的结构体对象从结构体数组里加载到堆栈上
	    	int i = (int) vs.getIndex();
	    	this.emit(Instruction.SIPUSH, ""+i);
	    	this.emit(Instruction.AALOAD);
	    }
	    
	    /*
	     * 如果我们要读取myTag.x 下面的语句会构造出
	     * CTag/x  I
	     */
	    String fieldType = "";
	    if (field.hasType(Specifier.INT)) {
			fieldType = "I";
		} else if (field.hasType(Specifier.CHAR)) {
			fieldType = "C";
		} else if (field.hasType(Specifier.CHAR) && field.getDeclarator(Declarator.POINTER) != null) {
			fieldType = "Ljava/lang/String;";
		}
	    
	    //通过getfield指令把结构体的成员变量读出来后压入堆栈顶部
	    Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
		StructDefine struct = sp.getStruct();
	    String fieldContent = struct.getTag() + "/" + field.getName() + " " + fieldType;
	    this.emit(Instruction.GETFIELD, fieldContent);
	}
	
	
	
	public void initFuncArguments(boolean b) {
	    isInitArguments = b;	
	}
	
	public boolean isPassingArguments() {
		return isInitArguments;
	}
	
	public  void setCurrentFuncName(String name) {
		nameStack.push(name);
	}
	
	public String getCurrentFuncName() {
		return nameStack.peek();
	}
	
	public void popFuncName() {
		nameStack.pop();
	}
	
	public int getLocalVariableIndex(Symbol symbol) {
		TypeSystem typeSys = TypeSystem.getInstance();
		String funcName = nameStack.peek();
		Symbol funcSym = typeSys.getSymbolByText(funcName, 0, "main");
		ArrayList<Symbol> localVariables = new ArrayList<Symbol>();
		Symbol s = funcSym.getArgList();
		while (s != null) {
			localVariables.add(s);
			s = s.getNextSymbol();
		}
		Collections.reverse(localVariables);
		
		ArrayList<Symbol> list = typeSys.getSymbolsByScope(symbol.getScope());
		for (int i = 0; i < list.size(); i++) {
			if (localVariables.contains(list.get(i)) == false) {
				localVariables.add(list.get(i));
			}
		}
		
		for (int i = 0; i < localVariables.size(); i++) {
			if (localVariables.get(i) == symbol) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void createArray(Symbol symbol) {
		if (arrayNameMap.containsKey(symbol.getScope())) {
			if (arrayNameMap.get(symbol.getScope()).equals(symbol.getName())) {
				return;
			}
		}
		
		arrayNameMap.put(symbol.getScope(), symbol.getName());
		
		Declarator declarator = symbol.getDeclarator(Declarator.ARRAY); 
		if (declarator == null) {
			return;
		}
		
		String type = "";
		if (symbol.hasType(Specifier.INT)) {
			type = "int";
		}
		
		int num = declarator.getElementNum();
		this.emit(Instruction.SIPUSH, ""+num);
		this.emit(Instruction.NEWARRAY , type);
		int idx = getLocalVariableIndex(symbol);
		this.emit(Instruction.ASTORE, "" + idx);
	}
	
	public void readArrayElement(Symbol symbol, Object index) {
		Declarator declarator = symbol.getDeclarator(Declarator.ARRAY); 
		if (declarator == null) {
			return;
		}
		
		int idx = getLocalVariableIndex(symbol); 
		//change here
		this.emit(Instruction.ALOAD, ""+idx);
		if (index instanceof Integer) {
		    this.emit(Instruction.SIPUSH, ""+index);
		} else if (index instanceof Symbol) {
			 int i = this.getLocalVariableIndex((Symbol)index);
			 this.emit(Instruction.ILOAD, ""+i);
		}
		this.emit(Instruction.IALOAD);
	}
	
	public void writeArrayElement(Symbol symbol, Object index, Object value) {
		Declarator declarator = symbol.getDeclarator(Declarator.ARRAY); 
		if (declarator == null) {
			return;
		}
		
		int idx = getLocalVariableIndex(symbol); 
		if (symbol.hasType(Specifier.INT)) {
			this.emit(Instruction.ALOAD, ""+idx);
			if (index instanceof Integer) {
			    this.emit(Instruction.SIPUSH, ""+index);
			} else {
				    int i = this.getLocalVariableIndex((Symbol)index);
				    this.emit(Instruction.ILOAD, ""+i);
				}
		}
		
		/*
		 * 如果是数组元素直接相互赋值，例如b[i] = a[j]
		 * 那么必须为a[j]读取生成相应的字节码
		 */
		if (value instanceof ArrayValueSetter) {
			ArrayValueSetter setter = (ArrayValueSetter)value;
			Object idxObj = setter.getIndex();
			Symbol arraySym = setter.getSymbol();
			if (idxObj instanceof Integer) {
				int i = (int)idxObj;
				this.readArrayElement(arraySym, i);
			} else {
				this.readArrayElement(arraySym, idxObj);
			}
		}
			
		if (value instanceof Integer) {
			int val = (int)value;
			this.emit(Instruction.SIPUSH, ""+val);
	    } else if (value instanceof Symbol) {
			ProgramGenerator generator = ProgramGenerator.getInstance();
			int i = generator.getLocalVariableIndex((Symbol)value);
			generator.emit(Instruction.ILOAD, "" + i);
		}
		
		
		this.emit(Instruction.IASTORE);
		
	}
	
	
	public static ProgramGenerator getInstance() {
		if (instance == null) {
			instance = new ProgramGenerator();
		}
		
		return instance;
	}
	
    private ProgramGenerator()  {
    	
    }
    
    public String getProgramName() {
    	return programName;
    }
    
    public void generateHeader() {
    	emitDirective(Directive.CLASS_PUBLIC, programName);
    	emitDirective(Directive.SUPER, "java/lang/Object");
    	emitBlankLine();
    	emitDirective(Directive.METHOD_PUBBLIC_STATIC, "main([Ljava/lang/String;)V");
    }
    
    
    @Override
    public void finish() {
    	emit(Instruction.RETURN);
    	emitDirective(Directive.END_METHOD);
    	emitBufferedContent();
    	emitDirective(Directive.END_CLASS);
    	emitClassDefinition();
    	super.finish();
    }
}
