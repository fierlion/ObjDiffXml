import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class declaration for ObjDiffXml
 * containing public method diffObj(objA, objB)
 * @author rfoote
 */
public class ObjDiffXml {
    private boolean verbose;
    
    /**
     * default constructor for ObjDiffXml
     */
    public ObjDiffXml(){
        verbose = true;
    }
    
    /**
     * constructor for ObjDiffXml
     * @param verboseIn 'true' for verbose mode, 
     *                  'false' for brevity. 
     */
    public ObjDiffXml(boolean verboseIn){
        verbose = verboseIn;
    }

    /**
     * setter for verbose mode
     * @param verboseIn 'true' for verbose mode, 
     *                  'false' for brevity. 
     */
    public void setVerbose(boolean verboseIn){
        verbose = verboseIn;
    }
    /**
     * diffObj compares two objects using reflection
     * this is an outer call which calls the overloaded diffObj below
     * @param objA compared to objB 
     * @param objB compared to objA
     * note objA, objB should be the same type of Object, and not null.
     * @return XML formatted string of the structure and diff between
     * objA and objB
     */
    public String diffObj(Object objA, Object objB){
        StringBuilder builder = new StringBuilder(); //contains xml string
        ArrayList<String> tagOut = new ArrayList<>(); //array contains list of open xml tags
        if(objA == null || objB == null){
            return String.format("<null object=&lt %s &gt %s />\n", objA, objB);
        } 
        
        //open XML tag
        openXml(builder, tagOut, 0, objA.getClass());  
        //entry point to recursion
        diffObj(objA, objB, builder, tagOut, 3, true);
        //recursion complete. close XML tag
        closeXml(builder, tagOut, 0);
        
        return builder.toString();
    }

    private void diffObj(Object objA, Object objB, StringBuilder sb, ArrayList tagOut, int indentLevel, boolean firstTime){ 
        
        if(objA == null || objB == null){
            sb.append(getIndent(indentLevel))
            .append(String.format("<null object=&lt %s &gt %s />\n", objA, objB));
            return;
        }
        
        Class cls  = objA.getClass();
        Class clsB = objB.getClass();
        //be sure classes are the same
        if (cls != clsB){
            sb.append(String.format("<class diff=&lt %s &gt %s />\n", objA.toString(), objB.toString()));
            return;
        }
        
        if(WrapperTest.isWrapperType(cls) || cls.isPrimitive()){
            return;
        }
        
//        if(cls.isIgnore()){
//            return;
//        }
        
        Field[] fields = cls.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            Class fieldClass = field.getType();         
            
            //check each type
            
            //String
            if(fieldClass.equals(String.class)){
                boolean isDiff = false;
                try {
                    if ( !(field.get(objA).equals(field.get(objB))) ){
                        if (firstTime == true) {
                            firstTime = false;
                            sb.append(getIndent(indentLevel));
                        }
                        else {
                            openXml(sb, tagOut, indentLevel, objA.getClass());
                            sb.append(getIndent(indentLevel + 3));
                        }
                        sb.append(String.format("<%s Type=%s diff=&lt %s &gt %s />\n",
                        field.getName(), field.getType().getSimpleName(),
                        field.get(objA), field.get(objB)));
                    } 
                    else if (verbose == true){
                        if (isDiff == true) sb.append(getIndent(indentLevel + 3)); 
                        else sb.append(getIndent(indentLevel));
                        sb.append(String.format("<%s />\n", field.getName()));
                    }  
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ObjDiffXml.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            
            //primitive or enum
            else if(WrapperTest.isWrapperType(fieldClass) || fieldClass.isPrimitive() || fieldClass.isEnum()){
                boolean isDiff = false;
                try {
                    if ( !(field.get(objA).equals(field.get(objB))) ){
                        if (firstTime == true) {
                            firstTime = false;
                            sb.append(getIndent(indentLevel));
                        }
                        else {
                            openXml(sb, tagOut, indentLevel, objA.getClass());
                            sb.append(getIndent(indentLevel + 3));
                        }
                        sb.append(String.format("<%s Type=%s diff=&lt %s &gt %s />\n",
                        field.getName(), field.getType().getSimpleName(),
                        field.get(objA), field.get(objB)));
                    } 
                    else if (verbose == true){
                        if (isDiff == true) sb.append(getIndent(indentLevel + 3)); 
                        else sb.append(getIndent(indentLevel));
                        sb.append(String.format("<%s />\n", field.getName()));
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ObjDiffXml.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            
            //arrays
            //(this section has its own recursive stack to deal with array Objects)
            else if(fieldClass.isArray()){
                int length;
                boolean isDiff = false;
                try {
                    length = Array.getLength(field.get(objA));
                    for (int i = 0; i < length; ++i) {
                        Object arrayObjectA = Array.get(field.get(objA), i);
                        Object arrayObjectB = Array.get(field.get(objB), i);
                        if (arrayObjectA == null || arrayObjectB == null){
                            sb.append(getIndent(indentLevel))
                            .append(String.format("<invalid null %s, %s />\n", objA, objB));
                            break;
                        } 
                        else {
                            if ( !(Array.get(field.get(objA), i).equals(Array.get(field.get(objB), i))) ){
                                isDiff = true;
                                openXml(sb, tagOut, indentLevel, fieldClass);
                                sb.append(getIndent(indentLevel + 3))
                                .append(String.format("<%s[%s] Type=%s diff=&lt %s &gt %s />\n",
                                field.getName(), i, arrayObjectA.getClass().getSimpleName(),
                                Array.get(field.get(objA), i),
                                Array.get(field.get(objB), i)));
                            } 
                            else if (verbose == true) {
                                if (isDiff == true) sb.append(getIndent(indentLevel + 3)); 
                                else sb.append(getIndent(indentLevel));
                                sb.append(String.format("<%s[%s] />\n", field.getName(), i));
                            }
                        }
                        //recurse (inner recursion for array Objects)
                        diffObj(arrayObjectA, arrayObjectB, sb, tagOut, indentLevel + 3, false);
                    }
                    if (isDiff == true) {
                        closeXml(sb, tagOut, indentLevel);
                    }
                } 
                catch (IllegalAccessException ex) {
                    Logger.getLogger(ObjDiffXml.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }
            else{
                try {
                    //recurse (outer recursion for loop Objects)
                    diffObj(field.get(objA), field.get(objB), sb, tagOut, indentLevel, false);
                } 
                catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ObjDiffXml.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {
                    //check that the tagOut array isn't closing the root XML node
                    if (tagOut.size() > 1) closeXml(sb, tagOut, indentLevel);
                }
            }
        }
    }
    
    //method returns String of 'level' spaces 
    private static String getIndent(int level){
        String indent = "";
        while(indent.length() < level){
            indent = indent.concat(" ");
        }
        return indent;
    }
    
    private static void openXml(StringBuilder sb, ArrayList tagOut, int indentLevel, Class cls){
        //open class name XML tags
        sb.append(getIndent(indentLevel))
        .append(String.format("<%s>\n", cls.getSimpleName()));
        tagOut.add(cls.getSimpleName());                  
    }
    
    private static void closeXml(StringBuilder sb, ArrayList tagOut, int indentLevel){
        if(tagOut.size() > 0){
            sb.append(getIndent(indentLevel))
            .append(String.format("</%s>\n", tagOut.get(tagOut.size() - 1)));
            tagOut.remove(tagOut.size()-1);
        }
    }   
}
