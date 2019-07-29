package symboltable;

import codegen.ArrayValueSetter;
import codegen.BaseGenerate;
import codegen.backend.Instruction;
import codegen.backend.ProgramGenerator;
import codegen.GenerateValueSetter;
import debug.ConsoleDebugColor;
import interpreter.ValueSetter;
import parse.LRStateTableParser;
import start.Start;

/**
 * Symbolic representation
 *
 * @author dejavudwh isHudw
 */

public class Symbol implements ValueSetter, GenerateValueSetter {
    public String name;
    String rname;

    int level;
    boolean implicit;
    boolean duplicate;

    public Symbol args = null;
    public Object value = null;

    Symbol next = null;

    public TypeLink typeLinkBegin = null;
    public TypeLink typeLinkEnd = null;

    private String symbolScope = LRStateTableParser.GLOBAL_SCOPE;

    /**
     * The following method is required only in code generation
     */
    private Object valueSetter = null;
    private boolean isMember = false;
    private Symbol structParent = null;

    public Symbol(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Symbol copy() {
        Symbol symbol = new Symbol(this.name, this.level);
        symbol.args = this.args;
        symbol.next = this.next;
        symbol.value = this.value;
        symbol.typeLinkBegin = this.typeLinkBegin;
        symbol.typeLinkEnd = this.typeLinkEnd;
        symbol.symbolScope = this.symbolScope;

        return symbol;
    }

    public void addDeclarator(TypeLink type) {
        if (typeLinkBegin == null) {
            typeLinkBegin = type;
            typeLinkEnd = type;
        } else {
            type.setNextLink(typeLinkBegin);
            typeLinkBegin = type;
        }
    }

    public void addSpecifier(TypeLink type) {
        if (typeLinkBegin == null) {
            typeLinkBegin = type;
            typeLinkEnd = type;
        } else {
            typeLinkEnd.setNextLink(type);
            typeLinkEnd = type;
        }
    }

    public Symbol getNextSymbol() {
        return next;
    }

    public String getScope() {
        return symbolScope;
    }

    public int getLevel() {
        return level;
    }

    public void setNextSymbol(Symbol symbol) {
        this.next = symbol;
    }

    public void addScope(String scope) {
        this.symbolScope = scope;
    }

    public Symbol getArgList() {
        return args;
    }

    public void setArgList(Symbol symbol) {
        this.args = symbol;
    }

    public String getName() {
        return name;
    }

    public Declarator getDeclarator(int type) {
        TypeLink begin = typeLinkBegin;
        while (begin != null && begin.getTypeObject() != null) {
            if (begin.isDeclarator) {
                Declarator declarator = (Declarator) begin.getTypeObject();
                if (declarator.getType() == type) {
                    return declarator;
                }
            }

            begin = begin.toNext();
        }

        return null;
    }

    public TypeLink getTypeHead() {
        return typeLinkBegin;
    }

    public Object getValue() {
        return value;
    }

    public int getByteSize() {
        int size = 0;
        TypeLink head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier) head.typeObject;
                if (sp.isLong() || sp.getType() == Specifier.INT ||
                        getDeclarator(Declarator.POINTER) != null) {
                    size = 4;
                    break;
                } else {
                    size = 1;
                    break;
                }
            }

            head = head.toNext();
        }

        return size;
    }

    @Override
    public void setValue(Object obj) {
        if (obj != null) {
            ConsoleDebugColor.outlnPurple("Assign Value of " + obj.toString() + " to Variable " + name);
        } else {
            return;
        }

        this.value = obj;

        /* The following is only executed when the code is generated */
        if (Start.STARTTYPE == Start.CODEGEN) {
            ProgramGenerator generator = ProgramGenerator.getInstance();

            if (BaseGenerate.resultOnStack == true) {
                /*
                 * 如果结果已经存在堆栈上了，那么赋值时无需再生成其他指令，例如j = i - 1
                 * i-1 的结果会直接放置到堆栈顶部，因此我们只需要直接通过istore指令把堆栈顶部的数值存储到
                 * 该变量所在的局部变量队列的位置即可
                 */
                this.value = obj;
                BaseGenerate.resultOnStack = false;
            } else if (obj instanceof ArrayValueSetter) {
                /*
                 * 处理 i = a[2] 这种用数组元素赋值的情形,此时要把数组元素的读取生成jvm字节码
                 */

                ArrayValueSetter setter = (ArrayValueSetter) obj;
                Symbol symbol = setter.getSymbol();
                Object index = setter.getIndex();
                if (symbol.getSpecifierByType(Specifier.STRUCTURE) == null) {
                    //如果是结构体数组，这里不做处理，留给下一步处理
                    if (index instanceof Symbol) {
                        ProgramGenerator.getInstance().readArrayElement(symbol, index);
                        if (((Symbol) index).getValue() != null) {
                            int i = (int) ((Symbol) index).getValue();
                            try {
                                this.value = symbol.getDeclarator(Declarator.ARRAY).getElement(i);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        int i = (int) index;
                        try {
                            this.value = symbol.getDeclarator(Declarator.ARRAY).getElement(i);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        ProgramGenerator.getInstance().readArrayElement(symbol, index);
                    }
                }


            } else if (obj instanceof Symbol) {
                Symbol symbol = (Symbol) obj;
                this.value = symbol.value;
                int i = generator.getLocalVariableIndex(symbol);
                generator.emit(Instruction.ILOAD, "" + i);
            } else if (obj instanceof Integer) {
                Integer val = (Integer) obj;
                generator.emit(Instruction.SIPUSH, "" + val);
                this.value = obj;
            }

            //change here
            if (this.value != null || BaseGenerate.isCompileMode == true) {
                /*
                 * 先判断该变量是否是一个结构体的成员变量，如果是，那么需要通过assignValueToStructMember来实现成员变量
                 * 的赋值，如果不是，那么就直接通过IStore语句直接赋值
                 */
                if (this.isStructMember() == false) {
                    int idx = generator.getLocalVariableIndex(this);
                    if (generator.isPassingArguments() == false) {
                        generator.emit(Instruction.ISTORE, "" + idx);
                    }
                } else {

                    if (this.getStructSymbol().getValueSetter() != null) {
                        generator.assignValueToStructMemberFromArray(this.getStructSymbol().getValueSetter(), this, this.value);
                    } else {
                        generator.assignValueToStructMember(this.getStructSymbol(), this, this.value);
                    }

                }

            }
        }
    }

    @Override
    public Symbol getSymbol() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        Symbol symbol = (Symbol) obj;
        return (this.name.equals(symbol.name) && this.level == symbol.level &&
                this.symbolScope.equals(symbol.symbolScope));
    }

    /**
     * The following method is required only in code generation
     */
    public Specifier getSpecifierByType(int type) {
        TypeLink head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier) head.typeObject;
                if (sp.getType() == type) {
                    return sp;
                }
            }

            head = head.toNext();
        }

        return null;
    }

    public boolean hasType(int type) {
        TypeLink head = typeLinkBegin;
        while (head != null) {
            if (!head.isDeclarator) {
                Specifier sp = (Specifier) head.typeObject;
                if (sp.getType() == type) {
                    return true;
                }
            }

            head = head.toNext();
        }

        return false;
    }

    public Object getValueSetter() {
        return this.valueSetter;
    }

    public void addValueSetter(Object setter) {
        this.valueSetter = setter;
    }

    public void setStructParent(Symbol parent) {
        this.isMember = true;
        this.structParent = parent;
    }

    public boolean isStructMember() {
        return isMember;
    }

    public Symbol getStructSymbol() {
        return this.structParent;
    }

}

